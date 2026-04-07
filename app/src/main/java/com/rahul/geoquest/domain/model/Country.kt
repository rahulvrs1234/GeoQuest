package com.rahul.geoquest.domain.model

data class Country(
    val code: String,
    val name: String,
    val capital: String,
    val region: String,
    val subregion: String,
    val flagUrl: String,
    val population: Long,
)

