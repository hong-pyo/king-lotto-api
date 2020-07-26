package com.king.lottoapi.enums


/**
 *   Created by hong-pyo on 05/06/2020
 *   Time : 1:36 오전
 */

enum class StatusType(val description: String) {
    REGISTER("가입"),
    NOT_FOUND("탈퇴"),
    DORMANT("휴먼")
}