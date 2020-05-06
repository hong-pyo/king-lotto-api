package com.king.lottoapi.domain

import com.king.lottoapi.controller.UsersRequest
import com.king.lottoapi.enums.UserAuth
import com.king.lottoapi.enums.UserType
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
        val type: UserType,
        @Enumerated(EnumType.STRING)
        val auth: UserAuth,
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
                auth = auth,
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
                                type = UserType.KAKAO, // todo 받아온거에서 찾아넣도록 변경해야함.
                                status = "REGIST", // todo 여기도 enum 으로 변경한후 바꿔주기.
                                auth = UserAuth.NORMAL,
                                profileImage = usersRequest.profileImage,
                                thumnailImage = usersRequest.thumnailImage,
                                visitCount = 0L
                        )
                }
        }
}