package com.vgretailersdk

class GetComboSlabSchemesRequest{
    var categoryIds: Array<Int> = emptyArray()
    var subCategoryIds: Array<Int> = emptyArray()
    var endDate: String = ""
    var fromDate: String = ""
    var status: String =""

    constructor(categoryIds: Array<Int>,subCategoryIds: Array<Int>,endDate: String,fromDate: String,status: String) {
        this.categoryIds = categoryIds
        this.subCategoryIds = subCategoryIds
        this.endDate = endDate
        this.fromDate = fromDate
        this.status = status
    }

}