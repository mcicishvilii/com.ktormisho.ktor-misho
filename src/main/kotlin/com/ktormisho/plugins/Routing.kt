package com.ktormisho.plugins

import com.ktormisho.routes.allItems
import com.ktormisho.routes.randomItem
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        randomItem()
        allItems()
        static {
            resources("/")
        }
    }
}