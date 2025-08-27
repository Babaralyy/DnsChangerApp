package com.dns.dnschangerapp.presentation.fragments

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.net.VpnService
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.dns.dnschangerapp.R
import com.dns.dnschangerapp.databinding.FragmentHomeBinding
import com.dns.dnschangerapp.presentation.vpn.DnsVpnService
import com.dns.dnschangerapp.utils.constant.Constants
import com.dns.dnschangerapp.utils.extensions.navigateTo
import com.dns.dnschangerapp.utils.extensions.onBackPressedDispatcher
import com.dns.dnschangerapp.utils.extensions.setDebounceClickListener
import com.dns.dnschangerapp.utils.extensions.withDelay
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.InetAddress
import kotlin.system.measureTimeMillis


class HomeFragment : Fragment() {
    private val binding by lazy {
        FragmentHomeBinding.inflate(layoutInflater)
    }

    private var isServiceRunning = false
    private var selectedDnsId: String? = null

    private val requestVpnPermission =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && selectedDnsId != null) {
                DnsVpnService.start(requireContext(), selectedDnsId!!)
            } else {
                binding.switchCustom.isChecked = false
                Toast.makeText(requireContext(), "VPN permission denied.", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inIt()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if (Constants.backFromDnsChangerFrag){
            Constants.backFromDnsChangerFrag = false
            isServiceRunning = false
            DnsVpnService.stop(requireContext())
            binding.switchCustom.isChecked = false
            binding.tvStatus.text = requireContext().getString(R.string.disconnected)
            binding.tvPrivate.text = requireContext().getString(R.string.your_internet_is_not_private)
        }
        calculatePing()
        withDelay {
            if (DnsVpnService.isServiceRunning()){
                isServiceRunning = true
                binding.switchCustom.isChecked = true
                binding.tvStatus.text = requireContext().getString(R.string.connected)
                binding.tvPrivate.text = requireContext().getString(R.string.your_internet_is_private)

                when (DnsVpnService.getActiveDnsId()) {
                    "google" -> {
                        binding.tvDnsName.text = "Google DNS"
                    }

                    "cloudflare" -> {
                        binding.tvDnsName.text = "Cloudflare DNS"
                    }

                    "adguard" -> {
                        binding.tvDnsName.text = "AdGuard DNS"
                    }

                    "opendns" -> {
                        binding.tvDnsName.text = "Open DNS"
                    }
                }

            } else {
                isServiceRunning = false
                binding.tvStatus.text = requireContext().getString(R.string.disconnected)
                binding.tvPrivate.text = requireContext().getString(R.string.your_internet_is_not_private)
            }
        }

    }

    private fun calculatePing() {
        binding.apply {
            lifecycleScope.launch {
                when (Constants.connectedDns) {
                    "google" -> {
                        tvDnsName.text = "Google DNS"
                        tvDnsPing.text = "${measureDnsPing("dns.google")}ms | DoT/DoH"
                    }

                    "cloud" -> {
                        tvDnsName.text = "Cloudflare DNS"
                        tvDnsPing.text =
                            "${measureDnsPing("cloudflare-dns.com")}ms | PrivateDoT/DoH"
                    }

                    "ad" -> {
                        tvDnsName.text = "AdGuard DNS"
                        tvDnsPing.text =
                            "${measureDnsPing("dns.adguard.com")}ms | Ad-blocking DoT/DoH"
                    }

                    "open" -> {
                        tvDnsName.text = "Open DNS"
                        tvDnsPing.text = "${measureDnsPing("resolver1.opendns.com")}ms | Secure Dns"
                    }

                    else -> {
                        tvDnsName.text = "Google DNS"
                        tvDnsPing.text = "${measureDnsPing("dns.google")}ms | DoT/DoH"
                    }
                }
            }
        }
    }

    private fun inIt() {
        onBackPressedDispatcher {
           showMaterialExitDialog()
        }
        clickListeners()
    }

    private fun clickListeners() {
        binding.apply {

            switchCustom.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    if (!isServiceRunning){
                        when (Constants.connectedDns) {
                            "google" -> {
                                prepareAndStart("google")
                            }

                            "cloud" -> {
                                prepareAndStart("cloudflare")
                            }

                            "ad" -> {
                                prepareAndStart("adguard")
                            }

                            "open" -> {
                                prepareAndStart("opendns")
                            }
                        }
                    }

                } else {
                    DnsVpnService.stop(requireContext())
                    switchCustom.isChecked = false
                }
            }

            ivTopSettings.setDebounceClickListener {
                navigateTo(R.id.homeFragment, R.id.action_homeFragment_to_settingsFragment)
            }
            mcvDefaultDns.setDebounceClickListener {
                navigateTo(R.id.homeFragment, R.id.action_homeFragment_to_changeDnsFragment)
            }
        }
    }

    private fun prepareAndStart(dnsId: String) {
        selectedDnsId = dnsId
        val intent = VpnService.prepare(requireContext())
        if (intent != null) {
            requestVpnPermission.launch(intent)
        } else {
            DnsVpnService.start(requireContext(), dnsId)
        }
    }

    suspend fun measureDnsPing(host: String): Long {
        return withContext(Dispatchers.IO) {
            var dnsTime = -1L
            try {
                dnsTime = measureTimeMillis {
                    InetAddress.getByName(host) // Force DNS resolution
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            dnsTime
        }
    }

    private fun showMaterialExitDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Exit")
            .setMessage("Are you sure you want to exit the app?")
            .setPositiveButton("Yes") { _, _ ->
                requireActivity().finish()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(true) // Prevents dialog from closing when tapping outside
            .show()
    }
}