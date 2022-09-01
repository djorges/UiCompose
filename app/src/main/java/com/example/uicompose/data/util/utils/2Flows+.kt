package com.example.uicompose.data.util.utils


/**
 * Example 1:
 * https://kotlinlang.org/docs/flow.html#flows
 * */
/*

fun simple():Flow<Int> = flow {
    for (i in 1..3){
        delay(1000)
        emit(i)
    }
}
fun main(){
    runBlocking {
        // Launch a concurrent coroutine to check if the main thread is blocked
        launch {
            for (k in 1..3){
                println("I'm not blocked $k")
                delay(1000)
            }
        }
        // Collect the flow
        simple().collect{value -> println(value)}
    }
}
*/
/**
 * Example 2: Flows are cold
 * Note:Flows are cold streams similar to sequences — the code inside a flow builder
 * does not run until the flow is collected
 * */
/*
fun simple(): Flow<Int> = flow {
    println("Flow started")
    for (i in 1..3) {
        delay(100)
        emit(i)
    }
}
fun main() = runBlocking<Unit> {
    println("Calling simple function...")
    val flow = simple()
    println("Calling collect...")
    flow.collect { value -> println(value) }
    println("Calling collect again...")
    flow.collect { value -> println(value) }
}*/
/**
 * Example 3: Flow Cancellation Basics
 * Note: cancelling a flow similar as coroutine cancellation
 * */
/*
fun simple(): Flow<Int> = flow {
    for (i in 1..3) {
        delay(1000)
        println("Emitting $i")
        emit(i)
    }
}
fun main() = runBlocking<Unit> {
    withTimeout(2500){//on timeout throws TimeoutCancellationException
        simple().collect{ value -> println(value) }
    }
    println("Done")
}
*/
/**
 * Example 4: Flow builders
 * Note:
 * -The flow{ } builder defines a flow emitting a dynamic set of values.
 * -The flowOf builder that defines a flow emitting a fixed set of values.
 * Various collections and sequences can be converted to flows using .asFlow() extension functions
 * */
/*
class FlowExample(){
    private val dynamicFlow: Flow<Int> = flow {
        for (i in 1..3) {
            delay(1000)
            emit(i)
        }
    }
    private val fixedFlow:Flow<IntRange> = flowOf(1..3) //or (1..3).asFlow()
}
*/
/**
 * Example 5: Intermediate flow operators
 * */
/*

suspend fun performRequest(request:Int):String {
    delay(1000)
    return "response $request"
}

fun main() = runBlocking {
    val listFlow = (1..8).asFlow()

        listFlow
            .map { requestNumber -> performRequest(requestNumber) } //map values
            .collect { response -> println(response) }


        listFlow
            .filter { number -> number > 5 } //filter values
            .collect { number -> println(number) }

        listFlow
            .transform { request ->
                emit("Making request $request")
                emit(performRequest(request))
            } //custom transform
            .collect { response -> println(response) }

        listFlow
            .take(3)//take first 3
            .collect{ response -> println(response) }


        listFlow
            .filter {
                println("Filter $it")
                it % 2 == 0
            }
            .map {
                println("Map $it")
                "string $it"
            }.collect {
                println("Collect $it")
            }

        listFlow
            .onEach { delay(400) }
            .collect {
                println("Collect $it")
            }
}
*/
/**
 * Example 6: flowOn operator
 * Note: the flowOn function that shall be used to change the context of the flow emission
 * */
/*
fun simple(): Flow<Int> = flow {
    for (i in 1..3) {
        delay(1000)
        println("Emitting $i")
        emit(i)
    }
}.flowOn(Dispatchers.IO)

fun main() = runBlocking(Dispatchers.Main) {
    simple().collect { value ->
        println("Collected $value")
    }
}

*/
/**
 * Example 7: TODO: Buffering
 *
 * */

/**
 * Example 8: Composing multiple flows
 * Note:  flows have a .zip operator that combines the corresponding values of two flows
 * Note: .combine Returns a Flow whose values are generated with transform function by
 * combining the most recently emitted values by each flow.
 * */
/*

fun main() {
    val nums = (1..3).asFlow()
    val strs = flowOf("one", "two", "three")

    runBlocking {
        //combine 2 flows
        nums.zip(strs){ number, string -> "$number -> $string"}
            .collect{ println(it)}
    }
}

fun main() {
    val flow = flowOf(1, 2).onEach { delay(10) }
    val flow2 = flowOf("a", "b", "c").onEach { delay(15) }

    runBlocking {
        flow.combine(flow2) { i, s -> i.toString() + s }
            .collect {println(it)} // Will print "1a 2a 2b 2c"
    }
}
*/
/**
 * Example 8: Flow exceptions
 * Note: try/catch block surrounding the collector catches any
 * exception happening in the emitter or in any intermediate or terminal operators
 * */
