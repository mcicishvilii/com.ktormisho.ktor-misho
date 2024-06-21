package com.ktormisho.data.model

import kotlinx.serialization.Serializable

@Serializable
data class RegisterPost(
    val username: String,
    val password: String,
    val email: String
)
