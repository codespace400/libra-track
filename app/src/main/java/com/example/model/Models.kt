package com.example.model

enum class BookStatus {
    AVAILABLE,
    LOW_STOCK,
    OUT_OF_STOCK
}

data class Book(
    val id: String,
    val title: String,
    val author: String,
    val isbn: String,
    val genre: String,
    val publisher: String,
    val totalCopies: Int,
    val availableCopies: Int,
    val shelfLocation: String,
    val summary: String,
    val publicationYear: Int,
    val coverGradientIndex: Int = 0
) {
    val status: BookStatus
        get() = when {
            availableCopies == 0 -> BookStatus.OUT_OF_STOCK
            availableCopies <= 2 -> BookStatus.LOW_STOCK
            else -> BookStatus.AVAILABLE
        }
}

data class Student(
    val id: String,
    val studentNumber: String,
    val name: String,
    val email: String,
    val department: String,
    val grade: String,
    val activeLoansCount: Int = 0,
    val totalBorrowedCount: Int = 0
)

enum class LoanStatus {
    ACTIVE,
    OVERDUE,
    RETURNED
}

data class Loan(
    val id: String,
    val bookId: String,
    val bookTitle: String,
    val bookAuthor: String,
    val studentId: String,
    val studentName: String,
    val studentNumber: String,
    val borrowDate: String,
    val dueDate: String,
    val returnDate: String? = null,
    val status: LoanStatus = LoanStatus.ACTIVE,
    val daysOverdue: Int = 0,
    val fineAmount: Double = 0.0
)

enum class ActivityType {
    BORROW,
    RETURN,
    ADD_BOOK,
    OVERDUE_ALERT,
    FINE_COLLECTED
}

data class ActivityLog(
    val id: String,
    val title: String,
    val subtitle: String,
    val timestamp: String,
    val type: ActivityType
)

enum class NavDestination(val label: String) {
    DASHBOARD("Dashboard"),
    BOOKS("Book Catalog"),
    STUDENTS("Students"),
    BORROW_RETURN("Borrow / Return"),
    OVERDUE("Overdue List"),
    REPORTS("Reports"),
    BOOK_DETAIL("Book Detail"),
    ADD_EDIT_BOOK("Add / Edit Book")
}

data class LibrarianUser(
    val name: String = "Sarah Jenkins",
    val title: String = "Head Admin Librarian",
    val email: String = "admin@libratrack.edu",
    val libraryName: String = "Central University Library"
)
