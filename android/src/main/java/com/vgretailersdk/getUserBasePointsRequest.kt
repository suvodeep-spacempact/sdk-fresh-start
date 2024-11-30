package com.vgretailersdk

import com.google.gson.annotations.SerializedName

class getUserBasePointsRequest{
    @SerializedName("SerializedName")
    var categoryIds: Array<String> = emptyArray()
    @SerializedName("subCategoryIds")
    var subCategoryIds: Array<String> = emptyArray()


    constructor(categoryIds: Array<String>, subCategoryIds: Array<String>) {
        this.categoryIds = categoryIds
        this.subCategoryIds = subCategoryIds
    }

}