package com.example.zad.data.repository

import androidx.compose.runtime.mutableStateListOf
import com.example.zad.data.model.Book

/**
 * Temporary in-memory state store for textbook listings.
 *
 * NOTE:
 * - This state is held in memory during the active session only.
 * - Listings do NOT persist across application restarts.
 * - This provides the simplest viable implementation to test the Add Book and Book Details flows.
 * - It will be replaced with a persistent repository once database integration is explicitly approved.
 */
object InMemoryBookStore {
    val books = mutableStateListOf<Book>()

    fun addBook(book: Book) {
        books.add(0, book)
    }

    fun getBookById(id: String): Book? {
        return books.find { it.id == id }
    }
}
