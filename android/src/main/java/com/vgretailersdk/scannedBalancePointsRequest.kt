package com.vgretailersdk

import com.google.gson.annotations.SerializedName

class ScannedBalancePointsRequest{
    @SerializedName("categories")
    var categories: Array<Int> = emptyArray()
    @SerializedName("subCategories")
    var subCategories: Array<Int> = emptyArray()
    @SerializedName("userId")
    var userId : String = ""
    @SerializedName("scheme")
    var scheme: Array<String> = emptyArray()
    constructor(categories: Array<Int>,subCategories: Array<Int>,userId: String, scheme: Array<String>) {
        this.userId = userId
        this.categories = categories
        this.subCategories = subCategories
        this.scheme = scheme
    }

}
