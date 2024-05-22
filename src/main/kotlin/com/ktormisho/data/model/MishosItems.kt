package com.ktormisho.data.model

import kotlinx.serialization.Serializable

@Serializable
data class MishosItems(
    val itemName:String,
    val description:String,
    val url:String
)
