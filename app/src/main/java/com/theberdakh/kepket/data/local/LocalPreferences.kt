package com.theberdakh.kepket.data.local

import android.content.SharedPreferences
import com.theberdakh.kepket.data.remote.models.login.UserInfo

class LocalPreferences(private val pref: SharedPreferences) {

    //Bearer Access Token
    private var tokenPreference by StringPreference(pref)

    //UserInfo
    private var userIdPreference by StringPreference(pref)
    private var usernamePreference by StringPreference(pref)
    private var passwordPreference by StringPreference(pref)
    private var imageWaiterPreference by StringPreference(pref)
    private var ratingPreference by IntPreference(pref)
    private var atWorkPreference by BooleanPreference(pref)
    private var restaurantIdPreference by StringPreference(pref)
    private var busyPreference by BooleanPreference(pref)
    private var vPreference by IntPreference(pref)

    //other preferences
    private var isLoggedInPreference by BooleanPreference(pref)

    fun getLoginStatus() = isLoggedInPreference
    fun setLoginStatus(isLoggedIn: Boolean)  {
        isLoggedInPreference = isLoggedIn
    }

    fun saveUserInfo(userInfo: UserInfo) {
        userIdPreference = userInfo.id
        usernamePreference = userInfo.username
        passwordPreference = userInfo.password
        imageWaiterPreference = userInfo.imageWaiter
        ratingPreference = userInfo.rating
        atWorkPreference = userInfo.atWork
        restaurantIdPreference = userInfo.restaurantId
        busyPreference = userInfo.busy
        vPreference = userInfo.v
    }

    fun getUserInfo(): UserInfo {
        return UserInfo(
            userIdPreference,
            usernamePreference,
            passwordPreference,
            imageWaiterPreference,
            ratingPreference,
            atWorkPreference,
            restaurantIdPreference,
            busyPreference,
            vPreference
        )

    }

    fun saveToken(token: String) {
        tokenPreference = token
    }

    fun getToken() = tokenPreference

}
