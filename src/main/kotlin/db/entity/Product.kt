package db.entity

data class Product(
    val id: Int,
    val name: String,
    val stockCount: Int,
    val price: Int
)