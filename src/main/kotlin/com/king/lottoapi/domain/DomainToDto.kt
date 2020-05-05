package com.king.lottoapi.domain


/**
 *   Created by hong-pyo on 06/05/2020
 *   Time : 1:24 오전
 */
   
interface DomainToDto<T> {
    fun toDto() : T
}