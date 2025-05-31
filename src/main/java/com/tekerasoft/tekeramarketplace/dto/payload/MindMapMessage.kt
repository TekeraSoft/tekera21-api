package com.tekerasoft.tekeramarketplace.dto.payload

import org.springframework.web.multipart.MultipartFile
import java.io.InputStream
import java.io.Serializable

data class MindMapMessage(
    val id: String,
    val imageFile: ByteArray
): Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MindMapMessage

        if (id != other.id) return false
        if (!imageFile.contentEquals(other.imageFile)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + imageFile.contentHashCode()
        return result
    }
}
