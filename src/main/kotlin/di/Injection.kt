package di

import ui.MainView
import ui.MainViewModel
import ui.View

object Injection {

    @Volatile
    private var view: View? = null

    @Volatile
    private var viewModel: MainViewModel? = null

    fun provideMainView(): View = view ?: synchronized(this) {
        view ?: MainView().also { view = it }
    }

    fun provideViewModel(): MainViewModel = viewModel ?: synchronized(this) {
        viewModel ?: MainViewModel().also { viewModel = it }
    }
}