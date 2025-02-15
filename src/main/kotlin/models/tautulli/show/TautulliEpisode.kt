package no.ohgod.models.tautulli.show

data class TautulliEpisode(
    val ratingKey: Int,
    val seasonId: Int,
    val episodeNumber: Int,
    val title: String,
    val duration: Int,
    val rating: String?,
    val fileSize: Long?,
    val playCount: Int,
    val lastPlayed: String?,
    val addedAt: String,
    val summary: String?,
    val directors: List<String> = emptyList(),
    val writers: List<String> = emptyList(),
    val airDate: String?
)