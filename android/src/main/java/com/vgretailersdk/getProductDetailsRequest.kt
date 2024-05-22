package com.vgretailersdk

class getProductDetailsRequest{
    var category: Array<Int> = emptyArray()
    var subCategory: Array<Int> = emptyArray()
    var schemeNumber: String = ""
    var PartNumber: String = ""

    constructor(category: Array<Int>, subCategory: Array<Int>, schemeNumber: String, PartNumber: String) {
        this.category = category
        this.subCategory = subCategory
        this.schemeNumber = schemeNumber
        this.PartNumber = PartNumber
    }

}