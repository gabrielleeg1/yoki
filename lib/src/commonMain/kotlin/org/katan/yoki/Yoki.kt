package org.katan.yoki

import io.ktor.client.HttpClient
import io.ktor.utils.io.core.Closeable
import kotlinx.serialization.json.Json
import org.katan.yoki.io.createHttpClient
import org.katan.yoki.resource.container.ContainerResource
import org.katan.yoki.resource.image.ImageResource
import org.katan.yoki.resource.network.NetworkResource
import org.katan.yoki.resource.secret.SecretResource
import org.katan.yoki.resource.volume.VolumeResource

@YokiDsl
public class Yoki @PublishedApi internal constructor(
    public val config: YokiConfig
) : Closeable {

    public companion object {
        @JvmStatic
        public fun create(config: YokiConfig): Yoki {
            return Yoki(config)
        }

        @JvmStatic
        public fun create(socketPath: String): Yoki {
            return Yoki {
                this.socketPath = socketPath
            }
        }

        @JvmStatic
        public fun createWithUnixDefaults(): Yoki {
            return Yoki { useUnixDefaults() }
        }

        @JvmStatic
        public fun createWithHttpDefaults(): Yoki {
            return Yoki { useHttpDefaults() }
        }
    }

    private val httpClient: HttpClient = createHttpClient(this)
    private val json: Json = Json {
        ignoreUnknownKeys = true
    }

    public val containers: ContainerResource = ContainerResource(httpClient, json)
    public val networks: NetworkResource = NetworkResource(httpClient, json)
    public val volumes: VolumeResource = VolumeResource(httpClient, json)
    public val secrets: SecretResource = SecretResource(httpClient, json)
    public val images: ImageResource = ImageResource(httpClient, json)

    override fun close() {
        httpClient.close()
    }
}

public inline fun Yoki(block: YokiConfig.() -> Unit = {}): Yoki {
    val config = YokiConfig().apply(block)
    return Yoki(config)
}

/**
 * DslMarker for Yoki.
 */
@DslMarker
public annotation class YokiDsl
