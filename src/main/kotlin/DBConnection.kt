import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement

class DBConnection {

    companion object {
        private const val JDBC_DRIVER = "com.mysql.jdbc.Driver"
        private const val DB_URL = "jdbc:mysql://localhost:3306/s2017112622/?characterEncoding=UTF-8&serverTimezone=UTC"
        private const val USER = "s2017112622"
        private const val PASSWD = ""
    }

    private var connection: Connection = DriverManager.getConnection(DB_URL, USER, PASSWD)
    private var statement: Statement? = null

    init {
        connection.createStatement()
    }
}