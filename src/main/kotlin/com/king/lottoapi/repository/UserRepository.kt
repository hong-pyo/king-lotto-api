package com.king.lottoapi.repository

import com.king.lottoapi.domain.Users
import org.springframework.data.jpa.repository.JpaRepository


/**
 *   Created by hong-pyo on 07/05/2020
 *   Time : 12:06 오전
 */

interface UserRepository : JpaRepository<Users, Long> {
}