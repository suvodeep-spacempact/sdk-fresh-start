package com.vgretailersdk

import com.google.gson.annotations.SerializedName

class GetComboSlabSchemesRequest{
    @SerializedName("categoryIds")
    var categoryIds: Array<Int> = emptyArray()
    @SerializedName("subCategoryIds")
    var subCategoryIds: Array<Int> = emptyArray()
    @SerializedName("endDate")
    var endDate: String = ""
    @SerializedName("fromDate")
    var fromDate: String = ""
    @SerializedName("status")
    var status: String =""

    constructor(categoryIds: Array<Int>,subCategoryIds: Array<Int>,endDate: String,fromDate: String,status: String) {
        this.categoryIds = categoryIds
        this.subCategoryIds = subCategoryIds
        this.endDate = endDate
        this.fromDate = fromDate
        this.status = status
    }

}