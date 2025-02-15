package no.ohgod.models.tautulli.show

data class TautulliShow(
    val ratingKey: Int,
    val title: String,
    val year: Int?,
    val contentRating: String?,
    val summary: String?,
    val addedAt: String,
    val lastUpdated: String?,
    val rating: String?,
    val studio: String?,
    val genres: List<String> = emptyList(),
    val posterUrl: String?
)

