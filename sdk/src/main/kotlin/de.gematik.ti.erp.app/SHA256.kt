/*
 * Copyright 2026 Bundesdruckerei GmbH
 * For the license, see the accompanying file LICENSE.md
 */

package de.gematik.ti.erp.app

import java.security.MessageDigest

internal object SHA256 {

    fun hash(bytesToHash: ByteArray): ByteArray {
        val digest = MessageDigest.getInstance("SHA256")

        digest.update(bytesToHash)

        return digest.digest()
    }

}
