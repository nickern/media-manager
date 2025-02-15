package no.ohgod.models.tautulli.show

data class TautulliSeason(
    val ratingKey: Int,
    val showId: Int,
    val seasonNumber: Int,
    val title: String?,
    val year: Int?,
    val addedAt: String,
    val posterUrl: String?,
    val summary: String?
)