package com.example.android.architecture.blueprints.todoapp.taskdetail

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.architecture.blueprints.todoapp.Event
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.data.Result
import com.example.android.architecture.blueprints.todoapp.data.Result.Success
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.TasksRepository
import com.example.android.architecture.blueprints.todoapp.util.wrapEspressoIdlingResource
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Details screen.
 */
class TaskDetailViewModel @Inject constructor(
    private val tasksRepository: TasksRepository
) : ViewModel() {

    private val _task = MutableLiveData<Task>()
    val task: LiveData<Task> = _task

    private val _isDataAvailable = MutableLiveData<Boolean>()
    val isDataAvailable: LiveData<Boolean> = _isDataAvailable

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _editTaskCommand = MutableLiveData<Event<Unit>>()
    val editTaskCommand: LiveData<Event<Unit>> = _editTaskCommand

    private val _deleteTaskCommand = MutableLiveData<Event<Unit>>()
    val deleteTaskCommand: LiveData<Event<Unit>> = _deleteTaskCommand

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarMessage: LiveData<Event<Int>> = _snackbarText

    private val taskId: String?
        get() = _task.value?.id

    // This LiveData depends on another so we can use a transformation.
    val completed: LiveData<Boolean> = Transformations.map(_task) { input: Task? ->
        input?.isCompleted ?: false
    }


    fun deleteTask() = viewModelScope.launch {
        taskId?.let {
            tasksRepository.deleteTask(it)
            _deleteTaskCommand.value = Event(Unit)
        }
    }

    fun editTask() {
        _editTaskCommand.value = Event(Unit)
    }

    fun setCompleted(completed: Boolean) = viewModelScope.launch {
        val task = _task.value ?: return@launch
        if (completed) {
            tasksRepository.completeTask(task)
            showSnackbarMessage(R.string.task_marked_complete)
        } else {
            tasksRepository.activateTask(task)
            showSnackbarMessage(R.string.task_marked_active)
        }
    }

    fun start(taskId: String?, forceRefresh: Boolean = false) {
        if (_isDataAvailable.value == true && !forceRefresh || _dataLoading.value == true) {
            return
        }

        // Show loading indicator
        _dataLoading.value = true

        wrapEspressoIdlingResource {

            viewModelScope.launch {
                if (taskId != null) {
                    tasksRepository.getTask(taskId, false).let { result ->
                        if (result is Success) {
                            onTaskLoaded(result.data)
                        } else {
                            onDataNotAvailable(result)
                        }
                    }
                }
                _dataLoading.value = false
            }
        }
    }

    private fun setTask(task: Task?) {
        this._task.value = task
        _isDataAvailable.value = task != null
    }

    private fun onTaskLoaded(task: Task) {
        setTask(task)
    }

    private fun onDataNotAvailable(result: Result<Task>) {
        _task.value = null
        _isDataAvailable.value = false
    }

    fun refresh() {
        taskId?.let { start(it, true) }
    }

    private fun showSnackbarMessage(@StringRes message: Int) {
        _snackbarText.value = Event(message)
    }
}