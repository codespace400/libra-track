package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Book
import com.example.model.Loan
import com.example.model.LoanStatus
import com.example.model.Student
import com.example.ui.components.BookCoverArt
import com.example.ui.components.LoanStatusBadge
import com.example.ui.theme.AccentGold
import com.example.ui.theme.AccentGoldContainer
import com.example.ui.theme.OverdueRed
import com.example.ui.theme.OverdueRedContainer
import com.example.ui.theme.PrimaryNavy
import com.example.ui.theme.PrimaryNavyContainer
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.SuccessGreenContainer
import com.example.ui.theme.SurfaceSlateBorder
import com.example.ui.theme.TextPrimarySlate
import com.example.ui.theme.TextSecondarySlate
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun BorrowReturnScreen(
    books: List<Book>,
    students: List<Student>,
    loans: List<Loan>,
    preselectedStudentId: String?,
    preselectedBookId: String?,
    onBorrowConfirm: (studentId: String, bookId: String, durationDays: Int) -> Boolean,
    onReturnConfirm: (loanId: String) -> Boolean,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Circulation Desk (Borrow & Return)",
                    color = TextPrimarySlate,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Issue book loans to enrolled students or process one-tap return check-ins",
                    color = TextSecondarySlate,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tabs
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.White,
            contentColor = PrimaryNavy,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, SurfaceSlateBorder, RoundedCornerShape(8.dp))
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.SwapHoriz, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Issue New Loan (Borrow)", fontWeight = FontWeight.Bold)
                    }
                },
                modifier = Modifier.testTag("tab_borrow")
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.AssignmentTurnedIn, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        val activeCount = loans.count { it.status == LoanStatus.ACTIVE || it.status == LoanStatus.OVERDUE }
                        Text("Process Returns ($activeCount On Loan)", fontWeight = FontWeight.Bold)
                    }
                },
                modifier = Modifier.testTag("tab_return")
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (selectedTab == 0) {
            BorrowSection(
                books = books,
                students = students,
                preselectedStudentId = preselectedStudentId,
                preselectedBookId = preselectedBookId,
                onBorrowConfirm = onBorrowConfirm
            )
        } else {
            ReturnSection(
                loans = loans,
                onReturnConfirm = onReturnConfirm
            )
        }
    }
}

