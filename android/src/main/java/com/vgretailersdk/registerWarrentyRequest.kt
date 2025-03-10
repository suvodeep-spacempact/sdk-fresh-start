package com.vgretailersdk

import com.google.gson.annotations.SerializedName

data class RegisterWarrantyRequest(
    @SerializedName("source")
    val source:String,
    @SerializedName("nameTitle")
    val nameTitle: String,
    @SerializedName("contactNo")
    val contactNo: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("currAdd")
    val currAdd: String,
    @SerializedName("alternateNo")
    val alternateNo: String,
    @SerializedName("state")
    val state: String,
    @SerializedName("district")
    val district: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("landmark")
    val landmark: String,
    @SerializedName("pinCode")
    val pinCode: String,
    @SerializedName("dealerName")
    val dealerName: String,
    @SerializedName("dealerAdd")
    val dealerAdd: String,
    @SerializedName("dealerState")
    val dealerState: String,
    @SerializedName("dealerDist")
    val dealerDist: String,
    @SerializedName("dealerCity")
    val dealerCity: String,
    @SerializedName("dealerPinCode")
    val dealerPinCode: String,
    @SerializedName("dealerNumber")
    val dealerNumber: String,
    @SerializedName("addedBy")
    val addedBy: Int,
    @SerializedName("billDetails")
    val billDetails: String,
    @SerializedName("warrantyPhoto")
    val warrantyPhoto: String,
    @SerializedName("sellingPrice")
    val sellingPrice: String,
    @SerializedName("emptStr")
    val emptStr: String,
    @SerializedName("cresp")
    val cresp: Cresp,
    @SerializedName("selectedProd")
    val selectedProd: SelectedProd,
    @SerializedName("latitude")
    val latitude: String,
    @SerializedName("longitude")
    val longitude: String,
    @SerializedName("geolocation")
    val geolocation: String,
    @SerializedName("dealerCategory")
    val dealerCategory: String

)

data class Cresp(
    @SerializedName("custIdForProdInstall")
    val custIdForProdInstall: String,
    @SerializedName("modelForProdInstall")
    val modelForProdInstall: String,
    @SerializedName("errorCode")
    val errorCode: Int,
    @SerializedName("errorMsg")
    val errorMsg: String,
    @SerializedName("statusType")
    val statusType: Int,
    @SerializedName("balance")
    val balance: String,
    @SerializedName("currentPoints")
    val currentPoints: String,
    @SerializedName("couponPoints")
    val couponPoints: String,
    @SerializedName("promotionPoints")
    val promotionPoints: String,
    @SerializedName("transactId")
    val transactId: String,
    @SerializedName("schemePoints")
    val schemePoints: String,
    @SerializedName("basePoints")
    val basePoints: String,
    @SerializedName("clubPoints")
    val clubPoints: String,
    @SerializedName("scanDate")
    val scanDate: String,
    @SerializedName("scanStatus")
    val scanStatus: String,
    @SerializedName("copuonCode")
    val copuonCode: String,
    @SerializedName("bitEligibleScratchCard")
    val bitEligibleScratchCard: Boolean,
    @SerializedName("partId")
    val partId: Int,
    @SerializedName("partNumber")
    val partNumber: String,
    @SerializedName("partName")
    val partName: String,
    @SerializedName("couponCode")
    val couponCode: String,
    @SerializedName("skuDetail")
    val skuDetail: String,
    @SerializedName("purchaseDate")
    val purchaseDate: String,
    @SerializedName("categoryId")
    val categoryId: String,
    @SerializedName("category")
    val category: String,
    @SerializedName("anomaly")
    val anomaly: Int,
    @SerializedName("warranty")
    val warranty: String
)

data class SelectedProd(
    @SerializedName("specs")
    val specs: String,
    @SerializedName("pointsFormat")
    val pointsFormat: String,
    @SerializedName("product")
    val product: String,
    @SerializedName("productName")
    val productName: String,
    @SerializedName("productCategory")
    val productCategory: String,
    @SerializedName("productCode")
    val productCode: String,
    @SerializedName("points")
    val points: Int,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("userId")
    val userId: String,
    @SerializedName("productId")
    val productId: String,
    @SerializedName("paytmMobileNo")
    val paytmMobileNo: String
)