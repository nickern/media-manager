package no.ohgod.services

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import no.ohgod.db.DatabaseFactory
import no.ohgod.models.tautulli.movie.TautulliMovie
import no.ohgod.models.tautulli.movie.TautulliResponse
import org.slf4j.LoggerFactory
import java.sql.Timestamp

class TautulliService(
    private val apiKey: String,
    private val baseUrl: String,
    private val httpClient: HttpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }
) {
    private val logger = LoggerFactory.getLogger(TautulliService::class.java)

    suspend fun importMovies() {
        try {
            logger.info("Starting movie import from Tautulli")
            val movies = fetchMoviesFromTautulli()
            saveMoviesToDatabase(movies)
            logger.info("Successfully imported ${movies.size} movies")
        } catch (e: Exception) {
            logger.error("Failed to import movies", e)
            throw e
        }
    }

    private suspend fun fetchMoviesFromTautulli(): List<TautulliMovie> {
        val response = httpClient.get("$baseUrl/api/v2") {
            parameter("apikey", apiKey)
            parameter("cmd", "get_libraries")
            parameter("section_type", "movie")
        }

        val tautulliResponse = response.body<TautulliResponse>()
        return tautulliResponse.response.data
    }

    private suspend fun saveMoviesToDatabase(movies: List<TautulliMovie>) {
        DatabaseFactory.query { connection ->
            connection.prepareStatement("""
                INSERT INTO movies (
                    id, title, year, rating, duration, file_size, 
                    play_count, last_played, added_at, summary, 
                    content_rating, studio, genre, directors, 
                    writers, actors
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                ON CONFLICT (id) DO UPDATE SET
                    title = EXCLUDED.title,
                    year = EXCLUDED.year,
                    rating = EXCLUDED.rating,
                    duration = EXCLUDED.duration,
                    file_size = EXCLUDED.file_size,
                    play_count = EXCLUDED.play_count,
                    last_played = EXCLUDED.last_played,
                    summary = EXCLUDED.summary,
                    content_rating = EXCLUDED.content_rating,
                    studio = EXCLUDED.studio,
                    genre = EXCLUDED.genre,
                    directors = EXCLUDED.directors,
                    writers = EXCLUDED.writers,
                    actors = EXCLUDED.actors
            """).use { stmt ->
                movies.forEach { movie ->
                    stmt.setInt(1, movie.ratingRey)
                    stmt.setString(2, movie.title)
                    stmt.setObject(3, movie.year)
                    stmt.setString(4, movie.rating)
                    stmt.setInt(5, movie.duration)
                    stmt.setObject(6, movie.fileSize)
                    stmt.setInt(7, movie.playCount)
                    stmt.setTimestamp(8, movie.lastPlayed?.let { Timestamp.valueOf(it) })
                    stmt.setTimestamp(9, Timestamp.valueOf(movie.addedAt))
                    stmt.setString(10, movie.summary)
                    stmt.setString(11, movie.contentRating)
                    stmt.setString(12, movie.studio)
                    stmt.setString(13, movie.genres.joinToString(","))
                    stmt.setString(14, movie.directors.joinToString(","))
                    stmt.setString(15, movie.writers.joinToString(","))
                    stmt.setString(16, movie.actors.joinToString(","))
                    stmt.addBatch()
                }
                stmt.executeBatch()
            }
        }
    }
}