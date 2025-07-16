package com.vgretailersdk

import com.google.gson.annotations.SerializedName

class getProductDetailsRequest{
    @SerializedName("category")
    var category: Array<Int> = emptyArray()
    @SerializedName("subCategory")
    var subCategory: Array<Int> = emptyArray()
    @SerializedName("schemeNumber")
    var schemeNumber: String = ""
    @SerializedName("PartNumber")
    var PartNumber: String = ""

    constructor(category: Array<Int>, subCategory: Array<Int>, schemeNumber: String, PartNumber: String) {
        this.category = category
        this.subCategory = subCategory
        this.schemeNumber = schemeNumber
        this.PartNumber = PartNumber
    }

}