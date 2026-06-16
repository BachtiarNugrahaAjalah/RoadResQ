package com.app.rrq.data.model

object UserData {
    val dataUser = listOf(
        User(
            userId = "1",
            name = "Indriyani Talita Putri",
            email = "indritp3@gmail.com",
            phone = "089504633633",
            date = "24 Apr 2026",
            status = Status.BANNED
        ),
        User(
            userId = "2",
            name = "Achira DL",
            email = "acihra@gmail.com",
            phone = "08157234946",
            date = "23 Apr 2026",
            role = Role.USER
        ),
        User(
            userId = "3",
            name = "Fiki",
            email = "tbuyut@gmail.com",
            phone = "08112121121",
            date = "22 Leo 3021",
            role = Role.ADMIN
        )
    )
}