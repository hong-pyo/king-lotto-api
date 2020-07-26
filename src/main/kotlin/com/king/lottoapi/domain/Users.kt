package com.king.lottoapi.domain

import com.king.lottoapi.controller.UsersRequest
import com.king.lottoapi.enums.AuthType
import com.king.lottoapi.enums.AccountType
import org.springframework.data.annotation.Persistent
import javax.persistence.*


/**
 *   Created by hong-pyo on 06/05/2020
 *   Time : 1:39 오전
 */

@Entity

@Table(name = "users")
data class Users(
        @Id
        val externalId : String,
        val nickName: String?,
        val gender: String?,
        val status: String,
        @Enumerated(EnumType.STRING)
        val type: AccountType,
        @Enumerated(EnumType.STRING)
        val authType: AuthType,
        val profileImage: String?,
        val thumnailImage: String?,
        @Persistent
        val visitCount: Long

) :BaseEntity<Users>() {
        override fun toDto() = Users (
                externalId = externalId,
                nickName =  nickName,
                gender = gender,
                status = status,
                type = type,
                authType = authType,
                profileImage = profileImage,
                thumnailImage = thumnailImage,
                visitCount = visitCount
        )

        companion object {
                fun of(usersRequest: UsersRequest) : Users{
                        return Users(
                                externalId = usersRequest.externalId,
                                nickName = usersRequest.nickName,
                                gender = usersRequest.gender,
                                type = AccountType.KAKAO, // todo 받아온거에서 찾아넣도록 변경해야함.
                                status = "REGIST", // todo 여기도 enum 으로 변경한후 바꿔주기.
                                authType = AuthType.NORMAL,
                                profileImage = usersRequest.profileImage,
                                thumnailImage = usersRequest.thumnailImage,
                                visitCount = 0L
                        )
                }
        }
}