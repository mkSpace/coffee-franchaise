package db.entity

import db.DBConnection
import java.time.LocalDateTime

data class Provide(
    val id: Int,
    val provideAt: LocalDateTime,
    val totalPrice: Int,
    val providedOrder: Int,
    val baristaName: String
) {
    companion object {
        fun create(totalPrice: Int, orderId: Int, baristaName: String): Provide {
            return Provide(
                id = DBConnection.getLatestProvideId() + 1,
                provideAt = LocalDateTime.now(),
                totalPrice = totalPrice,
                providedOrder = orderId,
                baristaName = baristaName
            )
        }
    }
}