package no.ohgod.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.config.*
import liquibase.command.CommandScope
import liquibase.command.core.UpdateCommandStep
import liquibase.database.DatabaseFactory
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.ClassLoaderResourceAccessor
import javax.sql.DataSource

object DatabaseFactory {
    private lateinit var dataSource: HikariDataSource

    fun init(config: ApplicationConfig) {
        val driverClassName = config.property("database.driverClassName").getString()
        val jdbcUrl = config.property("database.jdbcUrl").getString()
        dataSource = createHikariDataSource(driverClassName, jdbcUrl)
        runMigrations(dataSource)
    }

    private fun createHikariDataSource(driverClassName: String, jdbcUrl: String): HikariDataSource {
        val config = HikariConfig().apply {
            this.driverClassName = driverClassName
            this.jdbcUrl = jdbcUrl
            maximumPoolSize = 3
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }
        return HikariDataSource(config)
    }

    private fun runMigrations(dataSource: DataSource) {
        dataSource.connection.use { connection ->
            val database = DatabaseFactory.getInstance()
                .findCorrectDatabaseImplementation(JdbcConnection(connection))

            CommandScope(UpdateCommandStep.COMMAND_NAME[0])
                .addArgumentValue("changeLogFile", "db/changeset-master.xml")
                .addArgumentValue("database", database)
                .addArgumentValue("resourceAccessor", ClassLoaderResourceAccessor())
                .execute()
        }
    }


    // Example of a simple query function
    fun <T> query(block: (java.sql.Connection) -> T): T {
        return dataSource.connection.use { conn ->
            conn.autoCommit = false
            try {
                val result = block(conn)
                conn.commit()
                result
            } catch (e: Exception) {
                conn.rollback()
                throw e
            }
        }
    }
}