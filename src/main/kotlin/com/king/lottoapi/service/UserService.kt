package com.king.lottoapi.service

import com.king.lottoapi.controller.UsersRequest
import com.king.lottoapi.domain.Users
import com.king.lottoapi.helper.logger
import com.king.lottoapi.repository.UserRepository
import org.springframework.stereotype.Service


/**
 *   Created by hong-pyo on 07/05/2020
 *   Time : 12:07 오전
 */

@Service
class UserService(private val userRepository: UserRepository) {
    private val log = logger()

    fun saveUsers(users: UsersRequest) {
        log.info("SAVE {}", users)
        // todo - 회원가입 여부 체크 해야함.
        val user = Users.of(users)
        userRepository.save(user)
    }
}