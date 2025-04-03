# Timetable App

An Android app that converts timetable images into a digital timetable using GPT-4o.

## Features

- Firebase-based user authentication (Login / Sign Up)
- Upload and select timetable images
- Analyze images using GPT-4o
- Extract and display timetable data
- Responsive UI design

## Installation & Setup

### 1. Clone the repository

```bash
git clone https://github.com/your-username/timetable.git
cd timetable
```

### 2. Set your OpenAI API key

Add the following line to your `local.properties` file:

```properties
openai.api.key=your_openai_api_key
```

### 3. Configure Firebase

- Go to the [Firebase Console](https://console.firebase.google.com/) and create a new project.
- Add an Android app to the project. Use the package name: `com.company.timetable`.
- Download the `google-services.json` file and place it in the `app/` directory.
- In **Authentication > Sign-in method**, enable **Email/Password**.

### 4. Run the app

- Open the project in Android Studio.
- Sync Gradle when prompted.
- Click the **Run** button to launch the app on an emulator or device.

## Security Notes

- **Never** include API keys directly in your source code.
- Ensure that `local.properties` is listed in `.gitignore`.
- In production, use a secure key management service or call APIs from a backend server.

## Tech Stack

- **Kotlin** — main programming language
- **Jetpack Compose** — modern UI toolkit
- **Firebase Authentication** — user login system
- **Hilt** — dependency injection
- **Coroutines** — asynchronous programming
- **Retrofit2 / OkHttp** — HTTP client for networking
- **OpenAI GPT-4o API** — for intelligent image-to-data conversion