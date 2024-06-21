package com.ktormisho.plugins

import com.ktormisho.data.model.LoginPost
import com.ktormisho.data.model.RegisterPost
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.sql.DriverManager

fun Application.configureRouting() {
    val connection = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "")

    val statement = connection.createStatement()
    statement.execute("CREATE TABLE IF NOT EXISTS users (username VARCHAR(255) PRIMARY KEY, password VARCHAR(255), email VARCHAR(255))")
    statement.close()

    routing {
        route ("/"){
            get(){
                call.respondText(
                    "Hello"
                )
            }
        }
        route("/api") {
            post("/login") {
                val post = call.receive<LoginPost>()
                val statement = connection.prepareStatement("SELECT * FROM users WHERE username = ?")
                statement.setString(1, post.username)
                val resultSet = statement.executeQuery()
                if (resultSet.next() && resultSet.getString("password") == post.password) {  // Check hashed password
                    call.respond(HttpStatusCode.OK, "Login successful")
                } else {
                    call.respond(HttpStatusCode.Unauthorized, "Invalid username or password")
                }
                resultSet.close()
                statement.close()
            }
            post("/register") {
                val post = call.receive<RegisterPost>()
                if (post.username.isNotBlank() && post.password.isNotBlank() && post.email.isNotBlank()) {
                    val statement = connection.prepareStatement("INSERT INTO users (username, password, email) VALUES (?, ?, ?)")
                    statement.setString(1, post.username)
                    statement.setString(2, post.password) // Consider hashing passwords for security
                    statement.setString(3, post.email)
                    statement.executeUpdate()
                    statement.close()
                    call.respond(HttpStatusCode.OK, "Registration successful")
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Invalid registration information")
                }
            }
        }
    }
}
