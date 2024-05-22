package com.ktormisho.routes

import com.ktormisho.data.model.MishosItems
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

private const val BASE_URL_emulator = "http://10.0.2.2:8080"


private val mishosItems = listOf(
    MishosItems("Carl", "A cute brown rabbit", "$BASE_URL_emulator/images/img1.jpg"),
    MishosItems("Emma", "Emma likes to eat apples", "$BASE_URL_emulator/images/img2.jpg"),
    MishosItems("toko", "toko dzlevamosili ylea", "$BASE_URL_emulator/images/img2.jpg"),

)

fun Route.randomItem() {
    get("/randomrabbit") {
        call.respond(
            HttpStatusCode.OK,
            mishosItems.random()
        )
    }
}

fun Route.allItems() {
    get("/allrabbits") {
        call.respond(
            HttpStatusCode.OK,
            mishosItems
        )
    }
}