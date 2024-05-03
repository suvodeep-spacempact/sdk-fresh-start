package com.vgretailersdk

class getUserBasePointsRequest{
    var categoryIds: Array<String> = emptyArray()
    var subCategoryIds: Array<String> = emptyArray()


    constructor(categoryIds: Array<String>, subCategoryIds: Array<String>) {
        this.categoryIds = categoryIds
        this.subCategoryIds = subCategoryIds
    }

}