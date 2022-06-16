package db.repository

import db.entity.Provide
import java.sql.*

class ProvideRepository : BaseRepository() {

    @Throws(SQLException::class)
    fun save(provide: Provide): Provide {
        val sql = "INSERT INTO 공급(공급번호, 공급일자, 공급금액, 주문번호, 담당자) VALUES(?, ?, ?, ?, ?)"

        var connection: Connection? = null
        var preparedStatement: PreparedStatement? = null

        try {
            connection = getConnection()
            preparedStatement = connection.prepareStatement(sql)
            preparedStatement.apply {
                setInt(1, provide.id)
                setTimestamp(2, Timestamp.valueOf(provide.provideAt))
                setInt(3, provide.totalPrice)
                setInt(4, provide.providedOrder)
                setString(5, provide.baristaName)
            }
            preparedStatement.executeUpdate()
            return provide
        } catch (e: SQLException) {
            println("db error!!")
            throw e
        } finally {
            close(connection, preparedStatement, null)
        }
    }

    fun findByOrder(orderId: Int): Provide {
        val sql = "SELECT * FROM 공급 WHERE 주문번호 = ?"

        var connection: Connection? = null
        var preparedStatement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            connection = getConnection()
            preparedStatement = connection.prepareStatement(sql)
            preparedStatement.setInt(1, orderId)

            resultSet = preparedStatement.executeQuery()

            if (resultSet.next()) {
                return Provide(
                    id = resultSet.getInt("공급번호"),
                    provideAt = resultSet.getTimestamp("공급일자").toLocalDateTime(),
                    totalPrice = resultSet.getInt("공급금액"),
                    providedOrder = resultSet.getInt("주문번호"),
                    baristaName = resultSet.getString("담당자")
                )
            } else {
                throw NoSuchElementException("cannot found provide / orderId : $orderId")
            }
        } catch (e: SQLException) {
            println("db error!!")
            throw e
        } finally {
            close(connection, preparedStatement, null)
        }
    }

    fun findAll(): List<Provide> {
        val sql = "SELECT * FROM 공급"

        var connection: Connection? = null
        var preparedStatement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            connection = getConnection()
            preparedStatement = connection.prepareStatement(sql)
            resultSet = preparedStatement.executeQuery()
            val provideList = mutableListOf<Provide>()
            while (resultSet.next()) {
                provideList.add(
                    Provide(
                        id = resultSet.getInt("공급번호"),
                        provideAt = resultSet.getTimestamp("공급일자").toLocalDateTime(),
                        totalPrice = resultSet.getInt("공급금액"),
                        providedOrder = resultSet.getInt("주문번호"),
                        baristaName = resultSet.getString("담당자")
                    )
                )
            }
            return provideList
        } catch (e: SQLException) {
            println("db error!!")
            throw e
        } finally {
            close(connection, preparedStatement, resultSet)
        }
    }

    @Throws(SQLException::class)
    fun delete(provideId: Int) {
        val sql = "DELETE FROM 공급 where 공급번호 = ?"

        var connection: Connection? = null
        var preparedStatement: PreparedStatement? = null

        try {
            connection = getConnection()
            preparedStatement = connection.prepareStatement(sql)
            preparedStatement.setInt(1, provideId)
            preparedStatement.executeUpdate()
        } catch (e: SQLException) {
            println("db error!!")
            throw e
        } finally {
            close(connection, preparedStatement, null)
        }
    }
}