package com.dns.dnschangerapp.data.models

data class DnsProfile(
    val id: String,
    val displayName: String,
    val ipv4: List<String>,
    val ipv6: List<String>
)

object DnsProfiles {
    val GOOGLE = DnsProfile(
        id = "google",
        displayName = "Google DNS",
        ipv4 = listOf("8.8.8.8", "8.8.4.4"),
        ipv6 = listOf("2001:4860:4860::8888", "2001:4860:4860::8844")
    )
    val CLOUDFLARE = DnsProfile(
        id = "cloudflare",
        displayName = "Cloudflare",
        ipv4 = listOf("1.1.1.1", "1.0.0.1"),
        ipv6 = listOf("2606:4700:4700::1111", "2606:4700:4700::1001")
    )
    val ADGUARD = DnsProfile(
        id = "adguard",
        displayName = "AdGuard DNS",
        ipv4 = listOf("94.140.14.14", "94.140.15.15"),
        ipv6 = listOf("2a10:50c0::ad1:ff", "2a10:50c0::ad2:ff")
    )
    val OPENDNS = DnsProfile(
        id = "opendns",
        displayName = "OpenDNS",
        ipv4 = listOf("208.67.222.222", "208.67.220.220"),
        ipv6 = listOf("2620:119:35::35", "2620:119:53::53")
    )

    val ALL = listOf(GOOGLE, CLOUDFLARE, ADGUARD, OPENDNS)

    fun byId(id: String): DnsProfile =
        ALL.firstOrDefault(id) ?: GOOGLE

    private fun List<DnsProfile>.firstOrDefault(id: String): DnsProfile? =
        firstOrNull { it.id == id }
}