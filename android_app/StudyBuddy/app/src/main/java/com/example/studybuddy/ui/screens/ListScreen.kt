package com.example.studybuddy.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studybuddy.ui.components.AssignmentCard
import com.example.studybuddy.ui.theme.*
import com.example.studybuddy.viewmodel.AssignmentViewModel
import com.example.studybuddy.viewmodel.FilterTab


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    viewModel: AssignmentViewModel,
    onAddClick: () -> Unit,
    onEditClick: (Int) -> Unit,
    onLogout: () -> Unit
) {
    val allAssignments      by viewModel.allAssignments.collectAsState()
    val pendingAssignments  by viewModel.pendingAssignments.collectAsState()
    val completedAssignments by viewModel.completedAssignments.collectAsState()
    val activeFilter        by viewModel.activeFilter.collectAsState()
    val snackbarMsg         by viewModel.snackbar.collectAsState()

    var showLogoutDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    // snackbar appeares when there is a message from viewmodel
    LaunchedEffect(snackbarMsg) {
        snackbarMsg?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearSnackbar()
        }
    }


    val displayedList = when (activeFilter) {
        FilterTab.ALL       -> allAssignments
        FilterTab.PENDING   -> pendingAssignments
        FilterTab.COMPLETED -> completedAssignments
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "studybuddy",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Mint700
                        )
                        if (pendingAssignments.isNotEmpty()) {
                            Text(
                                "${pendingAssignments.size} due soon",
                                fontSize = 11.sp,
                                color = Mint400
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { showLogoutDialog = true }) {
                        Icon(Icons.Default.Logout, "logout", tint = Mint400)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Mint50)
            )
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = Mint400,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, "add assignment", modifier = Modifier.size(26.dp))
            }
        },
        // snackbar
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color(0xFFF6FDF9)
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            Surface(color = Mint50, shadowElevation = 1.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterTab.entries.forEach { tab ->
                        val count = when (tab) {
                            FilterTab.ALL       -> allAssignments.size
                            FilterTab.PENDING   -> pendingAssignments.size
                            FilterTab.COMPLETED -> completedAssignments.size
                        }
                        FilterChip(
                            selected = activeFilter == tab,
                            onClick  = { viewModel.setFilter(tab) },
                            label    = {
                                Text(
                                    "${tab.name.lowercase()} ($count)",
                                    fontSize = 12.sp
                                )
                            },
                            modifier = Modifier.weight(1f),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Mint400,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }


            if (displayedList.isEmpty()) {
                EmptyState(
                    filter = activeFilter,
                    onAddClick = onAddClick
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = 16.dp, end = 16.dp, top = 12.dp, bottom = 100.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(
                        items = displayedList,
                        key = { it.id }
                    ) { assignment ->
                        AssignmentCard(
                            assignment = assignment,
                            onToggleComplete = { viewModel.toggleComplete(assignment) },
                            onEdit   = { onEditClick(assignment.id) },
                            onDelete = { viewModel.delete(assignment) }
                        )
                    }
                }
            }
        }
    }


    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("log out?", fontWeight = FontWeight.SemiBold) },
            text  = { Text("you'll need to log back in next time") },
            confirmButton = {
                TextButton(onClick = { showLogoutDialog = false; onLogout() }) {
                    Text("log out", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) { Text("cancel") }
            },
            shape = RoundedCornerShape(20.dp)
        )
    }
}


@Composable
fun EmptyState(filter: FilterTab, onAddClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = when (filter) {
                    FilterTab.ALL       -> "📭"
                    FilterTab.PENDING   -> "🎉"
                    FilterTab.COMPLETED -> "📋"
                },
                fontSize = 60.sp
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = when (filter) {
                    FilterTab.ALL       -> "no assignments yet"
                    FilterTab.PENDING   -> "all caught up!"
                    FilterTab.COMPLETED -> "nothing completed yet"
                },
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Mint700
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = when (filter) {
                    FilterTab.ALL     -> "tap + to add your first assignment"
                    FilterTab.PENDING -> "you have no pending assignments"
                    FilterTab.COMPLETED -> "mark assignments as done to see them here"
                },
                fontSize = 14.sp,
                color = Mint400,
                textAlign = TextAlign.Center
            )
            if (filter == FilterTab.ALL) {
                Spacer(Modifier.height(24.dp))
                Button(
                    onClick = onAddClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Mint400),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Default.Add, null)
                    Spacer(Modifier.width(6.dp))
                    Text("add assignment", color = Color.White)
                }
            }
        }
    }
}
