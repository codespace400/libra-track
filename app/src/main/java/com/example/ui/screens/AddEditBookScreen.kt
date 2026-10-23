package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Book
import com.example.ui.components.BookCoverArt
import com.example.ui.components.coverPalettes
import com.example.ui.theme.AccentGold
import com.example.ui.theme.OverdueRed
import com.example.ui.theme.PrimaryNavy
import com.example.ui.theme.SurfaceSlateBorder
import com.example.ui.theme.TextPrimarySlate
import com.example.ui.theme.TextSecondarySlate

@Composable
fun AddEditBookScreen(
    editingBook: Book?,
    onSave: (
        title: String,
        author: String,
        isbn: String,
        genre: String,
        publisher: String,
        totalCopies: Int,
        shelfLocation: String,
        summary: String,
        publicationYear: Int,
        coverGradientIndex: Int
    ) -> Unit,
    onDelete: ((String) -> Unit)?,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    var title by remember { mutableStateOf(editingBook?.title ?: "") }
    var author by remember { mutableStateOf(editingBook?.author ?: "") }
    var isbn by remember { mutableStateOf(editingBook?.isbn ?: "") }
    var genre by remember { mutableStateOf(editingBook?.genre ?: "Computer Science") }
    var publisher by remember { mutableStateOf(editingBook?.publisher ?: "") }
    var totalCopiesStr by remember { mutableStateOf(editingBook?.totalCopies?.toString() ?: "5") }
    var shelfLocation by remember { mutableStateOf(editingBook?.shelfLocation ?: "Stack A-12, Shelf 2") }
    var summary by remember { mutableStateOf(editingBook?.summary ?: "") }
    var publicationYearStr by remember { mutableStateOf(editingBook?.publicationYear?.toString() ?: "2024") }
    var coverGradientIndex by remember { mutableIntStateOf(editingBook?.coverGradientIndex ?: 0) }

    var errorText by remember { mutableStateOf<String?>(null) }

    val dummyPreviewBook = remember(title, author, genre, coverGradientIndex) {
        Book(
            id = "preview",
            title = title.ifBlank { "Sample Book Title" },
            author = author.ifBlank { "Author Name" },
            isbn = isbn.ifBlank { "978-0000000000" },
            genre = genre.ifBlank { "General" },
            publisher = publisher.ifBlank { "Publisher" },
            totalCopies = totalCopiesStr.toIntOrNull() ?: 1,
            availableCopies = totalCopiesStr.toIntOrNull() ?: 1,
            shelfLocation = shelfLocation,
            summary = summary,
            publicationYear = publicationYearStr.toIntOrNull() ?: 2024,
            coverGradientIndex = coverGradientIndex
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack, modifier = Modifier.testTag("add_edit_back_btn")) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = PrimaryNavy
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = if (editingBook == null) "Catalog New Book" else "Edit Book Record",
                        color = TextPrimarySlate,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (editingBook == null) "Enter metadata and copy count to register into library system" else "Updating ISBN: ${editingBook.isbn}",
                        color = TextSecondarySlate,
                        fontSize = 12.sp
                    )
                }
            }

            if (editingBook != null && onDelete != null) {
                OutlinedButton(
                    onClick = { onDelete(editingBook.id) },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = OverdueRed),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("delete_book_btn")
                ) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Delete Book", fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Form Column
            Card(
                modifier = Modifier.weight(1.5f),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(SurfaceSlateBorder)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Bibliographic Metadata",
                        color = PrimaryNavy,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Book Title *") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("book_title_input"),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryNavy)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = author,
                            onValueChange = { author = it },
                            label = { Text("Author(s) *") },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("book_author_input"),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryNavy)
                        )

                        OutlinedTextField(
                            value = isbn,
                            onValueChange = { isbn = it },
                            label = { Text("ISBN-10 / ISBN-13 *") },
                            placeholder = { Text("978-...") },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("book_isbn_input"),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryNavy)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = genre,
                            onValueChange = { genre = it },
                            label = { Text("Genre / Category") },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("book_genre_input"),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryNavy)
                        )

                        OutlinedTextField(
                            value = publisher,
                            onValueChange = { publisher = it },
                            label = { Text("Publisher") },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("book_publisher_input"),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryNavy)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = totalCopiesStr,
                            onValueChange = { totalCopiesStr = it },
                            label = { Text("Total Copies *") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("book_copies_input"),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryNavy)
                        )

                        OutlinedTextField(
                            value = shelfLocation,
                            onValueChange = { shelfLocation = it },
                            label = { Text("Physical Shelf Location") },
                            placeholder = { Text("e.g. Stack B-04") },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("book_shelf_input"),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryNavy)
                        )
                    }

                    OutlinedTextField(
                        value = summary,
                        onValueChange = { summary = it },
                        label = { Text("Summary / Synopsis") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .testTag("book_summary_input"),
                        maxLines = 4,
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryNavy)
                    )

                    if (errorText != null) {
                        Text(
                            text = errorText ?: "",
                            color = OverdueRed,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = onBack,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Text("Cancel")
                        }

                        Button(
                            onClick = {
                                if (title.isBlank() || author.isBlank() || isbn.isBlank()) {
                                    errorText = "Please fill in all required fields (Title, Author, ISBN)."
                                    return@Button
                                }
                                val copies = totalCopiesStr.toIntOrNull() ?: 1
                                val year = publicationYearStr.toIntOrNull() ?: 2024
                                onSave(
                                    title,
                                    author,
                                    isbn,
                                    genre,
                                    publisher,
                                    copies,
                                    shelfLocation,
                                    summary,
                                    year,
                                    coverGradientIndex
                                )
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("save_book_btn")
                        ) {
                            Icon(imageVector = Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Save Book Record", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Cover Customization & Live Preview Column
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = androidx.compose.ui.graphics.SolidColor(SurfaceSlateBorder)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Cover Image & Binding",
                        color = PrimaryNavy,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.Start)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Select binding theme and view real-time catalog preview:",
                        color = TextSecondarySlate,
                        fontSize = 12.sp,
                        modifier = Modifier.align(Alignment.Start)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Live Art Cover Preview
                    BookCoverArt(
                        book = dummyPreviewBook,
                        width = 130.dp,
                        height = 190.dp
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Binding Spine & Palette",
                        color = TextPrimarySlate,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        coverPalettes.forEachIndexed { index, pair ->
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(pair.first)
                                    .border(
                                        width = if (coverGradientIndex == index) 3.dp else 1.dp,
                                        color = if (coverGradientIndex == index) AccentGold else SurfaceSlateBorder,
                                        shape = CircleShape
                                    )
                                    .clickable { coverGradientIndex = index }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Simulated Cover Art Upload Trigger
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .border(1.dp, PrimaryNavy.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                            .clickable {
                                // Cycle to a distinct cover style
                                coverGradientIndex = (coverGradientIndex + 1) % coverPalettes.size
                            }
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Image,
                                contentDescription = null,
                                tint = PrimaryNavy,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Upload / Cycle Cover Art",
                                color = PrimaryNavy,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}
