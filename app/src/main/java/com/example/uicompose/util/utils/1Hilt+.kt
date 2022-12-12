package com.example.uicompose.util.utils

import android.content.Context
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

/**
 * DaggerHilt
 * https://developer.android.com/training/dependency-injection/hilt-android
 * */
/*
build.gradle(project)
buildscript {
    ext {
        hilt_version = '2.38.1'
    }
    dependencies {
        classpath "com.google.dagger:hilt-android-gradle-plugin:$hilt_version"
    }
}

build.gradle(app)
plugins {
  id 'kotlin-kapt'
  id 'dagger.hilt.android.plugin'
}
android {
    ...
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
}
dependencies {
    implementation "com.google.dagger:hilt-android:2.38.1"
    kapt "com.google.dagger:hilt-compiler:2.38.1"
}

base/BaseApplication
@HiltAndroidApp
class BaseApplication : Application() { ... }

manifest.xml
<application
        android:name=".base.BaseApp"
>
 */
/**
 * Example 1: inyectar dependencias en clases de Android
 *
 * En la actualidad, la versión de Hilt admite las siguientes clases de Android:

    Application (mediante @HiltAndroidApp)
    ViewModel (mediante @HiltViewModel)
    Activity
    Fragment
    View
    Service
    BroadcastReceiver
 * */
/*

@AndroidEntryPoint
class ExampleActivity : AppCompatActivity() {
    @Inject
    lateinit var analytics: AnalyticsAdapter
}

@HiltViewModel
class MyViewModel @Inject constructor() : ViewModel{ ... }

class AnalyticsAdapter @Inject constructor(
  private val service: AnalyticsService
) { ... }

*/

/**
 * Example: Módulos de Hilt
 * Nota: Un módulo de Hilt es una clase anotada con @Module. Al igual que los
 * módulos de Dagger, informa a Hilt cómo proporcionar instancias de determinados tipos.
 * Nota: Debes anotar los módulos de Hilt con @InstallIn para indicarle a Hilt en qué clase
 * de Android se usará o instalará cada módulo.
 * */
/*
@Module
@InstallIn(ActivityComponent::class)
abstract class AnalyticsModule {

}
*/
/**
 * Ejemplo: Inyectar Instancias de Clases
 * Nota: La inyección de constructor tampoco es posible si la clase no es de tu propiedad
 * porque proviene de una biblioteca externa (clases como Retrofit, OkHttpClient o bases
 * de datos de Room), o si las instancias deben crearse con el patrón de compilador.
 * Nota: puedes indicarle a Hilt cómo proporcionar instancias de este tipo creando una
 * función dentro de un módulo de Hilt y anotando esa función con @Provides
 *
 * */
/*
//Caso: Patrón de Diseño(Multiples instancias)
@Module
@InstallIn(ActivityComponent::class)
object MainModule {
    @Provides
    fun provideRetrofit():CoinApi{
        return
    }
}

//Caso: Patrón de Diseño Singleton(Intancia única)
@Module
@InstallIn(SingletonComponent::class)
class MainModule {
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
*/
/**
 * Ejemplo: inyectar instancias de interfaces
 * */
/*
interface AnalyticsService {
  fun analyticsMethods()
}
class AnalyticsServiceImpl @Inject constructor() : AnalyticsService { ... }

@Module
@InstallIn(ActivityComponent::class)
abstract class AnalyticsModule {

  @Binds
  abstract fun bindAnalyticsService(
    analyticsServiceImpl: AnalyticsServiceImpl
  ): AnalyticsService
}

 */
/**
 * Ejemplo: proporcionar varias vinculaciones para el mismo tipo
 *  Nota: En los casos en los que necesites que Hilt proporcione diferentes implementaciones
 *  del mismo tipo como dependencias, debes proporcionarle varias vinculaciones. Puedes definir
 *  varias vinculaciones para el mismo tipo con calificadores
 *  Nota: Un calificador es una anotación que se usa para identificar una vinculación específica
 *  de un tipo cuando ese tipo tiene varias vinculaciones definidas
 **/
/*
//Ejemplo: OkHttpClient Interceptors
@Module
@InstallIn(ActivityComponent::class)
object AnalyticsModule {

  @Provides
  fun provideAnalyticsService(
    @AuthInterceptorOkHttpClient
    okHttpClient: OkHttpClient
  ): AnalyticsService {
      return Retrofit.Builder()
               .baseUrl(BASE_URL)
               .client(okHttpClient)
               .build()
               .create(AnalyticsService::class.java)
  }
  private const val BASE_URL:String ="https://example.com"
}

//Ejemplo: Extra Simple
di/annotation
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ShortTimeoutOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LongTimeoutOkHttpClient

di/module
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @ShortTimeoutOkHttpClient
    @Provides
    fun provideShortTimeoutOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(Duration.ofMillis(1000))
            .build()
    }

    @LongTimeoutOkHttpClient
    @Provides
    fun provideLongTimeoutOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(Duration.ofMillis(3000))
            .build()
    }
}
 */
/**
 * Ejemplo: Calificadores predefinidos en Hilt
 * */
/*
class AnalyticsAdapter @Inject constructor(
    @ActivityContext private val context: Context,
    private val service: AnalyticsService
) {

}

class AnalyticsAdapter @Inject constructor(
    @ApplicationContext private val context: Context,
    private val service: AnalyticsService
) { ... }
*/
/**
 *Ejemplo: Alcances de los componentes
 * Nota:Hilt crea y destruye automáticamente instancias de clases generadas por componentes
 * siguiendo el ciclo de vida de las clases de Android correspondientes.
 * Nota:si defines el alcance de AnalyticsAdapter mediante ActivityComponent con @ActivityScoped,
 * Hilt proporciona la misma instancia de AnalyticsAdapter durante el ciclo de vida de la actividad correspondiente
 * */
/*
@ActivityScoped
class AnalyticsAdapter @Inject constructor(
  private val service: AnalyticsService
) { ... }
 */