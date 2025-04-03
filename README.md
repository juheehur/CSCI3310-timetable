# Timetable App

시간표 이미지를 업로드하면 GPT-4o로 처리하여 디지털 시간표로 변환하는 안드로이드 앱입니다.

## 기능

- Firebase 기반 사용자 인증 (로그인/회원가입)
- 시간표 이미지 업로드 및 선택
- GPT-4o를 통한 이미지 분석
- 시간표 데이터 추출 및 표시
- 반응형 디자인

## 설치 및 설정

1. 저장소 복제:
```
git clone https://github.com/your-username/timetable.git
cd timetable
```

2. OpenAI API 키 설정:
   - `local.properties` 파일에 다음 라인을 추가하세요:
   ```
   openai.api.key=your_openai_api_key
   ```
   
3. Firebase 설정:
   - Firebase 콘솔에서 프로젝트를 생성합니다
   - 안드로이드 앱을 추가하고 패키지 이름을 `com.company.timetable`로 설정합니다
   - `google-services.json` 파일을 다운로드하여 `app/` 디렉토리에 추가합니다
   - Firebase Authentication에서 이메일/비밀번호 로그인을 활성화합니다

4. 앱 실행:
   - Android Studio에서 프로젝트를 열고 Gradle 동기화를 수행합니다
   - 실행 버튼을 클릭하여 앱을 실행합니다

## 보안 주의사항

- API 키는 소스 코드에 직접 포함하지 마세요
- `local.properties` 파일은 항상 `.gitignore`에 포함되어 있어야 합니다
- 프로덕션 환경에서는 키 관리 시스템 또는 백엔드 서버를 통한 호출을 고려하세요

## 기술 스택

- Kotlin
- Jetpack Compose
- Firebase Authentication
- Hilt 의존성 주입
- Coroutines
- Retrofit2 / OkHttp
- OpenAI GPT-4o API 