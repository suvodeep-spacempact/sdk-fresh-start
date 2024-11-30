package com.vgretailersdk

import com.google.gson.annotations.SerializedName

class getCategoriesListRequest{
    @SerializedName("categories")
    var categories: Array<Int> = emptyArray()

    constructor(categories: Array<Int>) {
        this.categories = categories
    }

}