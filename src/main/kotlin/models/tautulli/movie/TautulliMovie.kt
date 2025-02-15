package no.ohgod.models.tautulli.movie

data class TautulliMovie(
    val ratingRey: Int,
    val title: String,
    val year: Int?,
    val duration: Int,
    val rating: String?,
    val contentRating: String?,
    val summary: String?,
    val fileSize: Long?,
    val addedAt: String,
    val lastPlayed: String?,
    val playCount: Int,
    val studio: String?,
    val genres: List<String>,
    val directors: List<String>,
    val writers: List<String>,
    val actors: List<String>,
)

