package com.vgretailersdk

import com.google.gson.annotations.SerializedName

class ScannedBalancePointsRequest{
    @SerializedName("categories")
    var categories: Array<Int> = emptyArray()
    @SerializedName("subCategories")
    var subCategories: Array<Int> = emptyArray()
    @SerializedName("userId")
    var userId : String = ""
    constructor(categories: Array<Int>,subCategories: Array<Int>,userId: String) {
        this.userId = userId
        this.categories = categories
        this.subCategories = subCategories
    }

}