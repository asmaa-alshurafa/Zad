# AGENTS.md — Zad Android Project

تعليمات دائمة لجلسات الذكاء الاصطناعي المستقبلية. اقرأ هذا الملف في بداية كل جلسة قبل تعديل أي ملف.

---

## هوية المشروع (مُتحقَّق منها بالمستودع)

| العنصر | القيمة المؤكَّدة |
|---|---|
| Project name | `Zad` |
| Package | `com.example.zad` |
| Theme | `ZadTheme` |
| Language | Kotlin `2.0.21` |
| AGP | `8.13.2` |
| Gradle | `8.13` |
| compileSdk | `36` |
| minSdk | `24` |

لا تُغيّر أياً من هذه القيم دون مبرر تقني وموافقة صريحة.

---

## رؤية المشروع

Zad (ShareBox) هو تطبيق أندرويد مجتمعي يتيح للمستخدمين نشر الأغراض التي لا يحتاجونها ومطالبة الآخرين بها. الجمهور الأولي طلاب الجامعات.

---

## قاعدة الأولى: افحص قبل أن تُعدّل

قبل تعديل أي ملف:
1. اقرأ الملف الفعلي — لا تفترض محتواه.
2. نفّذ `./gradlew assembleDebug` وسجّل النتيجة الفعلية.
3. راجع `git status --short` لمعرفة التغييرات غير المُرحَّلة.
4. افحص `gradle/libs.versions.toml` قبل إضافة أي تبعية.

---

## التبعيات الحالية المؤكَّدة

| المكتبة | الإصدار | المصدر |
|---|---|---|
| `androidx.core:core-ktx` | `1.15.0` | libs.versions.toml |
| `androidx.lifecycle:lifecycle-runtime-ktx` | `2.8.7` | libs.versions.toml |
| `androidx.activity:activity-compose` | `1.13.0` | libs.versions.toml |
| Compose BOM | `2024.09.00` | libs.versions.toml |
| `androidx.navigation:navigation-compose` | `2.8.4` | libs.versions.toml |

**التبعيات التالية غير موجودة ولا تُضاف دون موافقة صريحة:**
- Firebase (BoM, Auth, Firestore, Storage)
- Hilt / KSP
- Coroutines (standalone)
- Lifecycle ViewModel Compose
- Room, Retrofit

---

## ما هو مُطبَّق فعلاً في الكود (Milestone 0)

| الملف | الحالة |
|---|---|
| `MainActivity.kt` | ✅ موجود — يُشغّل AppNavGraph |
| `navigation/AppDestination.kt` | ✅ موجود — sealed class بوجهة Home |
| `navigation/AppNavGraph.kt` | ✅ موجود — NavHost يربط Home |
| `feature/home/HomeScreen.kt` | ✅ موجود — شاشة ترحيبية مؤقتة |
| `ui/theme/` (Color, Theme, Type) | ✅ موجود — ZadTheme |
| Domain layer | ❌ غير موجودة بعد |
| Data layer | ❌ غير موجودة بعد |
| ViewModels | ❌ لا يوجد |
| Firebase | ❌ غير مُهيَّأ |
| Hilt | ❌ غير مُضاف |

---

## قواعد وضع الملفات (للمراحل المستقبلية)

| النوع | المكان المقترح |
|---|---|
| الشاشات | `feature/<feature-name>/` |
| تعريفات Navigation | `navigation/` |
| Theme, Colors, Typography | `ui/theme/` (الموجود) |
| Repository interfaces | `domain/repository/` |
| Domain models | `domain/model/` |
| Repository implementations | `data/repository/` |
| Firebase data sources | `data/remote/firebase/` |
| Hilt modules | `di/` |
| Shared UI components | `core/designsystem/components/` |
| Error types | `core/error/` |

---

## اتفاقيات Kotlin وCompose

- أسماء الشاشات تنتهي بـ `Screen`.
- أسماء الـ ViewModels تنتهي بـ `ViewModel`.
- UI state: `data class` غير قابلة للتعديل، تنتهي بـ `UiState`.
- لا تضع business logic في Composable functions.
- لا تضع feature logic في `MainActivity`.
- اجعل الـ `@Preview` خاصاً (`private`) دون مبرر للعمومية.

---

## قواعد البناء والتحقق

```bash
./gradlew assembleDebug    # بناء debug — الأمر الأساسي للتحقق
./gradlew test             # Unit Tests على الجهاز المحلي
./gradlew connectedAndroidTest  # Instrumented (يحتاج جهازاً)
./gradlew lint             # فحص الكود
```

- نفّذ `assembleDebug` **قبل وبعد** كل تعديل.
- سجّل النتيجة الفعلية — لا تفترض النجاح.
- إذا فشل البناء، أوقف وشخّص قبل أي تغيير إضافي.
- أفصل بين أخطاء الكود وأخطاء الإعداد الخارجي (مثل `google-services.json`).

---

## قواعد التغيير

1. **أقل تعديل ممكن** — لا تُعدّل ما لا علاقة له بالمهمة.
2. **لا ملفات فارغة** أو placeholder classes.
3. **لا تبعيات جديدة** بدون موافقة صريحة من صاحب المشروع.
4. **لا تُغيّر إصدارات التبعيات** دون سبب تقني مُبرَّر ومؤكَّد بالبناء.
5. **وثّق كل ملف تُنشئه** في `docs/project-structure.md`.
6. **لا تدّعي التوافق** إلا بعد نجاح `assembleDebug` الفعلي.
7. **انتظر الموافقة الصريحة** قبل البدء في أي Milestone جديد.

---

## قواعد Firebase (للمراحل المستقبلية فقط)

- لا تُضف `google-services.json` إلى Git — مُدرج في `.gitignore`.
- لا تُضف Firebase credentials في الكود أو الوثائق.
- لا تدّعي أن Firebase متصل دون التحقق الفعلي.
- Firestore Security Rules تُختبر بـ Firebase Emulator Suite قبل النشر.
- لا تُنشئ مسودة Security Rules دون توثيح واضح أنها مسودة غير جاهزة للإنتاج.

---

## ما هو خارج النطاق دائماً

- n8n أو أي automation workflow.
- AI matching services.
- نشر على Firebase أو تغيير live cloud resources.
- تغييرات signing configuration أو production build settings.
