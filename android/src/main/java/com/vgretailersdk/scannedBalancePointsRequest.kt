package com.vgretailersdk

class ScannedBalancePointsRequest{
    var categories: Array<Int> = emptyArray()
    var subCategories: Array<Int> = emptyArray()
    var userId : String = ""
    constructor(categories: Array<Int>,subCategories: Array<Int>,userId: String) {
        this.userId = userId
        this.categories = categories
        this.subCategories = subCategories
    }

}