/*
//Method 1(Recommended): Using .catch intermediate operator
fun simple(): Flow<Int> = flow {
    for (i in 1..3) {
        println("Emitting $i")
        emit(i)
    }
}

fun main() = runBlocking<Unit> {
    simple()
        .onEach { value ->
            check(value <= 1) { "Collected $value" }
            println(value)
        }
        .catch { e -> println("Caught $e") }
        .collect()
}

//Method 2: Using .catch in emitter & handler in coroutine launcher
fun simple(): Flow<Int> = flow {
    for (i in 1..3) {
        println("Emitting $i")
        emit(i)
    }
}

fun main() {
    val handler = CoroutineExceptionHandler{ context, throwable->
        println("Show message: ${throwable.message}")
    }
    runBlocking(Dispatchers.Main + handler) {
        simple()
            .catch { e -> println("Caught $e") }
            .collect { value ->
                check(value <= 1) { "Collected $value" }
                println(value)
            }
    }
}

//Method 3: Using try/catch block in collector
fun simple(): Flow<Int> = flow {
    for (i in 1..3) {
        println("Emitting $i")
        if(i % 2 == 0){
            throw IOException()
        }
        emit(i)
    }
}
fun main() = runBlocking {
    try {//
        simple().collect { value ->
            println(value)
            check(value <= 1) { "Collected $value" }
        }
    } catch (e: Throwable) {
        println("Caught $e")
    }
}
*/
/**
 * Example: Flow completion
 * Note: perform and action when collector
 * */
/*
//Method 1: using .onCompletion intermediate operator
fun simple(): Flow<Int> = (1..3).asFlow()

fun main() = runBlocking<Unit> {
    simple()
        .onCompletion { println("Done") }
        .collect { value -> println(value) }
}
*/
/**
 * Example: Launching Flow
 * TODO: https://kotlinlang.org/docs/flow.html#launching-flow
 * */


/**
 * Example 1: Flow + Coroutines + State(Compose)
 * Infinite emit of new values from flow, then modify the stream. Flow + Coroutines + State(Compose)
 * */

/*interface INewsApi {
    suspend fun fetchLatestNews(): List<ArticleHeadline>
}

data class ArticleHeadline (
    val id:Int,
    val country:String
)

class NewsRemoteDataSource @Inject constructor(
    private val newsApi:INewsApi
){
    val latestNews: Flow<List<ArticleHeadline>> = flow {
        while (true){
            //Fetch Remote Data
            //val latestNews = newsApi.fetchLatestNews()

            //Fetch Local Data
            val latestNews = List(10){ i -> ArticleHeadline(i,"Article $i")}

            emit(latestNews)

            delay(REFRESH_INTERVAL_MILLISECONDS)
        }
    }

    companion object{
        private const val REFRESH_INTERVAL_MILLISECONDS:Long = 5000
    }
}

class NewsRepository @Inject constructor(
    private val newsRemoteDataSource: NewsRemoteDataSource,
){
    //Filters
    private val filterByCountry:(ArticleHeadline)->Boolean = { it.country == "Argentina" }

    val favouriteLatestNews:Flow<List<ArticleHeadline>> =
        newsRemoteDataSource.latestNews
            .map { news -> news.filter(filterByCountry)}
            .onEach { println("This list has ${it.size} items")}
}

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository
): ViewModel() {
    private val _stateList = mutableStateOf(NewsListState())
    val stateList: State<NewsListState> = _stateList

    init {
        viewModelScope.launch {

            _stateList.value = NewsListState(isLoading = true)

            newsRepository.favouriteLatestNews
                .catch { exception ->  _stateList.value = NewsListState(error = "Error found: ${exception.message}")  }
                .collect{ list ->  _stateList.value = NewsListState(news = list) }
        }
    }
}

data class NewsListState(
    val isLoading: Boolean = false,
    val news:List<ArticleHeadline> = emptyList(),
    val error:String = ""
)

@Composable
fun NewsListScreen(
    navController: NavController,
    viewModel: NewsViewModel,
){
    val state = viewModel.stateList.value
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()){
        //List of Coins
        LazyColumn(modifier = Modifier.fillMaxSize()){
            items(state.news){ article ->
                //Article Item
                ArticleItem(
                    article = article,
                    onItemClick = {
                        //Show Message
                        Toast.makeText(context,"Clicked", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }

        //Error text
        if(state.error.isNotBlank()){
            Text(
                text = state.error,
                color = MaterialTheme.colors.error,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            )
        }
        //Progress Indicator
        if(state.isLoading){
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Composable
fun ArticleItem(
    article: ArticleHeadline,
    onItemClick:() -> Unit = {}
) {
    Text(
        text = article.country,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )
}*/
