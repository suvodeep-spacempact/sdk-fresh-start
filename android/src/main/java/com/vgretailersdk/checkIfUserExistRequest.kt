package com.vgretailersdk

import com.google.gson.annotations.SerializedName

class CheckIfUserExistRequest{
    @SerializedName("userMobile")
    var userMobile : String = ""
    
    constructor(mobileNumber: String) {
        this.userMobile = mobileNumber
    }

}