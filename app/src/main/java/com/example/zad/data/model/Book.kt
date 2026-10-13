package com.example.zad.data.model

/**
 * Type of textbook listing.
 */
enum class ListingType {
    SALE,
    DONATION
}

/**
 * Physical condition of the textbook.
 */
enum class BookCondition {
    NEW,
    VERY_GOOD,
    GOOD,
    ACCEPTABLE
}

/**
 * Availability status of the textbook listing.
 */
enum class ListingStatus {
    AVAILABLE,
    COMPLETED
}

/**
 * Core model representing a university textbook listing in Zad.
 *
 * Designed to be clean, lightweight, and independent of any backend SDK.
 */
data class Book(
    val id: String,
    val title: String,
    val author: String? = null,
    val description: String? = null,
    val major: String,
    val courseName: String? = null,
    val condition: BookCondition,
    val listingType: ListingType,
    val price: Double? = null,
    val imageUrl: String? = null,
    val contactInfo: String,
    val ownerId: String = "local_user",
    val createdAt: Long = System.currentTimeMillis(),
    val status: ListingStatus = ListingStatus.AVAILABLE
)
