package com.example.uicompose.data.util.compose.example

/**
 *  Example 2: CUstom PagingSource. INfinite load
 * PagingSource
 *  -load(Retorna LoadResult):
 *      inicializar start index y range,
 *      returnar LoadResult.Page
 *  -refreshKey(Provide a Key used for the initial load for the next PagingSource)
 *
 * */
/*class GetArticlesUseCase @Inject constructor(
    private val localDataSource: LocalArticleDataSource
) {
    suspend operator fun invoke(): Flow<PagingData<Article>> = withContext(Dispatchers.IO){
        Pager(
            config = PagingConfig(
                pageSize = ITEMS_PER_PAGE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { localDataSource.getAllArticles() }
        ).flow//emits new instances of PagingData once they become invalidated by PagingSource.invalidate
    }
    companion object {
        private const val ITEMS_PER_PAGE = 12
    }
}
class ArticlePagingSource @Inject constructor(): PagingSource<Int, Article>(){
    private fun ensureValidKey(key: Int) = Integer.max(STARTING_KEY, key)

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        val anchorPosition = state.anchorPosition ?: return null //Most recently accessed index in the list
        val article = state.closestItemToPosition(anchorPosition) ?: return null
        val midPageCount = state.config.pageSize/2
        return ensureValidKey(key = article.id - midPageCount)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val start = params.key ?: STARTING_KEY
        val range = start.until(start + params.loadSize)

        return LoadResult.Page(
            data = range.map { number->
                Article(
                    id = number,
                    title = "Article $number",
                    description = "This describes article $number",
                    created = LocalDateTime.now().minusDays(number.toLong())
                )
            },
            prevKey = when (start) {
                STARTING_KEY -> null
                else -> ensureValidKey(key = range.first - params.loadSize)
            },
            nextKey = range.last + 1
        )
    }

    companion object {
        private const val STARTING_KEY: Int = 0
    }
}*/


/**
 *  Example 1: Room + PAging3.
 * PagingSource
 *  -load(Retorna LoadResult):
 *      inicializar start index y range,
 *      returnar LoadResult.Page
 *  -refreshKey(Provide a Key used for the initial load for the next PagingSource)
 *
 * */
/*
//LocalArticleDataSource
class LocalArticleDataSource @Inject constructor(
    private val database: AppDatabase
) {
    suspend fun insert(list: List<Article>){
        database.getArticleDao().insertAll(list)
    }
    suspend fun deleteAll(){ database.getArticleDao().clearAll()}
    fun getAllArticles() = database.getArticleDao().getAll()
}

//AppDatabase
@Database(
    version = 2,
    entities = [Article::class],
    exportSchema = false
)
@TypeConverters(LocalDateTimeConverter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getArticleDao(): ArticleDao
}

//ArticleDao
@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articles: List<Article>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(articles: Article)

    @Query("SELECT * FROM articles")
    fun getAll(): PagingSource<Int, Article>

    @Query("DELETE FROM articles")
    suspend fun clearAll()
}

//LocalDateTimeConverter
class LocalDateTimeConverter {
    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun toDate(dateString: String?): LocalDateTime? {
        return try {
            LocalDateTime.parse(dateString)
        }catch (e:Exception){
            null
        }
    }

    @TypeConverter
    fun toString(date: LocalDateTime?): String {
        return date.toString()
    }
}

//Article
@Entity(tableName = "articles")
data class Article(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "date_created")
    val created: LocalDateTime?
)

//MainModule
@Module
@InstallIn(SingletonComponent::class)
class MainModule {
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ):AppDatabase{
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    companion object{
        private const val DATABASE_NAME = "APP_DB"
    }
}

//ViewModel
@HiltViewModel
class MainViewModel @Inject constructor(
    private val localDataSource: LocalArticleDataSource
) : ViewModel() {
    init {
        val list = List(30){ i -> Article(i, "Article $i","", null)}
        insertData(list)
    }
    val items:Flow<PagingData<Article>> = Pager(
        config = PagingConfig(
            pageSize = ITEMS_PER_PAGE,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { localDataSource.getAllArticles() }
    ).flow//emits new instances of PagingData once they become invalidated by PagingSource.invalidate
     .cachedIn(viewModelScope)//maintain paging state through configuration or navigation changes

    private fun insertData(list:List<Article>){
        viewModelScope.launch {
            localDataSource.insert(list)
        }
    }
    companion object {
        private const val ITEMS_PER_PAGE = 12
    }
}

//ArticleScreen
@Composable
fun ArticleItem(
    article: Article?,
    onItemClick: ()->Unit = {}
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        backgroundColor= Color.Cyan,
        border= BorderStroke(width = 2.dp, color = Color.White),
        elevation = 10.dp,
    ) {
        Text(
            text = article?.title.toString(),
            modifier = Modifier.fillMaxWidth()
                .padding(vertical = 10.dp),
            textAlign = TextAlign.Center,
            color = Color.Blue
        )
    }
}

@Preview
@Composable
fun ListComponent(
    viewModel: MainViewModel = hiltViewModel(),
    //navController:NavController
){
    val state = viewModel.items.collectAsLazyPagingItems()
    Box(
        modifier = Modifier.fillMaxSize()
    ){
        //List of Coins
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ){
            items(items = state){ article ->
                //Coin Item
                ArticleItem(
                    article = article,
                    onItemClick = {
                        //Navigate to DetailsScreen
                        //navController.navigate("details/${coin.id}")
                    }
                )
            }
        }
    }
}*/
