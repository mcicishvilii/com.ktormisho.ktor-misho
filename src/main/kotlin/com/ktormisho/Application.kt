package com.ktormisho

import com.ktormisho.plugins.*
import io.ktor.server.application.*

import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main(args: Array<String>) {
    val port = System.getenv("PORT")?.toIntOrNull() ?: 8080
    embeddedServer(Netty, port = port, module = Application::module).start(wait = true)
}

fun Application.module() {
    configureSerialization()
    configureMonitoring()
    configureRouting()
}
