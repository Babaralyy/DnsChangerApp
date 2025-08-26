package com.dns.dnschangerapp.presentation.fragments

import android.app.Activity.RESULT_OK
import android.net.VpnService
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.dns.dnschangerapp.R
import com.dns.dnschangerapp.databinding.FragmentHomeBinding
import com.dns.dnschangerapp.presentation.vpn.DnsVpnService
import com.dns.dnschangerapp.utils.extensions.navigateTo
import com.dns.dnschangerapp.utils.extensions.setDebounceClickListener


class HomeFragment : Fragment() {
    private val binding by lazy {
        FragmentHomeBinding.inflate(layoutInflater)
    }
    private var selectedDnsId: String? = null

    private val requestVpnPermission =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && selectedDnsId != null) {
                DnsVpnService.start(requireContext(), selectedDnsId!!)
            } else {
                Toast.makeText(requireContext(), "VPN permission denied.", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inIt()
        return binding.root
    }

    private fun inIt() {
        prepareAndStart("google")
        clickListeners()
    }

    private fun clickListeners() {
        binding.apply {
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
            // Already granted earlier
            DnsVpnService.start(requireContext(), dnsId)
        }
    }
}