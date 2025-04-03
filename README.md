# Timetable App

An Android app that converts timetable images into a digital format using GPT-4o.

Features
	•	Firebase-based user authentication (Login / Sign Up)
	•	Upload and select timetable images
	•	Image analysis via GPT-4o
	•	Extract and display timetable data
	•	Responsive UI design

Installation & Setup
	1.	Clone the repository:

git clone https://github.com/your-username/timetable.git
cd timetable


	2.	Set your OpenAI API key:
	•	Add the following line to your local.properties file:

openai.api.key=your_openai_api_key


	3.	Configure Firebase:
	•	Create a new project in the Firebase Console
	•	Add an Android app with the package name com.company.timetable
	•	Download the google-services.json file and place it in the app/ directory
	•	Enable Email/Password authentication in Firebase Authentication
	4.	Run the app:
	•	Open the project in Android Studio and sync Gradle
	•	Click the run button to launch the app

Security Notes
	•	Never include API keys directly in the source code
	•	Always add local.properties to your .gitignore file
	•	In production, consider using a secure key management system or calling APIs from a backend server

Tech Stack
	•	Kotlin
	•	Jetpack Compose
	•	Firebase Authentication
	•	Hilt (Dependency Injection)
	•	Coroutines
	•	Retrofit2 / OkHttp
	•	OpenAI GPT-4o API

