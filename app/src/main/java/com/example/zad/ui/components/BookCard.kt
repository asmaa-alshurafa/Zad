package com.example.zad.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
 * Modern, minimalist card displaying textbook listing summary.
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
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
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
                // Price / Free badge
                val isSale = book.listingType == ListingType.SALE
                val priceLabel = if (isSale) {
                    val p = book.price
                    if (p != null) {
                        if (p % 1.0 == 0.0) "${p.toInt()} ₪" else "$p ₪"
                    } else {
                        stringResource(R.string.filter_sale)
                    }
                } else {
                    stringResource(R.string.filter_donation)
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (isSale) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.tertiaryContainer
                    }
                ) {
                    Text(
                        text = priceLabel,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (isSale) {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        } else {
                            MaterialTheme.colorScheme.onTertiaryContainer
                        },
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }

                // Condition badge
                val conditionLabel = when (book.condition) {
                    BookCondition.NEW -> stringResource(R.string.condition_new)
                    BookCondition.VERY_GOOD -> stringResource(R.string.condition_very_good)
                    BookCondition.GOOD -> stringResource(R.string.condition_good)
                    BookCondition.ACCEPTABLE -> stringResource(R.string.condition_acceptable)
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.background,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Text(
                        text = conditionLabel,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}