@Composable
fun BorrowSection(
    books: List<Book>,
    students: List<Student>,
    preselectedStudentId: String?,
    preselectedBookId: String?,
    onBorrowConfirm: (studentId: String, bookId: String, durationDays: Int) -> Boolean
) {
    var studentSearch by remember { mutableStateOf("") }
    var bookSearch by remember { mutableStateOf("") }

    var selectedStudentId by remember { mutableStateOf(preselectedStudentId ?: students.firstOrNull()?.id ?: "") }
    var selectedBookId by remember { mutableStateOf(preselectedBookId ?: books.firstOrNull { it.availableCopies > 0 }?.id ?: "") }
    var loanDurationDays by remember { mutableIntStateOf(14) }

    val selectedStudent = students.find { it.id == selectedStudentId }
    val selectedBook = books.find { it.id == selectedBookId }

    // Auto-calculate Due Date string
    val calculatedDueDate = remember(loanDurationDays) {
        val sdf = SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.US)
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, loanDurationDays)
        sdf.format(cal.time)
    }

    val filteredStudents = remember(students, studentSearch) {
        if (studentSearch.isBlank()) students
        else students.filter { it.name.contains(studentSearch, true) || it.studentNumber.contains(studentSearch, true) }
    }

    val filteredBooks = remember(books, bookSearch) {
        if (bookSearch.isBlank()) books
        else books.filter { it.title.contains(bookSearch, true) || it.author.contains(bookSearch, true) || it.isbn.contains(bookSearch, true) }
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Step 1: Select Student Card
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
                    .padding(20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(PrimaryNavy),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("1", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Select Student",
                        color = PrimaryNavy,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = studentSearch,
                    onValueChange = { studentSearch = it },
                    placeholder = { Text("Search student name or ID...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = PrimaryNavy) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("borrow_student_search"),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryNavy),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "STUDENTS LIST",
                    color = Color(0xFF94A3B8),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    filteredStudents.forEach { student ->
                        val isSelected = student.id == selectedStudentId
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) PrimaryNavyContainer else Color(0xFFF8FAFC))
                                .border(
                                    1.dp,
                                    if (isSelected) PrimaryNavy else SurfaceSlateBorder,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { selectedStudentId = student.id }
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) PrimaryNavy else Color(0xFFE2E8F0)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = student.name.firstOrNull()?.toString() ?: "S",
                                    color = if (isSelected) Color.White else TextPrimarySlate,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = student.name,
                                    color = TextPrimarySlate,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${student.studentNumber} • ${student.department}",
                                    color = TextSecondarySlate,
                                    fontSize = 10.sp
                                )
                            }
                            if (isSelected) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = PrimaryNavy, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }
        }

        // Step 2: Select Book Card
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
                    .padding(20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(PrimaryNavy),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("2", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Select Book to Issue",
                        color = PrimaryNavy,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = bookSearch,
                    onValueChange = { bookSearch = it },
                    placeholder = { Text("Search title, author, or ISBN...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = PrimaryNavy) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("borrow_book_search"),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryNavy),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "AVAILABLE BOOKS",
                    color = Color(0xFF94A3B8),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    filteredBooks.forEach { book ->
                        val isSelected = book.id == selectedBookId
                        val isAvailable = book.availableCopies > 0
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) PrimaryNavyContainer else Color(0xFFF8FAFC))
                                .border(
                                    1.dp,
                                    if (isSelected) PrimaryNavy else SurfaceSlateBorder,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable(enabled = isAvailable) { selectedBookId = book.id }
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            BookCoverArt(book = book, width = 28.dp, height = 38.dp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = book.title,
                                    color = if (isAvailable) TextPrimarySlate else Color(0xFF94A3B8),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1
                                )
                                Text(
                                    text = "${book.author} • ${book.availableCopies} avail",
                                    color = if (isAvailable) Color(0xFF16A34A) else Color(0xFFDC2626),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            if (isSelected) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = PrimaryNavy, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }
        }

        // Step 3: Loan Parameters & Confirmation Card
        Card(
            modifier = Modifier.weight(1.1f),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = androidx.compose.ui.graphics.SolidColor(SurfaceSlateBorder)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(AccentGold),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("3", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Loan Term & Confirmation",
                        color = PrimaryNavy,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Loan Duration Period",
                    color = TextPrimarySlate,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf(7, 14, 21, 30).forEach { days ->
                        val isPicked = loanDurationDays == days
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isPicked) PrimaryNavy else Color(0xFFF1F5F9))
                                .clickable { loanDurationDays = days }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$days Days",
                                color = if (isPicked) Color.White else TextPrimarySlate,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Calculated Due Date Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(AccentGoldContainer.copy(alpha = 0.5f))
                        .border(1.dp, AccentGold.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = null,
                                tint = AccentGold,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "AUTO-CALCULATED DUE DATE",
                                color = Color(0xFF92400E),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = calculatedDueDate,
                            color = PrimaryNavy,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Loan Summary Box
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFF8FAFC))
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Student: ${selectedStudent?.name ?: "None selected"}",
                        color = TextPrimarySlate,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Book: ${selectedBook?.title ?: "None selected"}",
                        color = TextSecondarySlate,
                        fontSize = 12.sp
                    )
                    Text(
                        text = "Shelf: ${selectedBook?.shelfLocation ?: "N/A"}",
                        color = Color(0xFF94A3B8),
                        fontSize = 11.sp
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        if (selectedStudentId.isNotBlank() && selectedBookId.isNotBlank()) {
                            onBorrowConfirm(selectedStudentId, selectedBookId, loanDurationDays)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("confirm_borrow_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryNavy),
                    shape = RoundedCornerShape(8.dp),
                    enabled = selectedStudent != null && selectedBook != null && (selectedBook.availableCopies > 0)
                ) {
                    Icon(imageVector = Icons.Default.SwapHoriz, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Issue Loan Record", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ReturnSection(
    loans: List<Loan>,
    onReturnConfirm: (loanId: String) -> Boolean
) {
    var searchQuery by remember { mutableStateOf("") }
    val activeLoans = remember(loans, searchQuery) {
        loans.filter { it.status == LoanStatus.ACTIVE || it.status == LoanStatus.OVERDUE }
            .filter {
                searchQuery.isBlank() ||
                        it.bookTitle.contains(searchQuery, true) ||
                        it.studentName.contains(searchQuery, true) ||
                        it.studentNumber.contains(searchQuery, true)
            }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Search & Filter
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Filter active loans by student name, book title, or matric ID...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = PrimaryNavy) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear")
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().testTag("return_search_input"),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryNavy,
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Table
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = androidx.compose.ui.graphics.SolidColor(SurfaceSlateBorder)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            val horizontalScrollState = rememberScrollState()

            Column(modifier = Modifier.fillMaxSize()) {
                // Table Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF8FAFC))
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .horizontalScroll(horizontalScrollState)
                ) {
                    Row(
                        modifier = Modifier.width(960.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "BORROWED BOOK",
                            color = TextSecondarySlate,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.width(280.dp)
                        )
                        Text(
                            text = "STUDENT BORROWER",
                            color = TextSecondarySlate,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.width(220.dp)
                        )
                        Text(
                            text = "ISSUE DATE",
                            color = TextSecondarySlate,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.width(130.dp)
                        )
                        Text(
                            text = "DUE DATE",
                            color = TextSecondarySlate,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.width(130.dp)
                        )
                        Text(
                            text = "STATUS",
                            color = TextSecondarySlate,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.width(110.dp)
                        )
                        Text(
                            text = "ACTION",
                            color = TextSecondarySlate,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.width(90.dp)
                        )
                    }
                }

                HorizontalDivider(color = SurfaceSlateBorder)

                if (activeLoans.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.AssignmentTurnedIn,
                                contentDescription = null,
                                tint = Color(0xFF16A34A),
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "All books are currently returned!",
                                color = TextPrimarySlate,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .horizontalScroll(horizontalScrollState)
                    ) {
                        items(activeLoans, key = { it.id }) { loan ->
                            Row(
                                modifier = Modifier
                                    .width(960.dp)
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Book
                                Column(modifier = Modifier.width(280.dp)) {
                                    Text(
                                        text = loan.bookTitle,
                                        color = TextPrimarySlate,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1
                                    )
                                    Text(
                                        text = loan.bookAuthor,
                                        color = TextSecondarySlate,
                                        fontSize = 11.sp,
                                        maxLines = 1
                                    )
                                }

                                // Student
                                Column(modifier = Modifier.width(220.dp)) {
                                    Text(
                                        text = loan.studentName,
                                        color = TextPrimarySlate,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = loan.studentNumber,
                                        color = TextSecondarySlate,
                                        fontSize = 11.sp
                                    )
                                }

                                // Issue Date
                                Text(
                                    text = loan.borrowDate,
                                    color = TextSecondarySlate,
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily.Monospace,
                                    modifier = Modifier.width(130.dp)
                                )

                                // Due Date
                                Text(
                                    text = loan.dueDate,
                                    color = if (loan.status == LoanStatus.OVERDUE) OverdueRed else PrimaryNavy,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    modifier = Modifier.width(130.dp)
                                )

                                // Status Badge
                                Box(modifier = Modifier.width(110.dp)) {
                                    LoanStatusBadge(status = loan.status, daysOverdue = loan.daysOverdue)
                                }

                                // One-Tap Mark Returned Action
                                Box(modifier = Modifier.width(90.dp)) {
                                    Button(
                                        onClick = { onReturnConfirm(loan.id) },
                                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                                        shape = RoundedCornerShape(6.dp),
                                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                        modifier = Modifier
                                            .height(30.dp)
                                            .testTag("mark_returned_btn_${loan.id}")
                                    ) {
                                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Return", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                            HorizontalDivider(color = Color(0xFFF1F5F9))
                        }
                    }
                }
            }
        }
    }
}
