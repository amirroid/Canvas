package ir.amirroid.canvas.data.file

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import me.tatarka.inject.annotations.Inject
import platform.Foundation.NSData
import platform.Foundation.NSURL
import platform.Foundation.create
import platform.Foundation.dataWithContentsOfURL
import platform.Foundation.writeToURL
import platform.posix.memcpy
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@ContributesBinding(AppScope::class)
@SingleIn(AppScope::class)
class IosDocumentStorage : DocumentStorage {
    @OptIn(ExperimentalForeignApi::class)
    override fun read(uri: String): ByteArray {
        val nsUrl = NSURL(string = uri)
        val data = NSData.dataWithContentsOfURL(nsUrl)
            ?: throw IllegalArgumentException("Cannot read data from URL: $uri")
        val length = data.length.toInt()
        return ByteArray(length).also { array ->
            data.bytes?.let { ptr ->
                array.usePinned { pinned ->
                    memcpy(pinned.addressOf(0), ptr, length.toULong())
                }
            } ?: throw IllegalArgumentException("NSData.bytes is null for URL: $uri")
        }
    }

    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    override fun write(uri: String, content: ByteArray) {
        val nsUrl = NSURL(string = uri)
        val data = content.usePinned { pinned ->
            NSData.create(bytes = pinned.addressOf(0), length = content.size.toULong())
        }
        val success = data.writeToURL(nsUrl, atomically = true)
        if (!success) throw IllegalArgumentException("Cannot write data to URL: $uri")
    }

    override fun takePermission(uri: String) {
        // no-op
    }
}