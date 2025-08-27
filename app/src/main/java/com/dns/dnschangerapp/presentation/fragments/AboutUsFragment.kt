package com.dns.dnschangerapp.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dns.dnschangerapp.R
import com.dns.dnschangerapp.databinding.FragmentAboutUsBinding

class AboutUsFragment : Fragment() {

    private val binding by lazy{
        FragmentAboutUsBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        inIt()
        return binding.root
    }

    private fun inIt() {
        binding.aboutText.text = aboutUsText
    }

    val aboutUsText = """
            About Us

            Softease Technologies pvt Ltd, we specialize in mobile app development and publishing, with a focus on creating simple, effective solutions for everyday users. As a dedicated mobile app company, we design, build, and publish high-quality applications on the Google Play Store.

            Our flagship app, DNS Changer, has helped thousands of users improve their internet speed, privacy, and access by allowing easy customization of DNS settings without root access. We're passionate about building intuitive tools that enhance digital experiences and are committed to expanding our portfolio with useful, performance-driven apps.

            Whether it's improving connectivity or simplifying mobile tasks, our mission is to deliver apps that make a real difference.
        """.trimIndent()
}