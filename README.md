**Android Finance App**

A modern personal finance tracking application built for Android using Kotlin and Jetpack Compose.

The goal of this project is to demonstrate clean architecture principles, reactive data handling, and modern Android development practices while providing a simple and efficient way to track monthly income and expenses.

**Features**
- Add income and expense transactions
- View monthly income and expense totals
- See top spending categories
- Navigate between months
- Local persistence with Room
- Reactive UI using Flow and StateFlow
- Material 3 design with Jetpack Compose

**Architecture**

The application follows MVVM with a Repository pattern and clear separation of concerns.


```
UI (Jetpack Compose)
        ↓
ViewModel
        ↓
UseCase
        ↓
Repository (interface)
        ↓
Room (DAO + Entities)
```

**UI Layer**

-Built entirely with Jetpack Compose
-Uses unidirectional data flow
-Observes immutable StateFlow from ViewModels

**ViewModel Layer**

-Manages UI state
-Combines reactive streams using Flow operators (combine, flatMapLatest)
-Delegates business logic to use cases
-Does not directly access the database

**Domain Layer**

-Contains business models
-Defines repository interfaces
-Implements use cases (e.g. AddTransactionUseCase)

**Data Layer**

-Room database
-DAO interfaces returning Flow streams
-TypeConverters for enum persistence
-Entity-to-domain model mapping

**Tech Stack**

-Kotlin
-Jetpack Compose
-Room
-Coroutines
-Flow / StateFlow
-Hilt (Dependency Injection)
-Navigation Compose
-Material 3

**Project Structure**


```
app/
 ├── data/
 │    ├── dao/          # Room DAO interfaces
 │    ├── entity/       # Database entities
 │    └── repository/   # Repository implementations
 │
 ├── domain/
 │    ├── model/        # Business models
 │    ├── repo/         # Repository interfaces
 │    └── usecase/      # Application use cases
 │
 ├── ui/
 │    ├── screens/      # Compose screens
 │    ├── navigation/   # Navigation graph
 │    └── state/        # UI state models
 │
 └── di/                # Hilt dependency injection modules
```

## Run
1. Open in Android Studio
2. Gradle sync
3. Run `app`

**Future improvements**
-Add reamining screens
-Ability to remove and edit data
-Visual upgrades
