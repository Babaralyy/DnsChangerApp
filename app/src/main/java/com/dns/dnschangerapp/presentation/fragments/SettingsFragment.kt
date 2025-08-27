package com.dns.dnschangerapp.presentation.fragments

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dns.dnschangerapp.R
import com.dns.dnschangerapp.databinding.FragmentSettingsBinding
import com.dns.dnschangerapp.utils.extensions.navigateTo
import com.dns.dnschangerapp.utils.extensions.setDebounceClickListener

class SettingsFragment : Fragment() {

    private val binding by lazy {
        FragmentSettingsBinding.inflate(layoutInflater)
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
        binding.tvVersion.text = getAppVersionName(requireActivity())
    }

    private fun clickListeners() {
        binding.apply {
            tvAbout.setDebounceClickListener {
                navigateTo(R.id.settingsFragment, R.id.action_settingsFragment_to_aboutUsFragment)
            }
            tvPrivacy.setDebounceClickListener {
                navigateTo(R.id.settingsFragment, R.id.action_settingsFragment_to_privacyFragment)
            }
            tvTerms.setDebounceClickListener {
                navigateTo(R.id.settingsFragment, R.id.action_settingsFragment_to_termsFragment)
            }
        }
    }

    fun getAppVersionName(context: Context): String? {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            null
        }
    }
}