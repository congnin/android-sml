# MVVM benefit
* Handle configuration changes:
ViewModel objects are automatically retained whenever activity is recreated due to configuration changes.
* Lifecycle Awareness:
ViewModel objects are also lifecycle-aware. They are automatically cleared when the Lifecycle they are observing gets permanently destroyed.
* Data Sharing:
Data can be easily shared between fragments in an activity using ViewModels.
* Kotlin-Coroutines support:
ViewModel includes support for Kotlin-Coroutines. So, they can be easily integrated for any asynchronous processing.


## The creation of viewmodel involves 2 steps:

 * Creation of ViewModelProvider
 * Getting the instance of Viewmodel from ViewModelProvider

## Full document
[Link](https://blog.mindorks.com/android-viewmodels-under-the-hood)