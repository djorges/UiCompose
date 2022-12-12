package com.example.uicompose.util.example

/**
 * Example: Retrofit + Coroutines + Flow + Compose
 * Nota: Reqres API
 * */
/*
manifest.xml
    <uses-permission android:name="android.permission.INTERNET" />


build.gradle(app)
//Compose
    implementation "androidx.compose.material:material:$compose_version"
    implementation 'androidx.activity:activity-compose:1.4.0'
    implementation "androidx.compose.ui:ui:$compose_version"
    implementation "androidx.compose.ui:ui-tooling-preview:$compose_version"
    androidTestImplementation "androidx.compose.ui:ui-test-junit4:$compose_version"
    debugImplementation "androidx.compose.ui:ui-tooling:$compose_version"
    debugImplementation "androidx.compose.ui:ui-test-manifest:$compose_version"

    // ViewModel
    def lifecycle_version = "2.6.0-alpha01"
    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version"
    implementation "androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycle_version"
    implementation "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version"
    implementation "androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version"
    implementation "androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycle_version"
    kapt "androidx.lifecycle:lifecycle-compiler:$lifecycle_version"

    //Navigation
    def nav_version = "2.5.1"
    implementation "androidx.navigation:navigation-compose:$nav_version"
    implementation 'androidx.hilt:hilt-navigation-compose:1.0.0'

    //Hilt
    implementation "com.google.dagger:hilt-android:$hilt_version"
    kapt "com.google.dagger:hilt-android-compiler:$hilt_version"

    //Retrofit
    def retrofit_version = "2.9.0"
    def okhttp_version = "4.9.3"
    implementation "com.squareup.retrofit2:retrofit:$retrofit_version"
    implementation "com.squareup.retrofit2:converter-gson:$retrofit_version"
    implementation "com.squareup.okhttp3:okhttp:$okhttp_version"


    //Paging https://developer.android.com/topic/libraries/architecture/paging/v3-overview
    def paging_version = "3.1.1"
    implementation "androidx.paging:paging-runtime:$paging_version"
    implementation "androidx.paging:paging-compose:1.0.0-alpha14"

    //Coil
    def coil_version = "2.1.0"
    implementation "io.coil-kt:coil-compose:$coil_version"

    //Room
    def room_version = "2.4.2"
    implementation "androidx.room:room-runtime:$room_version"
    kapt "androidx.room:room-compiler:$room_version"
    implementation "androidx.room:room-ktx:$room_version"// Coroutines Ext
    implementation "androidx.room:room-paging:2.4.0-alpha04"

di/module/ReqresModule
@Module
@InstallIn(SingletonComponent::class)
class ReqresModule {

    @Provides
    @Singleton
    fun provideService(): ReqresService {
        return Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ReqresService::class.java)
    }
    companion object{
        private const val API_URL:String = "https://reqres.in/api/"
    }
}

----------------------------Data--------------------------------
api/ReqresService
interface ReqresService {
    //Users
    @GET("users")
    suspend fun getListUsers(@Query("page") page:Int): Page<UserModel>

    //Employee
    @POST("users")
    suspend fun createEmployee(@Body employee: Employee): EmployeeData

    @PUT("users/{id}")
    suspend fun updateEmployee(@Path("id") id:Int, @Body employee: Employee): ResponseBody

    @DELETE("users/{id}")
    suspend fun deleteEmployee(@Path("id") id:Int): ResponseBody
}

data/datasource/UserPagingSource
class UserPagingSource @Inject constructor(
    private val service: ReqresService
) : PagingSource<Int, UserModel>() {
    override fun getRefreshKey(state: PagingState<Int, UserModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                    ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserModel> {
        val pageIndex = params.key ?: STARTING_PAGE_INDEX
        return try {
            val page = service.getListUsers(page = pageIndex)
            val nextPage = pageIndex + 1

            LoadResult.Page(
                    data = page.data,
                    prevKey = null,
                    nextKey = nextPage
            )
        }catch (exception:Exception){
            LoadResult.Error(exception)
        }
    }

    companion object {
        private const val STARTING_PAGE_INDEX = 1
    }
}

data/dto/Page
data class UserModel(
    val id: Int,
    val email: String,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    val avatar: String
): Serializable

data class Resource(
    val id: Int,
    val name: String,
    val year: Int,
    val color: String,
    @SerializedName("pantone_value")
    val tone: String
)

data/repository/MainRepository
class MainRepository @Inject constructor(
    private val userPagingSource: UserPagingSource
){
    fun listUsers(): Flow<PagingData<UserModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
                maxSize = MAX_PAGES
            ),
            pagingSourceFactory = { userPagingSource }
        ).flow
    }

    companion object {
        private const val PAGE_SIZE = 6
        private const val MAX_PAGES = 50
    }
}

-------------------Presentation----------------------------
presentation/viewmodel/
@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository,
) : ViewModel() {
    val listUsers: Flow<PagingData<UserModel>> = repository.listUsers().cachedIn(viewModelScope)
}

presentation/ui/MainActivity
@Preview(
    showBackground = true,
    widthDp = 400,
    heightDp = 800
)
@Composable
@OptIn(ExperimentalFoundationApi::class)
fun ListUsersScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    val lazyPagingItems = viewModel.listUsers.collectAsLazyPagingItems()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyVerticalGrid(
            modifier= Modifier.fillMaxSize(),
            cells = GridCells.Fixed(4),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ){
            items(lazyPagingItems){ user ->
                //User Item
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(user?.avatar),
                        contentDescription = "User Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }
    }
}
@ExperimentalFoundationApi
public fun <T: Any> LazyGridScope.items(
    lazyPagingItems: LazyPagingItems<T>,
    itemContent: @Composable LazyItemScope.(value: T?) -> Unit
) {
    items(lazyPagingItems.itemCount) { index ->
        itemContent(lazyPagingItems[index])
    }
}
 */

