package db.repository

import db.entity.Barista
import db.entity.Product
import java.sql.*

class ProductRepository : BaseRepository() {

    @Throws(SQLException::class)
    fun save(product: Product): Product {
        val sql = "INSERT INTO 상품(상품번호, 상품명, 재고량, 단가) VALUES(?, ?, ?, ?)"

        var connection: Connection? = null
        var preparedStatement: PreparedStatement? = null

        try {
            connection = getConnection()
            preparedStatement = connection.prepareStatement(sql)
            preparedStatement.apply {
                setInt(1, product.id)
                setString(2, product.name)
                setInt(3, product.stockCount)
                setInt(3, product.price)
            }
            preparedStatement.executeUpdate()
            return product
        } catch (e: SQLException) {
            println("db error!!")
            throw e
        } finally {
            close(connection, preparedStatement, null)
        }
    }

    @Throws(SQLException::class)
    fun findAll(): List<Product> {
        val sql = "SELECT * FROM 상품"

        var connection: Connection? = null
        var preparedStatement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            connection = getConnection()
            preparedStatement = connection.prepareStatement(sql)
            resultSet = preparedStatement.executeQuery()
            val productList = mutableListOf<Product>()
            while (resultSet.next()) {
                productList.add(
                    Product(
                        id = resultSet.getInt("상품번호"),
                        name = resultSet.getString("상품명"),
                        stockCount = resultSet.getInt("재고량"),
                        price = resultSet.getInt("단가")
                    )
                )
            }
            return productList
        } catch (e: SQLException) {
            println("db error!!")
            throw e
        } finally {
            close(connection, preparedStatement, resultSet)
        }
    }

    @Throws(SQLException::class)
    fun findById(productId: Int): Product {
        val sql = "SELECT * FROM 상품 WHERE 상품번호 = ?"

        var connection: Connection? = null
        var preparedStatement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            connection = getConnection()
            preparedStatement = connection.prepareStatement(sql)
            preparedStatement.setInt(1, productId)
            resultSet = preparedStatement.executeQuery()
            if (resultSet.next()) {
                return Product(
                    id = resultSet.getInt("상품번호"),
                    name = resultSet.getString("상품명"),
                    stockCount = resultSet.getInt("재고량"),
                    price = resultSet.getInt("단가")
                )
            } else {
                throw NoSuchElementException("cannot found product / productId : $productId")
            }
        } catch (e: SQLException) {
            println("db error!!")
            throw e
        } finally {
            close(connection, preparedStatement, resultSet)
        }
    }

    @Throws(SQLException::class)
    fun updateStockCount(productId: Int, stockCount: Int) {
        val sql = "UPDATE 상품 set 재고량 = ? where 상품번호 = ?"

        var connection: Connection? = null
        var preparedStatement: PreparedStatement? = null

        try {
            connection = getConnection()
            preparedStatement = connection.prepareStatement(sql)
            preparedStatement.apply {
                setInt(1, stockCount)
                setInt(2, productId)
            }
            preparedStatement.executeUpdate()
        } catch (e: SQLException) {
            println("db error!!")
            throw e
        } finally {
            close(connection, preparedStatement, null)
        }
    }

    @Throws(SQLException::class)
    fun delete(productId: Int) {
        val sql = "DELETE FROM 상품 where 상품번호 = ?"

        var connection: Connection? = null
        var preparedStatement: PreparedStatement? = null

        try {
            connection = getConnection()
            preparedStatement = connection.prepareStatement(sql)
            preparedStatement.setInt(1, productId)
            preparedStatement.executeUpdate()
        } catch (e: SQLException) {
            println("db error!!")
            throw e
        } finally {
            close(connection, preparedStatement, null)
        }
    }
}