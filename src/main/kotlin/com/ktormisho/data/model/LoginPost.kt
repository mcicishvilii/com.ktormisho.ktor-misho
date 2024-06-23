package com.ktormisho.data.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginPost(val username: String, val password: String)
