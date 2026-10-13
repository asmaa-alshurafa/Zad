# Firebase Security — Zad (ShareBox)

## ⚠️ Example — Not Deployed (مسودة توضيحية — غير منشورة وغير نهائية)

قواعد Firestore Security الواردة أدناه هي **مجرد مثال توضيحي ومسودة أولية غير منشورة** مبنية على نموذج البيانات المقترح مستقبلاً في [`docs/data-model.md`](data-model.md).
القواعد النهائية تعتمد اعتماداً كلياً على نموذج البيانات الفعلي عند اعتماده وعلى متطلبات الصلاحيات الدقيقة بعد تطبيقها واختبارها.

**تنبيه حاسم:**
- هذه القواعد **لم تُنشر** على أي بيئة سحابية، ولا يوجد أي مشروع Firebase مرتبط بالكود حالياً.
- لا يجوز اعتبار هذه القواعد جاهزة للإنتاج أو آمنة تماماً بدون اختبار شامل.
- يجب مراجعة واختبار القواعد باستخدام Firebase Emulator Suite قبل أي اعتماد أو نشر مستقبلي.
- يتطلب أي نشر للقواعد موافقة صريحة من صاحب المشروع.

---

## مبادئ الأمان المقترحة

| المبدأ | التطبيق المقترح |
|---|---|
| المصادقة أولاً | كل عملية تتطلب `request.auth != null` |
| الملكية | المستخدم يُعدّل بياناته فقط |
| منع الطلب الذاتي | يُمنع طلب عنصر يملكه نفس المستخدم |
| التحقق من الطبقة الصحيحة | التحقق من جهة العميل (Client validation) ليس حداً أمنياً كافياً |
| حماية المفاتيح | `google-services.json` مستبعد في `.gitignore` |
| لا امتيازات عليا في التطبيق | لا يُضمَّن Admin SDK أو مفاتيح سرية في تطبيق Android |

---

## Example — Not Deployed: مسودة `firestore.rules` توضيحية

```firestore
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {

    // ─── USERS ─────────────────────────────────────────────────────
    match /users/{userId} {
      // أي مستخدم مصادَق يقرأ الملفات الشخصية
      allow read: if request.auth != null;

      // المستخدم يُعدّل ملفه الشخصي فقط
      allow write: if request.auth != null
                   && request.auth.uid == userId;
    }

    // ─── ITEMS ─────────────────────────────────────────────────────
    match /items/{itemId} {
      // أي مستخدم مصادَق يقرأ العناصر
      allow read: if request.auth != null;

      // إنشاء: صاحب العنصر هو المستخدم الحالي
      allow create: if request.auth != null
                    && request.resource.data.ownerId == request.auth.uid;

      // تعديل/حذف: المالك فقط
      allow update, delete: if request.auth != null
                            && resource.data.ownerId == request.auth.uid;
    }

    // ─── NEEDS ─────────────────────────────────────────────────────
    match /needs/{needId} {
      allow read: if request.auth != null;

      allow create: if request.auth != null
                    && request.resource.data.userId == request.auth.uid;

      allow update, delete: if request.auth != null
                            && resource.data.userId == request.auth.uid;
    }

    // ─── REQUESTS ──────────────────────────────────────────────────
    match /requests/{requestId} {
      // المطالب أو المالك يقرآن الطلب
      allow read: if request.auth != null
                  && (resource.data.requesterId == request.auth.uid
                      || resource.data.ownerId == request.auth.uid);

      // إنشاء: المطالب هو المستخدم الحالي
      //        ويُمنع طلب عنصر المستخدم نفسه
      allow create: if request.auth != null
                    && request.resource.data.requesterId == request.auth.uid
                    && request.resource.data.requesterId != request.resource.data.ownerId;

      // تعديل: المالك فقط يغيّر status (قبول/رفض)
      //        المطالب فقط يلغي طلبه
      allow update: if request.auth != null
                    && (resource.data.ownerId == request.auth.uid
                        || resource.data.requesterId == request.auth.uid);

      // لا حذف — الطلبات تُحتفظ للسجل التاريخي
      allow delete: if false;
    }

    // ─── NOTIFICATIONS ─────────────────────────────────────────────
    match /notifications/{notificationId} {
      // المستلم فقط يقرأ إشعاراته
      allow read: if request.auth != null
                  && resource.data.recipientId == request.auth.uid;

      // الإشعارات تُنشأ بالـ server (Cloud Functions) — لا client
      allow create: if false;

      // المستلم يُعدّل isRead فقط
      allow update: if request.auth != null
                    && resource.data.recipientId == request.auth.uid
                    && request.resource.data.diff(resource.data).affectedKeys()
                       .hasOnly(['isRead']);

      allow delete: if false;
    }

    // كل ما لم يُذكر — مرفوض
    match /{document=**} {
      allow read, write: if false;
    }
  }
}
```

---

## سيناريوهات يجب اختبارها بالمحاكي

| السيناريو | النتيجة المتوقعة |
|---|---|
| مستخدم غير مصادَق يقرأ item | ❌ مرفوض |
| مستخدم يُعدّل عنصر لا يملكه | ❌ مرفوض |
| مستخدم يطلب عنصله الشخصي | ❌ مرفوض |
| مستخدم يقبل طلباً على عنصره | ✅ مسموح |
| مستخدم يلغي طلبه هو | ✅ مسموح |
| مستخدم يحذف طلب شخص آخر | ❌ مرفوض |
| client يُنشئ notification | ❌ مرفوض |
| مستخدم يُعدّل `isRead` في إشعاره | ✅ مسموح |
| مستخدم يُعدّل `recipientId` في إشعار | ❌ مرفوض |

---

## اختبار القواعد بالمحاكي

```bash
# تثبيت Firebase CLI
npm install -g firebase-tools

# تهيئة
firebase init emulators

# تشغيل
firebase emulators:start

# اختبار القواعد
firebase emulators:exec "npm test"
```

راجع [Firebase Security Rules testing documentation](https://firebase.google.com/docs/firestore/security/test-rules-emulator).

---

## قرارات غير مُحسومة (تحتاج مراجعة)

| القرار | الخيارات | الموقف |
|---|---|---|
| من يُنشئ الإشعارات؟ | Cloud Functions أو Triggers | **يُفضَّل Cloud Functions** — لم يُنفَّذ |
| تحديث `status` في Item عند قبول request | يدوياً أو تلقائياً | غير محدَّد |
| صلاحيات المشرف (admin) | قاعدة مخصَّصة أو Custom Claims | غير محدَّد |

**لا تنشر هذه القواعد قبل حسم النقاط أعلاه واختبارها.**
