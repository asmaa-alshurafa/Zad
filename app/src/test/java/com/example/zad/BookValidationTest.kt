package com.example.zad

import com.example.zad.data.model.Book
import com.example.zad.data.model.BookCondition
import com.example.zad.data.model.ListingStatus
import com.example.zad.data.model.ListingType
import com.example.zad.data.repository.InMemoryBookStore
import com.example.zad.feature.addbook.normalizeDigits
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.UUID

/**
 * Unit tests verifying textbook model rules, in-memory store, and validation helpers.
 */
class BookValidationTest {

    @Before
    fun setUp() {
        InMemoryBookStore.books.clear()
    }

    @Test
    fun bookCreation_saleWithPositivePrice_isValid() {
        val book = Book(
            id = UUID.randomUUID().toString(),
            title = "أساسيات قواعد البيانات",
            author = "أحمد خالد",
            major = "تكنولوجيا المعلومات",
            courseName = "CS201",
            condition = BookCondition.VERY_GOOD,
            listingType = ListingType.SALE,
            price = 25.0,
            contactInfo = "0591234567"
        )

        assertEquals("أساسيات قواعد البيانات", book.title)
        assertEquals(ListingType.SALE, book.listingType)
        assertEquals(25.0, book.price!!, 0.001)
        assertEquals(ListingStatus.AVAILABLE, book.status)
    }

    @Test
    fun bookCreation_donationWithNullPrice_isValid() {
        val book = Book(
            id = UUID.randomUUID().toString(),
            title = "مقدمة في هندسة البرمجيات",
            author = null,
            major = "هندسة البرمجيات",
            courseName = null,
            condition = BookCondition.GOOD,
            listingType = ListingType.DONATION,
            price = null,
            contactInfo = "0569876543"
        )

        assertEquals("مقدمة في هندسة البرمجيات", book.title)
        assertEquals(ListingType.DONATION, book.listingType)
        assertNull(book.price)
        assertNull(book.author)
        assertNull(book.courseName)
    }

    @Test
    fun inMemoryBookStore_addAndRetrieve_worksCorrectly() {
        val book1 = Book(
            id = "id-1",
            title = "كتاب 1",
            major = "تكنولوجيا المعلومات",
            condition = BookCondition.NEW,
            listingType = ListingType.SALE,
            price = 30.0,
            contactInfo = "0590000001"
        )
        val book2 = Book(
            id = "id-2",
            title = "كتاب 2",
            major = "إدارة أعمال",
            condition = BookCondition.ACCEPTABLE,
            listingType = ListingType.DONATION,
            price = null,
            contactInfo = "0590000002"
        )

        InMemoryBookStore.addBook(book1)
        InMemoryBookStore.addBook(book2)

        assertEquals(2, InMemoryBookStore.books.size)
        // Newest added is at the top
        assertEquals("id-2", InMemoryBookStore.books[0].id)
        assertEquals("id-1", InMemoryBookStore.books[1].id)

        val retrieved = InMemoryBookStore.getBookById("id-1")
        assertNotNull(retrieved)
        assertEquals("كتاب 1", retrieved?.title)

        val notFound = InMemoryBookStore.getBookById("non-existent")
        assertNull(notFound)
    }

    @Test
    fun normalizeDigits_convertsArabicIndicNumeralsCorrectly() {
        val arabicPrice = "٢٥"
        val normalized = arabicPrice.normalizeDigits()
        assertEquals("25", normalized)
        assertEquals(25.0, normalized.toDoubleOrNull()!!, 0.001)

        val arabicWithComma = "١٥٫٥"
        val normalizedDecimal = arabicWithComma.normalizeDigits()
        assertEquals("15.5", normalizedDecimal)
        assertEquals(15.5, normalizedDecimal.toDoubleOrNull()!!, 0.001)

        val mixedDigits = "1٢3"
        assertEquals("123", mixedDigits.normalizeDigits())
    }

    @Test
    fun priceValidation_requiresPositiveNumberForSale() {
        fun isValidSalePrice(input: String): Boolean {
            val parsed = input.trim().normalizeDigits().toDoubleOrNull()
            return parsed != null && parsed > 0.0 && !parsed.isNaN() && !parsed.isInfinite()
        }

        assertTrue(isValidSalePrice("25"))
        assertTrue(isValidSalePrice("٢٥"))
        assertTrue(isValidSalePrice("10.5"))
        assertTrue(isValidSalePrice("١٠٫٥"))

        // Invalid cases
        assertTrue(!isValidSalePrice("0"))
        assertTrue(!isValidSalePrice("٠"))
        assertTrue(!isValidSalePrice("-10"))
        assertTrue(!isValidSalePrice("abc"))
        assertTrue(!isValidSalePrice(""))
        assertTrue(!isValidSalePrice("   "))
    }
}
