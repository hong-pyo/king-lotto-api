package com.king.lottoapi.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


/**
 *   Created by hong-pyo on 11/02/2020
 *   Time : 12:56 오전
 */

@RestController
class HelloController {
    @RequestMapping("/api/ping")
    fun hello() : String {
        return "ok~!! king lotto!"
    }
}