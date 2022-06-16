package db.repository

import db.entity.Member
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

class MemberRepository : BaseRepository() {

    @Throws(SQLException::class)
    fun save(member: Member): Member {
        val sql = "INSERT INTO 회원(회원아이디, 비밀번호, 이름, 나이, 직업, 쿠폰) VALUES(?, ?, ?, ?, ?, ?)"

        var connection: Connection? = null
        var preparedStatement: PreparedStatement? = null

        try {
            connection = getConnection()
            preparedStatement = connection.prepareStatement(sql)
            preparedStatement.apply {
                setString(1, member.memberId)
                setString(2, member.password)
                setString(3, member.name)
                setInt(4, member.age)
                setString(5, member.job)
                setInt(6, member.couponCount)
            }
            preparedStatement.executeUpdate()
            return member
        } catch (e: SQLException) {
            println("db error!!")
            throw e
        } finally {
            close(connection, preparedStatement, null)
        }
    }

    @Throws(SQLException::class)
    fun findByIdAndPassword(id: String, password: String): Member? {
        val sql = "SELECT * FROM 회원 WHERE 회원아이디 = ? AND 비밀번호 = ?"

        var connection: Connection? = null
        var preparedStatement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            connection = getConnection()
            preparedStatement = connection.prepareStatement(sql)
            preparedStatement.setString(1, id)
            preparedStatement.setString(2, password)

            resultSet = preparedStatement.executeQuery()

            if (resultSet.next()) {
                return Member(
                    memberId = resultSet.getString("회원아이디"),
                    password = resultSet.getString("비밀번호"),
                    name = resultSet.getString("이름"),
                    age = resultSet.getInt("나이"),
                    job = resultSet.getString("직업"),
                    couponCount = resultSet.getInt("쿠폰")
                )
            } else {
                return null
            }
        } catch (e: SQLException) {
            println("db error!!")
            throw e
        } finally {
            close(connection, preparedStatement, null)
        }
    }

    @Throws(SQLException::class)
    fun findById(id: String): Member {
        val sql = "SELECT * FROM 회원 WHERE 회원아이디 = ?"

        var connection: Connection? = null
        var preparedStatement: PreparedStatement? = null
        var resultSet: ResultSet? = null

        try {
            connection = getConnection()
            preparedStatement = connection.prepareStatement(sql)
            preparedStatement.setString(1, id)

            resultSet = preparedStatement.executeQuery()

            if (resultSet.next()) {
                return Member(
                    memberId = resultSet.getString("회원아이디"),
                    password = resultSet.getString("비밀번호"),
                    name = resultSet.getString("이름"),
                    age = resultSet.getInt("나이"),
                    job = resultSet.getString("직업"),
                    couponCount = resultSet.getInt("쿠폰")
                )
            } else {
                throw NoSuchElementException("cannot found member / memberId : $id")
            }
        } catch (e: SQLException) {
            println("db error!!")
            throw e
        } finally {
            close(connection, preparedStatement, null)
        }
    }

    @Throws(SQLException::class)
    fun updateCouponCount(memberId: String, couponCount: Int) {
        val sql = "UPDATE 회원 set 쿠폰 = ? where 회원아이디 = ?"

        var connection: Connection? = null
        var preparedStatement: PreparedStatement? = null

        try {
            connection = getConnection()
            preparedStatement = connection.prepareStatement(sql)
            preparedStatement.apply {
                setInt(1, couponCount)
                setString(2, memberId)
            }
            preparedStatement.executeUpdate()
        } catch (e: SQLException) {
            println("db error!!")
            throw e
        } finally {
            close(connection, preparedStatement, null)
        }
    }
}