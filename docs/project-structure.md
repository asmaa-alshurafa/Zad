# Project Structure — Zad (ShareBox)

فهرس كل ملف ذي معنى في المشروع. يُحدَّث هذا الملف عند إضافة أي ملف جديد أو تغيير مسؤوليته.

---

## هيكل المشروع الكامل

```
Zad/                                          ← مجلد git repository
└── Zad/                                      ← مشروع Android الفعلي
    ├── AGENTS.md
    ├── README.md
    ├── .gitignore
    ├── build.gradle.kts
    ├── settings.gradle.kts
    ├── gradle.properties
    ├── gradlew / gradlew.bat
    ├── local.properties                      ← غير مُتتبَّع في Git
    ├── gradle/
    │   ├── libs.versions.toml
    │   └── wrapper/
    │       └── gradle-wrapper.properties
    ├── docs/
    │   ├── architecture.md
    │   ├── project-structure.md              ← هذا الملف
    │   ├── data-model.md
    │   ├── firebase-setup.md
    │   ├── firebase-security.md
    │   ├── implementation-roadmap.md
    │   └── decisions/
    │       └── ADR-001-architecture.md
    └── app/
        ├── build.gradle.kts
        ├── proguard-rules.pro
        └── src/
            ├── main/
            │   ├── AndroidManifest.xml
            │   ├── java/com/example/zad/
            │   │   ├── MainActivity.kt
            │   │   ├── navigation/
            │   │   │   ├── AppDestination.kt
            │   │   │   └── AppNavGraph.kt
            │   │   ├── data/
            │   │   │   ├── model/
            │   │   │   │   └── Book.kt
            │   │   │   └── repository/
            │   │   │       └── InMemoryBookStore.kt
            │   │   ├── feature/
            │   │   │   ├── home/
            │   │   │   │   └── HomeScreen.kt
            │   │   │   ├── addbook/
            │   │   │   │   └── AddBookScreen.kt
            │   │   │   └── bookdetails/
            │   │   │       └── BookDetailsScreen.kt
            │   │   └── ui/
            │   │       ├── components/
            │   │       │   └── BookCard.kt
            │   │       └── theme/
            │   │           ├── Color.kt
            │   │           ├── Theme.kt
            │   │           └── Type.kt
            │   └── res/
            │       ├── drawable/
            │       ├── mipmap-*/
            │       ├── values/
            │       │   ├── colors.xml
            │       │   ├── strings.xml
            │       │   └── themes.xml
            │       └── xml/
            │           ├── backup_rules.xml
            │           └── data_extraction_rules.xml
            ├── test/
            │   └── java/com/example/zad/
            │       └── ExampleUnitTest.kt
            └── androidTest/
                └── java/com/example/zad/
                    └── ExampleInstrumentedTest.kt
```

---

## جدول الملفات

### Gradle وإعداد المشروع

| المسار | المسؤولية | الطبقة | يعتمد عليه |
|---|---|---|---|
| `gradle/libs.versions.toml` | Version Catalog مركزي لكل التبعيات والإصدارات | إعداد | كل `build.gradle.kts` |
| `gradle/wrapper/gradle-wrapper.properties` | يُحدّد إصدار Gradle (8.13) | إعداد | Gradle wrapper |
| `build.gradle.kts` (root) | يُعلن عن plugins على مستوى المشروع — لا يُطبّقها | إعداد | `app/build.gradle.kts` |
| `app/build.gradle.kts` | يُهيّئ وحدة التطبيق: compileSdk، minSdk، dependencies | إعداد | البناء كله |
| `settings.gradle.kts` | يُعرّف اسم المشروع (`Zad`) والـ modules | إعداد | Gradle |
| `gradle.properties` | إعدادات JVM وAndroidX وR class | إعداد | Gradle daemon |
| `local.properties` | مسار Android SDK — **غير مُتتبَّع في Git** | إعداد | Gradle locally |
| `proguard-rules.pro` | قواعد ProGuard لبناء release — فارغة حالياً | إعداد | release build |
| `.gitignore` | يستثني: `.gradle`، `build/`، `local.properties`، `google-services.json`، مفاتيح التوقيع | إعداد | Git |

---

### Android Application

| المسار | المسؤولية | الطبقة | يعتمد عليه | ما يجب ألّا يفعله |
|---|---|---|---|---|
| `app/src/main/AndroidManifest.xml` | يُعرّف التطبيق: Activity، permissions، theme | إعداد | نظام Android | لا يحتوي بيانات أو منطق |
| `app/src/main/java/com/example/zad/MainActivity.kt` | Activity الوحيدة — تُطبّق ZadTheme وتُشغّل AppNavGraph | Presentation | AppNavGraph، ZadTheme | لا feature logic، لا data access |

---

### Navigation

