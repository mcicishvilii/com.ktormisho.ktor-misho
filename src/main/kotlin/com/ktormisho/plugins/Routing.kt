package com.ktormisho.plugins

import com.ktormisho.data.model.LoginPost
import com.ktormisho.data.model.RegisterPost
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
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
                if (post.username.isNotBlank() && post.password.isNotBlank()) {
                    call.respond(HttpStatusCode.OK, "Login successful")
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Invalid login credentials")
                }
            }
            post("/register") {
                val post = call.receive<RegisterPost>()
                if (post.username.isNotBlank() && post.password.isNotBlank() && post.email.isNotBlank()) {
                    call.respond(HttpStatusCode.OK, "Registration successful")
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Invalid registration information")
                }
            }
        }
    }
}
