# mobileGame

😄 **Welcome to _mobileGame_!** 🎮

Welcome to the official repository for **mobileGame**, an engaging Android game where you dodge enemies, collect stars, and face off against epic bosses. Ready to jump in and start playing? Let’s go! 🚀

---
## 🎯 Goals & Objectives

- 🎮 Create a fun, fast-paced 2D mobile game
- 🔄 Practice Android development with the latest SDK and tooling
- 💡 Demonstrate custom game loop and view rendering
- 📊 Integrate ads for monetization using Google Play Services

---
## ✨ Key Features

- 🏁 **Splash Screen** to welcome players
- 🎨 **Custom GameView** for drawing sprites and handling touch input
- 👾 **Variety of Enemies** (Boss, Enemy, Star, Boom) with unique behaviors
- 🔫 **Shooting Mechanics** via bullets
- 🏆 **Win & Game Over Screens** for game flow
- 🤝 **Menu & Credits** activities
- 📢 **Google Mobile Ads** integration (com.google.android.gms:play-services-ads)

---
## 🚀 Technologies & Tools Used

- Java 11
- Android SDK (compileSdk 35, minSdk 24, targetSdk 35)
- AndroidX Libraries: AppCompat, Material, Activity KTX, ConstraintLayout
- Android Game SDK (androidx.games:games-activity)
- Google Play Services Ads v23.0.0
- Gradle Kotlin DSL (`build.gradle.kts`)
- ViewBinding for safe UI references

---
## 🔧 Prerequisites

- Android Studio Arctic Fox or later
- JDK 11
- Android SDK Platform 35
- Git

---
## 🛠️ Installation & Setup

```bash
# 1. Clone the repository
git clone https://github.com/AndresLapSol/mobilegame.git
cd mobilegame

# 2. (Optional) Initialize submodules or wrappers
./gradlew wrapper

# 3. Open in Android Studio
#    File → Open → select the project root
```

---
## 💻 How to Run & Test

1. In Android Studio, select an emulator or physical device.
2. Click **Run ▶️** or execute:
   ```bash
   ./gradlew installDebug
   ```
3. (Optional) Run unit & instrumentation tests:
   ```bash
   ./gradlew test
   ./gradlew connectedAndroidTest
   ```

---
## 📂 Project Folder Structure

```
.mobileGame/
├─ .idea/                   # IDE settings
├─ gradle/                  # Gradle wrapper files
├─ app/                     # Main Android module
│  ├─ build.gradle.kts      # App-level build config
│  ├─ src/
│  │  └─ main/
│  │     ├─ java/com/example/mygame/
│  │     │  ├─ Activity/           # All Activity classes
│  │     │  │  ├─ MainActivity.java
│  │     │  │  ├─ MenuActivity.java
│  │     │  │  ├─ GameActivity.java
│  │     │  │  ├─ GameOverActivity.java
│  │     │  │  ├─ WinActivity.java
│  │     │  │  └─ CreditsActivity.java
│  │     │  ├─ GameView.java        # Custom view for game loop
│  │     │  ├─ SplashScreen.java    # Initial launcher screen
│  │     │  └─ Models/              # Game entity data classes
│  │     │     ├─ Player.java
│  │     │     ├─ Enemy.java
│  │     │     ├─ Boss.java
│  │     │     ├─ Bullet.java
│  │     │     ├─ Boom.java
│  │     │     └─ Star.java
│  │     └─ res/                  # Layouts, drawables, values, etc.
│  └─ proguard-rules.pro      # Release ProGuard rules
├─ build.gradle.kts          # Top-level build config
├─ gradle.properties
├─ gradlew, gradlew.bat      # Gradle wrapper scripts
└─ settings.gradle.kts       # Project settings
```

---
## 🤝 Contributing

We love contributions! Please follow these steps:

1. 🍴 **Fork** the repository
2. 🔀 **Create** a new branch: `git checkout -b feature/YourFeature`
3. 💬 **Commit** your changes: `git commit -m "Add awesome feature"`
4. 📤 **Push** to your branch: `git push origin feature/YourFeature`
5. 🔍 **Open a Pull Request** and describe your changes

---
## ⚖️ License

This project is licensed under the **MIT License**. See the [LICENSE](LICENSE) file for details.

---
## 📫 Contact

- GitHub: [@AndresLapSol](https://github.com/AndresLapSol)
- Email: andreslapsol@gmail.com
