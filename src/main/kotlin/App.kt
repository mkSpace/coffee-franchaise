import di.Injection

fun main(args: Array<String>) {
    val view = Injection.provideMainView()
    view.setupViews()
    DBConnection()
}