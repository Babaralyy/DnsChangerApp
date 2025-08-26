package com.dns.dnschangerapp.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dns.dnschangerapp.R
import com.dns.dnschangerapp.databinding.FragmentChangeDnsBinding
import com.dns.dnschangerapp.utils.extensions.popFrom
import com.dns.dnschangerapp.utils.extensions.setDebounceClickListener

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
        binding.apply {
            tvGoogle.setDebounceClickListener {
                popFrom(R.id.changeDnsFragment)
            }
            tvCloud.setDebounceClickListener {
                popFrom(R.id.changeDnsFragment)
            }
            tvAdguard.setDebounceClickListener {
                popFrom(R.id.changeDnsFragment)
            }
            tvOpen.setDebounceClickListener {
                popFrom(R.id.changeDnsFragment)
            }
        }
    }
}