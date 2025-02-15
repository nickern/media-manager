package no.ohgod.models.tautulli.user

data class TautulliUser(
    val id: Int,
    val username: String,
    val email: String,
    val isActive: Boolean = true,
    val lastSeen: String?,
    val deviceName: String?,
    val platform: String?,
    val playCount: Int = 0,
    val totalPlays: Int = 0,
    val totalTime: Long = 0
)