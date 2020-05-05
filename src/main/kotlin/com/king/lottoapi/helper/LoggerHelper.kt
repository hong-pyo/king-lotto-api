package com.king.lottoapi.helper

import org.slf4j.Logger
import org.slf4j.LoggerFactory


/**
 *   Created by hong-pyo on 06/05/2020
 *   Time : 1:45 오전
 */

inline fun <reified T> T.logger() : Logger {
    return LoggerFactory.getLogger(T::class.java)
}