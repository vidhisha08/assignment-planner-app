package com.example.studybuddy.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studybuddy.data.entity.AssignmentEntity
import com.example.studybuddy.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


@Composable
fun AssignmentCard(
    assignment: AssignmentEntity,
    onToggleComplete: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    val daysLeft = remember(assignment.dueDate) {
        TimeUnit.MILLISECONDS.toDays(assignment.dueDate - System.currentTimeMillis())
    }
    val isOverdue = !assignment.isCompleted && daysLeft < 0


    val cardBg = if (assignment.isCompleted) Color(0xFFF5F5F5) else Color.White
    val borderColor = when {
        assignment.isCompleted -> Color(0xFFE0E0E0)
        isOverdue -> Blush100
        daysLeft <= 2 -> Butter300
        else -> Mint100
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = CardDefaults.outlinedCardBorder().copy(
            // color the border of the card based on urgency
        ),
        elevation = CardDefaults.cardElevation(if (assignment.isCompleted) 0.dp else 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {

            IconButton(onClick = onToggleComplete, modifier = Modifier.size(36.dp)) {
                Icon(
                    imageVector = if (assignment.isCompleted) Icons.Outlined.CheckCircle else Icons.Default.RadioButtonUnchecked,
                    contentDescription = "toggle complete",
                    tint = if (assignment.isCompleted) Mint400 else Lavender300,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(Modifier.width(8.dp))


            Column(modifier = Modifier.weight(1f)) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = assignment.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (assignment.isCompleted) Color(0xFF999999) else Color(0xFF3D3D3A),
                        textDecoration = if (assignment.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    PriorityBadge(assignment.priority) // based on assignments priority
                }

                Spacer(Modifier.height(3.dp))

                Text(
                    text = assignment.moduleCode,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Mint400
                )


                if (assignment.notes.isNotBlank()) {
                    Spacer(Modifier.height(3.dp))
                    Text(
                        text = assignment.notes,
                        fontSize = 12.sp,
                        color = Color(0xFF888888),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(Modifier.height(8.dp))


                DueDateChip(
                    dueDate = assignment.dueDate,
                    isCompleted = assignment.isCompleted,
                    isOverdue = isOverdue,
                    daysLeft = daysLeft
                )
            }


            Column(horizontalAlignment = Alignment.End) {
                IconButton(onClick = onEdit, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Edit, "edit", tint = Lavender300, modifier = Modifier.size(16.dp))
                }
                IconButton(onClick = { showDeleteDialog = true }, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Delete, "delete", tint = Blush400, modifier = Modifier.size(16.dp))
                }
            }
        }
    }


    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("delete assignment?", fontWeight = FontWeight.SemiBold) },
            text = { Text("\"${assignment.title}\" will be permanently removed.") },
            confirmButton = {
                TextButton(onClick = { showDeleteDialog = false; onDelete() }) {
                    Text("delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("cancel") }
            },
            shape = RoundedCornerShape(20.dp)
        )
    }
}

// color of the priority badge
@Composable
fun PriorityBadge(priority: String) {
    val (bg, textColor) = when (priority) {
        "High"   -> Blush100 to Blush700
        "Low"    -> Mint50 to Mint700
        else     -> Butter50 to Butter700
    }
    Box(
        modifier = Modifier
            .background(bg, RoundedCornerShape(20.dp))
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Text(priority.lowercase(), fontSize = 11.sp, color = textColor, fontWeight = FontWeight.SemiBold)
    }
}

// function that shows days left until due date
@Composable
fun DueDateChip(dueDate: Long, isCompleted: Boolean, isOverdue: Boolean, daysLeft: Long) {
    val formatter = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }
    val dateText = formatter.format(Date(dueDate))
    val suffix = when {
        isCompleted     -> " ✓"
        isOverdue       -> " (${-daysLeft}d overdue)"
        daysLeft == 0L  -> " (today!)"
        daysLeft == 1L  -> " (tomorrow)"
        daysLeft <= 7L  -> " (${daysLeft}d left)"
        else            -> ""
    }
    val (bg, textColor) = when {
        isCompleted  -> Color(0xFFF0F0F0) to Color(0xFF999999)
        isOverdue    -> Blush50 to Blush700
        daysLeft <= 2 -> Butter50 to Butter700
        else          -> Mint50 to Mint700
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .background(bg, RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Icon(Icons.Default.CalendarToday, null, tint = textColor, modifier = Modifier.size(11.dp))
        Text("$dateText$suffix", fontSize = 11.sp, color = textColor,
            fontWeight = if (isOverdue || daysLeft <= 2) FontWeight.SemiBold else FontWeight.Normal)
    }
}
