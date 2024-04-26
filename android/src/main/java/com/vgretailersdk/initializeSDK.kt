package com.vgretailersdk

object SDKConfig {
    var refreshtoken: String = ""
    var baseurl: String = ""
    var accesstoken: String = ""
}

class InitializeSDK{
    var baseurl : String = ""
    var accesstoken : String = ""
    var refreshtoken : String = ""
    
    constructor(baseurl: String,accesstoken : String,refreshtoken : String) {
        SDKConfig.baseurl = baseurl
        SDKConfig.accesstoken = accesstoken
        SDKConfig.refreshtoken = refreshtoken
    }
    
}