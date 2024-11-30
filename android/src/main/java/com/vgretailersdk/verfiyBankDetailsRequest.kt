package com.vgretailersdk
import android.util.Log
import com.google.gson.annotations.SerializedName

class VerifyBankDetailsRequest{
    @SerializedName("bankIfsc")
    var bankIfsc : String = ""
    @SerializedName("bankAccNo")
    var bankAccNo : String = ""
    @SerializedName("bankAccHolderName")
    var bankAccHolderName : String = ""
    @SerializedName("bankAccType")
    var bankAccType : String = ""
    @SerializedName("bankNameAndBranch")
    var bankNameAndBranch : String = ""
    @SerializedName("checkPhoto")
    var checkPhoto : String = ""
    
    constructor(bankIfsc: String, bankAccNo: String,bankAccHolderName: String,bankAccType: String,bankNameAndBranch: String,checkPhoto: String) {
        this.bankIfsc = bankIfsc
        this.bankAccNo = bankAccNo
        this.bankAccHolderName = bankAccHolderName
        this.bankAccType = bankAccType
        this.bankNameAndBranch = bankNameAndBranch
        this.checkPhoto = checkPhoto
    }

    fun printProperties(){
        Log.d("TAG","Properties of verifyBankDetailsRequest are :"+this.bankIfsc)
    }

}