package com.example.connectionapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.connectionapp.databinding.MainBinder
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainFragment : Fragment, CompoundButton.OnCheckedChangeListener {

    companion object {
        private val TAG : String = MainFragment::class.java.getSimpleName()

        public fun newInstance() : MainFragment = MainFragment()
    }

    private var binder : MainBinder? = null
    private val viewModel : MainViewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }

    constructor() {

    }

    override fun onCreateView(inflater : LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?) : View? {
        binder = DataBindingUtil.inflate(inflater, R.layout.fragment_main,container,false)
        binder?.setViewModel(viewModel)
        binder?.setLifecycleOwner(this@MainFragment)
        return binder?.root ?: super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        Coroutines.main(this@MainFragment, { scope ->
            scope.launch( block = { binder?.getViewModel()?.observeToast()?.collectLatest ( action = { message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }) })
            scope.launch( block = { binder?.getViewModel()?.observeLiveData()?.collectLatest ( action = { tag ->
                binder?.textViewExplanation?.setText(tag)
            }) })
        })
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCheckedChanged(buttonView : CompoundButton?, isChecked : Boolean) {
        if (buttonView == binder?.toggleButton)
            viewModel.onCheckNFC(isChecked)
    }
}