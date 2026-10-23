package com.example.data

import com.example.model.ActivityLog
import com.example.model.ActivityType
import com.example.model.Book
import com.example.model.Loan
import com.example.model.LoanStatus
import com.example.model.Student

object SampleData {
    val initialBooks = listOf(
        Book(
            id = "b1",
            title = "Introduction to Algorithms",
            author = "Thomas H. Cormen, Charles E. Leiserson",
            isbn = "978-0262033848",
            genre = "Computer Science",
            publisher = "MIT Press",
            totalCopies = 8,
            availableCopies = 3,
            shelfLocation = "Stack A-14, Shelf 3",
            summary = "Comprehensive textbook on algorithmic techniques, analysis, graph theory, and dynamic programming.",
            publicationYear = 2009,
            coverGradientIndex = 0
        ),
        Book(
            id = "b2",
            title = "Design Patterns: Elements of Reusable Object-Oriented Software",
            author = "Erich Gamma, Richard Helm, Ralph Johnson, John Vlissides",
            isbn = "978-0201633610",
            genre = "Computer Science",
            publisher = "Addison-Wesley",
            totalCopies = 6,
            availableCopies = 2,
            shelfLocation = "Stack A-14, Shelf 4",
            summary = "The classic Gang of Four reference cataloging 23 fundamental software architecture and design patterns.",
            publicationYear = 1994,
            coverGradientIndex = 1
        ),
        Book(
            id = "b3",
            title = "Clean Architecture: A Craftsman's Guide to Software Structure",
            author = "Robert C. Martin",
            isbn = "978-0134494166",
            genre = "Computer Science",
            publisher = "Prentice Hall",
            totalCopies = 5,
            availableCopies = 0,
            shelfLocation = "Stack A-15, Shelf 1",
            summary = "Practical rules and architectural principles for creating robust, decoupled, and maintainable systems.",
            publicationYear = 2017,
            coverGradientIndex = 2
        ),
        Book(
            id = "b4",
            title = "Principles of Neural Science",
            author = "Eric R. Kandel, James H. Schwartz",
            isbn = "978-0071390118",
            genre = "Medicine & Biology",
            publisher = "McGraw-Hill",
            totalCopies = 4,
            availableCopies = 1,
            shelfLocation = "Stack C-02, Shelf 2",
            summary = "Authoritative guide to cellular and molecular biology of neural development and cognition.",
            publicationYear = 2012,
            coverGradientIndex = 3
        ),
        Book(
            id = "b5",
            title = "To Kill a Mockingbird",
            author = "Harper Lee",
            isbn = "978-0061120084",
            genre = "Literature",
            publisher = "HarperCollins",
            totalCopies = 12,
            availableCopies = 7,
            shelfLocation = "Stack B-08, Shelf 1",
            summary = "Pulitzer Prize-winning masterpiece exploring empathy, racism, and justice in 1930s Alabama.",
            publicationYear = 1960,
            coverGradientIndex = 4
        ),
        Book(
            id = "b6",
            title = "A Brief History of Time",
            author = "Stephen Hawking",
            isbn = "978-0553380163",
            genre = "Physics",
            publisher = "Bantam Books",
            totalCopies = 7,
            availableCopies = 5,
            shelfLocation = "Stack D-05, Shelf 2",
            summary = "Exploration of the cosmos, black holes, time travel, and the nature of the expanding universe.",
            publicationYear = 1988,
            coverGradientIndex = 5
        ),
        Book(
            id = "b7",
            title = "The Republic",
            author = "Plato",
            isbn = "978-0140455526",
            genre = "Philosophy",
            publisher = "Penguin Classics",
            totalCopies = 10,
            availableCopies = 8,
            shelfLocation = "Stack E-01, Shelf 3",
            summary = "Socratic dialogue on justice, the order and character of the just city-state, and the philosopher king.",
            publicationYear = 2007,
            coverGradientIndex = 0
        ),
        Book(
            id = "b8",
            title = "Database System Concepts",
            author = "Abraham Silberschatz, Henry F. Korth",
            isbn = "978-0078022159",
            genre = "Computer Science",
            publisher = "McGraw-Hill",
            totalCopies = 7,
            availableCopies = 4,
            shelfLocation = "Stack A-16, Shelf 2",
            summary = "Foundational concepts for relational engines, transaction management, indexing, and distributed databases.",
            publicationYear = 2019,
            coverGradientIndex = 1
        )
    )

    val initialStudents = listOf(
        Student(
            id = "s1",
            studentNumber = "STU-2024-0012",
            name = "Alex Vance",
            email = "alex.vance@campus.edu",
            department = "Computer Science",
            grade = "Senior (Year 4)",
            activeLoansCount = 2,
            totalBorrowedCount = 14
        ),
        Student(
            id = "s2",
            studentNumber = "STU-2024-0045",
            name = "Maya Lin",
            email = "maya.lin@campus.edu",
            department = "Biomedical Engineering",
            grade = "Junior (Year 3)",
            activeLoansCount = 1,
            totalBorrowedCount = 9
        ),
        Student(
            id = "s3",
            studentNumber = "STU-2023-0118",
            name = "Marcus Thorne",
            email = "marcus.t@campus.edu",
            department = "Physics & Astronomy",
            grade = "Graduate Student",
            activeLoansCount = 2,
            totalBorrowedCount = 21
        ),
        Student(
            id = "s4",
            studentNumber = "STU-2024-0322",
            name = "Elena Rostova",
            email = "elena.r@campus.edu",
            department = "Humanities & Literature",
            grade = "Sophomore (Year 2)",
            activeLoansCount = 0,
            totalBorrowedCount = 7
        ),
        Student(
            id = "s5",
            studentNumber = "STU-2025-0504",
            name = "David Chen",
            email = "d.chen@campus.edu",
            department = "Computer Science",
            grade = "Freshman (Year 1)",
            activeLoansCount = 1,
            totalBorrowedCount = 3
        ),
        Student(
            id = "s6",
            studentNumber = "STU-2023-0899",
            name = "Sophia Al-Mansoor",
            email = "sophia.m@campus.edu",
            department = "Philosophy & Law",
            grade = "Senior (Year 4)",
            activeLoansCount = 1,
            totalBorrowedCount = 18
        )
    )

