package com.example.viewmodel

import androidx.lifecycle.ViewModel
import com.example.data.SampleData
import com.example.model.ActivityLog
import com.example.model.ActivityType
import com.example.model.Book
import com.example.model.LibrarianUser
import com.example.model.Loan
import com.example.model.LoanStatus
import com.example.model.NavDestination
import com.example.model.Student
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID

class LibraryViewModel : ViewModel() {

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _currentUser = MutableStateFlow(LibrarianUser())
    val currentUser: StateFlow<LibrarianUser> = _currentUser.asStateFlow()

    private val _currentDestination = MutableStateFlow(NavDestination.DASHBOARD)
    val currentDestination: StateFlow<NavDestination> = _currentDestination.asStateFlow()

    private val _selectedBookId = MutableStateFlow<String?>("b1")
    val selectedBookId: StateFlow<String?> = _selectedBookId.asStateFlow()

    private val _editingBook = MutableStateFlow<Book?>(null)
    val editingBook: StateFlow<Book?> = _editingBook.asStateFlow()

    private val _books = MutableStateFlow(SampleData.initialBooks)
    val books: StateFlow<List<Book>> = _books.asStateFlow()

    private val _students = MutableStateFlow(SampleData.initialStudents)
    val students: StateFlow<List<Student>> = _students.asStateFlow()

    private val _loans = MutableStateFlow(SampleData.initialLoans)
    val loans: StateFlow<List<Loan>> = _loans.asStateFlow()

    private val _activities = MutableStateFlow(SampleData.initialActivities)
    val activities: StateFlow<List<ActivityLog>> = _activities.asStateFlow()

    // Filters & Search
    private val _bookSearchQuery = MutableStateFlow("")
    val bookSearchQuery: StateFlow<String> = _bookSearchQuery.asStateFlow()

    private val _selectedGenre = MutableStateFlow("All")
    val selectedGenre: StateFlow<String> = _selectedGenre.asStateFlow()

    private val _studentSearchQuery = MutableStateFlow("")
    val studentSearchQuery: StateFlow<String> = _studentSearchQuery.asStateFlow()

    private val _returnSearchQuery = MutableStateFlow("")
    val returnSearchQuery: StateFlow<String> = _returnSearchQuery.asStateFlow()

    private val _feedbackMessage = MutableStateFlow<String?>(null)
    val feedbackMessage: StateFlow<String?> = _feedbackMessage.asStateFlow()

    // Pre-selection helpers when jumping from book or student into Borrow tab
    private val _preselectedStudentId = MutableStateFlow<String?>(null)
    val preselectedStudentId: StateFlow<String?> = _preselectedStudentId.asStateFlow()

    private val _preselectedBookId = MutableStateFlow<String?>(null)
    val preselectedBookId: StateFlow<String?> = _preselectedBookId.asStateFlow()

    fun login(email: String, pass: String): Boolean {
        if (email.isNotBlank() && pass.isNotBlank()) {
            _isLoggedIn.value = true
            _currentDestination.value = NavDestination.DASHBOARD
            _feedbackMessage.value = "Welcome back, Sarah Jenkins!"
            return true
        }
        return false
    }

    fun logout() {
        _isLoggedIn.value = false
        _currentDestination.value = NavDestination.DASHBOARD
    }

    fun navigate(destination: NavDestination) {
        _currentDestination.value = destination
    }

    fun setBookSearchQuery(query: String) {
        _bookSearchQuery.value = query
    }

    fun setSelectedGenre(genre: String) {
        _selectedGenre.value = genre
    }

    fun setStudentSearchQuery(query: String) {
        _studentSearchQuery.value = query
    }

    fun setReturnSearchQuery(query: String) {
        _returnSearchQuery.value = query
    }

    fun clearFeedback() {
        _feedbackMessage.value = null
    }

    fun openBookDetail(book: Book) {
        _selectedBookId.value = book.id
        _currentDestination.value = NavDestination.BOOK_DETAIL
    }

    fun openAddBook() {
        _editingBook.value = null
        _currentDestination.value = NavDestination.ADD_EDIT_BOOK
    }

    fun openEditBook(book: Book) {
        _editingBook.value = book
        _currentDestination.value = NavDestination.ADD_EDIT_BOOK
    }

    fun saveBook(
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
    ) {
        val existing = _editingBook.value
        if (existing == null) {
            val newBook = Book(
                id = "b_${UUID.randomUUID().toString().take(6)}",
                title = title.trim(),
                author = author.trim(),
                isbn = isbn.trim(),
                genre = genre.trim(),
                publisher = publisher.trim(),
                totalCopies = totalCopies,
                availableCopies = totalCopies,
                shelfLocation = shelfLocation.ifBlank { "Stack General" },
                summary = summary.trim(),
                publicationYear = publicationYear,
                coverGradientIndex = coverGradientIndex
            )
            _books.value = listOf(newBook) + _books.value
            addActivity(
                title = "New Book Cataloged: ${newBook.title}",
                subtitle = "Total copies: ${newBook.totalCopies} • ${newBook.genre}",
                type = ActivityType.ADD_BOOK
            )
            _feedbackMessage.value = "Book '${newBook.title}' added to catalog!"
        } else {
            val diff = totalCopies - existing.totalCopies
            val newAvailable = (existing.availableCopies + diff).coerceAtLeast(0)
            val updated = existing.copy(
                title = title.trim(),
                author = author.trim(),
                isbn = isbn.trim(),
                genre = genre.trim(),
                publisher = publisher.trim(),
                totalCopies = totalCopies,
                availableCopies = newAvailable,
                shelfLocation = shelfLocation.ifBlank { existing.shelfLocation },
                summary = summary.trim(),
                publicationYear = publicationYear,
                coverGradientIndex = coverGradientIndex
            )
            _books.value = _books.value.map { if (it.id == updated.id) updated else it }
            _feedbackMessage.value = "Book '${updated.title}' details updated!"
        }
        _currentDestination.value = NavDestination.BOOKS
    }

