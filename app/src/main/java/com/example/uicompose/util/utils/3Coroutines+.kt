package com.example.uicompose.util.utils

import android.util.Log
import kotlinx.coroutines.*
import java.io.IOException
import kotlin.coroutines.CoroutineContext
import kotlin.system.measureTimeMillis

/**
 * https://developer.android.com/kotlin/coroutines
 * Complemetar https://medium.com/swlh/kotlin-coroutines-in-android-basics-9904c98d4714
 * */
class MainViewModel {
    /**
     * Example: CoroutineScope
     * Note:Defines a scope for new coroutines. Every coroutine builder is
     * an extension on CoroutineScope and inherits its coroutineContext to automatically propagate both context elements and cancellation.
    All coroutines run inside a CoroutineScope and it takes a CoroutineContext
    (I’ll talk about it later) as a parameter.
     * */
    private fun CoroutineScopeExample(){
        //Scope with custom CoroutineContext
        val customContext = Dispatchers.Default + Job()
        CoroutineScope(customContext).launch {

        }

        //main scope for UI components, has as context: SupervisorJob() + Dispatchers.Main
        MainScope().launch {

        }
        //scope which doesn’t bound to any job.launch top-level coroutines which are operating on the whole application lifetime
        GlobalScope.launch{

        }
    }
    /**
     * Example: Dispatchers.Unconfined
     * Note: Dispatchers defines which thread runs the coroutine. A coroutine can switch
     * Dispatchers anytime with withContext().
     * */
    private fun DispatchersExample(){
        CoroutineScope(Dispatchers.Unconfined).launch {

            delay(1000)
            // Writes code here running on `kotlinx.coroutines.DefaultExecutor`.

            withContext(Dispatchers.IO) {
                // Writes code running on I/O thread.
            }

            withContext(Dispatchers.Main) {
                // Writes code running on Main thread.
            }
        }
    }
    /**
     * Example: Exception Handlers
     * */
    private fun ExceptionExample(){
        val handler = CoroutineExceptionHandler{context,throwable->
            Log.i("UICOMPOSE_APP", throwable.message ?: "No message for this error")
        }
        val context:CoroutineContext = Dispatchers.IO+ handler
        CoroutineScope(context).launch {
            delay(10000)
            throw IOException()
        }
    }

    /**
     * Example: Finish Coroutine with exception
     * */
    private fun CancellationExceptionExample(){
        val parentJob = Job()
        val context:CoroutineContext = Dispatchers.IO + parentJob
        CoroutineScope(context).launch {
            delay(10000)
            throw CancellationException()
        }
    }
    /**
     * TODO: Example: jobs
     * Notes: Controls the lifetime of the coroutine, has states.
     * */


    /**
     * Example: Builders launch & async-await
     * */
    private fun LaunchBuilderExample(){
        val parentJob = Job()
        val context:CoroutineContext = Dispatchers.Main + parentJob
        CoroutineScope(context).launch {
            val time = measureTimeMillis {
                val one = fetchDataFromServerOne()
                val two = fetchDataFromServerTwo()
                Log.d(APP_TAG, "The sum is ${one + two}")
            }
            Log.d(APP_TAG, "Completed in $time ms")
        }
    }
    private fun AsyncBuilderExample(){
        CoroutineScope(Dispatchers.Default).launch {
            val time = measureTimeMillis {
                val one = async { fetchDataFromServerOne() }
                val two = async { fetchDataFromServerTwo() }
                Log.d(APP_TAG,"The sum is ${one.await() + two.await()}")
            }
            Log.d(APP_TAG, "Completed in $time ms")
        }
    }

    private suspend fun fetchDataFromServerOne():Int{
        Log.d(APP_TAG, "fetchDataFromServerOne()")

        delay(1_000)

        return 1;
    }
    private suspend fun fetchDataFromServerTwo():Int{
        Log.d(APP_TAG, "fetchDataFromServerTwo()")

        delay(1_000)

        return 2;
    }

    companion object{
        const val APP_TAG:String = "IUCOMPOSE_APP"
    }
}