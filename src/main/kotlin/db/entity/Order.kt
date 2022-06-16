package db.entity

import db.DBConnection
import java.time.LocalDateTime

data class Order(
    val id: Int,
    val orderMethod: String,
    val orderProduct: Int,
    val orderMember: String,
    val orderCount: Int,
    val totalPrice: Int,
    val orderDate: LocalDateTime,
    val couponCount: Int = 0
) {
    companion object {
        fun create(orderMethod: Method, orderCount: Int, member: Member, product: Product, couponCount: Int = 0): Order {
            var totalPrice = product.price * (orderCount - couponCount)
            totalPrice = if (member.job == "대학생") {
                println("대학생이시군요? 학생할인 해드릴게요!!")
                totalPrice / 10 * 9
            } else {
                totalPrice
            }
            return Order(
                DBConnection.getLatestOrderId() + 1,
                orderMethod.name,
                product.id,
                member.memberId,
                orderCount,
                totalPrice,
                LocalDateTime.now(),
                couponCount
            )
        }
    }
}

enum class Method {
    CARD, COUPON, CASH
}