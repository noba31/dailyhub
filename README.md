# Daily Hub (Kotlin + Jetpack Compose)

Screens: Home (greeting, clock, quote), Tasks (saved on device), About (theme toggle).

## Get the APK without Android Studio (GitHub Actions)
1. Create a new GitHub repository and upload this folder's contents (keep the `.github` folder).
2. Open the repo's **Actions** tab -> "Build APK" -> wait ~5 min for it to finish.
   (It also runs automatically on every push; or click "Run workflow".)
3. Open the finished run, download the **DailyHub-debug-apk** artifact, unzip it -> `app-debug.apk`.
4. Copy to your phone and install (allow "Install unknown apps" for your file manager/browser).

## Build locally on Windows (optional)
Install JDK 17, Gradle 8.7, and the Android SDK command-line tools (platform 34, build-tools 34.0.0),
set ANDROID_HOME, then run `gradle assembleDebug` in this folder.
Output: `app/build/outputs/apk/debug/app-debug.apk`.
