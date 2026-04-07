# Gibbs Reflective Cycle: GeoQuest

## Description

For Assignment 3, I developed GeoQuest, an educational Android app in Kotlin using Jetpack Compose. The app helps secondary-school learners practise geography through short quiz rounds based on country flags and capital cities. It includes the four required screens from the brief: a home screen, quiz activity screen, settings screen, and statistics screen. It also uses the REST Countries API for online content, Room for local persistence, DataStore for user preferences, WorkManager for optional reminders, and notification permission handling.

My goal was not just to satisfy the checklist, but to create an app that felt realistic and responsible. I wanted it to look polished, run properly on a phone, and reflect ethical concerns discussed earlier in the subject. That meant thinking carefully about privacy, permissions, reminders, and the tone of the learning experience, rather than focusing only on technical features.

## Feelings

At the beginning, I felt confident about the individual technologies because the lectures had already introduced Compose, navigation, Retrofit, Room, and modern Android architecture. However, I also felt pressure because combining them into one app is much harder than following lecture examples one by one. It is easier to understand a concept in isolation than to make it work inside a complete app with multiple screens, persistence, networking, and background behaviour.

I also felt unsure about how polished the app needed to be. A basic prototype might have technically passed, but I wanted the result to feel closer to a real Play Store app. As development progressed, I became more motivated because the app started to feel coherent. Once the app was running on a real phone and the UI was improved, I felt proud of the project because it looked like a complete product rather than only an assessment artifact.

## Evaluation

Overall, I think the project went well. The strongest aspect is that the app combines the required Android concepts into one working flow. A user can open the home screen, start a quiz, answer questions, store the result locally, and then review their performance in the statistics screen. This makes the submission feel complete instead of disconnected.

Another success is that the app reflects lecture content in a practical way. Compose made it possible to iterate quickly on the interface, Room supported quiz history and cached content, Retrofit handled remote data, and WorkManager was appropriate for reminders. The project also benefited from clear architectural separation between UI, repository, domain, workers, and preferences.

The weaknesses were mostly in usability rather than missing features. Some layouts looked fine in Android Studio but were less comfortable on a smaller phone. The quiz screen originally required too much scrolling, and the navigation behaviour needed refinement so the Home tab felt natural. These issues taught me that an app can technically work while still needing design improvement.

## Analysis

The most important lesson from this assignment is that Android development is not just about implementing features. It is about balancing architecture, platform behaviour, and user experience. The lectures gave me the technical building blocks, but this project forced me to combine them into practical decisions.

One clear example was persistence. Room and DataStore both store data, but they serve different purposes. Room is appropriate for structured records such as quiz attempts and cached country content, while DataStore is more suitable for lightweight preferences such as difficulty, theme, hints, and reminders. This reflects good architecture because each tool is used for the kind of data it handles best.

The networking feature also showed real trade-offs. Using the REST Countries API made the app feel more realistic and satisfied the internet connectivity requirement, but it introduced the risk of network failure. To reduce this risk, fallback data was included so the app still remains usable even if the API is unavailable. This connected lecture content about reliability and error handling to actual design choices.

The ethical side of the project was especially important. Educational apps often try to maximise engagement through default notifications, guilt-based streaks, and unnecessary data collection. I wanted GeoQuest to take a different approach. Reminders are off by default, notification permission is requested only when needed, and quiz progress is stored locally rather than in the cloud. These choices reduce privacy risk and support user autonomy. They also align well with Android’s own platform model, where permission visibility and controlled background work are important parts of user trust.

The UI refinement process also deepened my understanding of responsive design. A quiz layout that looked acceptable at first became frustrating when tested on a real phone, especially when long answer options and flag images reduced the available screen space. By resizing the question area, improving the answer layout, and hiding hints until the user taps for them, I made the app more usable and more realistic.

## Conclusion

In conclusion, I believe GeoQuest is a successful result because it meets the assignment requirements while also demonstrating thoughtful Android design. It applies the key lecture topics, including Compose, Material 3, navigation, Retrofit, Room, DataStore, WorkManager, permissions, and testing, in one coherent project.

This assignment also changed how I think about software quality. I now understand more clearly that responsible design is part of technical design, not something separate from it. Choices about storage, reminders, permissions, and wording directly affect whether an app feels trustworthy. A good educational app should support learners without manipulating them, and this project helped me understand that balance more clearly.

## Action Plan

If I were to continue this project, I would improve it in several ways. I would add more quiz types, such as continent matching and population comparison, to make the learning experience broader. I would also test accessibility more thoroughly with larger fonts, screen readers, and more device sizes. In addition, I would strengthen the app with more UI tests for navigation, settings persistence, and quiz completion flows.

For future projects, I would start usability testing earlier instead of leaving layout refinement until late in development. I would also continue to think about ethics from the beginning of the design process rather than treating it as something to mention only in the final report. This assignment showed me that the strongest mobile app is not the one with the most features, but the one where technical design, user experience, and ethical decisions support each other clearly.
