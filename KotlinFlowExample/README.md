<img src=https://raw.githubusercontent.com/MindorksOpenSource/Kotlin-Flow-Android-Examples/master/art/export-kotlin-banner.png >

# Kotlin-Flow-Android-Examples - Learn Flow for Android by Examples

[![Mindorks](https://img.shields.io/badge/mindorks-opensource-blue.svg)](https://mindorks.com/open-source-projects)
[![Mindorks Community](https://img.shields.io/badge/join-community-blue.svg)](https://mindorks.com/join-community)
[![Mindorks Android Store](https://img.shields.io/badge/Mindorks%20Android%20Store-Kotlin%20Flow%20Android%20Examples-blue.svg?style=flat)](https://mindorks.com/android/store)
[![Open Source Love](https://badges.frapsoft.com/os/v1/open-source.svg?v=102)](https://opensource.org/licenses/Apache-2.0)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](https://github.com/MindorksOpenSource/Kotlin-Flow-Android-Examples/blob/master/LICENSE)

## About this project:
* If you want to get started with Kotlin Flow, then this project is for you.
* Common use-cases of Kotlin Flow in Android has been implemented in this project.
* This is your one-stop solution for learning Kotlin Flow for Android Development.

## Steps to learn Kotlin Flow by examples from this project
* First, learn the concepts of Kotlin Flow from this **[blog](https://blog.mindorks.com/what-is-flow-in-kotlin-and-how-to-use-it-in-android-project)**
* Understanding Terminal Operators in Kotlin Flow. [Learn from here](https://blog.mindorks.com/terminal-operators-in-kotlin-flow)
* Creating Flow Using Flow Builder in Kotlin. [Learn from here](https://blog.mindorks.com/creating-flow-using-flow-builder-in-kotlin)
* Exception Handling in Kotlin Flow. [Learn from here](https://blog.mindorks.com/exception-handling-in-kotlin-flow)
* StateFlow APIs in Kotlin. [Learn from here](https://blog.mindorks.com/stateflow-apis-in-kotlin)
* Implement Instant Search Using Kotlin Flow Operators. [Learn from here](https://blog.mindorks.com/instant-search-using-kotlin-flow-operators)
* Zip Operator for Parallel Multiple Network Calls. [Learn from here](https://blog.mindorks.com/kotlin-flow-zip-operator-parallel-multiple-network-calls)
* Then, just clone, build, run the project and start learning Kotlin Flow by examples.

## This Kotlin Flow Example Project will help you in learning the following for Android App Development:
* What is Kotlin Flow?
* How to use Kotlin Flow in Android?
* Step by Step guide on how to implement the Kotlin flow in Android?
* Doing simple task in Kotlin Flow
* Using operators like filter, map, reduce, flatMapConcat, zip, and etc.
* Exception in Kotlin Flow
* How to use onCompletion in Flow?
* Using Kotlin Flow with Retrofit.
* Using Kotlin Flow with Room Database.
* Making two network calls in parallel using Kotlin Flow.
* Doing task in series using Kotlin Flow.
* Unit test in Flow.

### Kotlin Flow Examples for Android Development:
* **Single Network Call:**  Learn how to make a network call using Kotlin Flow. This is a very simple use-case in Android App Development.
    * [Activity Code](app/src/main/java/com/mindorks/kotlinFlow/learn/retrofit/single/SingleNetworkCallActivity.kt)
    * [ViewModel Code](app/src/main/java/com/mindorks/kotlinFlow/learn/retrofit/single/SingleNetworkCallViewModel.kt)

* **Series Network Call:**  Learn how to make network calls in series using Kotlin Flow. This is useful when you want to make a network call which is dependent on an another network call.
    * [Activity Code](app/src/main/java/com/mindorks/kotlinFlow/learn/retrofit/series/SeriesNetworkCallsActivity.kt)
    * [ViewModel Code](app/src/main/java/com/mindorks/kotlinFlow/learn/retrofit/series/SeriesNetworkCallsViewModel.kt)

* **Parallel Network Call:**  Learn how to make network calls in parallel using Kotlin Flow. This is useful when you want to make a network call which is dependent on an another network call.
    * [Activity Code](app/src/main/java/com/mindorks/kotlinFlow/learn/retrofit/parallel/ParallelNetworkCallsActivity.kt)
    * [ViewModel Code](app/src/main/java/com/mindorks/kotlinFlow/learn/retrofit/parallel/ParallelNetworkCallsViewModel.kt)

* **Room Database Operation:** Learn how to fetch or insert entity in database using Kotlin Flow. This is useful when you are using Room Database in your Android Application.
    * [Activity Code](app/src/main/java/com/mindorks/kotlinFlow/learn/room/RoomDBActivity.kt)
    * [ViewModel Code](app/src/main/java/com/mindorks/kotlinFlow/learn/room/RoomDBViewModel.kt)

* **Long Running Task:** Learn how to do a long running task using Kotlin Flow. If you want to do any of your task in background thread using the Kotlin Flow, then this is useful.
    * [Activity Code](app/src/main/java/com/mindorks/kotlinFlow/learn/task/onetask/LongRunningTaskActivity.kt)
    * [ViewModel Code](app/src/main/java/com/mindorks/kotlinFlow/learn/task/onetask/LongRunningTaskViewModel.kt)

* **Two Long Running Tasks:** Learn how to run two long running tasks in parallel using Kotlin Flow.
    * [Activity Code](app/src/main/java/com/mindorks/kotlinFlow/learn/task/twotasks/TwoLongRunningTasksActivity.kt)
    * [ViewModel Code](app/src/main/java/com/mindorks/kotlinFlow/learn/task/twotasks/TwoLongRunningTasksViewModel.kt)

* **Catch Error Handling:** Learn how to handle error in Kotlin Flow using Catch.
    * [Activity Code](app/src/main/java/com/mindorks/kotlinFlow/learn/errorhandling/catch/CatchActivity.kt)
    * [ViewModel Code](app/src/main/java/com/mindorks/kotlinFlow/learn/errorhandling/catch/CatchViewModel.kt)

* **EmitAll Error Handling:** Learn how to handle error in Kotlin Flow using emitAll.
    * [Activity Code](app/src/main/java/com/mindorks/kotlinFlow/learn/errorhandling/emitall/EmitAllActivity.kt)
    * [ViewModel Code](app/src/main/java/com/mindorks/kotlinFlow/learn/errorhandling/emitall/EmitAllViewModel.kt)

* **Completion:**
    * [Activity Code](app/src/main/java/com/mindorks/kotlinFlow/learn/completion/CompletionActivity.kt)
    * [ViewModel Code](app/src/main/java/com/mindorks/kotlinFlow/learn/completion/CompletionViewModel.kt)

* **Reduce:**
    * [Activity Code](app/src/main/java/com/mindorks/kotlinFlow/learn/reduce/ReduceActivity.kt)
    * [ViewModel Code](app/src/main/java/com/mindorks/kotlinFlow/learn/reduce/ReduceViewModel.kt)

* **Map:**
    * [Activity Code](app/src/main/java/com/mindorks/kotlinFlow/learn/map/MapActivity.kt)
    * [ViewModel Code](app/src/main/java/com/mindorks/kotlinFlow/learn/map/MapViewModel.kt)

* **Filter:**
    * [Activity Code](app/src/main/java/com/mindorks/kotlinFlow/learn/filter/FilterActivity.kt)
    * [ViewModel Code](app/src/main/java/com/mindorks/kotlinFlow/learn/filter/FilterViewModel.kt)

* **Search Feature:** Implement Search Using Kotlin Flow Operators - Debounce, Filter, DistinctUntilChanged, FlatMapLatest.
    * [Activity Code](app/src/main/java/com/mindorks/kotlinFlow/learn/search/SearchActivity.kt)