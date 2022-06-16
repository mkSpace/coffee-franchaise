package db.entity

data class Member(
    val memberId: String,
    val password: String,
    val name: String,
    val age: Int,
    val job: String,
    val couponCount: Int = 0
)