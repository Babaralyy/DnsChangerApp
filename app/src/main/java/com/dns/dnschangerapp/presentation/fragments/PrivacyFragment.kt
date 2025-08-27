package com.dns.dnschangerapp.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dns.dnschangerapp.R
import com.dns.dnschangerapp.databinding.FragmentPrivacyBinding

class PrivacyFragment : Fragment() {

    private val binding by lazy {
        FragmentPrivacyBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        inIt()
        return binding.root
    }

    private fun inIt() {
        binding.privacyText.text = privacyPolicyText
    }


    val privacyPolicyText = """
            Privacy Policy
            Effective Date: June 19, 2025

            Welcome to DNS Changer. This Privacy Policy explains how we collect, use, and protect your information when you use our DNS changer mobile application.

            1. Information We Collect
            We do not collect any personally identifiable information (PII). Specifically:

            We do not collect names, emails, phone numbers, or any user-identifiable data.

            We do not log DNS queries or internet activity.

            We do not use cookies or trackers.

            2. Permissions and Functionality
            The App uses Apple’s NetworkExtension API to enable custom DNS profiles. This allows you to select a third-party DNS provider for improved privacy and ad blocking. All configuration changes are made locally on your device.

            3. Use of Third-Party DNS Providers
            You may choose to route your DNS queries through third-party providers like AdGuard DNS, Cloudflare DNS, or NextDNS. These services have their own privacy policies and practices. We do not control or take responsibility for how these services process your data.

            4. Data Sharing and Storage
            We do not store, share, or sell any user data.

            All preferences and settings are stored locally on your device only.

            5. Security
            We follow best practices to ensure your configuration data remains secure and private. Since no personal data is collected or transmitted, your privacy is inherently protected.

            6. Children’s Privacy
            DNS Changer is not intended for use by children under the age of 13. We do not knowingly collect information from children.

            7. Changes to this Policy
            We may update this policy as needed. If changes are made, we will update this notice within the app.
        """.trimIndent()
}