package com.dns.dnschangerapp.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dns.dnschangerapp.R
import com.dns.dnschangerapp.databinding.FragmentSplashBinding
import com.dns.dnschangerapp.utils.extensions.navigateTo
import com.dns.dnschangerapp.utils.extensions.withDelay


class SplashFragment : Fragment() {
    private val binding by lazy {
        FragmentSplashBinding.inflate(layoutInflater)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        inIt()
        return binding.root
    }

    private fun inIt() {
        withDelay(2000) {
            navigateTo(R.id.splashFragment, R.id.action_splashFragment_to_homeFragment)
        }
    }
}