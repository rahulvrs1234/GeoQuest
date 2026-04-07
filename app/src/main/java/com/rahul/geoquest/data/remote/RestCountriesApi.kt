package com.rahul.geoquest.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface RestCountriesApi {
    @GET("all")
    suspend fun getCountries(
        @Query("fields") fields: String = "name,capital,region,subregion,population,flags,cca2",
    ): List<RestCountryDto>
}

