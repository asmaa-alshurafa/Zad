package com.example.zad.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.zad.R
import com.example.zad.data.model.Book
import com.example.zad.data.model.BookCondition
import com.example.zad.data.model.ListingType

/**
 * Reusable card displaying summary information for a textbook listing.
 */
@Composable
fun BookCard(
    book: Book,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Book Title
            Text(
                text = book.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            // Major & Course
            val academicInfo = buildString {
                append(book.major)
                if (!book.courseName.isNullOrBlank()) {
                    append(" • ")
                    append(book.courseName)
                }
            }
            Text(
                text = academicInfo,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Price/Donation and Condition Badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Price / Donation label
                val priceLabel = when (book.listingType) {
                    ListingType.SALE -> {
                        val p = book.price
                        if (p != null) {
                            if (p % 1.0 == 0.0) "${p.toInt()} ₪" else "$p ₪"
                        } else {
                            stringResource(R.string.filter_sale)
                        }
                    }
                    ListingType.DONATION -> stringResource(R.string.filter_donation)
                }

                SuggestionChip(
                    onClick = onClick,
                    label = {
                        Text(
                            text = priceLabel,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        containerColor = if (book.listingType == ListingType.SALE) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            MaterialTheme.colorScheme.tertiaryContainer
                        },
                        labelColor = if (book.listingType == ListingType.SALE) {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        } else {
                            MaterialTheme.colorScheme.onTertiaryContainer
                        }
                    )
                )

                // Condition badge
                val conditionLabel = when (book.condition) {
                    BookCondition.NEW -> stringResource(R.string.condition_new)
                    BookCondition.VERY_GOOD -> stringResource(R.string.condition_very_good)
                    BookCondition.GOOD -> stringResource(R.string.condition_good)
                    BookCondition.ACCEPTABLE -> stringResource(R.string.condition_acceptable)
                }

                Text(
                    text = conditionLabel,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}
