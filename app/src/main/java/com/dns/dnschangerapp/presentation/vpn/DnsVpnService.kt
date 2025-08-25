package com.dns.dnschangerapp.presentation.vpn


import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.VpnService
import android.os.Build
import android.os.ParcelFileDescriptor
import androidx.core.app.NotificationCompat
import com.dns.dnschangerapp.R
import com.dns.dnschangerapp.data.models.DnsProfile
import com.dns.dnschangerapp.data.models.DnsProfiles
import com.dns.dnschangerapp.presentation.activities.MainActivity
import kotlinx.coroutines.*
import java.io.IOException

class DnsVpnService : VpnService() {

    companion object {
        const val ACTION_START = "com.example.dnschanger.action.START"
        const val ACTION_STOP = "com.example.dnschanger.action.STOP"
        const val EXTRA_DNS_ID = "extra_dns_id"

        private const val NOTIF_CHANNEL_ID = "dns_vpn_channel"
        private const val NOTIF_ID = 42

        fun start(context: Context, dnsId: String) {
            val i = Intent(context, DnsVpnService::class.java).apply {
                action = ACTION_START
                putExtra(EXTRA_DNS_ID, dnsId)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(i)
            } else {
                context.startService(i)
            }
        }

        fun stop(context: Context) {
            val i = Intent(context, DnsVpnService::class.java).apply { action = ACTION_STOP }
            context.startService(i)
        }
    }

    private var vpnInterface: ParcelFileDescriptor? = null
    private var serviceScope: CoroutineScope? = null
    private var currentProfile: DnsProfile? = null

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> {
                val dnsId = intent.getStringExtra(EXTRA_DNS_ID) ?: DnsProfiles.GOOGLE.id
                val profile = DnsProfiles.byId(dnsId)
                startForeground(NOTIF_ID, buildNotification("Connecting to ${profile.displayName}…"))
                startVpn(profile)
            }
            ACTION_STOP -> stopVpn()
        }
        return START_STICKY
    }

    private fun startVpn(profile: DnsProfile) {
        currentProfile = profile
        serviceScope?.cancel()
        serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        serviceScope?.launch {
            try {
                // Tear down if already up
                vpnInterface?.closeSilently()

                val builder = Builder()
                    .setSession("DNS Changer – ${profile.displayName}")
                    .setMtu(1500)

                // Dummy TUN address (local only) to make a valid interface
                // IPv4
                builder.addAddress("10.111.222.1", 32)
                // IPv6 (ULA example)
                builder.addAddress("fd66:66:66::1", 128)

                // Route all traffic so system uses our DNS (no packet processing needed)
                builder.addRoute("0.0.0.0", 0)
                builder.addRoute("::", 0)

                // Set DNS servers
                profile.ipv4.forEach { builder.addDnsServer(it) }
                profile.ipv6.forEach { builder.addDnsServer(it) }

                // Optional: disallow our own app to avoid loops (usually not required)
                // builder.addDisallowedApplication(packageName)

                // Establish the VPN
                vpnInterface = builder.establish()

                // Update notification
                withContext(Dispatchers.Main) {
                    val n = buildNotification("DNS active: ${profile.displayName}")
                    val nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                    nm.notify(NOTIF_ID, n)
                }

                // Keep service alive while interface is up
                monitorTunnel()
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    val nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                    nm.notify(NOTIF_ID, buildNotification("Error: ${e.message ?: e.javaClass.simpleName}"))
                }
                stopSelf()
            }
        }
    }

    private suspend fun monitorTunnel() {
        // We don't handle packets; just keep the interface open.
        // Wait until the fd is closed or service is stopped.
        while (vpnInterface != null) {
            delay(1000)
        }
    }

    private fun stopVpn() {
        serviceScope?.cancel()
        serviceScope = null
        vpnInterface?.closeSilently()
        vpnInterface = null
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onDestroy() {
        stopVpn()
        super.onDestroy()
    }

    private fun buildNotification(text: String): Notification {
        val pi = PendingIntent.getActivity(
            this, 0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        return NotificationCompat.Builder(this, NOTIF_CHANNEL_ID)
            .setSmallIcon(R.drawable.app_icon)
            .setContentTitle("DNS Changer")
            .setContentText(text)
            .setOngoing(true)
            .setContentIntent(pi)
            .addAction(
                R.drawable.app_icon,
                "Stop",
                PendingIntent.getService(
                    this, 1,
                    Intent(this, DnsVpnService::class.java).apply { action = ACTION_STOP },
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIF_CHANNEL_ID,
                "DNS Changer",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "VPN status"
            }
            val nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            nm.createNotificationChannel(channel)
        }
    }

    private fun ParcelFileDescriptor.closeSilently() {
        try { close() } catch (_: IOException) {}
    }
}
