package com.king.lottoapi.controller

import com.king.lottoapi.service.UserService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


/**
 *   Created by hong-pyo on 05/05/2020
 *   Time : 2:15 오전
 */

@RestController
class UserController(private val userService: UserService) {
    @PostMapping("/api/v1/user")
    fun checkAndSave(@RequestBody users: UsersRequest) : String {
        userService.saveUsers(users)
        return "ok~!! User API!"
    }
}

data class UsersRequest(
        val externalId : String,
        val nickName: String?,
        val gender: String?,
        val type: String,
        val profileImage: String?,
        val thumnailImage: String?
)