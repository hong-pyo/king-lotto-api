package com.king.lottoapi.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


/**
 *   Created by hong-pyo on 05/05/2020
 *   Time : 2:15 오전
 */

@RestController
class UserController {
    @RequestMapping("/api/v1/user")
    fun hello() : String {
        return "ok~!! User API!"
    }
}