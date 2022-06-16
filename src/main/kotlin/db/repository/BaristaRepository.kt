package db.repository

import db.entity.Barista
import java.sql.*

class BaristaRepository : BaseRepository() {

    @Throws(SQLException::class)
    fun save(barista: Barista): Barista {
        val sql = "INSERT INTO 바리스타(바리스타명, 입사일, 전화번호) VALUES(?, ?, ?)"

        var connection: Connection? = null
        var preparedStatement: PreparedStatement? = null

        try {
            connection = getConnection()
            preparedStatement = connection.prepareStatement(sql)
            preparedStatement.apply {
                setString(1, barista.name)
                setDate(2, Date.valueOf(barista.entryAt))
                setString(3, barista.phoneNumber)
            }
            preparedStatement.executeUpdate()
            return barista
        } catch (e: SQLException) {
            println("db error!!")
            throw e
        } finally {
            close(connection, preparedStatement, null)
        }
    }

    fun findAll(): List<Barista> {
        val sql = "SELECT * FROM 바리스타"

        var connection: Connection? = null
        var preparedStatement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            connection = getConnection()
            preparedStatement = connection.prepareStatement(sql)
            resultSet = preparedStatement.executeQuery()
            val baristaList = mutableListOf<Barista>()
            while (resultSet.next()) {
                baristaList.add(
                    Barista(
                        name = resultSet.getString("바리스타명"),
                        entryAt = resultSet.getDate("입사일").toLocalDate(),
                        phoneNumber = resultSet.getString("전화번호")
                    )
                )
            }
            return baristaList
        } catch (e: SQLException) {
            println("db error!!")
            throw e
        } finally {
            close(connection, preparedStatement, resultSet)
        }
    }

    fun findByName(baristaName: String): Barista {
        val sql = "SELECT * FROM 바리스타 WHERE 바리스타명 = ?"

        var connection: Connection? = null
        var preparedStatement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            connection = getConnection()
            preparedStatement = connection.prepareStatement(sql)
            preparedStatement.setString(1, baristaName)
            resultSet = preparedStatement.executeQuery()
            if (resultSet.next()) {
                return Barista(
                    name = resultSet.getString("바리스타명"),
                    entryAt = resultSet.getDate("입사일").toLocalDate(),
                    phoneNumber = resultSet.getString("전화번호")
                )
            } else {
                throw NoSuchElementException("cannot found barista / baristaName : $baristaName")
            }
        } catch (e: SQLException) {
            println("db error!!")
            throw e
        } finally {
            close(connection, preparedStatement, resultSet)
        }
    }

    @Throws(SQLException::class)
    fun delete(baristaName: String) {
        val sql = "DELETE FROM 바리스타 where 바리스타명 = ?"

        var connection: Connection? = null
        var preparedStatement: PreparedStatement? = null

        try {
            connection = getConnection()
            preparedStatement = connection.prepareStatement(sql)
            preparedStatement.setString(1, baristaName)
            preparedStatement.executeUpdate()
        } catch (e: SQLException) {
            println("db error!!")
            throw e
        } finally {
            close(connection, preparedStatement, null)
        }
    }
}