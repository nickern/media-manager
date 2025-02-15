package no.ohgod.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

fun Application.configureRouting() {
    val staticFilesDir = System.getenv("STATIC_FILES_DIR") ?: "frontend/build"

    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause" , status = HttpStatusCode.InternalServerError)
        }
    }
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        // Static files configuration
        staticFiles("/", File(staticFilesDir)) {
            default("index.html")

            singlePageApplication {
                useResources = true
                filesPath = staticFilesDir
                defaultPage = "index.html"
                applicationRoute = "/"
            }
        }
    }
}
