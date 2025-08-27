package com.dns.dnschangerapp.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dns.dnschangerapp.R
import com.dns.dnschangerapp.databinding.FragmentTermsBinding

class TermsFragment : Fragment() {

    private val binding by lazy{
        FragmentTermsBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.termsText.text = termsAndConditionsText
        return binding.root
    }

    val termsAndConditionsText = """
            Terms and Conditions
            Effective Date: June 19, 2025

            Welcome to DNS Changer. These Terms and Conditions (“Terms”) govern your use of the DNS Changer mobile application. By downloading or using the App, you agree to the following:

            1. App Purpose
            DNS Changer allows you to select and use a public DNS provider of your choice to improve privacy and block ads. The app does not modify or monitor content and operates by changing DNS settings only.

            2. Acceptable Use
            You agree to use the App:

            For lawful purposes only.

            Without attempting to reverse-engineer, modify, or interfere with the App’s functionality.

            Without using the App to bypass geo-restrictions or access restricted content in violation of local laws.

            3. DNS Providers
            DNS Changer provides access to public DNS servers offered by third-party providers. We are not responsible for the performance, availability, or privacy practices of these services. Use of third-party DNS is at your own risk.

            4. No Warranty
            The App is provided “as is” and “as available.” We make no warranties regarding uptime, performance, or suitability for a particular purpose.

            5. Limitation of Liability
            We are not liable for any indirect, incidental, or consequential damages arising from your use of the App or any third-party DNS services it connects to.

            6. Termination
            We reserve the right to suspend or discontinue the App at any time, with or without notice.

            7. Updates
            We may release updates or improvements to the App periodically. You are encouraged to keep the App up to date for best performance and security.

            8. Governing Law
            These Terms are governed by the laws of Country.
        """.trimIndent()

}