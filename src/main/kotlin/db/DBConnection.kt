package db

import Constants.DB_NAME
import Constants.DB_URL
import Constants.JDBC_DRIVER
import Constants.PASSWD
import Constants.USER
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

object DBConnection {
    private lateinit var statement: Statement

    fun getConnection(): Connection {
        Class.forName(JDBC_DRIVER)
        val connection = DriverManager.getConnection(DB_URL, USER, PASSWD)
        statement = connection.createStatement()
        statement.execute("USE $DB_NAME")
        statement.close()
        return connection
    }

    fun createTableIfNotExists() {
        create회원Table()
        create상품Table()
        create바리스타Table()
        create주문Table()
        create공급Table()
    }

    private fun `create회원Table`() {
        val connection = getConnection()
        val statement = connection.createStatement()
        statement.execute("CREATE TABLE IF NOT EXISTS 회원(" +
                "회원아이디 VARCHAR(20) NOT NULL," +
                "비밀번호 VARCHAR(20) NOT NULL," +
                "이름 VARCHAR(10) NOT NULL," +
                "나이 INT," +
                "직업 VARCHAR(10) NOT NULL," +
                "쿠폰 INT," +
                "PRIMARY KEY(회원아이디)" +
                ");")
    }

    private fun create상품Table() {
        val connection = getConnection()
        val statement = connection.createStatement()
        statement.execute("CREATE TABLE IF NOT EXISTS 상품(" +
                "상품번호 INT NOT NULL," +
                "상품명 VARCHAR(20) NOT NULL," +
                "재고량 INT DEFAULT 0," +
                "단가 INT NOT NULL," +
                "PRIMARY KEY(상품번호)" +
                ");")
    }

    private fun create바리스타Table() {
        val connection = getConnection()
        val statement = connection.createStatement()
        statement.execute("CREATE TABLE IF NOT EXISTS 바리스타(" +
                "바리스타명 VARCHAR(10) NOT NULL," +
                "입사일 DATE NOT NULL," +
                "전화번호 VARCHAR(20)," +
                "PRIMARY KEY(바리스타명)" +
                ");")
    }

    private fun create주문Table() {
        val connection = getConnection()
        val statement = connection.createStatement()
        statement.execute("CREATE TABLE IF NOT EXISTS 주문(" +
                "주문번호 INT NOT NULL," +
                "주문방법 VARCHAR(10)," +
                "주문상품 INT NOT NULL," +
                "주문회원 VARCHAR(20) NOT NULL," +
                "주문수량 INT DEFAULT 1," +
                "주문금액 INT DEFAULT 0," +
                "주문일자 DATETIME," +
                "사용쿠폰 INT DEFAULT 0," +
                "PRIMARY KEY(주문번호)," +
                "FOREIGN KEY(주문상품) REFERENCES 주문(주문번호)," +
                "FOREIGN KEY(주문회원) REFERENCES 회원(회원아이디)" +
                ");")
    }

    private fun create공급Table() {
        val connection = getConnection()
        val statement = connection.createStatement()
        statement.execute("CREATE TABLE IF NOT EXISTS 공급(" +
                "공급번호 INT NOT NULL," +
                "공급일자 DATETIME NOT NULL," +
                "공급금액 INT DEFAULT 0," +
                "주문번호 INT NOT NULL," +
                "담당자 VARCHAR(10) NOT NULL," +
                "PRIMARY KEY(공급번호)," +
                "FOREIGN KEY(주문번호) REFERENCES 주문(주문번호)," +
                "FOREIGN KEY(담당자) REFERENCES 바리스타(바리스타명)" +
                ");")
    }

    @Throws(SQLException::class)
    fun getLatestProductId(): Int {
        val sql = "SELECT MAX(상품번호) AS 상품번호 FROM 상품"

        var connection: Connection? = null
        var resultSet: ResultSet? = null

        try {
            connection = getConnection()
            resultSet = connection.prepareStatement(sql).executeQuery()
            return if (resultSet.next()) {
                resultSet.getInt("상품번호")
            } else {
                0
            }
        } catch (e: SQLException) {
            println("db error!!")
            throw e
        } finally {
            connection?.close()
            resultSet?.close()
        }
    }

    @Throws(SQLException::class)
    fun getLatestOrderId(): Int {
        val sql = "SELECT MAX(주문번호) AS 주문번호 FROM 주문"

        var connection: Connection? = null
        var resultSet: ResultSet? = null

        try {
            connection = getConnection()
            resultSet = connection.prepareStatement(sql).executeQuery()
            return if (resultSet.next()) {
                resultSet.getInt("주문번호")
            } else {
                0
            }
        } catch (e: SQLException) {
            println("db error!!")
            throw e
        } finally {
            connection?.close()
            resultSet?.close()
        }
    }

    @Throws(SQLException::class)
    fun getLatestProvideId(): Int {
        val sql = "SELECT MAX(공급번호) AS 공급번호 FROM 공급"

        var connection: Connection? = null
        var resultSet: ResultSet? = null

        try {
            connection = getConnection()
            resultSet = connection.prepareStatement(sql).executeQuery()
            return if (resultSet.next()) {
                resultSet.getInt("공급번호")
            } else {
                0
            }
        } catch (e: SQLException) {
            println("db error!!")
            throw e
        } finally {
            connection?.close()
            resultSet?.close()
        }
    }
}