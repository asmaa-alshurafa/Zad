package com.example.zad.navigation

/**
 * Defines all navigation destinations in the application.
 *
 * Each destination is a singleton object carrying a unique [route] string used
 * by Navigation Compose.
 */
sealed class AppDestination(val route: String) {
    data object Home : AppDestination("home")
    data object AddBook : AppDestination("add_book")
    data object BookDetails : AppDestination("book_details/{bookId}") {
        fun createRoute(bookId: String): String = "book_details/$bookId"
    }
}
