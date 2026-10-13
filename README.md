# Zad (ShareBox)

تطبيق أندرويد مجتمعي يتيح للمستخدمين نشر الأغراض التي لا يحتاجونها ومطالبة الآخرين بها. الجمهور الأولي طلاب الجامعات الذين يتشاركون الكتب والمواد الدراسية واللوازم الجامعية.

---

## حالة التطبيق الحالية

المشروع حالياً في المرحلة التأسيسية الأولى (Milestone 0) — الأساس التقني فقط:

| العنصر | الحالة في الكود |
|---|---|
| هيكل المشروع ونواة التنقل (Navigation Compose) | ✅ مُطبَّق |
| ثيم Material 3 (`ZadTheme`) | ✅ مُطبَّق |
| شاشة Home ترحيبية مؤقتة (`HomeScreen`) | ✅ مُطبَّق |
| Firebase (Auth, Firestore, Storage) | ⏳ دليل إعداد توثيقي فقط — غير مُدمج في الكود |
| طبقات المعمارية الكاملة (Domain و Data و ViewModels) | ⏳ مقترحة للمراحل القادمة — غير مُطبَّقة |
| المصادقة وإدارة المستخدمين | ⏳ مرحلة مستقبلية تتطلب موافقة صريحة |
| نشر وتصفح العناصر والطلبات | ⏳ مرحلة مستقبلية |

---

## المكدّس التقني الحالي (المُتحقَّق منه في الكود)

- **Language:** Kotlin 2.0.21
- **UI:** Jetpack Compose + Material 3 (Compose BOM 2024.09.00)
- **Architecture:** نواة طبقة العرض (Single-Activity + Navigation Compose) — تخطيط لاعتماد MVVM بثلاث طبقات في المراحل القادمة.
- **Navigation:** Navigation Compose 2.8.4
- **Build System:** Android Gradle Plugin 8.13.2 · Gradle 8.13 · compileSdk 36 · minSdk 24
- **Database / Auth:** غير مُدمجين حالياً (Cloud Firestore و Firebase Authentication مقترحان مستقبلاً).

---

## المتطلبات المسبقة

- Android Studio Ladybug أو أحدث
- Android SDK (API 36)
- JDK 11 أو أحدث
- حساب Firebase (للمراحل المستقبلية)

---

## كيفية فتح المشروع وبنائه

```bash
# استنساخ المستودع
git clone <repository-url>
cd Zad

# بناء debug APK
./gradlew assembleDebug

# تشغيل Unit Tests
./gradlew test
```

افتح مجلد `Zad/` (المجلد الداخلي) في Android Studio.

> **ملاحظة:** يحتاج المشروع إلى `local.properties` يحتوي على مسار Android SDK.
> يُنشئه Android Studio تلقائياً عند فتح المشروع.

---

## إعداد Firebase

Firebase غير مُضاف بعد. راجع [`docs/firebase-setup.md`](docs/firebase-setup.md) للخطوات اليدوية الكاملة.

---

## تشغيل الاختبارات

```bash
# Unit Tests
./gradlew test

# Instrumented Tests (يحتاج جهازاً أو محاكياً متصلاً)
./gradlew connectedAndroidTest
```

---

## الوثائق

| الملف | المحتوى |
|---|---|
| [`docs/architecture.md`](docs/architecture.md) | المعمارية، الطبقات، تدفق البيانات |
| [`docs/project-structure.md`](docs/project-structure.md) | فهرس كل ملف في المشروع |
| [`docs/data-model.md`](docs/data-model.md) | نماذج البيانات المقترحة وFirestore |
| [`docs/firebase-setup.md`](docs/firebase-setup.md) | خطوات إعداد Firebase يدوياً |
| [`docs/firebase-security.md`](docs/firebase-security.md) | مبادئ الأمان وقواعد Firestore |
| [`docs/implementation-roadmap.md`](docs/implementation-roadmap.md) | خريطة التطوير القادمة |
| [`docs/decisions/ADR-001-architecture.md`](docs/decisions/ADR-001-architecture.md) | قرار المعمارية الأولي |
| [`AGENTS.md`](AGENTS.md) | تعليمات للذكاء الاصطناعي |
