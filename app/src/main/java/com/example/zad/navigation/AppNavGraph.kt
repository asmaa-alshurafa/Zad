package com.example.zad.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.zad.feature.addbook.AddBookScreen
import com.example.zad.feature.bookdetails.BookDetailsScreen
import com.example.zad.feature.home.HomeScreen

/**
 * Root navigation graph for the application.
 *
 * Connects:
 * - [AppDestination.Home] -> [HomeScreen]
 * - [AppDestination.AddBook] -> [AddBookScreen]
 * - [AppDestination.BookDetails] -> [BookDetailsScreen]
 */
@Composable
fun AppNavGraph(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = AppDestination.Home.route,
        modifier = modifier
    ) {
        composable(AppDestination.Home.route) {
            HomeScreen(
                onAddBookClick = {
                    navController.navigate(AppDestination.AddBook.route)
                },
                onBookClick = { bookId ->
                    navController.navigate(AppDestination.BookDetails.createRoute(bookId))
                }
            )
        }

        composable(AppDestination.AddBook.route) {
            AddBookScreen(
                onBackClick = { navController.popBackStack() },
                onBookAdded = { navController.popBackStack() }
            )
        }

        composable(
            route = AppDestination.BookDetails.route,
            arguments = listOf(
                navArgument("bookId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getString("bookId") ?: ""
            BookDetailsScreen(
                bookId = bookId,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