    val initialLoans = listOf(
        Loan(
            id = "l1",
            bookId = "b1",
            bookTitle = "Introduction to Algorithms",
            bookAuthor = "Thomas H. Cormen",
            studentId = "s1",
            studentName = "Alex Vance",
            studentNumber = "STU-2024-0012",
            borrowDate = "2026-10-10",
            dueDate = "2026-10-24",
            status = LoanStatus.ACTIVE,
            daysOverdue = 0,
            fineAmount = 0.0
        ),
        Loan(
            id = "l2",
            bookId = "b3",
            bookTitle = "Clean Architecture",
            bookAuthor = "Robert C. Martin",
            studentId = "s1",
            studentName = "Alex Vance",
            studentNumber = "STU-2024-0012",
            borrowDate = "2026-09-28",
            dueDate = "2026-10-12",
            status = LoanStatus.OVERDUE,
            daysOverdue = 11,
            fineAmount = 5.50
        ),
        Loan(
            id = "l3",
            bookId = "b4",
            bookTitle = "Principles of Neural Science",
            bookAuthor = "Eric R. Kandel",
            studentId = "s2",
            studentName = "Maya Lin",
            studentNumber = "STU-2024-0045",
            borrowDate = "2026-10-02",
            dueDate = "2026-10-16",
            status = LoanStatus.OVERDUE,
            daysOverdue = 7,
            fineAmount = 3.50
        ),
        Loan(
            id = "l4",
            bookId = "b6",
            bookTitle = "A Brief History of Time",
            bookAuthor = "Stephen Hawking",
            studentId = "s3",
            studentName = "Marcus Thorne",
            studentNumber = "STU-2023-0118",
            borrowDate = "2026-10-14",
            dueDate = "2026-10-28",
            status = LoanStatus.ACTIVE,
            daysOverdue = 0,
            fineAmount = 0.0
        ),
        Loan(
            id = "l5",
            bookId = "b2",
            bookTitle = "Design Patterns",
            bookAuthor = "Erich Gamma",
            studentId = "s3",
            studentName = "Marcus Thorne",
            studentNumber = "STU-2023-0118",
            borrowDate = "2026-09-20",
            dueDate = "2026-10-04",
            status = LoanStatus.OVERDUE,
            daysOverdue = 19,
            fineAmount = 9.50
        ),
        Loan(
            id = "l6",
            bookId = "b5",
            bookTitle = "To Kill a Mockingbird",
            bookAuthor = "Harper Lee",
            studentId = "s5",
            studentName = "David Chen",
            studentNumber = "STU-2025-0504",
            borrowDate = "2026-10-15",
            dueDate = "2026-10-29",
            status = LoanStatus.ACTIVE,
            daysOverdue = 0,
            fineAmount = 0.0
        ),
        Loan(
            id = "l7",
            bookId = "b7",
            bookTitle = "The Republic",
            bookAuthor = "Plato",
            studentId = "s6",
            studentName = "Sophia Al-Mansoor",
            studentNumber = "STU-2023-0899",
            borrowDate = "2026-10-18",
            dueDate = "2026-11-01",
            status = LoanStatus.ACTIVE,
            daysOverdue = 0,
            fineAmount = 0.0
        ),
        Loan(
            id = "l8",
            bookId = "b8",
            bookTitle = "Database System Concepts",
            bookAuthor = "Abraham Silberschatz",
            studentId = "s4",
            studentName = "Elena Rostova",
            studentNumber = "STU-2024-0322",
            borrowDate = "2026-09-10",
            dueDate = "2026-09-24",
            returnDate = "2026-09-22",
            status = LoanStatus.RETURNED,
            daysOverdue = 0,
            fineAmount = 0.0
        )
    )

    val initialActivities = listOf(
        ActivityLog(
            id = "a1",
            title = "Book Issued: The Republic",
            subtitle = "Issued to Sophia Al-Mansoor • Due Nov 01",
            timestamp = "Today, 10:15 AM",
            type = ActivityType.BORROW
        ),
        ActivityLog(
            id = "a2",
            title = "Book Returned: Database System Concepts",
            subtitle = "Returned on time by Elena Rostova",
            timestamp = "Yesterday, 04:30 PM",
            type = ActivityType.RETURN
        ),
        ActivityLog(
            id = "a3",
            title = "Overdue Alert: Clean Architecture",
            subtitle = "Alex Vance • 11 days overdue ($5.50 fine)",
            timestamp = "Yesterday, 09:00 AM",
            type = ActivityType.OVERDUE_ALERT
        ),
        ActivityLog(
            id = "a4",
            title = "Catalog Updated: 2 New Copies",
            subtitle = "Introduction to Algorithms added to Stack A-14",
            timestamp = "Oct 21, 02:40 PM",
            type = ActivityType.ADD_BOOK
        ),
        ActivityLog(
            id = "a5",
            title = "Fine Collected: $4.00",
            subtitle = "Paid by Marcus Thorne via University Cashier",
            timestamp = "Oct 20, 11:20 AM",
            type = ActivityType.FINE_COLLECTED
        )
    )
}
