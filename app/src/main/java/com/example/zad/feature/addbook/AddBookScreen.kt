package com.example.zad.feature.addbook

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.zad.R
import com.example.zad.data.model.Book
import com.example.zad.data.model.BookCondition
import com.example.zad.data.model.ListingType
import com.example.zad.data.repository.InMemoryBookStore
import com.example.zad.ui.theme.ZadTheme
import java.util.UUID

/**
 * Screen for listing a university textbook in Zad.
 *
 * Implements strict validation:
 * - Title, major, condition, and contact info are required.
 * - Price is required and must be a positive number if listing type is SALE.
 * - Price is hidden and cleared when listing type is DONATION.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBookScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onBookAdded: () -> Unit = {}
) {
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var major by remember { mutableStateOf("") }
    var courseName by remember { mutableStateOf("") }
    var condition by remember { mutableStateOf(BookCondition.GOOD) }
    var listingType by remember { mutableStateOf(ListingType.SALE) }
    var priceText by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var contactInfo by remember { mutableStateOf("") }

    // Validation error states
    var titleError by remember { mutableStateOf(false) }
    var majorError by remember { mutableStateOf(false) }
    var priceError by remember { mutableStateOf(false) }
    var contactError by remember { mutableStateOf(false) }

    fun validateAndSave() {
        val trimmedTitle = title.trim()
        val trimmedMajor = major.trim()
        val trimmedContact = contactInfo.trim()

        titleError = trimmedTitle.isEmpty()
        majorError = trimmedMajor.isEmpty()
        contactError = trimmedContact.isEmpty()

        val normalizedPriceText = priceText.trim().normalizeDigits()
        val parsedPrice = normalizedPriceText.toDoubleOrNull()
        priceError = listingType == ListingType.SALE && (parsedPrice == null || parsedPrice <= 0.0 || parsedPrice.isNaN() || parsedPrice.isInfinite())

        if (!titleError && !majorError && !contactError && !priceError) {
            val newBook = Book(
                id = UUID.randomUUID().toString(),
                title = trimmedTitle,
                author = author.trim().ifEmpty { null },
                major = trimmedMajor,
                courseName = courseName.trim().ifEmpty { null },
                condition = condition,
                listingType = listingType,
                price = if (listingType == ListingType.SALE) parsedPrice else null,
                description = description.trim().ifEmpty { null },
                contactInfo = trimmedContact
            )
            InMemoryBookStore.addBook(newBook)
            onBookAdded()
        }
    }

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.colorScheme.surface,
        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        unfocusedBorderColor = MaterialTheme.colorScheme.outline
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.add_book_screen_title),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_navigation),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Book Title (Required)
                    OutlinedTextField(
                        value = title,
                        onValueChange = {
                            title = it
                            if (titleError && it.isNotBlank()) titleError = false
                        },
                        label = { Text(stringResource(R.string.field_title_label)) },
                        placeholder = { Text(stringResource(R.string.field_title_placeholder)) },
                        isError = titleError,
                        supportingText = if (titleError) {
                            { Text(stringResource(R.string.validation_title_required)) }
                        } else null,
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        colors = textFieldColors
                    )

                    // Author (Optional)
                    OutlinedTextField(
                        value = author,
                        onValueChange = { author = it },
                        label = { Text(stringResource(R.string.field_author_label)) },
                        placeholder = { Text(stringResource(R.string.field_author_placeholder)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        colors = textFieldColors
                    )

                    // Academic Major (Required)
                    OutlinedTextField(
                        value = major,
                        onValueChange = {
                            major = it
                            if (majorError && it.isNotBlank()) majorError = false
                        },
                        label = { Text(stringResource(R.string.field_major_label)) },
                        placeholder = { Text(stringResource(R.string.field_major_placeholder)) },
                        isError = majorError,
                        supportingText = if (majorError) {
                            { Text(stringResource(R.string.validation_major_required)) }
                        } else null,
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        colors = textFieldColors
                    )

                    // Course Name (Optional)
                    OutlinedTextField(
                        value = courseName,
                        onValueChange = { courseName = it },
                        label = { Text(stringResource(R.string.field_course_label)) },
                        placeholder = { Text(stringResource(R.string.field_course_placeholder)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        colors = textFieldColors
                    )
                }
            }

            // Listing Type & Price Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = stringResource(R.string.field_listing_type_label),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            selected = listingType == ListingType.SALE,
                            onClick = { listingType = ListingType.SALE },
                            label = { Text(stringResource(R.string.filter_sale)) },
                            shape = RoundedCornerShape(8.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                        FilterChip(
                            selected = listingType == ListingType.DONATION,
                            onClick = {
                                listingType = ListingType.DONATION
                                priceText = ""
                                priceError = false
                            },
                            label = { Text(stringResource(R.string.listing_type_donation)) },
                            shape = RoundedCornerShape(8.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        )
                    }

                    // Price Field (Visible only if SALE)
                    AnimatedVisibility(visible = listingType == ListingType.SALE) {
                        OutlinedTextField(
                            value = priceText,
                            onValueChange = {
                                priceText = it
                                if (priceError) priceError = false
                            },
                            label = { Text(stringResource(R.string.field_price_label)) },
                            placeholder = { Text(stringResource(R.string.field_price_placeholder)) },
                            isError = priceError,
                            supportingText = if (priceError) {
                                { Text(stringResource(R.string.validation_price_invalid)) }
                            } else null,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp),
                            colors = textFieldColors
                        )
                    }
                }
            }

            // Condition Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = stringResource(R.string.field_condition_label),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = condition == BookCondition.NEW,
                            onClick = { condition = BookCondition.NEW },
                            label = { Text(stringResource(R.string.condition_new)) },
                            shape = RoundedCornerShape(8.dp)
                        )
                        FilterChip(
                            selected = condition == BookCondition.VERY_GOOD,
                            onClick = { condition = BookCondition.VERY_GOOD },
                            label = { Text(stringResource(R.string.condition_very_good)) },
                            shape = RoundedCornerShape(8.dp)
                        )
                        FilterChip(
                            selected = condition == BookCondition.GOOD,
                            onClick = { condition = BookCondition.GOOD },
                            label = { Text(stringResource(R.string.condition_good)) },
                            shape = RoundedCornerShape(8.dp)
                        )
                        FilterChip(
                            selected = condition == BookCondition.ACCEPTABLE,
                            onClick = { condition = BookCondition.ACCEPTABLE },
                            label = { Text(stringResource(R.string.condition_acceptable)) },
                            shape = RoundedCornerShape(8.dp)
                        )
                    }
                }
            }

            // Description & Contact Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Description / Notes (Optional)
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text(stringResource(R.string.field_description_label)) },
                        placeholder = { Text(stringResource(R.string.field_description_placeholder)) },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        maxLines = 5,
                        shape = RoundedCornerShape(10.dp),
                        colors = textFieldColors
                    )

                    // Contact Info (Required)
                    OutlinedTextField(
                        value = contactInfo,
                        onValueChange = {
                            contactInfo = it
                            if (contactError && it.isNotBlank()) contactError = false
                        },
                        label = { Text(stringResource(R.string.field_contact_label)) },
                        placeholder = { Text(stringResource(R.string.field_contact_placeholder)) },
                        supportingText = {
                            if (contactError) {
                                Text(stringResource(R.string.validation_contact_required))
                            } else {
                                Text(stringResource(R.string.field_contact_helper))
                            }
                        },
                        isError = contactError,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        colors = textFieldColors
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Action Buttons: Save & Cancel
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onBackClick,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Text(
                        text = stringResource(R.string.action_cancel),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Button(
                    onClick = { validateAndSave() },
                    modifier = Modifier.weight(1.5f),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = stringResource(R.string.action_save_book),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AddBookScreenPreview() {
    ZadTheme {
        AddBookScreen()
    }
}

/**
 * Normalizes Eastern Arabic (Arabic-Indic) numerals and Arabic comma separators
 * into standard ASCII digits and decimal dots.
 */
internal fun String.normalizeDigits(): String {
    return this.map { char ->
        when (char) {
            '٠' -> '0'
            '١' -> '1'
            '٢' -> '2'
            '٣' -> '3'
            '٤' -> '4'
            '٥' -> '5'
            '٦' -> '6'
            '٧' -> '7'
            '٨' -> '8'
            '٩' -> '9'
            '٫', '،' -> '.'
            else -> char
        }
    }.joinToString("")
}
