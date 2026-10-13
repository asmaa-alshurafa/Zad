# Architecture — ShareBox (Zad)

---

## الحالة الحالية — ما هو مُطبَّق فعلاً

المشروع في Milestone 0. لا توجد طبقة Domain أو Data في الكود حالياً. البنية الموجودة هي:

```
MainActivity
    └── ZadTheme                   (ui/theme/Theme.kt)
            └── Scaffold
                    └── AppNavGraph (navigation/AppNavGraph.kt)
                            └── HomeScreen  (feature/home/HomeScreen.kt)
                                    [route: "home"]
```

### الملفات المُطبَّقة وأدوارها

| الملف | الدور الحالي |
|---|---|
| `MainActivity.kt` | Activity الوحيدة — تُطبّق ZadTheme وتُشغّل AppNavGraph داخل Scaffold |
| `navigation/AppDestination.kt` | `sealed class` يُعرّف route strings — حالياً وجهة واحدة: `Home` |
| `navigation/AppNavGraph.kt` | `NavHost` يربط كل route بشاشته — يملك `NavController` |
| `feature/home/HomeScreen.kt` | شاشة ترحيبية مؤقتة تعرض "مرحباً بك في ShareBox" |
| `ui/theme/Theme.kt` | `ZadTheme` — يُطبّق `MaterialTheme` مع دعم Dynamic Color (Android 12+) |
| `ui/theme/Color.kt` | ألوان Material 3 الافتراضية (Purple + Pink) |
| `ui/theme/Type.kt` | إعدادات Typography الأساسية |

### تدفق التنقل الحالي

```
تشغيل التطبيق
    → MainActivity.onCreate()
    → setContent { ZadTheme { Scaffold { AppNavGraph() } } }
    → NavHost يبدأ بـ startDestination = "home"
    → HomeScreen() تظهر
```

لإضافة شاشة جديدة:
1. أضف `data object NewScreen : AppDestination("new-screen")` في `AppDestination`.
2. أضف `composable(AppDestination.NewScreen.route) { NewScreen() }` في `AppNavGraph`.
3. أنشئ `feature/newscreen/NewScreen.kt`.

### القيود الحالية

- **لا ViewModel:** `HomeScreen` لا تملك ViewModel — تعرض محتوى ثابتاً فقط.
- **لا Domain layer:** لا نماذج بيانات، لا repository interfaces في الكود.
- **لا Data layer:** لا Firebase، لا data sources.
- **لا Hilt:** لا dependency injection.
- **لا Authentication:** لا تحقق من هوية المستخدم.
- `HomeScreen` مؤقتة وستُستبدل بالشاشة الحقيقية في Milestone 2.

---

## المعمارية المقترحة للمراحل المستقبلية

> **ملاحظة:** ما يلي مقترح وليس مُطبَّقاً. يتطلب موافقة صريحة لكل milestone.

### الطبقات الثلاث المقترحة

**Presentation** — `feature/`, `navigation/`, `ui/theme/`
- Compose screens + ViewModels + UI State.
- يعتمد على Domain (contracts + models) فقط.
- لا يستدعي Firebase مباشرةً.

**Domain** — `domain/model/`, `domain/repository/`
- نماذج البيانات (User, Item, ...) وrepository interfaces.
- مستقل تماماً عن Firebase SDK وCompose.

**Data** — `data/remote/firebase/`, `data/repository/`
- تُنفّذ domain repository interfaces.
- تحوّل Firebase documents إلى domain models.
- تحوّل Firebase exceptions إلى application-level errors.

### اتجاه التبعيات المقترح

```
Presentation  →  Domain  ←  Data
```

- Presentation لا تعرف شيئاً عن Data.
- Firebase محصور في Data.
- لا circular dependencies.

### نمط UI State المقترح

```kotlin
// مثال — لم يُنفَّذ بعد
data class HomeUiState(
    val isLoading: Boolean = false,
    val items: List<Item> = emptyList(),
    val errorMessage: String? = null
)
```

الـ ViewModel يُصدر الحالة عبر `StateFlow`. الحالات: Loading، Success، Empty، Error.

### قرارات التصميم المقترحة

| القرار | السبب |
|---|---|
| MVVM بدلاً من MVI | أبسط لمطوّر مبتدئ |
| لا Use Cases إلزامية | تُضاف فقط عند وجود business logic حقيقي |
| Hilt للـ DI (مؤجَّل) | لا dependencies تحتاج injection حتى تُوجد Repositories |
| Firebase في Data فقط | يُمكّن الاختبار بـ fakes |

راجع [`docs/decisions/ADR-001-architecture.md`](decisions/ADR-001-architecture.md) للتفاصيل.
راجع [`docs/implementation-roadmap.md`](implementation-roadmap.md) لترتيب الـ milestones.
