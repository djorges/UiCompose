package com.example.uicompose.data.util.compose.example
/**
 * Example: Clean Arch + Retrofit + Coroutines + Flow + Compose
 * Note: fetch Coins, fetch Coin
 *
 * Note: Componentes Extra Simplificado
 *  Capa de Datos:
 *  -Repository(Coroutines): Obtiene datos de un datasource local o remoto
 *  Capa de Presentación:
 *  -ViewModel(Flow): Prepara los datos para la vista.
 *  -Activity(State): Muestra los datos mediante una interfaz
*
 * Note: Phillip Lackner: Example https://www.youtube.com/watch?v=EF33KmyprEQ
 * Note: API https://api.coinpaprika.com
 * */
/*
manifest
    <uses-permission android:name="android.permission.INTERNET" />

build.gradle(app)
    //Compose
    implementation "androidx.compose.ui:ui:$compose_version"
    implementation "androidx.compose.ui:ui-tooling:$compose_version"    // Tooling support (Previews, etc.)
    implementation "androidx.compose.foundation:foundation:$compose_version"     // Foundation (Border, Background, Box, Image, Scroll, shapes, animations, etc.)
    implementation "androidx.compose.material:material:$compose_version"    // Material Design
    //implementation "androidx.compose.material:material-icons-core:$compose_version"    // Material design icons
    //implementation "androidx.compose.material:material-icons-extended:$compose_version"
    implementation 'androidx.activity:activity-compose:1.4.0'    // Integration with activities


    // Navigation
    implementation "androidx.navigation:navigation-fragment-ktx:$nav_version"
    implementation "androidx.navigation:navigation-ui-ktx:$nav_version"
    implementation "androidx.navigation:navigation-compose:$nav_version"    // Jetpack Compose Integration

    def lifecycle_version = "2.4.0"
    def coroutines_version = "1.5.2"
    // ViewModel
    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version"
    implementation "androidx.lifecycle:lifecycle-common-java8:$lifecycle_version"
    implementation "androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycle_version"        // Saved state module for ViewModel
    implementation "androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycle_version"    // Compose integration with ViewModels

    //Coroutines & Flow
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version"
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines_version"

    //Dagger Hilt
    implementation "com.google.dagger:hilt-android:$hilt_version"
    kapt "com.google.dagger:hilt-android-compiler:$hilt_version"
    implementation "androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha03" //ViewModel ext
    implementation 'androidx.hilt:hilt-navigation-compose:1.0.0'//Navigation Compose Hilt Integration

    //Retrofit
    def retrofit_version = "2.9.0"
    def okhttp_version = "4.9.3"
    implementation "com.squareup.retrofit2:retrofit:$retrofit_version"
    implementation "com.squareup.retrofit2:converter-gson:$retrofit_version"
    implementation "com.squareup.okhttp3:okhttp:$okhttp_version"

    //Room
    def room_version = "2.4.2"
    implementation "androidx.room:room-runtime:$room_version"
    kapt "androidx.room:room-compiler:$room_version"
    implementation "androidx.room:room-ktx:$room_version"// Coroutines Ext

base/BaseApplication
@HiltAndroidApp
class BaseApplication:Application() { }

di/MainModule
@Module
@InstallIn(SingletonComponent::class)
object MainModule {
    @Provides
    @Singleton
    fun provideRetrofit():CoinApi{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CoinApi::class.java)
    }
    companion object{
        private const val BASE_URL:String = "https://api.coinpaprika.com"
    }
}

di/RepositoryModule
@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindCoinLocalDataSource(
        coinDataSourceImpl: CoinLocalDataSourceImpl
    ): CoinLocalDataSource

    @Binds
    abstract fun bindCoinRemoteDataSource(
        coinDataSourceImpl: CoinRemoteDataSourceImpl
    ): CoinRemoteDataSource

    @Binds
    abstract fun bindCoinRepository(
        coinRepositoryImpl: CoinRepositoryImpl
    ): CoinRepository
}

--------------------------------------Data--------------------------------
data/api/CoinApi
interface CoinApi {
    @GET("/v1/coins")
    suspend fun getAll():List<CoinDto>

    @GET("/v1/coins/{coinId}")
    suspend fun getById(@Path("coinId") coinId:String):CoinDetailDto
}

data/datasource/CoinRemoteDataSourceImpl
class CoinRemoteDataSourceImpl @Inject constructor(
    private val api: CoinApi
) : CoinRemoteDataSource {

    override suspend fun getAll(): List<CoinDto> {
        return api.getAll()
    }

    override suspend fun getById(coinId: String): CoinDetailDto {
        return api.getById(coinId)
    }
}

data/repository/CoinRepositoryImpl
class CoinRepositoryImpl @Inject constructor(
    private val remoteDataSource: CoinRemoteDataSource,
    private val localDataSource: CoinLocalDataSource
) : CoinRepository {
    override suspend fun fetchAll(): List<CoinDto> {
        return remoteDataSource.getAll()
    }

    override suspend fun fetchById(coinId: String): CoinDetailDto {
        return remoteDataSource.getById(coinId)
    }
}
------------------------------Domain----------------------------------------
domain/FetchCoinsUseCase
class FetchCoinsUseCase @Inject constructor(
    private val repository: CoinRepository
){
    operator fun invoke() : Flow<Result<List<Coin>>> = flow{
        emit(Result.Loading())

        try {
            //Fetch coins
            val coins = repository.fetchAll().map { it.toCoin() }
            emit(Result.Success(coins))
        }catch(e: HttpException){
            emit(Result.Failure(e))
        }catch(e: IOException){
            emit(Result.Failure(e))
        }
    }
}
domain/usecase/FetchCoinUseCase
class FetchCoinUseCase @Inject constructor(
    private val repository: CoinRepository
){
    operator fun invoke(coinId:String) : Flow<Result<CoinDetail>> = flow{
        emit(Result.Loading())

        try {
            //Fetch coin
            val coin = repository.fetchById(coinId).toCoinDetail()
            emit(Result.Success(coin))
        }catch(e: HttpException){
            emit(Result.Failure(e))
        }catch(e: IOException){
            emit(Result.Failure(e))
        }
    }
}

-------------------------presentation---------------------------------
presentation/viewmodel/CoinsViewModel
@HiltViewModel
class CoinsViewModel @Inject constructor(
    private val fetchCoinsUseCase: FetchCoinsUseCase,
    savedStateHandle: SavedStateHandle
):ViewModel() {
    private val _stateList = mutableStateOf(CoinListState())
    val stateList: State<CoinListState> = _stateList

    private val _stateDetail = mutableStateOf(CoinDetailState())
    val stateDetail: State<CoinDetailState> = _stateDetail

    init {
        fetchCoins()

        savedStateHandle.get<String>("coinId")?.let{
            fetchCoin(it)
        }
    }

    private fun fetchCoins(){

        fetchCoinsUseCase().onEach {
                when(it){
                    is Result.Loading ->{
                        _stateList.value = CoinListState(isLoading = true)
                    }
                    is Result.Success ->{
                        _stateList.value = CoinListState(coins = it.data)
                    }
                    is Result.Failure ->{
                        _stateList.value = CoinListState(error = "Unexpected Error")
                    }
                }
        }.launchIn(viewModelScope)
    }
}

presentation/state/CoinListState
data class CoinListState(
    val isLoading: Boolean = false,
    val coins:List<Coin> = emptyList(),
    val error:String = ""
)

data class CoinDetailState(
    val isLoading: Boolean = false,
    val coin:CoinDetail? = null,
    val error:String = ""
)

presentation/ui/MainActivity
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CryptocurrencyTheme {
                val navController = rememberNavController()
                val viewmodel:CoinsViewModel = hiltViewModel()

                Scaffold{ innerPadding ->
                    //Navhost
                    NavHost(
                        navController= navController,
                        startDestination = "main",
                        modifier =Modifier.padding(innerPadding)
                    ){
                        composable("main") {
                            CoinListScreen(navController,viewmodel)
                        }
                        composable("details", arguments = listOf(navArgument("coinId"){type = NavType.IntType})) {
                            CoinDetailScreen(navController, viewmodel)
                        }
                    }
                }
            }
        }
    }
}

presentation/ui/screen/CoinListScreen
@Composable
fun CoinListScreen(
    navController: NavController,
    viewModel: CoinsViewModel,
){
    val state = viewModel.stateList.value
    Box(modifier = Modifier.fillMaxSize()){
        //List of Coins
        LazyColumn(modifier = Modifier.fillMaxSize()){
            items(state.coins){ coin ->
                //Coin Item
                CoinListItem(
                    coin = coin,
                    onItemClick = {
                        //Navigate to DetailsScreen
                        navController.navigate("details/${coin.id}")
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
fun CoinListItem(
    coin: Coin,
    onItemClick: (Coin) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(coin) }
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        //Title   . Ex: 1. Bitcoin (BTC)
        Text(
            text = "${coin.rank}, ${coin.name}, (${coin.symbol})",
            style = MaterialTheme.typography.body1,
            overflow = TextOverflow.Ellipsis
        )
        //Status
        Text(
            text = if(coin.isActive) "active" else "inactive",
            color = if(coin.isActive) Color.Green else Color.Red,
            fontStyle = FontStyle.Italic,
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.body2,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}

presentation/ui/screen/CoinDetailScreen
@Composable
fun CoinDetailScreen(
    navController: NavController,
    viewModel: CoinsViewModel
) {

}

@Composable
fun CoinTag(
    tag:String
){
    Box(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colors.primary,
                shape = RoundedCornerShape(100.dp)
            )
            .padding()
    )
}

*/

