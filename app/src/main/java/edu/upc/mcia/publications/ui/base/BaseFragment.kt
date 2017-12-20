package edu.upc.mcia.publications.ui.base

import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.disposables.CompositeDisposable

abstract class BaseFragment : Fragment() {

    protected val disposables = CompositeDisposable()

    abstract val binding: ViewDataBinding

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return binding.root
    }

    override fun onDestroyView() {
        disposables.clear()

        super.onDestroyView()
    }
}