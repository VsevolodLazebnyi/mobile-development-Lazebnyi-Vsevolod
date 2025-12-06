# Lab2
Мобильная разработка 4 курс Лабораторная работа 1 



## 🛠️ Технологический стек / Tech Stack

<div style="display: flex; flex-wrap: wrap; gap: 6px; margin-bottom: 15px;">
  <img alt="Kotlin" src="https://img.shields.io/badge/Kotlin-7F52FF?logo=kotlin&logoColor=white&style=for-the-badge">
  <img alt="Java" src="https://img.shields.io/badge/Java-ED8B00?logo=java&logoColor=white&style=for-the-badge">
  <img alt="Android Studio" src="https://img.shields.io/badge/Android_Studio-3DDC84?logo=android-studio&logoColor=white&style=for-the-badge">
  <img alt="XML" src="https://img.shields.io/badge/XML-4285F4?logo=xml&logoColor=white&style=for-the-badge">
</div>

## 📱 Интерфейс приложения

### Светлая тема
<div style="display: flex; gap: 10px; margin-bottom: 20px;">
  <img src="docs/light1.png" width="30%" alt="Light theme screenshot 1">
  <img src="docs/light2.png" width="30%" alt="Light theme screenshot 2">
  <img src="docs/light3.png" width="30%" alt="Light theme screenshot 3">
</div>

### Темная тема
<div style="display: flex; gap: 10px; margin-bottom: 20px;">
  <img src="docs/dark1.png" width="30%" alt="Dark theme screenshot 1">
  <img src="docs/dark2.png" width="30%" alt="Dark theme screenshot 2">
  <img src="docs/dark3.png" width="30%" alt="Dark theme screenshot 3">
</div>



## Реализация

- Неоморфный дизайн интерфейса
- Поддержка темной и светлой темы
- Отслеживание жизненного цикла Activity
- Логирование событий жизненного цикла

## Архитектура

```
project

├── app
│   ├── build.gradle.kts
│   ├── proguard-rules.pro
│   └── src
│       └── main
│           ├── AndroidManifest.xml
│           ├── java
│           │   └── com
│           │       └── example
│           │           └── massenger
│           │               ├── FeedFragment.java
│           │               ├── MainActivity.java
│           │               ├── ProfileFragment.java
│           │               └── SettingsFragment.java
│           └── res
│               ├── drawable
│               │   ├── bottom_nav_gradient.xml
│               │   ├── ic_launcher_background.xml
│               │   ├── ic_launcher_foreground.xml
│               │   ├── icon_selector.xml
│               │   └── nano.png
│               ├── drawable-night
│               │   └── bottom_nav_gradient.xml
│               ├── layout
│               │   ├── activity_main.xml
│               │   ├── fragment_feed.xml
│               │   ├── fragment_profile.xml
│               │   └── fragment_settings.xml
│               ├── menu
│               │   └── bottom_nav_menu.xml
│               ├── mipmap *
│               ├── navigation
│               │   └── nav_graph.xml
│               ├── values
│               │   ├── colors.xml
│               │   ├── strings.xml
│               │   └── themes.xml
│               ├── values-night
│               │   ├── colors.xml
│               │   └── themes.xml
│               └── xml
│                   ├── backup_rules.xml
│                   └── data_extraction_rules.xml
├── build.gradle.kts
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradle.properties
├── gradlew
├── gradlew.bat
├── local.properties
└── settings.gradle.kts
```

## Зависимости

- `Android_neumorphic:1.2.0` для неоморфного дизайна
- `Android SDK 21`
- `Kotlin 1.9`

P.S. посмотрите другие проекты, поставьте звезды <3
