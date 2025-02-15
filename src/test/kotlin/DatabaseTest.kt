package no.ohgod

import io.ktor.server.config.*
import io.ktor.server.testing.*
import no.ohgod.db.DatabaseFactory
import kotlin.test.Test

class DatabaseTest {
    @Test
    fun testDatabase() = testApplication {
        environment {
            config = ApplicationConfig("application-test.conf")
        }

        application {
            DatabaseFactory.init(environment.config)
        }

        // Your test code here
    }
}