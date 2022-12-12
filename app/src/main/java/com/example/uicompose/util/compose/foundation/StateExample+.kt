package com.example.uicompose.util.compose.foundation
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Ejemplo de notas android
 *
 * */
@Preview(
    showBackground = true,
    name = "Hello Example",
    device = Devices.PIXEL_4
)
@Composable
fun StateExample() {
    var name by remember { mutableStateOf("") }
    Column(modifier = Modifier.padding(16.dp)) {
        if (name.isNotEmpty()) {
            //
            Text(
                text = "Hello, $name!",
                modifier = Modifier.padding(bottom = 8.dp),
                style = MaterialTheme.typography.h5
            )
        }
        //
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") }
        )
    }
}

@Preview(
    showBackground = true,
    name="ButtonState",
    widthDp = 300,
    heightDp = 300
)
@Composable
fun ButtonState(){
    var clicks by remember{ mutableStateOf(0)}
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = 16.dp
            )
    ) {
        Button(
            onClick = { clicks++ }
        ){
            Text(text = clicks.toString() )
        }
    }
}

/**
 * Example Codelab State Compose
 * https://developer.android.com/codelabs/jetpack-compose-state?index=..%2F..index#11
 * */
/*class WellnessViewModel : ViewModel() {
    private val _tasks = getWellnessTasks().toMutableStateList()
    val tasks: List<WellnessTask>
        get() = _tasks


    fun remove(item: WellnessTask) {
        _tasks.remove(item)
    }
    fun changeTaskChecked(item: WellnessTask, checked: Boolean) =
        tasks.find { it.id == item.id }?.let { task ->
            task.checked = checked
        }
}

private fun getWellnessTasks() = List(30) { i -> WellnessTask(i, "Task # $i") }

data class WellnessTask(
    val id:Int,
    val message:String,
    var checked: Boolean = false
)
@Composable
fun WellnessScreen(
    modifier: Modifier = Modifier,
    wellnessViewModel: WellnessViewModel = viewModel()
) {
    Column(modifier = modifier) {
        StatefulCounter()

        WellnessTasksList(
            list = wellnessViewModel.tasks,
            onCheckedTask = { task, checked ->
                wellnessViewModel.changeTaskChecked(task, checked)
            },
            onCloseTask = { task -> wellnessViewModel.remove(task) })
    }
}
@Composable
fun StatefulCounter(){}

@Composable
fun WellnessTasksList(
    list:List<WellnessTask>,
    onCheckedTask: (WellnessTask, Boolean) -> Unit,
    onCloseTask: (WellnessTask) -> Unit,
    modifier: Modifier = Modifier
){
    LazyColumn(
        modifier = modifier
    ) {
        items(
            items = list,
            key = { task -> task.id }
        ) { task ->
            WellnessTaskItem(
                taskName = task.message,
                checked = task.checked,
                onCheckedChange = { checked -> onCheckedTask(task, checked) },
                onClose = { onCloseTask(task) }
            )
        }
    }
}
@Composable
fun WellnessTaskItem(
    taskName:String,
    checked:Boolean,
    onCheckedChange:(Boolean)->Unit,
    onClose:()->Unit
){}*/

/**
 * Example: uiState(Architecture)
 *
 * */
/*
sealed class UiState {
    object SignedOut : UiState()
    object InProgress : UiState()
    object Error : UiState()
    object SignIn : UiState()
}

class MyViewModel : ViewModel() {
    private val _uiState = MutableLiveData<UiState>(UiState.SignedOut)
    val uiState: LiveData<UiState>
        get() = _uiState

    // ...
}

@Composable
fun MyComposable(viewModel: MyViewModel) {
    val uiState = viewModel.uiState.observeAsState()
    // ...
}*/

/**
 * Example: List

    Init List in screen

    val taskList = viewModel.tasks.toList()
    //List of Task
        TaskList(
        tasks = taskList,
        itemOnClick = {
        //Do somethings here
        }
    )

 */
/*
@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private val _tasks = getTasks().toMutableStateList()
    val tasks: SnapshotStateList<Task>
        get() = _tasks
    private fun getTasks() = List(30) { i -> Task(i, "Task # $i") }
    companion object {
        private const val TAG:String = "UICOMPOSE_APP"
    }
}
@Composable
fun TaskList(
    tasks: List<Task> = emptyList(),
    itemOnClick:() -> Unit = {}
){
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        items(
            items = tasks,
            key = {task->task.id}
        ){ task->
            //Item Task
            TaskItem(task, itemOnClick)
        }
    }
}

@Preview
@Composable
fun TaskItem(
    task: Task = Task(1,"Gym", true),
    onclick: () -> Unit = {}
){
    Card(
        modifier = Modifier
            .fillMaxSize(),
    ){
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
            ,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = task.id.toString(),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Text(
                text = task.message,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            OutlinedButton(
                onClick = onclick,
            ){

            }
        }
    }
}*/
