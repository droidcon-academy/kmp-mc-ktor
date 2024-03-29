package api

import domain.Photos
import domain.RequestState
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.json.Json

class UnsplashApi {
    companion object {
        const val UNSPLASH_URL = "https://api.unsplash.com"
        const val ACCESS_KEY = "YOUR_ACCESS_KEY"
    }

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 10000
        }
        install(DefaultRequest) {
            headers {
                append(HttpHeaders.Authorization, "Client-ID $ACCESS_KEY")
            }
        }
    }

    fun getPhotos(): Flow<RequestState<Photos>> {
        return flow {
            emit(RequestState.Loading)
            delay(2000)
            try {
//            throw io.ktor.client.plugins.ConnectTimeoutException(url = UNSPLASH_URL, timeout = 10000)
                emit(
                    RequestState.Success(
                        data = Photos(
                            items = httpClient.get("${UNSPLASH_URL}/photos").body()
                        )
                    )
                )
            } catch (e: Exception) {
//                Logger.setTag("ProductsApi")
//                Logger.e { e.message.toString() }
                emit(RequestState.Error(message = "$e"))
            }
        }.flowOn(Dispatchers.IO)
    }
}