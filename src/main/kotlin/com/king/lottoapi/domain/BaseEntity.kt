package com.king.lottoapi.domain

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.MappedSuperclass


/**
 *   Created by hong-pyo on 06/05/2020
 *   Time : 1:22 오전
 */

@MappedSuperclass
abstract class BaseEntity<T>(
        @CreationTimestamp
        val createdAt: LocalDateTime? = null,
        @UpdateTimestamp
        val updatedAt: LocalDateTime? = null
) : DomainToDto<T>