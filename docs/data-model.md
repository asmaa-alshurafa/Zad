# Data Model — Zad (ShareBox)

## الحالة الحالية للنماذج

- **نموذج الكتب المُطبَّق (Phase 2):** تم تطبيق نموذج الكتب الأساسي [`Book`](file:///C:/Users/acc/Desktop/Zad/Zad/app/src/main/java/com/example/zad/data/model/Book.kt) والتصنيفات المرتبطة به (`ListingType` و `BookCondition` و `ListingStatus`) في المسار `data/model/Book.kt`. النموذج خفيف ومستقل تماماً عن أي مكتبات خارجية ويُخزن مؤقتاً في جلسة العمل عبر `InMemoryBookStore`.
- **النماذج المتبقية:** نماذج المستخدمين (`User`) والطلبات والإشعارات تظل مقترحة للمراحل القادمة عند ربط قواعد البيانات السحابية والمصادقة.

---

## نماذج النطاق المستقبلية المقترحة (Proposed Future Models)

> **تنبيه:** جميع النماذج والهياكل أدناه هي مقترحات تصميمية للمراحل القادمة وليست هياكل مُنفَّذة في الكود أو في قواعد بيانات حية.
> تُصمم نماذج النطاق (Domain Models) لتكون مستقلة تماماً عن أي مكتبات خارجية (بما فيها Firebase SDK)، ولذلك تُستخدم أنواع Kotlin القياسية (مثل `Long` للوقت بدلاً من `com.google.firebase.Timestamp`). التحويل من وإلى أنواع Firebase يتم حصراً داخل طبقة البيانات (Data Layer).

### User (مقترح مستقبلي)
```kotlin
data class User(
    val id: String,
    val displayName: String,
    val email: String,
    val photoUrl: String? = null,
    val createdAtEpochMs: Long
)
```
**مجموعة Firestore المقترحة:** `users/{userId}`  
**الملكية:** كل مستخدم يملك وثيقته ويُعدلها فقط.

---

### Item (مقترح مستقبلي)
```kotlin
data class Item(
    val id: String,
    val ownerId: String,
    val title: String,
    val description: String,
    val category: String,
    val condition: ItemCondition,   // NEW, LIKE_NEW, GOOD, FAIR
    val listingType: ListingType,   // FREE, EXCHANGE
    val imageUrls: List<String> = emptyList(),
    val status: ItemStatus,         // AVAILABLE, RESERVED, GIVEN
    val createdAtEpochMs: Long,
    val updatedAtEpochMs: Long
)
```
**مجموعة Firestore المقترحة:** `items/{itemId}`  
**الاستعلامات المتوقعة في المستقبل:**
- `where ownerId == currentUser.id` — عناصر المستخدم
- `where status == AVAILABLE` — العناصر المتاحة
- `where category == X` — فلترة حسب الفئة
- `orderBy createdAt desc` — الأحدث أولاً

---

### Need (مقترح مستقبلي)
```kotlin
data class Need(
    val id: String,
    val userId: String,
    val title: String,
    val description: String? = null,
    val category: String? = null,
    val isActive: Boolean,
    val createdAtEpochMs: Long
)
```
**مجموعة Firestore المقترحة:** `needs/{needId}`  
**الاستعلامات المتوقعة في المستقبل:**
- `where userId == currentUser.id` — احتياجات المستخدم
- `where isActive == true` — الاحتياجات النشطة

---

### ItemRequest (مقترح مستقبلي)
```kotlin
data class ItemRequest(
    val id: String,
    val itemId: String,
    val requesterId: String,
    val ownerId: String,
    val message: String? = null,
    val status: RequestStatus,   // PENDING, ACCEPTED, REJECTED, CANCELLED
    val createdAtEpochMs: Long,
    val updatedAtEpochMs: Long
)
```
**مجموعة Firestore المقترحة:** `requests/{requestId}`  
**الاستعلامات المتوقعة في المستقبل:**
- `where requesterId == currentUser.id` — طلباتي كمطالِب
- `where ownerId == currentUser.id` — الطلبات الواردة عليّ
- `where itemId == X` — الطلبات على عنصر معين

**قاعدة أمنية مقترحة:** `requesterId != ownerId` — يُمنع المستخدم من المطالبة بعنصره.

---

### AppNotification (مقترح مستقبلي)
```kotlin
data class AppNotification(
    val id: String,
    val recipientId: String,
    val type: NotificationType,   // REQUEST_RECEIVED, REQUEST_ACCEPTED, REQUEST_REJECTED
    val title: String,
    val body: String,
    val relatedItemId: String? = null,
    val relatedRequestId: String? = null,
    val isRead: Boolean,
    val createdAtEpochMs: Long
)
```
**مجموعة Firestore المقترحة:** `notifications/{notificationId}`  
**الاستعلامات المتوقعة في المستقبل:**
- `where recipientId == currentUser.id` — إشعاراتي
- `where recipientId == X && isRead == false` — الإشعارات غير المقروءة

---

## هيكل Firestore Collections

```
firestore/
├── users/
│   └── {userId}/             ← وثيقة المستخدم
├── items/
│   └── {itemId}/             ← وثيقة العنصر
├── needs/
│   └── {needId}/             ← وثيقة الاحتياج
├── requests/
│   └── {requestId}/          ← وثيقة الطلب
└── notifications/
    └── {notificationId}/     ← وثيقة الإشعار
```

**ملاحظات تصميم:**
- كل وثيقة تحمل `id` مساوياً لمعرّف Firestore لتسهيل التحويل.
- `ownerId` في `requests` مُكرَّر من `items` لتفعيل استعلامات `where ownerId` دون join.
- الصور تُخزَّن في Firebase Storage — `imageUrls` يحتوي روابطها فقط.
- Timestamps من نوع `com.google.firebase.Timestamp` في طبقة Data، وتُحوَّل إلى `Long` أو `kotlinx.datetime` في Domain.

---

## Enums المقترحة

```kotlin
enum class ItemCondition { NEW, LIKE_NEW, GOOD, FAIR }
enum class ListingType { FREE, EXCHANGE }
enum class ItemStatus { AVAILABLE, RESERVED, GIVEN }
enum class RequestStatus { PENDING, ACCEPTED, REJECTED, CANCELLED }
enum class NotificationType { REQUEST_RECEIVED, REQUEST_ACCEPTED, REQUEST_REJECTED }
```
