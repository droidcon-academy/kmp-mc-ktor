package data.api

import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.coroutines.delay

class ApiService(private val settings: Settings) {
    companion object {
        const val API_URL = "dummyjson.com"
        const val SIGN_IN = "/auth/login"
        const val GET_USER = "/auth/me"
        const val REFRESH_TOKEN = "/auth/refresh"
    }

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        install(DefaultRequest) {
            url {
                protocol = URLProtocol.HTTPS
                host = API_URL
            }
        }
    }

    suspend fun signIn() {
        val response = httpClient.post {
            url {
                contentType(ContentType.Application.Json)
                path(SIGN_IN)
            }
            setBody(
                User(
                    username = "atuny0",
                    password = "9uQFF1Lh",
                    expiresInMins = 2
                )
            )
        }
        settings.putString("token", response.body<SignedInUser>().token)
        println("RESPONSE: ${response.bodyAsText()}")
    }

    suspend fun getCurrentUser() {
        val response = httpClient.get {
            url {
                contentType(ContentType.Application.Json)
                path(GET_USER)
            }
            val token = settings.getStringOrNull("token")
            bearerAuth(token ?: "0")
        }
        println("RESPONSE: ${response.bodyAsText()}")
        if (response.status.isSuccess()) {
            delay(2000)
            refreshToken()
        }
    }

    suspend fun refreshToken() {
        val response = httpClient.post {
            url {
                contentType(ContentType.Application.Json)
                path(REFRESH_TOKEN)
            }
            val token = settings.getStringOrNull("token")
            bearerAuth(token ?: "0")
            setBody<ExtendToken>(ExtendToken(expiresInMins = 5))
        }
        if (response.status.isSuccess()) {
            settings.putString("token", response.body<SignedInUser>().token)
        }
        println("RESPONSE: ${response.bodyAsText()}")
    }
}

@Serializable
data class User(
    val username: String,
    val password: String,
    val expiresInMins: Int
)

@Serializable
data class SignedInUser(
    val username: String,
    val email: String,
    val token: String
)

@Serializable
data class ExtendToken(
    val expiresInMins: Int
)