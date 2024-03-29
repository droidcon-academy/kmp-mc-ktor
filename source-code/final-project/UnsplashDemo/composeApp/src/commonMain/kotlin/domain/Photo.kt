package domain

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Photos(
    val items: List<Photo>
)

@Serializable
data class Photo(
    val id: String,
    @SerialName("created_at")
    val createdAt: String,
    val likes: Int,
    val description: String?,
    val user: User,
    val urls: Urls
) {
    fun parseDate() = Instant.parse(createdAt)
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .date.toString()
}

@Serializable
data class Urls(
    val regular: String,
    val small: String
)

@Serializable
data class User(
    val id: String,
    val username: String,
    @SerialName("profile_image")
    val image: ProfileImage
)

@Serializable
data class ProfileImage(
    val small: String
)