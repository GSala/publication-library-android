package edu.upc.mcia.publications.ui

import android.arch.lifecycle.*
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v4.app.Fragment
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlin.properties.ObservableProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

@Suppress("UNCHECKED_CAST")
inline fun <reified VM : ViewModel> Fragment.viewModelProvider(crossinline provider: () -> VM) = lazy {
    val factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>) =
                provider() as T
    }
    ViewModelProviders.of(activity, factory).get(VM::class.java)
}

inline fun <reified BINDING : ViewDataBinding> Fragment.bindingProvider(layoutId: Int, vararg variables: Pair<Int, Any>) = lazy {
    DataBindingUtil.inflate<BINDING>(layoutInflater, layoutId, null, false)
            .apply { variables.forEach { setVariable(it.first, it.second) } }
}

inline fun <T> observableValue(initialValue: T, crossinline onChange: (newValue: T) -> Unit):
        ReadWriteProperty<Any?, T> = object : ObservableProperty<T>(initialValue) {
    override fun afterChange(property: KProperty<*>, oldValue: T, newValue: T) = onChange(newValue)
}

fun Disposable.addTo(disposable: CompositeDisposable) {
    disposable.add(this)
}

fun <T> Flowable<T>.toLiveData() {
    LiveDataReactiveStreams.fromPublisher(this)
}

fun <T> Observable<T>.toLiveData(): LiveData<T> {
    return LiveDataReactiveStreams.fromPublisher(this.toFlowable(io.reactivex.BackpressureStrategy.LATEST))
}