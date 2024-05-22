package com.vgretailersdk
data class UserData(
    val userRole: String,
    val imageRelated: String,
    val file: UserFile
)

data class UserFile(
    val uri: String,
    val type: String,
    val name: String
)
