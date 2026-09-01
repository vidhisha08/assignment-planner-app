package com.example.studybuddy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.studybuddy.data.entity.AssignmentEntity
import com.example.studybuddy.data.repository.AssignmentRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AssignmentViewModel(
    private val repository: AssignmentRepository
) : ViewModel() {


    private val _userId = MutableStateFlow(-1)


    @OptIn(ExperimentalCoroutinesApi::class)
    val allAssignments: StateFlow<List<AssignmentEntity>> = _userId
        .flatMapLatest { id ->
            if (id == -1) flowOf(emptyList())
            else repository.getAllForUser(id)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val pendingAssignments: StateFlow<List<AssignmentEntity>> = _userId
        .flatMapLatest { id ->
            if (id == -1) flowOf(emptyList())
            else repository.getPendingForUser(id)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val completedAssignments: StateFlow<List<AssignmentEntity>> = _userId
        .flatMapLatest { id ->
            if (id == -1) flowOf(emptyList())
            else repository.getCompletedForUser(id)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    private val _formState = MutableStateFlow(AssignmentFormState())
    val formState: StateFlow<AssignmentFormState> = _formState.asStateFlow()


    private val _activeFilter = MutableStateFlow(FilterTab.ALL)
    val activeFilter: StateFlow<FilterTab> = _activeFilter.asStateFlow()


    private val _snackbar = MutableStateFlow<String?>(null)
    val snackbar: StateFlow<String?> = _snackbar.asStateFlow()

    fun setUser(userId: Int) { _userId.value = userId }

    fun setFilter(tab: FilterTab) { _activeFilter.value = tab }


    fun onTitleChange(v: String)      { _formState.update { it.copy(title = v, titleError = null) } }
    fun onModuleChange(v: String)     { _formState.update { it.copy(moduleCode = v, moduleError = null) } }
    fun onNotesChange(v: String)      { _formState.update { it.copy(notes = v) } }
    fun onDueDateChange(millis: Long) { _formState.update { it.copy(dueDate = millis, dateError = null) } }
    fun onPriorityChange(p: String)   { _formState.update { it.copy(priority = p) } }

    fun resetForm() { _formState.value = AssignmentFormState() }

    fun loadForEdit(assignment: AssignmentEntity) {
        _formState.value = AssignmentFormState(
            editingId  = assignment.id,
            title      = assignment.title,
            moduleCode = assignment.moduleCode,
            notes      = assignment.notes,
            dueDate    = assignment.dueDate,
            priority   = assignment.priority
        )
    }

    fun saveAssignment(onDone: () -> Unit) {
        val s = _formState.value
        var valid = true
        if (s.title.isBlank()) { _formState.update { it.copy(titleError = "Title is required") }; valid = false }
        if (s.moduleCode.isBlank()) { _formState.update { it.copy(moduleError = "Module code is required") }; valid = false }
        if (s.dueDate == null) { _formState.update { it.copy(dateError = "Please pick a due date") }; valid = false }
        if (!valid) return

        viewModelScope.launch {
            if (s.editingId != null) {
                val existing = repository.getById(s.editingId) ?: return@launch
                repository.update(
                    existing.copy(
                        title      = s.title.trim(),
                        moduleCode = s.moduleCode.trim().uppercase(),
                        notes      = s.notes.trim(),
                        dueDate    = s.dueDate!!,
                        priority   = s.priority
                    )
                )
                _snackbar.value = "Assignment updated!"
            } else {
                repository.add(
                    AssignmentEntity(
                        title      = s.title.trim(),
                        moduleCode = s.moduleCode.trim().uppercase(),
                        notes      = s.notes.trim(),
                        dueDate    = s.dueDate!!,
                        priority   = s.priority,
                        userId     = _userId.value
                    )
                )
                _snackbar.value = "Assignment added!"
            }
            resetForm()
            onDone()
        }
    }

    fun toggleComplete(assignment: AssignmentEntity) {
        viewModelScope.launch {
            repository.update(assignment.copy(isCompleted = !assignment.isCompleted))
            _snackbar.value = if (!assignment.isCompleted) "Marked as done!" else "Marked as pending"
        }
    }

    fun delete(assignment: AssignmentEntity) {
        viewModelScope.launch {
            repository.delete(assignment)
            _snackbar.value = "Assignment deleted"
        }
    }

    fun clearSnackbar() { _snackbar.value = null }

    class Factory(private val repository: AssignmentRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return AssignmentViewModel(repository) as T
        }
    }
}

data class AssignmentFormState(
    val editingId: Int?   = null,
    val title: String     = "",
    val moduleCode: String = "",
    val notes: String     = "",
    val dueDate: Long?    = null,
    val priority: String  = "Medium",
    val titleError: String?  = null,
    val moduleError: String? = null,
    val dateError: String?   = null
)

enum class FilterTab { ALL, PENDING, COMPLETED }
