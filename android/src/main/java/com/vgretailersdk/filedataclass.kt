package com.vgretailersdk

import com.google.gson.annotations.SerializedName

data class UserData(
    @SerializedName("userRole")
    val userRole: String,
    @SerializedName("imageRelated")
    val imageRelated: String,
    @SerializedName("file")
    val file: UserFile
)

data class UserFile(
    @SerializedName("uri")
    val uri: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("name")
    val name: String
)
