package db.repository

import db.DBConnection
import java.sql.Connection
import java.sql.ResultSet
import java.sql.Statement

open class BaseRepository {

    protected fun close(connection: Connection?, statement: Statement?, resultSet: ResultSet?) {
        connection?.close()
        statement?.close()
        resultSet?.close()
    }

    protected fun getConnection(): Connection = DBConnection.getConnection()

}