| المسار | المسؤولية | الطبقة | يعتمد عليه | ما يجب ألّا يفعله |
|---|---|---|---|---|
| `navigation/AppDestination.kt` | Sealed class لوجهات التنقل: Home، AddBook، وBookDetails | Presentation | AppNavGraph | لا UI، لا منطق |
| `navigation/AppNavGraph.kt` | NavHost يربط مسارات الوجهات بالشاشات المناسبة | Presentation | AppDestination، الشاشات | لا منطق عمل، لا تخزين |

---

### Features & UI Components

| المسار | المسؤولية | الطبقة | يعتمد عليه | ما يجب ألّا يفعله |
|---|---|---|---|---|
| `feature/home/HomeScreen.kt` | شاشة العرض والاستكشاف الرئيسية — تتضمن البحث، الفلترة، قائمة الكتب، وحالة الفراغ | Presentation | ZadTheme، InMemoryBookStore، BookCard | لا استدعاءات شبكية أو قاعدة بيانات مباشرة |
| `feature/addbook/AddBookScreen.kt` | نموذج إضافة كتاب دراسي مع التحقق من الحقول الإلزامية والسعر والتواصل | Presentation | ZadTheme، InMemoryBookStore | لا حفظ دائم، لا رفع ملفات |
| `feature/bookdetails/BookDetailsScreen.kt` | شاشة عرض تفاصيل الكتاب ومعلومات التواصل المباشرة مع صاحب الكتاب | Presentation | ZadTheme، InMemoryBookStore | لا تعديل أو حذف في هذه المرحلة |
| `ui/components/BookCard.kt` | بطاقة مخصصة لإظهار ملخص بيانات الكتاب وحالته وسعره | Presentation | ZadTheme، Book | لا يقرر التنقل بذاته بل يستقبل onClick |

---

### Data Layer (النماذج ومخزن الحالة المؤقت)

| المسار | المسؤولية | الطبقة | يعتمد عليه | ما يجب ألّا يفعله |
|---|---|---|---|---|
| `data/model/Book.kt` | نموذج بيانات الكتاب الجامعي وتصنيفاته (ListingType, BookCondition, ListingStatus) | Data / Model | لا شيء | لا يعتمد على أي مكتبات خارجية |
| `data/repository/InMemoryBookStore.kt` | مخزن بيانات مؤقت في الذاكرة (In-Memory) لجلسة العمل الحالية | Data / Store | Book | لا يدّعي حفظ البيانات بعد إغلاق التطبيق |

---

### Theme وDesign System

| المسار | المسؤولية | الطبقة | يعتمد عليه | ما يجب ألّا يفعله |
|---|---|---|---|---|
| `ui/theme/Color.kt` | تعريفات الألوان الأساسية | Presentation | Theme.kt | لا منطق |
| `ui/theme/Theme.kt` | `ZadTheme` — يُطبّق MaterialTheme بألوان وخطوط وshapes | Presentation | كل الشاشات | لا business logic |
| `ui/theme/Type.kt` | تعريفات Typography (Material 3) | Presentation | Theme.kt | لا منطق |

> **ملاحظة:** الموقع المستقبلي المقصود هو `core/designsystem/theme/` — يُنقل عندما يكون هناك مبرر فعلي.

---

### Resources

| المسار | المسؤولية |
|---|---|
| `res/values/strings.xml` | نصوص قابلة للترجمة — حالياً `app_name = "Zad"` فقط |
| `res/values/themes.xml` | XML theme للنظام (`Theme.Zad`) |
| `res/values/colors.xml` | ألوان XML للنظام |
| `res/drawable/` | أصول SVG للأيقونة |
| `res/mipmap-*/` | أيقونات التطبيق بكثافات مختلفة |
| `res/xml/backup_rules.xml` | قواعد النسخ الاحتياطي لـ Android |
| `res/xml/data_extraction_rules.xml` | قواعد استخراج البيانات |

---

### Tests

| المسار | المسؤولية | الحالة |
|---|---|---|
| `test/.../ExampleUnitTest.kt` | اختبار وحدة أساسي (2+2=4) من Android Studio template | موجود — placeholder |
| `androidTest/.../ExampleInstrumentedTest.kt` | اختبار instrumented يتحقق من package name | موجود — placeholder |

> **ملاحظة:** الاختبارات الفعلية تُضاف عند وجود منطق يستحق الاختبار.

---

## ما لم يُنشأ بعد (مع السبب)

| المسار | سبب التأجيل |
|---|---|
| `ShareBoxApp.kt` | Hilt مؤجَّل — لا حاجة لـ Application class مخصَّص الآن |
| `di/` | Hilt مؤجَّل |
| `domain/model/` | لا feature تستخدم النماذج بعد |
| `domain/repository/` | لا Repository implementations بعد |
| `data/` | Firebase مؤجَّل |
| `core/error/AppError.kt` | لا ViewModel أو Repository يستخدمه بعد |
| `core/util/Result.kt` | لا use case يستدعيه بعد |
| `feature/auth/` وباقي الـ features | مراحل مستقبلية |
