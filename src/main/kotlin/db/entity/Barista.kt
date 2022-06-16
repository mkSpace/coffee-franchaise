package db.entity

import java.time.LocalDate

data class Barista(
    val name: String,
    val entryAt: LocalDate,
    val phoneNumber: String
)