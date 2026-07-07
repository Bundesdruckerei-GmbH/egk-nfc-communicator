package de.gematik.ti.erp.app

import java.security.MessageDigest

internal object SHA256 {

    fun hash(bytesToHash: ByteArray): ByteArray {
        val digest = MessageDigest.getInstance("SHA256")

        digest.update(bytesToHash)

        return digest.digest()
    }

}