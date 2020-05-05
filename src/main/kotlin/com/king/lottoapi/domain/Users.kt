package com.king.lottoapi.domain

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table


/**
 *   Created by hong-pyo on 06/05/2020
 *   Time : 1:39 오전
 */

@Entity
@Table(name = "users")
data class Users(
        @Id
        val id : Long
) {
}