package com.example.studybuddy.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studybuddy.ui.theme.*
import com.example.studybuddy.viewmodel.AssignmentViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditScreen(
    viewModel: AssignmentViewModel,
    assignmentId: Int?,
    onDone: () -> Unit,
    onBack: () -> Unit
) {
    val formState by viewModel.formState.collectAsState()
    val allAssignments by viewModel.allAssignments.collectAsState()
    val isEditing = assignmentId != null

    var showDatePicker by remember { mutableStateOf(false) }


    LaunchedEffect(assignmentId) {
        if (isEditing) {
            viewModel.resetForm()
            val assignment = allAssignments.find { it.id == assignmentId }
            assignment?.let { viewModel.loadForEdit(it) }
        } else {
            viewModel.resetForm()
        }
    }

    val dateFormatter = remember { SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (isEditing) "edit assignment" else "new assignment",
                        fontWeight = FontWeight.Bold,
                        color = Blush700
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "back", tint = Blush400)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Blush50)
            )
        },
        containerColor = Color(0xFFFFF9FC)
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            FormLabel("assignment title")
            OutlinedTextField(
                value = formState.title,
                onValueChange = viewModel::onTitleChange,
                placeholder = { Text("e.g. Lab Report", color = Blush100) },
                isError = formState.titleError != null,
                supportingText = formState.titleError?.let { { Text(it) } },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = blushTextFieldColors()
            )

            FormLabel("module code")
            OutlinedTextField(
                value = formState.moduleCode,
                onValueChange = viewModel::onModuleChange,
                placeholder = { Text("e.g. ET1234", color = Blush100) },
                isError = formState.moduleError != null,
                supportingText = formState.moduleError?.let { { Text(it) } },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = blushTextFieldColors()
            )

            FormLabel("notes (optional)")
            OutlinedTextField(
                value = formState.notes,
                onValueChange = viewModel::onNotesChange,
                placeholder = { Text("any extra details...", color = Blush100) },
                minLines = 3,
                maxLines = 5,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = blushTextFieldColors()
            )

            FormLabel("due date")
            OutlinedCard(
                onClick = { showDatePicker = true },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(if (formState.dateError != null) MaterialTheme.colorScheme.error.copy(.05f) else Color.Transparent)
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(Icons.Default.CalendarToday, null, tint = Blush400, modifier = Modifier.size(18.dp))
                    Text(
                        text = formState.dueDate?.let { dateFormatter.format(Date(it)) }
                            ?: "tap to choose a date",
                        fontSize = 14.sp,
                        color = if (formState.dueDate != null) Blush700 else Blush100
                    )
                }
            }
            formState.dateError?.let {
                Text(it, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
            }

            FormLabel("priority")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Low", "Medium", "High").forEach { level ->
                    val selected = formState.priority == level
                    val (bg, text) = when (level) {
                        "High"   -> if (selected) Blush400 to Color.White else Blush50 to Blush400
                        "Low"    -> if (selected) Mint400 to Color.White  else Mint50  to Mint400
                        else     -> if (selected) Color(0xFFDEB887) to Color.White else Butter50 to Butter700
                    }
                    FilterChip(
                        selected = selected,
                        onClick = { viewModel.onPriorityChange(level) },
                        label = { Text(level.lowercase(), fontSize = 13.sp) },
                        modifier = Modifier.weight(1f),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = bg,
                            selectedLabelColor = text
                        )
                    )
                }
            }


            if (isEditing) {
                val assignment = allAssignments.find { it.id == assignmentId }
                assignment?.let {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Blush50, RoundedCornerShape(14.dp))
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("mark as complete", fontSize = 14.sp, color = Blush700)
                        Switch(
                            checked = it.isCompleted,
                            onCheckedChange = { _ -> viewModel.toggleComplete(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = Mint400
                            )
                        )
                    }
                }
            }

            Spacer(Modifier.height(4.dp))


            Button(
                onClick = { viewModel.saveAssignment(onDone) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Blush400)
            ) {
                Text(
                    if (isEditing) "save changes" else "save assignment",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }


            OutlinedButton(
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Blush400)
            ) {
                Text("cancel", fontSize = 15.sp)
            }

            Spacer(Modifier.height(16.dp))
        }
    }


    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = formState.dueDate ?: System.currentTimeMillis()
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { viewModel.onDueDateChange(it) }
                    showDatePicker = false
                }) { Text("ok", color = Blush400) }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}


@Composable
private fun FormLabel(text: String) {
    Text(
        text = text,
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold,
        color = Blush400
    )
}

@Composable
private fun blushTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor   = Blush400,
    unfocusedBorderColor = Blush100,
    focusedLabelColor    = Blush400,
    unfocusedLabelColor  = Blush100,
    cursorColor          = Blush400
)
