# Firebase Setup — Zad (ShareBox)

## ⚠️ الحالة الحالية: إعداد مستقبلي مقترح

**Firebase غير مُهيَّأ وغير مُدمج برمجياً في هذا المشروع.**
هذا الملف يوثّق الخطوات اليدوية والإرشادية المطلوبة لإعداده في حال تمت الموافقة الصريحة على دمجه في مرحلة مستقبلية.
كتابة هذا الدليل لا تعني تفعيل خدمات Firebase أو اتصال التطبيق بها.

> **تنبيه حول الإصدارات:** إصدارات الإضافات والمكتبات المذكورة في هذا الدليل (مثل Google Services Plugin و Firebase BoM) هي **إصدارات مرشحة مقترحة فقط** لم يتم التحقق الفعلي من توافقها ببناء ناجح في المشروع، ولن يتم اعتمادها أو إضافتها إلى Gradle إلا عند الموافقة على مرحلة التنفيذ الخاصة بها.

**تنبيه أمني دائم:** ملف `google-services.json` يحتوي على بيانات تعريفية للمشروع السحابي ويجب ألّا يُرفع أبداً إلى مستودع Git (مُدرج بالفعل ضمن `.gitignore`).

---

## الخدمات المقترحة مستقبلاً

| الخدمة | الاستخدام | متى |
|---|---|---|
| Firebase Authentication | تسجيل الدخول وإدارة المستخدمين | المرحلة التالية |
| Cloud Firestore | قاعدة البيانات الرئيسية | المرحلة التالية |
| Firebase Storage | رفع صور العناصر | عند تنفيذ رفع الصور |

---

## الخطوات اليدوية

### 1. إنشاء مشروع Firebase

1. انتقل إلى [Firebase Console](https://console.firebase.google.com).
2. انقر **Add project**.
3. اختر اسماً للمشروع (مثل `sharebox-prod`).
4. فعّل أو أوقف Google Analytics حسب الحاجة.
5. انقر **Create project**.

---

### 2. تسجيل تطبيق Android

1. داخل مشروع Firebase، انقر أيقونة Android **`</>`**.
2. أدخل **Android package name:** `com.example.zad`
3. أدخل **App nickname** (اختياري): `ShareBox`
4. أدخل **Debug signing certificate SHA-1** (اختياري للمصادقة بـ Google):
   ```bash
   ./gradlew signingReport
   # أو
   keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android
   ```
5. انقر **Register app**.

---

### 3. تنزيل `google-services.json`

1. بعد التسجيل، نزّل `google-services.json`.
2. ضعه في: `app/google-services.json` (**ليس** في المجلد الجذر).
3. تحقق: يجب ألّا يظهر في `git status` (مُدرج في `.gitignore`).

---

### 4. إضافة Google Services Plugin (إجراء مستقبلي مقترح)

بعد وضع `google-services.json`، يُقترح تجربة إضافة الإضافة بالنسخة المرشحة:

**`gradle/libs.versions.toml`** — في `[versions]`:
```toml
# إصدار مرشح مقترح (يخضع للاختبار ببناء فعلي)
googleServices = "4.4.2"
```

**`gradle/libs.versions.toml`** — في `[plugins]`:
```toml
google-services = { id = "com.google.gms.google-services", version.ref = "googleServices" }
```

**`build.gradle.kts`** (root) — في `plugins`:
```kotlin
alias(libs.plugins.google.services) apply false
```

**`app/build.gradle.kts`** — في `plugins`:
```kotlin
alias(libs.plugins.google.services)
```

---

### 5. إضافة Firebase Dependencies (إجراء مستقبلي مقترح)

**`gradle/libs.versions.toml`** — في `[versions]`:
```toml
# إصدار مرشح مقترح (يخضع للاختبار ببناء فعلي)
firebaseBom = "33.7.0"
```

**`gradle/libs.versions.toml`** — في `[libraries]`:
```toml
firebase-bom = { group = "com.google.firebase", name = "firebase-bom", version.ref = "firebaseBom" }
firebase-auth = { group = "com.google.firebase", name = "firebase-auth-ktx" }
firebase-firestore = { group = "com.google.firebase", name = "firebase-firestore-ktx" }
# أضف firebase-storage عند الحاجة لرفع الصور:
# firebase-storage = { group = "com.google.firebase", name = "firebase-storage-ktx" }
```

**`app/build.gradle.kts`** — في `dependencies`:
```kotlin
implementation(platform(libs.firebase.bom))
implementation(libs.firebase.auth)
implementation(libs.firebase.firestore)
```

> BoM يُدير الإصدارات تلقائياً — لا تُحدّد إصداراً لكل مكتبة على حدة.

---

### 6. تفعيل Authentication

1. في Firebase Console، افتح **Authentication > Sign-in method**.
2. فعّل: **Email/Password** (الحد الأدنى).
3. اختياري لاحقاً: Google Sign-In.

---

### 7. إنشاء Firestore Database

1. في Firebase Console، افتح **Firestore Database**.
2. انقر **Create database**.
3. اختر وضع **Production mode** (rules مقيِّدة افتراضياً).
4. اختر المنطقة الجغرافية الأقرب للمستخدمين.
5. انقر **Done**.

> **لا تستخدم Test mode** — يُتيح قراءة وكتابة كاملة لأي مستخدم.

---

### 8. إعداد Storage (عند الحاجة)

1. في Firebase Console، افتح **Storage**.
2. انقر **Get started**.
3. اختر Production mode.
4. اختر المنطقة (يجب أن تتطابق مع Firestore).
5. أضف `firebase-storage-ktx` عند تنفيذ رفع الصور فعلياً.

---

### 9. التحقق من الإعداد

بعد إتمام الخطوات:

```bash
./gradlew assembleDebug
```

إذا نجح البناء دون أخطاء متعلقة بـ `google-services`، فالإعداد الأساسي صحيح.

للتحقق من الاتصال الفعلي، أضف log بسيطاً في MainActivity:
```kotlin
// اختبار مؤقت فقط — احذفه بعد التحقق
val auth = Firebase.auth
Log.d("Firebase", "Auth initialized: ${auth.currentUser}")
```

---

## Firebase Emulator Suite (للتطوير المحلي)

يُوصى باستخدام Emulator Suite بدلاً من قاعدة البيانات الحقيقية أثناء التطوير:

```bash
# تثبيت Firebase CLI
npm install -g firebase-tools

# تسجيل الدخول
firebase login

# تهيئة المشروع
firebase init emulators

# تشغيل المحاكيات
firebase emulators:start
```

راجع [`docs/firebase-security.md`](firebase-security.md) لاختبار Security Rules بالمحاكي.
