package com.rahul.geoquest.data.remote

import com.google.gson.annotations.SerializedName
import com.rahul.geoquest.data.local.CountryEntity

data class RestCountryDto(
    @SerializedName("cca2") val code: String?,
    @SerializedName("capital") val capital: List<String>?,
    @SerializedName("region") val region: String?,
    @SerializedName("subregion") val subregion: String?,
    @SerializedName("population") val population: Long?,
    @SerializedName("flags") val flags: FlagsDto?,
    @SerializedName("name") val name: NameDto?,
)

data class FlagsDto(
    @SerializedName("png") val png: String?,
)

data class NameDto(
    @SerializedName("common") val common: String?,
)

fun RestCountryDto.toEntityOrNull(syncedAtMillis: Long): CountryEntity? {
    val normalizedCode = code?.trim().orEmpty()
    val normalizedName = name?.common?.trim().orEmpty()
    if (normalizedCode.isBlank() || normalizedName.isBlank()) {
        return null
    }

    return CountryEntity(
        code = normalizedCode,
        name = normalizedName,
        capital = capital?.firstOrNull().orEmpty(),
        region = region.orEmpty(),
        subregion = subregion.orEmpty(),
        flagUrl = flags?.png.orEmpty(),
        population = population ?: 0L,
        syncedAtMillis = syncedAtMillis,
    )
}

