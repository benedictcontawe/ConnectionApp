package com.example.nfcapp

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

public object Coroutines {
    //region UI contexts
    fun main(work : suspend (() -> Unit)) =
        CoroutineScope(Dispatchers.Main.immediate).launch {
            work()
        }
    fun main(activity : AppCompatActivity, work : suspend ((scope : CoroutineScope) -> Unit)) =
        activity.lifecycleScope.launch {
            activity.lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                work(this)
            }
        }
    fun main(fragment : BottomSheetDialogFragment, work : suspend ((scope : CoroutineScope) -> Unit)) =
        fragment.lifecycleScope.launch {
            fragment.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                work(this)
            }
        }
    fun main(fragment : DialogFragment, work : suspend ((scope : CoroutineScope) -> Unit)) =
        fragment.lifecycleScope.launch {
            fragment.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                work(this)
            }
        }
    fun main(fragment : Fragment, work : suspend ((scope : CoroutineScope) -> Unit)) =
        fragment.lifecycleScope.launch {
            fragment.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                work(this)
            }
        }
    //endregion
    //region I/O operations
    fun io(work : suspend (() -> Unit)) =
        CoroutineScope(Dispatchers.IO).launch {
            work()
        }

    fun io(viewModel : ViewModel, work : suspend (() -> Unit)) {
        viewModel.viewModelScope.launch(Dispatchers.IO) {
            work()
        }
    }
    //endregion
    //region Uses heavy CPU computation
    fun default(work : suspend (() -> Unit)) =
        CoroutineScope(Dispatchers.Default).launch {
            work()
        }

    fun default(viewModel : ViewModel, work : suspend (() -> Unit)) =
        viewModel.viewModelScope.launch(Dispatchers.Default) {
            work()
        }
    //endregion
    //region No need to run on specific context
    fun unconfined(work : suspend (() -> Unit)) =
        CoroutineScope(Dispatchers.Unconfined).launch {
            work()
        }
    //endregion
}