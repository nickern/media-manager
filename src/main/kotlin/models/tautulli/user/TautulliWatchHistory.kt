package no.ohgod.models.tautulli.user

data class TautulliWatchHistory(
    val id: Int,
    val userId: Int,
    val contentType: String, // "movie" or "episode"
    val contentId: Int,
    val watchedAt: String,
    val durationWatched: Int,
    val completed: Boolean,
    val platform: String?,
    val device: String?
)