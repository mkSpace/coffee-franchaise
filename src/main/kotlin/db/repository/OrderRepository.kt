package db.repository

import db.entity.Order
import java.sql.*

class OrderRepository : BaseRepository() {

    @Throws(SQLException::class)
    fun save(order: Order): Order {
        val sql = "INSERT INTO 주문(주문번호, 주문방법, 주문상품, 주문회원, 주문수량, 주문금액, 주문일자, 사용쿠폰) VALUES(?, ?, ?, ?, ?, ?, ?, ?)"

        var connection: Connection? = null
        var preparedStatement: PreparedStatement? = null

        try {
            connection = getConnection()
            preparedStatement = connection.prepareStatement(sql)
            preparedStatement.apply {
                setInt(1, order.id)
                setString(2, order.orderMethod)
                setInt(3, order.orderProduct)
                setString(4, order.orderMember)
                setInt(5, order.couponCount)
                setInt(6, order.totalPrice)
                setTimestamp(7, Timestamp.valueOf(order.orderDate))
                setInt(8, order.couponCount)
            }
            preparedStatement.executeUpdate()
            return order
        } catch (e: SQLException) {
            println("db error!!")
            throw e
        } finally {
            close(connection, preparedStatement, null)
        }
    }

    @Throws(SQLException::class)
    fun findById(orderId: Int): Order {
        val sql = "SELECT * FROM 주문 WHERE 주문번호 = ?"

        var connection: Connection? = null
        var preparedStatement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            connection = getConnection()
            preparedStatement = connection.prepareStatement(sql)
            preparedStatement.setInt(1, orderId)
            resultSet = preparedStatement.executeQuery()
            if (resultSet.next()) {
                return Order(
                    id = resultSet.getInt("주문번호"),
                    orderMethod = resultSet.getString("주문방법"),
                    orderProduct = resultSet.getInt("주문상품"),
                    orderMember = resultSet.getString("주문회원"),
                    orderCount = resultSet.getInt("주문수량"),
                    totalPrice = resultSet.getInt("주문금액"),
                    orderDate = resultSet.getTimestamp("주문일자").toLocalDateTime(),
                    couponCount = resultSet.getInt("사용쿠폰"),
                )
            } else {
                throw NoSuchElementException("cannot found order / orderId : $orderId")
            }
        } catch (e: SQLException) {
            println("db error!!")
            throw e
        } finally {
            close(connection, preparedStatement, resultSet)
        }
    }

    @Throws(SQLException::class)
    fun findAll(): List<Order> {
        val sql = "SELECT * FROM 주문"

        var connection: Connection? = null
        var preparedStatement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            connection = getConnection()
            preparedStatement = connection.prepareStatement(sql)
            resultSet = preparedStatement.executeQuery()
            val orderList = mutableListOf<Order>()
            while (resultSet.next()) {
                orderList.add(
                    Order(
                        id = resultSet.getInt("주문번호"),
                        orderMethod = resultSet.getString("주문방법"),
                        orderProduct = resultSet.getInt("주문상품"),
                        orderMember = resultSet.getString("주문회원"),
                        orderCount = resultSet.getInt("주문수량"),
                        totalPrice = resultSet.getInt("주문금액"),
                        orderDate = resultSet.getTimestamp("주문일자").toLocalDateTime(),
                        couponCount = resultSet.getInt("사용쿠폰"),
                    )
                )
            }
            return orderList
        } catch (e: SQLException) {
            println("db error!!")
            throw e
        } finally {
            close(connection, preparedStatement, resultSet)
        }
    }

    @Throws(SQLException::class)
    fun delete(orderId: Int) {
        val sql = "DELETE FROM 주문 where 주문번호 = ?"

        var connection: Connection? = null
        var preparedStatement: PreparedStatement? = null

        try {
            connection = getConnection()
            preparedStatement = connection.prepareStatement(sql)
            preparedStatement.setInt(1, orderId)
            preparedStatement.executeUpdate()
        } catch (e: SQLException) {
            println("db error!!")
            throw e
        } finally {
            close(connection, preparedStatement, null)
        }
    }
}