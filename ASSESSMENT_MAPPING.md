# Assessment Mapping

This document maps the GeoQuest app to the main Assignment 3 requirements and the key practical topics covered in lectures.

## Assessment Requirements

| Requirement | How GeoQuest meets it |
| --- | --- |
| Android app built in Android Studio | Project is a native Android Studio project using Gradle and Kotlin |
| Kotlin implementation | Entire app is written in Kotlin |
| Jetpack Compose UI | All screens are implemented with Compose |
| Material Design 3 | App theme and components use Material 3 |
| Landing screen | `HomeScreen.kt` |
| Activity screen | `QuizScreen.kt` |
| Settings screen | `SettingsScreen.kt` |
| User statistics screen | `StatsScreen.kt` |
| Navigation | Implemented with Navigation Compose in `GeoQuestApp.kt` |
| Internet connectivity | REST Countries API via Retrofit in `RestCountriesApi.kt` |
| Local persistence | Room database in `GeoQuestDatabase.kt` and DAO files |
| User settings persistence | DataStore in `UserPreferencesRepository.kt` |
| Architecture | ViewModel + Repository + DI |
| Dependency injection | Koin module in `AppModule.kt` |
| Background processing | WorkManager in `CountryRefreshWorker.kt`, `StudyReminderWorker.kt`, and `WorkScheduler.kt` |
| Runtime permissions | Notification permission flow in `SettingsScreen.kt` |
| Testing | Unit tests and Compose UI test source included |

## Lecture Alignment

| Lecture topic | Evidence in project |
| --- | --- |
| Compose layouts and state | Compose screens and screen state models |
| Material Design | Shared theme, typography, shapes, and components |
| MVVM architecture | ViewModels for Home, Quiz, Settings, and Stats |
| Repository pattern | `GeoQuestRepository.kt` |
| Networking with Retrofit | `data/remote/` package |
| Room database | `data/local/` package |
| Preferences and persistence | DataStore-based settings |
| Navigation | `GeoQuestApp.kt` and `GeoQuestDestination.kt` |
| Background tasks | WorkManager-based reminders and sync |
| Runtime permissions | notification permission request |
| Testing and validation | quiz engine tests, stats tests, UI test source |
| Ethics and responsible UX | local data storage, optional reminders, hint control, transparent settings text |

## Key Files

- `app/src/main/java/com/rahul/geoquest/ui/home/HomeScreen.kt`
- `app/src/main/java/com/rahul/geoquest/ui/quiz/QuizScreen.kt`
- `app/src/main/java/com/rahul/geoquest/ui/settings/SettingsScreen.kt`
- `app/src/main/java/com/rahul/geoquest/ui/stats/StatsScreen.kt`
- `app/src/main/java/com/rahul/geoquest/data/repository/GeoQuestRepository.kt`
- `app/src/main/java/com/rahul/geoquest/data/preferences/UserPreferencesRepository.kt`
- `app/src/main/java/com/rahul/geoquest/worker/WorkScheduler.kt`
- `app/src/test/java/com/rahul/geoquest/domain/quiz/QuizEngineTest.kt`

## Short Justification

GeoQuest does not just meet the minimum checklist items. It combines the required Android features into a coherent educational app with realistic navigation, persistent user state, remote content, background processing, and an ethical design stance suitable for a student-focused product.
