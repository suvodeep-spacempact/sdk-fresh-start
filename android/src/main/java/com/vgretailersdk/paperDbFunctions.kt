package com.vgretailersdk

import io.paperdb.Paper

class PaperDbFunctions{
    fun getBaseURL(): String {
        return Paper.book().read("baseurl", "") ?: ""
    }

    fun getAccessToken(): String {
        return Paper.book().read("accesstoken", "") ?: ""
    }

    fun getRefreshToken(): String {
        return Paper.book().read("refreshtoken", "") ?: ""
    }

    fun setBaseURL(baseurl: String) {
        Paper.book().write("baseurl", baseurl)
    }

    fun setAccessToken(accesstoken: String) {
        Paper.book().write("accesstoken", accesstoken)
    }

    fun setRefreshToken(refreshtoken: String) {
        Paper.book().write("refreshtoken", refreshtoken)
    }
}