/**
 * Example 3: MVVM Retrofit + Flow + Coroutines + Compose
 * Note: Philip Lackner DictionaryApp
 * Note: API https://api.dictionaryapi.dev/
 * */
/*

build.gradle(project)
ext{
        compose_version = "1.1.1" //o 1.1.1
        kotlin_plugin_version = "1.6.21"
        nav_version = "2.4.2"
        hilt_version= "2.40.5"
    }
build.gradle(module)
//Compose
    implementation "androidx.compose.ui:ui:$compose_version"
    implementation "androidx.compose.ui:ui-tooling:$compose_version"    // Tooling support (Previews, etc.)
    implementation "androidx.compose.foundation:foundation:$compose_version"     // Foundation (Border, Background, Box, Image, Scroll, shapes, animations, etc.)
    implementation "androidx.compose.material:material:$compose_version"    // Material Design
    //implementation "androidx.compose.material:material-icons-core:$compose_version"    // Material design icons
    //implementation "androidx.compose.material:material-icons-extended:$compose_version"
    implementation 'androidx.activity:activity-compose:1.4.0'    // Integration with activities

    //Testing
    testImplementation 'junit:junit:4.+'
    androidTestImplementation 'androidx.test.ext:junit:1.1.3'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.4.0'

    // Navigation
    implementation "androidx.navigation:navigation-fragment-ktx:$nav_version"
    implementation "androidx.navigation:navigation-ui-ktx:$nav_version"
    implementation "androidx.navigation:navigation-compose:$nav_version"    // Jetpack Compose Integration

    def lifecycle_version = "2.4.0"
    def coroutines_version = "1.5.2"
    // ViewModel
    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version"
    implementation "androidx.lifecycle:lifecycle-common-java8:$lifecycle_version"
    implementation "androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycle_version"        // Saved state module for ViewModel
    implementation "androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycle_version"    // Compose integration with ViewModels

    //Coroutines & Flow
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version"
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines_version"

    //Dagger Hilt
    implementation "com.google.dagger:hilt-android:$hilt_version"
    kapt "com.google.dagger:hilt-android-compiler:$hilt_version"
    implementation "androidx.hilt:hilt-lifecycle-viewmodel:1.0.0-alpha03" //ViewModel ext
    implementation 'androidx.hilt:hilt-navigation-compose:1.0.0'//Navigation Compose Hilt Integration

    //Retrofit
    def retrofit_version = "2.9.0"
    def okhttp_version = "4.9.3"
    implementation "com.squareup.retrofit2:retrofit:$retrofit_version"
    implementation "com.squareup.retrofit2:converter-gson:$retrofit_version"
    implementation "com.squareup.okhttp3:okhttp:$okhttp_version"

    //Room
    def room_version = "2.4.2"
    implementation "androidx.room:room-runtime:$room_version"
    kapt "androidx.room:room-compiler:$room_version"
    implementation "androidx.room:room-ktx:$room_version"// Coroutines Ext

base/BaseApplication
@HiltAndroidApp
class BaseApplication: Application() {
}

di/MainModule
@Module
@InstallIn(SingletonComponent::class)
object MainModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): DictionaryDatabase {

        return Room.databaseBuilder(
                context,
                DictionaryDatabase::class.java,
                DATABASE_NAME
            ).addTypeConverter(ListConverter(GsonParser(Gson())))
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(@ApplicationContext context: Context):WordInfoDao{
        return Retrofit.Builder()
            .baseUrl(RETROFIT_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WordInfoDao::class.java)
    }

    private const val RETROFIT_BASE_URL = "https://api.dictionaryapi.dev/"
    private const val DATABASE_NAME = "dictionary_database"
}

di/RepoModule
@Module
@InstallIn(ViewModelComponent::class)
abstract class RepoModule {
    @Binds
    abstract fun bindDictionaryRepo(
        dictionaryRepositoryImpl: DictionaryRepositoryImpl
    ): DictionaryRepository
}

data/api/DictionaryApi
interface DictionaryApi {
    @GET("/api/v2/entries/{lang}/{word}")
    suspend fun getWordInfo(
        @Path("lang") language:String,
        @Path("word") word:String
    ):List<WordInfo>

    @GET("/api/v2/synonyms/{lang}/{adjective}")
    suspend fun getSynonymInfo(
        @Path("lang") language:String,
        @Path("adjective") adjective:String
    ):List<SynonymInfo>

    @GET("/api/v2/{lang1}/{lang2}/{word}")
    suspend fun getTranslationInfo(
        @Path("lang1") initLang:String,
        @Path("lang2") finalLang:String,
        @Path("word") word:String
    ):List<Sense>
}

data/db/DictionaryDatabase
@Database(
    entities = [WordInfoEntity::class,SynonymInfoEntity::class],
    version = 2,
    exportSchema = false,
    autoMigrations = [
        AutoMigration (from = 1, to = 2)
    ]
)
@TypeConverters(value = [ListConverter::class, ListConverterExt::class])
abstract class DictionaryDatabase : RoomDatabase() {
    abstract fun getWordInfoDao(): WordInfoDao
    abstract fun getSynonymInfoDao(): SynonymInfoDao
}

data/db/ListConverter
@ProvidedTypeConverter
class ListConverter(
    private val jsonParser: JsonParser
){
    @TypeConverter
    fun convertJSONToList(json: String): List<MeaningModel>{
        return jsonParser.fromJson<ArrayList<MeaningModel>>(
            json,
            object: TypeToken<ArrayList<MeaningModel>>(){}.type
        ) ?: emptyList()
    }

    @TypeConverter
    fun convertListToJSON(list: List<MeaningModel>):String {
        return jsonParser.toJson(
            list,
            object: TypeToken<ArrayList<MeaningModel>>(){}.type
        ) ?: "[]"
    }
}

data/db/WOrdInfoDao
@Dao
interface WordInfoDao{
    @Query("SELECT * FROM wordinfoentity")
    suspend fun getAll(): List<WordInfoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entities:List<WordInfoEntity>)

    @Update
    suspend fun update(entity: WordInfoEntity)

    @Delete
    suspend fun delete(entity: WordInfoEntity)

    @Query("DELETE FROM wordinfoentity WHERE word IN (:words)")
    suspend fun deleteByWord(words:List<String>)

    @Query("SELECT * FROM wordinfoentity WHERE word LIKE '%'||:word||'%'")
    suspend fun getByWord(word: String): List<WordInfoEntity>
}

data/dto/
class Definition(
    val definition: String,
    val example:String,
    val synonyms:List<String>,
    val antonyms:List<String>
){
    fun toDefinitionModel(): DefinitionModel {
        return DefinitionModel(
            definition,
            example,
            synonyms,
            antonyms
        )
    }
}
class Meaning (
    val partOfSpeech:String,
    val definitions:List<Definition>
){
    fun toMeaningModel(): MeaningModel {
        return MeaningModel(
            partOfSpeech,
            definitions.map { it.toDefinitionModel() }
        )
    }
}
data class Phonetic(
    val text:String,
    val audio:String
)
class WordInfo(
    val word:String,
    val phonetic:String,
    val phonetics: List<Phonetic>,
    val origin:String,
    val meanings:List<Meaning>,
){
    fun toWordInfoEntity(): WordInfoEntity {
        return WordInfoEntity(
            word = word,
            phonetic = phonetic,
            origin = origin,
            meanings = meanings.map { it.toMeaningModel() }
        )
    }
}

data/entity/WordInfoEntity
@Entity
class WordInfoEntity(
    @PrimaryKey
    val id:Int? = null,
    val word:String,
    val phonetic:String,
    val origin:String,
    val meanings: List<MeaningModel>
){
    fun toWordInfoModel():WordInfoModel{
        return WordInfoModel(
            word,
            phonetic,
            origin,
            meanings
        )
    }
}

data/repository
class DictionaryRepositoryImpl @Inject constructor(
    private val api: DictionaryApi,
    private val database: DictionaryDatabase
):DictionaryRepository {
    private val wordInfoDao = database.getWordInfoDao()

    override fun getWordInfo(language:String,word: String): Flow<Result<List<WordInfoModel>>> = flow{
        emit(Result.Loading())

        //Get wordInfo local
        val localWordInfos = wordInfoDao.getAll().map{ it.toWordInfoModel()}
        emit(Result.Loading(data = localWordInfos))

        try {
            //Get wordInfo remote
            val remoteWordInfos = api.getWordInfo(language,word)
            //Update wordInfo local
            wordInfoDao.deleteByWord(remoteWordInfos.map { it.word })
            wordInfoDao.insert(remoteWordInfos.map { it.toWordInfoEntity() })
        }catch(e:HttpException){
            emit(Result.Failure(e))
        }catch(e: IOException){
            emit(Result.Failure(e))
        }
        val updatedWordInfos = wordInfoDao.getByWord(word).map { it.toWordInfoModel() }
        emit(Result.Success(updatedWordInfos))
    }
}

data/util
class GsonParser(
    private val gson: Gson
):JsonParser {
    override fun <T> fromJson(json: String, type: Type): T? {
        return gson.fromJson(json, type)
    }

    override fun <T> toJson(obj: T, type: Type): String? {
        return gson.toJson(obj,type)
    }
}
interface JsonParser {
    fun <T> fromJson(json:String, type: Type):T?
    fun <T> toJson(obj:T, type: Type):String?
}

domain/model
data class DefinitionModel(
    val definition: String,
    val example:String,
    val synonyms:List<String>,
    val antonyms:List<String>
)
class MeaningModel(
    val partOfSpeech:String,
    val definitions:List<DefinitionModel>
)
data class WordInfoModel(
    val word:String,
    val phonetic:String,
    val origin:String,
    val meanings:List<MeaningModel>,
)
sealed class Result<out T> {
    data class Loading<out T>(val data: T? = null) : Result<T>()
    data class Success<out T>(val data: T) : Result<T>()
    data class Failure<out T>(val throwable: Throwable) : Result<T>()
}

domain/repository/DictionaryRepository
interface DictionaryRepository {
    fun getWordInfo(language:String, word:String): Flow<Result<List<WordInfoModel>>>
    fun getSynonymInfo(language: String, adjective: String): Flow<Result<List<SynonymInfoModel>>>
}

domain/usecase/GetWordInfoUseCase
class GetWordInfoUseCase @Inject constructor(
    private val repository: DictionaryRepository
) {
    operator fun invoke(word:String): Flow<Result<List<WordInfoModel>>> {
        if(word.isBlank()){
            return flow{ }
        }
        return repository.getWordInfo( "en",word)
    }
}


presentation/state/DictionaryState
data class DictionaryState(
    val wordInfoItems:List<WordInfoModel> = emptyList(),
    val isLoading: Boolean = false
)

presentation/viewmodel/DictionaryViewModel
@HiltViewModel
class DictionaryViewModel @Inject constructor(
    private val getWordInfo:GetWordInfoUseCase
):ViewModel(){
    //query state
    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    //wordinfo state
    private val _state = mutableStateOf(DictionaryState())
    val state:State<DictionaryState> = _state

    //
    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var searchJob: Job? = null

    fun onSearch(query:String,option:String){
        _searchQuery.value = query

        searchJob?.cancel()
        searchJob = viewModelScope.launch{

            //
            if(true){
                findWordInfo(query)
            }else{

            }
        }
    }

    private suspend fun findWordInfo(query: String) {
        getWordInfo(query).onEach { result ->
            when (result) {
                is Result.Loading -> {
                    _state.value = state.value.copy(
                        wordInfoItems = result.data ?: emptyList(),
                        isLoading = true
                    )
                }
                is Result.Success -> {
                    _state.value = state.value.copy(
                        wordInfoItems = result.data ?: emptyList(),
                        isLoading = false
                    )
                }
                is Result.Failure -> {
                    _state.value = state.value.copy(
                        wordInfoItems = emptyList(),
                        isLoading = false
                    )
                    //
                    _eventFlow.emit(
                        UIEvent.ShowSnackbar(
                            result.throwable.message ?: "Unexpected Error"
                        )
                    )
                }
            }
        }
    }

    sealed class UIEvent{
        data class ShowSnackbar(val message:String):UIEvent()
    }
}


presentation/ui/screen/HomeScreen
val viewModel:DictionaryViewModel = hiltViewModel()
                val state = viewModel.state.value

                val scaffoldState = rememberScaffoldState()

                // Create a list of cities
                val optionList = listOf("info", "synonym", "translate")
                var menuIsExpanded by rememberSaveable{ mutableStateOf(false) }
                var optionSelected by rememberSaveable{ mutableStateOf(optionList[0]) }

                //Effect
                LaunchedEffect(key1 = true){
                    viewModel.eventFlow.collectLatest { event ->
                        when(event){
                            is DictionaryViewModel.UIEvent.ShowSnackbar ->{
                                //Show Snackbar
                                scaffoldState.snackbarHostState.showSnackbar(
                                    message = event.message
                                )
                            }
                        }
                    }
                }

                Scaffold(scaffoldState = scaffoldState) {
                    Box(modifier = Modifier.background(MaterialTheme.colors.background)){
                        Column(modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp)){
                            //Search Field
                            TextField(
                                value = viewModel.searchQuery.value,
                                onValueChange = { viewModel.onSearch(it, optionSelected) } ,
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = {
                                    Text(text="Buscar palabra...")
                                }
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            //Dropdown options
                            DropdownMenu(
                                expanded = menuIsExpanded,
                                onDismissRequest = { menuIsExpanded = false },
                                modifier = Modifier.fillMaxWidth()

                            ) {
                                optionList.forEach { label ->
                                    DropdownMenuItem(onClick = {
                                        optionSelected = label
                                        menuIsExpanded = false
                                    }) {
                                        Text(text = label)
                                    }
                                }
                            }

                            //WordInfo Data
                            LazyColumn(modifier = Modifier.fillMaxSize()){
                                items( count = state.wordInfoItems.size){ i ->
                                    val wordInfo = state.wordInfoItems[i]

                                    if(i > 0){
                                        Spacer(modifier = Modifier.height(8.dp))
                                    }

                                    WordInfoItem(wordInfo = wordInfo)

                                    if(i<state.wordInfoItems.size -1){
                                        Divider()
                                    }
                                }
                            }
                        }
                    }
                    if(state.isLoading){
                        //Show Progress Indicator
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentSize(Alignment.Center)
                        )
                    }
                }
*/

