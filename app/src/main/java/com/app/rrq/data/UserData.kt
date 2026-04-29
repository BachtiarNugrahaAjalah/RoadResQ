package com.app.rrq.data

import com.app.rrq.model.Role
import com.app.rrq.model.User
import com.app.rrq.model.Status

object UserData {
    val dataUser = listOf(
        User(
            "Indriyani Talita Putri",
            "indritp3@gmail.com",
            "089504633633",
            "24 Apr 2026",
            status = Status.BANNED
        ),
        User(
            "Achira DL",
            "acihra@gmail.com",
            "08157234946",
            "23 Apr 2026",
            role = Role.USER
        ),
        User(
            name = "Fiki",
            email = "tbuyut@gmail.com",
            phone = "08112121121",
            date = "22 Leo 3021",
            role = Role.ADMIN
        )
    )
}