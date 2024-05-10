package com.vgretailersdk

import io.paperdb.Paper

class InitializeSDKNew{
    var baseurl : String = ""
    var accesstoken : String = ""
    var refreshtoken : String = ""
    
    constructor(baseurl: String,accesstoken : String,refreshtoken : String) {
        Paper.book().write("baseurl", baseurl)
        Paper.book().write("accesstoken", accesstoken)
        Paper.book().write("refreshtoken", refreshtoken)
    }
}