/**
 * Example: Retrofit + Coroutines + Flow + Compose
 * Nota: Peticiones a API de Pagina12
 * */
/*
manifest.xml
<uses-permission android:name="android.permission.INTERNET" />

build.gradle
//Compose
    implementation "androidx.compose.material:material:$compose_version"
    implementation 'androidx.activity:activity-compose:1.4.0'
    implementation "androidx.compose.ui:ui:$compose_version"
    implementation "androidx.compose.ui:ui-tooling-preview:$compose_version"
    androidTestImplementation "androidx.compose.ui:ui-test-junit4:$compose_version"
    debugImplementation "androidx.compose.ui:ui-tooling:$compose_version"
    debugImplementation "androidx.compose.ui:ui-test-manifest:$compose_version"

    // ViewModel
    def lifecycle_version = "2.6.0-alpha01"
    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version"
    implementation "androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycle_version"
    implementation "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version"
    implementation "androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version"
    implementation "androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycle_version"
    kapt "androidx.lifecycle:lifecycle-compiler:$lifecycle_version"

    //Navigation
    def nav_version = "2.5.1"
    implementation "androidx.navigation:navigation-compose:$nav_version"
    implementation 'androidx.hilt:hilt-navigation-compose:1.0.0'

    //Hilt
    implementation "com.google.dagger:hilt-android:$hilt_version"
    kapt "com.google.dagger:hilt-android-compiler:$hilt_version"

    //Retrofit
    def retrofit_version = "2.9.0"
    implementation "com.squareup.retrofit2:retrofit:$retrofit_version"
    implementation "com.squareup.retrofit2:converter-gson:$retrofit_version"
    implementation "com.squareup.retrofit2:converter-scalars:$retrofit_version"
    implementation "com.squareup.retrofit2:converter-jackson:$retrofit_version"
    implementation "com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.9.6"
    implementation "javax.xml.stream:stax-api:1.0-2"
    //Coil
    def coil_version = "2.1.0"
    implementation "io.coil-kt:coil-compose:$coil_version"

    //Room
    def room_version = "2.4.2"
    implementation "androidx.room:room-runtime:$room_version"
    kapt "androidx.room:room-compiler:$room_version"
    implementation "androidx.room:room-ktx:$room_version"// Coroutines Ext
    implementation "androidx.room:room-paging:2.4.0-alpha04"

di/RSSModule
@Module
@InstallIn(SingletonComponent::class)
class RSSModule {
    @Provides
    @Singleton
    fun provideService(): RSSMap {
        //Turn off namespace processing for Stax processor
        val input = WstxInputFactory()
        input.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, false)

        //Setup XML Module
        val mapper: ObjectMapper = XmlMapper(input)
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        return Retrofit.Builder()
            .baseUrl(RSS_URL)
            .addConverterFactory(JacksonConverterFactory.create(mapper))
            .build()
            .create(RSSMap::class.java)
    }
    companion object{
        private const val RSS_URL:String = "https://www.pagina12.com.ar/"
    }
}

data/RSSMap
interface RSSMap {
    @GET("rss/portada")
    suspend fun getPortada(): Portada
}

cata/Channel
class Channel {
    val title:String ?= null
    val description:String ?= null
    val image: Img?= null
    val copyright:String ?= null
    val language:String ?= null
    val managingEditor:String ?= null
    val webMaster:String ?= null
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName="item")
    val items:List<Item> ?= null
}
class Item{
    val title:String?= null
    val description:String?= null
    val link:String?= null
    val category:String?= null
    @JacksonXmlProperty(localName="dc:creator")
    val creator:String?= null
    val pubDate:String?= null
    @JacksonXmlProperty(localName="content:encoded")
    val content:String?= null
    @JacksonXmlProperty(localName="media:content")
    val media: Media?= null
    class Media{
        @JacksonXmlProperty(isAttribute = true)
        val url:String?= null
        @JacksonXmlProperty(isAttribute = true)
        val type:String?= null
        @JacksonXmlProperty(isAttribute = true)
        val medium:String?= null
    }
}
class Img{
    val url:String?= null
    val title:String?= null
    val link:String?= null
}

data/Portada
class Portada {
    val channel: Channel? = null
}

data/PortadaState
data class PortadaState(
    val isLoading: Boolean = false,
    val items:List<Item>? = emptyList(),
    val error:String = ""
)

data/RSSRepository
class RSSRepository @Inject constructor(
    private val service: RSSMap
){
    suspend fun getPortada(): Portada {
        return service.getPortada()
    }
}

presentation/viewmodel/MainViewModel
@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository:RSSRepository
) : ViewModel() {
    private val _state = mutableStateOf(PortadaState())
    val state:State<PortadaState> = _state

    init {
        fetchData()
    }

    fun fetchData(){
        viewModelScope.launch {
            _state.value = PortadaState( isLoading = true)
            try {
                _state.value = PortadaState( items = repository.getPortada().channel?.items)
            }catch (e:Exception){
                _state.value = PortadaState( error = e.message.toString())
            }
        }
    }
}

presentation/ui/MainActivity
@Composable
fun PortadaScreen(
    viewModel: MainViewModel = hiltViewModel()
){
    val state = viewModel.state.value

    Box(modifier = Modifier.fillMaxSize()){
        //List of Coins
        LazyColumn(modifier = Modifier.fillMaxSize()){
            items(state.items!!){ article ->
                ArticleItem(article)
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
    item: Item
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(item.media?.url),
            contentDescription = "User Image",
            modifier = Modifier
                .height(50.dp)
                .weight(2f),
            contentScale = ContentScale.Fit,
        )
        Column(
            modifier = Modifier
                .weight(4f),
        ) {
            Text(
                text = item.title.toString(),
                modifier = Modifier.fillMaxWidth(),
                fontSize= 15.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = item.description.toString(),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
 */