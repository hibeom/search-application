package com.pinkcloud.domain.model

data class Document(
    val thumbnailUrl: String,
    val datetime: String,
    var isSelected: Boolean = false
)
