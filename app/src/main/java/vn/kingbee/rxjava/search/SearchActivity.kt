package vn.kingbee.rxjava.search

import vn.kingbee.widget.BaseActivity
import android.os.Bundle
import android.widget.SearchView
import android.widget.TextView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Predicate
import io.reactivex.schedulers.Schedulers
import vn.kingbee.widget.R
import java.util.concurrent.TimeUnit


class SearchActivity : BaseActivity() {
    private lateinit var searchView: SearchView
    private lateinit var textViewResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        searchView = findViewById(R.id.searchView)
        textViewResult = findViewById(R.id.textViewResult)

        setUpSearchObservable()
    }

    private fun setUpSearchObservable() {
        RxSearchObservable.fromView(searchView).debounce(300, TimeUnit.MILLISECONDS)
            .filter(object : Predicate<String> {
                override fun test(t: String): Boolean {
                    if (t.isEmpty()) {
                        textViewResult.text = ""
                        return false
                    } else {
                        return true
                    }
                }
            }).distinctUntilChanged().switchMap { query -> dataFromNetwork(query) }
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe { result -> textViewResult.text = result }
    }

    private fun dataFromNetwork(query: String): Observable<String> {
        return Observable.just(true).delay(2, TimeUnit.SECONDS).map { query }
    }
}