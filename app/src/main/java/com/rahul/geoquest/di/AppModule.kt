package com.rahul.geoquest.di

import androidx.room.Room
import com.rahul.geoquest.data.local.GeoQuestDatabase
import com.rahul.geoquest.data.preferences.UserPreferencesRepository
import com.rahul.geoquest.data.remote.RestCountriesApi
import com.rahul.geoquest.data.repository.GeoQuestRepository
import com.rahul.geoquest.domain.quiz.QuizEngine
import com.rahul.geoquest.ui.settings.SettingsViewModel
import com.rahul.geoquest.ui.home.HomeViewModel
import com.rahul.geoquest.ui.quiz.QuizViewModel
import com.rahul.geoquest.ui.stats.StatsViewModel
import com.rahul.geoquest.worker.WorkScheduler
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
    }

    single {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl("https://restcountries.com/v3.1/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RestCountriesApi::class.java)
    }

    single {
        Room.databaseBuilder(
            get(),
            GeoQuestDatabase::class.java,
            "geoquest.db",
        ).fallbackToDestructiveMigration().build()
    }

    single { get<GeoQuestDatabase>().quizAttemptDao() }
    single { QuizEngine() }
    single { UserPreferencesRepository(get()) }
    single { GeoQuestRepository(get(), get(), get(), get()) }
    single { WorkScheduler(get()) }

    viewModel { HomeViewModel(get(), get()) }
    viewModel { QuizViewModel(get(), get()) }
    viewModel { StatsViewModel(get()) }
    viewModel { SettingsViewModel(get(), get()) }
}