/**
 * Example 2:
 *
 * DistanceMatrix: https://developers.google.com/maps/documentation/distance-matrix/overview
 *  Places: https://developers.google.com/maps/documentation/places/web-service/query
 * */
/*

build.gradle(Module)
    //Retrofit Json & XML Converters
    def retrofit_version = "2.9.0"
    implementation "com.squareup.retrofit2:retrofit:$retrofit_version"
    implementation "com.squareup.retrofit2:converter-gson:$retrofit_version"
    implementation 'com.google.code.gson:gson:2.8.9'

    def lifecycle_version = "2.3.1"
    def coroutines_version = "1.5.0"
    // ViewModel & LiveData
    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version"
    implementation "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version"
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version"
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines_version"

    //Maps
    def play_services_version = "18.0.0"
    implementation "com.google.android.gms:play-services-location:$play_services_version"
    implementation "com.google.android.gms:play-services-maps:$play_services_version"
    implementation 'com.google.android.libraries.places:places:2.5.0'

MainActivity
class MainActivity : AppCompatActivity() {
    private val scope = MainScope()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initService()
    }
    private fun initService(){
        val service = DistanceMatrixClient().createService()

        val handler = CoroutineExceptionHandler{ _, throwable ->
            throwable.message?.let {
                Log.e("DISTANCE_APP", it)
            }
        }
        scope.launch {
            val model = withContext(Dispatchers.IO + handler){
                val origins = URLEncoder.encode("Boston,MA|Charlestown,MA", "UTF-8")
                val destinations = URLEncoder.encode("Lexington,MA|Concord,MA", "UTF-8")

                service.getDistance(
                    FORMAT_RESPONSE,
                    origins,
                    destinations,
                    API_KEY
                )
            }
            Log.i("DISTANCE_APP", model.toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        scope.cancel()
    }
    companion object{
        private val FORMAT_RESPONSE = "json"
        private val API_KEY = "AIzaSyC8Tp-JqfbemQhVIMUZcQmqrhijJG0xPUY"
    }
}

model/
data class DistanceModel (
    val destination_addresses:List<String>,
    val error_message: String,
    val origin_addresses:List<String>,
    val rows: List<RowModel>,
    val status:String
)

data class RowModel(
    val elements: List<ElementModel>
)
data class ElementModel(
    val distance: InfoModel,
    val duration: InfoModel,
    val status:String
)
data class InfoModel(
    val text:String,
    val value: Int
)

di/module/MainModule
class DistanceMatrixClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl(GOOGLE_MAPS_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun createService():DistanceMatrixService{
        return retrofit.create(DistanceMatrixService::class.java)
    }
    companion object{
        private const val GOOGLE_MAPS_URL:String = "https://maps.googleapis.com/"
    }
}

data/api/DistanceMatrixService
interface DistanceMatrixService {

    /* Example with Coordinates
     url "maps/api/distancematrix/json?origins=40.6655101%2C-73.89188969999998&destinations=40.659569%2C-73.933783%7C40.729029%2C-73.851524%7C40.6860072%2C-73.6334271%7C40.598566%2C-73.7527626&key=YOUR_API_KEY"
      getDistance(
       "json",
       "40.6655101%2C-73.89188969999998",
       "40.659569%2C-73.933783",
       "..."
       )


    */
    @GET("maps/api/distancematrix/{format}")
    suspend fun getDistance(
        @Path("format") format:String,
        @Query("origins") origins:String,
        @Query("destinations") destinations:String,
        @Query("key") key:String
    ): DistanceModel
}
*/