# Campus Lost and Found (Jetpack Compose)

Open the folder in Android Studio (Koala+), let Gradle sync, run on an emulator/device (API 24+).

Demo login (seeded in SQLite): mary@example.com / password123 — or register a new account.

## Structure
app/src/main/java/com/campus/lostfound
- MainActivity.kt, LostFoundApp.kt        Scaffold + bottom navigation
- data/model                              User, Item, ItemType, ItemStatus
- data/db/DatabaseHelper.kt               SQLiteOpenHelper (users, items tables + seed)
- data/repo                               Repository (IO dispatcher), SessionManager, Security
- viewmodel/MainViewModel.kt              State + actions
- ui/theme, ui/components                 Colors, shared composables
- ui/navigation                           Routes, AppNavHost
- ui/screens                              Splash, Welcome, Register, Login, Home, Search,
                                          Report (lost/found), Details, UpdateStatus, MyReports, Profile
