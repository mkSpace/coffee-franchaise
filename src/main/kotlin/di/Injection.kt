package di

import db.repository.*
import ui.MainView
import ui.View

object Injection {

    @Volatile
    private var view: View? = null

    fun provideMainView(): View = view ?: synchronized(this) {
        view ?: MainView(
            memberRepository = MemberRepository(),
            productRepository = ProductRepository(),
            baristaRepository = BaristaRepository(),
            orderRepository = OrderRepository(),
            provideRepository = ProvideRepository()
        ).also { view = it }
    }
}