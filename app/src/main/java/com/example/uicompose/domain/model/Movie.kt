package com.example.uicompose.domain.model


data class Movie(
    val id: Int? = null,
    val title: String? = null,
    val category: String? = null,
    val rating:Float? = null,
    val year: Int? = null,
    val durationMil: Int? = null,
    val description: String? = null
)