    fun deleteBook(bookId: String) {
        val book = _books.value.find { it.id == bookId }
        _books.value = _books.value.filterNot { it.id == bookId }
        _feedbackMessage.value = "Removed '${book?.title ?: "book"}' from library."
        if (_currentDestination.value == NavDestination.BOOK_DETAIL) {
            _currentDestination.value = NavDestination.BOOKS
        }
    }

    fun addStudent(
        studentNumber: String,
        name: String,
        email: String,
        department: String,
        grade: String
    ) {
        val newStudent = Student(
            id = "s_${UUID.randomUUID().toString().take(6)}",
            studentNumber = studentNumber.trim().ifBlank { "STU-${(1000..9999).random()}" },
            name = name.trim(),
            email = email.trim(),
            department = department.trim(),
            grade = grade.trim(),
            activeLoansCount = 0,
            totalBorrowedCount = 0
        )
        _students.value = _students.value + newStudent
        _feedbackMessage.value = "Student ${newStudent.name} registered successfully!"
    }

    fun prepareBorrow(studentId: String? = null, bookId: String? = null) {
        _preselectedStudentId.value = studentId
        _preselectedBookId.value = bookId
        _currentDestination.value = NavDestination.BORROW_RETURN
    }

    fun borrowBook(studentId: String, bookId: String, durationDays: Int = 14): Boolean {
        val book = _books.value.find { it.id == bookId }
        val student = _students.value.find { it.id == studentId }

        if (book == null || student == null) {
            _feedbackMessage.value = "Error: Please select both a student and a book."
            return false
        }

        if (book.availableCopies <= 0) {
            _feedbackMessage.value = "Error: No copies of '${book.title}' currently available."
            return false
        }

        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val now = Calendar.getInstance()
        val borrowDateStr = sdf.format(now.time)
        now.add(Calendar.DAY_OF_YEAR, durationDays)
        val dueDateStr = sdf.format(now.time)

        val newLoan = Loan(
            id = "l_${UUID.randomUUID().toString().take(6)}",
            bookId = book.id,
            bookTitle = book.title,
            bookAuthor = book.author,
            studentId = student.id,
            studentName = student.name,
            studentNumber = student.studentNumber,
            borrowDate = borrowDateStr,
            dueDate = dueDateStr,
            status = LoanStatus.ACTIVE
        )

        _loans.value = listOf(newLoan) + _loans.value

        // Update book copy count
        _books.value = _books.value.map {
            if (it.id == book.id) it.copy(availableCopies = (it.availableCopies - 1).coerceAtLeast(0)) else it
        }

        // Update student borrow count
        _students.value = _students.value.map {
            if (it.id == student.id) it.copy(
                activeLoansCount = it.activeLoansCount + 1,
                totalBorrowedCount = it.totalBorrowedCount + 1
            ) else it
        }

        addActivity(
            title = "Book Borrowed: ${book.title}",
            subtitle = "Loaned to ${student.name} • Due $dueDateStr",
            type = ActivityType.BORROW
        )

        _feedbackMessage.value = "Loan created! Due on $dueDateStr"
        _preselectedStudentId.value = null
        _preselectedBookId.value = null
        return true
    }

    fun returnBook(loanId: String): Boolean {
        val loan = _loans.value.find { it.id == loanId } ?: return false
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val todayStr = sdf.format(Date())

        _loans.value = _loans.value.map {
            if (it.id == loanId) {
                it.copy(
                    status = LoanStatus.RETURNED,
                    returnDate = todayStr
                )
            } else it
        }

        // Restore book available copy
        _books.value = _books.value.map {
            if (it.id == loan.bookId) it.copy(
                availableCopies = (it.availableCopies + 1).coerceAtMost(it.totalCopies)
            ) else it
        }

        // Decrement student active loans
        _students.value = _students.value.map {
            if (it.id == loan.studentId) it.copy(
                activeLoansCount = (it.activeLoansCount - 1).coerceAtLeast(0)
            ) else it
        }

        addActivity(
            title = "Book Returned: ${loan.bookTitle}",
            subtitle = "Checked in from ${loan.studentName}",
            type = ActivityType.RETURN
        )

        _feedbackMessage.value = "'${loan.bookTitle}' checked in and returned to stack!"
        return true
    }

    fun collectFine(loanId: String) {
        val loan = _loans.value.find { it.id == loanId } ?: return
        val amount = loan.fineAmount
        returnBook(loanId)
        addActivity(
            title = "Fine Collected: $${String.format(Locale.US, "%.2f", amount)}",
            subtitle = "Settled for ${loan.bookTitle} by ${loan.studentName}",
            type = ActivityType.FINE_COLLECTED
        )
        _feedbackMessage.value = "Fine of $${String.format(Locale.US, "%.2f", amount)} collected & book returned."
    }

    fun sendOverdueReminder(loanId: String) {
        val loan = _loans.value.find { it.id == loanId } ?: return
        _feedbackMessage.value = "Overdue notice sent to ${loan.studentName} (${loan.studentNumber})!"
    }

    private fun addActivity(title: String, subtitle: String, type: ActivityType) {
        val sdf = SimpleDateFormat("h:mm a", Locale.US)
        val time = "Today, ${sdf.format(Date())}"
        val item = ActivityLog(
            id = "act_${UUID.randomUUID().toString().take(6)}",
            title = title,
            subtitle = subtitle,
            timestamp = time,
            type = type
        )
        _activities.value = listOf(item) + _activities.value.take(15)
    }
}
