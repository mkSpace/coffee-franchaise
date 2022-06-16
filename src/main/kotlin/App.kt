import db.DBConnection
import di.Injection

object App {
    @JvmStatic fun main(args: Array<String>) {
        DBConnection.createTableIfNotExists()
        val view = Injection.provideMainView()
        view.setupViews()
    }
}