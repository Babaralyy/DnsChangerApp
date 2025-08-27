package com.dns.dnschangerapp.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.dns.dnschangerapp.R
import com.dns.dnschangerapp.databinding.FragmentChangeDnsBinding
import com.dns.dnschangerapp.utils.constant.Constants
import com.dns.dnschangerapp.utils.extensions.popFrom
import com.dns.dnschangerapp.utils.extensions.setDebounceClickListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.InetAddress
import kotlin.system.measureTimeMillis

class ChangeDnsFragment : Fragment() {

    private val binding by lazy {
        FragmentChangeDnsBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        inIt()
        return binding.root
    }

    private fun inIt() {
        clickListeners()
        calculatePing()
    }

    private fun clickListeners() {
        binding.apply {
            tvGoogle.setDebounceClickListener {
                Constants.connectedDns = "google"
                Constants.backFromDnsChangerFrag = true
                popFrom(R.id.changeDnsFragment)
            }
            tvCloud.setDebounceClickListener {
                Constants.connectedDns = "cloud"
                Constants.backFromDnsChangerFrag = true
                popFrom(R.id.changeDnsFragment)
            }
            tvAdguard.setDebounceClickListener {
                Constants.connectedDns = "ad"
                Constants.backFromDnsChangerFrag = true
                popFrom(R.id.changeDnsFragment)
            }
            tvOpen.setDebounceClickListener {
                Constants.connectedDns = "open"
                Constants.backFromDnsChangerFrag = true
                popFrom(R.id.changeDnsFragment)
            }
        }
    }

    private fun calculatePing() {
        binding.apply {
            lifecycleScope.launch {
                tvGooglePing.text = "${measureDnsPing("dns.google")}ms | DoT/DoH"
                tvCloudPing.text = "${measureDnsPing("cloudflare-dns.com")}ms | PrivateDoT/DoH"
                tvAdPing.text = "${measureDnsPing("dns.adguard.com")}ms | Ad-blocking DoT/DoH"
                tvOpenPing.text = "${measureDnsPing("resolver1.opendns.com")}ms | Secure Dns"
            }
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
}