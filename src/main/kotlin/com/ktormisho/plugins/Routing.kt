package com.ktormisho.plugins

import com.ktormisho.data.model.LoginPost
import com.ktormisho.data.model.RegisterPost
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.sql.Connection
import java.sql.DriverManager
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource

object DatabaseFactory {
    private val dataSource: HikariDataSource

    init {
        val config = HikariConfig()
        config.jdbcUrl = "jdbc:mysql://localhost:3306/mishosdb"
        config.username = "root"
        config.password = "Suckartwell0!"
        config.maximumPoolSize = 10
        dataSource = HikariDataSource(config)

        // Initialize the database
        dataSource.connection.use { conn ->
            val statement = conn.createStatement()
            statement.execute("CREATE TABLE IF NOT EXISTS users (username VARCHAR(255) PRIMARY KEY, password VARCHAR(255), email VARCHAR(255))")
        }
    }

    fun getConnection(): Connection = dataSource.connection
}

fun Application.configureRouting() {
    routing {
        get("/users") {
            DatabaseFactory.getConnection().use { connection ->
                val statement = connection.prepareStatement("SELECT username, email FROM users")
                val resultSet = statement.executeQuery()

                val users = mutableListOf<Map<String, String>>()
                while (resultSet.next()) {
                    val user = mapOf(
                        "username" to resultSet.getString("username"),
                        "email" to resultSet.getString("email")
                    )
                    users.add(user)
                }
                call.respond(HttpStatusCode.OK, users)
            }
        }

        route("/api") {
            post("/login") {
                val post = call.receive<LoginPost>()
                DatabaseFactory.getConnection().use { connection ->
                    val statement = connection.prepareStatement("SELECT * FROM users WHERE username = ?")
                    statement.setString(1, post.username)
                    val resultSet = statement.executeQuery()
                    if (resultSet.next() && resultSet.getString("password") == post.password) {
                        call.respond(HttpStatusCode.OK, mapOf("message" to "Login successful"))
                    } else {
                        call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Invalid username or password"))
                    }
                }
            }

            post("/register") {
                val post = call.receive<RegisterPost>()
                if (post.username.isNotBlank() && post.password.isNotBlank() && post.email.isNotBlank()) {
                    DatabaseFactory.getConnection().use { connection ->
                        val checkStatement = connection.prepareStatement("SELECT * FROM users WHERE username = ?")
                        checkStatement.setString(1, post.username)
                        val resultSet = checkStatement.executeQuery()

                        if (resultSet.next()) {
                            call.respond(HttpStatusCode.Conflict, mapOf("error" to "User already exists"))
                            return@post
                        }

                        val insertStatement = connection.prepareStatement("INSERT INTO users (username, password, email) VALUES (?, ?, ?)")
                        insertStatement.setString(1, post.username)
                        insertStatement.setString(2, post.password) // Consider hashing passwords for security
                        insertStatement.setString(3, post.email)
                        insertStatement.executeUpdate()
                        call.respond(HttpStatusCode.OK, mapOf("message" to "Registration successful"))
                    }
                } else {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid registration information"))
                }
            }
        }
    }
}