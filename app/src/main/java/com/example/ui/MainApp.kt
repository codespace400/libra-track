package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.LoanStatus
import com.example.model.NavDestination
import com.example.ui.components.AppTopBar
import com.example.ui.components.SidebarNav
import com.example.ui.screens.AddEditBookScreen
import com.example.ui.screens.BookCatalogScreen
import com.example.ui.screens.BookDetailScreen
import com.example.ui.screens.BorrowReturnScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.OverdueScreen
import com.example.ui.screens.ReportsScreen
import com.example.ui.screens.StudentsScreen
import com.example.ui.theme.BackgroundSlate
import com.example.viewmodel.LibraryViewModel
import kotlinx.coroutines.launch

@Composable
fun MainApp(
    viewModel: LibraryViewModel = viewModel()
) {
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val currentDestination by viewModel.currentDestination.collectAsState()

    val books by viewModel.books.collectAsState()
    val students by viewModel.students.collectAsState()
    val loans by viewModel.loans.collectAsState()
    val activities by viewModel.activities.collectAsState()

    val bookSearchQuery by viewModel.bookSearchQuery.collectAsState()
    val selectedGenre by viewModel.selectedGenre.collectAsState()
    val studentSearchQuery by viewModel.studentSearchQuery.collectAsState()

    val selectedBookId by viewModel.selectedBookId.collectAsState()
    val editingBook by viewModel.editingBook.collectAsState()
    val feedbackMessage by viewModel.feedbackMessage.collectAsState()

    val preselectedStudentId by viewModel.preselectedStudentId.collectAsState()
    val preselectedBookId by viewModel.preselectedBookId.collectAsState()

    val overdueCount = remember(loans) {
        loans.count { it.status == LoanStatus.OVERDUE }
    }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(feedbackMessage) {
        feedbackMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearFeedback()
        }
    }

    if (!isLoggedIn) {
        LoginScreen(
            onLoginSuccess = { email, pass ->
                viewModel.login(email, pass)
            }
        )
        return
    }

    // Back handling
    BackHandler(enabled = currentDestination != NavDestination.DASHBOARD) {
        when (currentDestination) {
            NavDestination.BOOK_DETAIL, NavDestination.ADD_EDIT_BOOK -> {
                viewModel.navigate(NavDestination.BOOKS)
            }
            else -> {
                viewModel.navigate(NavDestination.DASHBOARD)
            }
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isWideScreen = maxWidth >= 720.dp
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()

        val selectedBook = remember(books, selectedBookId) {
            books.find { it.id == selectedBookId } ?: books.firstOrNull()
        }

        if (isWideScreen) {
            // Tablet / Desktop layout with persistent Sidebar
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BackgroundSlate)
                    .statusBarsPadding()
                    .navigationBarsPadding()
            ) {
                SidebarNav(
                    currentDestination = currentDestination,
                    onNavigate = { dest -> viewModel.navigate(dest) },
                    overdueCount = overdueCount,
                    librarian = currentUser,
                    onLogout = { viewModel.logout() },
                    onQuickAddBook = { viewModel.openAddBook() }
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    AppTopBar(
                        currentDestination = currentDestination,
                        overdueCount = overdueCount,
                        onMenuClick = {},
                        onOverdueBannerClick = { viewModel.navigate(NavDestination.OVERDUE) },
                        showMenuButton = false
                    )

                    Scaffold(
                        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                        containerColor = BackgroundSlate
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            when (currentDestination) {
                                NavDestination.DASHBOARD -> DashboardScreen(
                                    books = books,
                                    students = students,
                                    loans = loans,
                                    activities = activities,
                                    onNavigate = { viewModel.navigate(it) },
                                    onBookClick = { viewModel.openBookDetail(it) }
                                )

                                NavDestination.BOOKS -> BookCatalogScreen(
                                    books = books,
                                    searchQuery = bookSearchQuery,
                                    onSearchQueryChange = { viewModel.setBookSearchQuery(it) },
                                    selectedGenre = selectedGenre,
                                    onSelectedGenreChange = { viewModel.setSelectedGenre(it) },
                                    onAddBookClick = { viewModel.openAddBook() },
                                    onBookClick = { viewModel.openBookDetail(it) },
                                    onEditBookClick = { viewModel.openEditBook(it) },
                                    onQuickBorrowClick = { viewModel.prepareBorrow(bookId = it.id) }
                                )

                                NavDestination.BOOK_DETAIL -> {
                                    if (selectedBook != null) {
                                        BookDetailScreen(
                                            book = selectedBook,
                                            loans = loans,
                                            onBack = { viewModel.navigate(NavDestination.BOOKS) },
                                            onEdit = { viewModel.openEditBook(it) },
                                            onIssue = { viewModel.prepareBorrow(bookId = it.id) }
                                        )
                                    } else {
                                        viewModel.navigate(NavDestination.BOOKS)
                                    }
                                }

                                NavDestination.ADD_EDIT_BOOK -> AddEditBookScreen(
                                    editingBook = editingBook,
                                    onSave = { t, a, i, g, p, c, s, sum, y, pal ->
                                        viewModel.saveBook(t, a, i, g, p, c, s, sum, y, pal)
                                    },
                                    onDelete = { id -> viewModel.deleteBook(id) },
                                    onBack = { viewModel.navigate(NavDestination.BOOKS) }
                                )

                                NavDestination.STUDENTS -> StudentsScreen(
                                    students = students,
                                    searchQuery = studentSearchQuery,
                                    onSearchQueryChange = { viewModel.setStudentSearchQuery(it) },
                                    onAddStudent = { num, name, email, dept, grade ->
                                        viewModel.addStudent(num, name, email, dept, grade)
                                    },
                                    onQuickBorrowForStudent = { viewModel.prepareBorrow(studentId = it.id) }
                                )

                                NavDestination.BORROW_RETURN -> BorrowReturnScreen(
                                    books = books,
                                    students = students,
                                    loans = loans,
                                    preselectedStudentId = preselectedStudentId,
                                    preselectedBookId = preselectedBookId,
                                    onBorrowConfirm = { sId, bId, days ->
                                        viewModel.borrowBook(sId, bId, days)
                                    },
                                    onReturnConfirm = { loanId ->
                                        viewModel.returnBook(loanId)
                                    }
                                )

                                NavDestination.OVERDUE -> OverdueScreen(
                                    loans = loans,
                                    onCollectFine = { viewModel.collectFine(it) },
                                    onSendReminder = { viewModel.sendOverdueReminder(it) }
                                )

                                NavDestination.REPORTS -> ReportsScreen(
                                    books = books,
                                    students = students,
                                    loans = loans
                                )
                            }
                        }
                    }
                }
            }
        } else {
            // Compact / Mobile Layout with Navigation Drawer
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    ModalDrawerSheet(
                        drawerContainerColor = Color.White
                    ) {
                        SidebarNav(
                            currentDestination = currentDestination,
                            onNavigate = { dest ->
                                viewModel.navigate(dest)
                                scope.launch { drawerState.close() }
                            },
                            overdueCount = overdueCount,
                            librarian = currentUser,
                            onLogout = {
                                viewModel.logout()
                                scope.launch { drawerState.close() }
                            },
                            onQuickAddBook = {
                                viewModel.openAddBook()
                                scope.launch { drawerState.close() }
                            }
                        )
                    }
                }
            ) {
                Scaffold(
                    topBar = {
                        AppTopBar(
                            currentDestination = currentDestination,
                            overdueCount = overdueCount,
                            onMenuClick = { scope.launch { drawerState.open() } },
                            onOverdueBannerClick = { viewModel.navigate(NavDestination.OVERDUE) },
                            showMenuButton = true
                        )
                    },
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                    containerColor = BackgroundSlate,
                    modifier = Modifier
                        .statusBarsPadding()
                        .navigationBarsPadding()
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (currentDestination) {
                            NavDestination.DASHBOARD -> DashboardScreen(
                                books = books,
                                students = students,
                                loans = loans,
                                activities = activities,
                                onNavigate = { viewModel.navigate(it) },
                                onBookClick = { viewModel.openBookDetail(it) }
                            )

                            NavDestination.BOOKS -> BookCatalogScreen(
                                books = books,
                                searchQuery = bookSearchQuery,
                                onSearchQueryChange = { viewModel.setBookSearchQuery(it) },
                                selectedGenre = selectedGenre,
                                onSelectedGenreChange = { viewModel.setSelectedGenre(it) },
                                onAddBookClick = { viewModel.openAddBook() },
                                onBookClick = { viewModel.openBookDetail(it) },
                                onEditBookClick = { viewModel.openEditBook(it) },
                                onQuickBorrowClick = { viewModel.prepareBorrow(bookId = it.id) }
                            )

                            NavDestination.BOOK_DETAIL -> {
                                if (selectedBook != null) {
                                    BookDetailScreen(
                                        book = selectedBook,
                                        loans = loans,
                                        onBack = { viewModel.navigate(NavDestination.BOOKS) },
                                        onEdit = { viewModel.openEditBook(it) },
                                        onIssue = { viewModel.prepareBorrow(bookId = it.id) }
                                    )
                                } else {
                                    viewModel.navigate(NavDestination.BOOKS)
                                }
                            }

                            NavDestination.ADD_EDIT_BOOK -> AddEditBookScreen(
                                editingBook = editingBook,
                                onSave = { t, a, i, g, p, c, s, sum, y, pal ->
                                    viewModel.saveBook(t, a, i, g, p, c, s, sum, y, pal)
                                },
                                onDelete = { id -> viewModel.deleteBook(id) },
                                onBack = { viewModel.navigate(NavDestination.BOOKS) }
                            )

                            NavDestination.STUDENTS -> StudentsScreen(
                                students = students,
                                searchQuery = studentSearchQuery,
                                onSearchQueryChange = { viewModel.setStudentSearchQuery(it) },
                                onAddStudent = { num, name, email, dept, grade ->
                                    viewModel.addStudent(num, name, email, dept, grade)
                                },
                                onQuickBorrowForStudent = { viewModel.prepareBorrow(studentId = it.id) }
                            )

                            NavDestination.BORROW_RETURN -> BorrowReturnScreen(
                                books = books,
                                students = students,
                                loans = loans,
                                preselectedStudentId = preselectedStudentId,
                                preselectedBookId = preselectedBookId,
                                onBorrowConfirm = { sId, bId, days ->
                                    viewModel.borrowBook(sId, bId, days)
                                },
                                onReturnConfirm = { loanId ->
                                    viewModel.returnBook(loanId)
                                }
                            )

                            NavDestination.OVERDUE -> OverdueScreen(
                                loans = loans,
                                onCollectFine = { viewModel.collectFine(it) },
                                onSendReminder = { viewModel.sendOverdueReminder(it) }
                            )

                            NavDestination.REPORTS -> ReportsScreen(
                                books = books,
                                students = students,
                                loans = loans
                            )
                        }
                    }
                }
            }
        }
    }
}
