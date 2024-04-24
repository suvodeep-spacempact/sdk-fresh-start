package com.vgretailersdk
import android.util.Log
class VerifyBankDetailsRequest{
    var bankIfsc : String = ""
    var bankAccNo : String = ""
    var bankAccHolderName : String = ""
    var bankAccType : String = ""
    var bankNameAndBranch : String = ""
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