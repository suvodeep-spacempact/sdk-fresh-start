package com.vgretailersdk

import com.google.gson.annotations.SerializedName

data class TdsCertificate(
    @SerializedName("quater")
    val quater: Array<String>,
    @SerializedName("fiscalYear")
    val fiscalYear: Array<String>
)