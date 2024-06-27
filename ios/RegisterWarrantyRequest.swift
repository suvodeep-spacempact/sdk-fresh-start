import Foundation

class RegisterWarrantyRequest: Codable {
    var nameTitle: String
    var contactNo: String
    var name: String
    var email: String
    var currAdd: String
    var alternateNo: String
    var state: String
    var district: String
    var city: String
    var landmark: String
    var pinCode: String
    var dealerName: String
    var dealerAdd: String
    var dealerState: String
    var dealerDist: String
    var dealerCity: String
    var dealerPinCode: String
    var dealerNumber: String
    var addedBy: Int
    var billDetails: String
    var warrantyPhoto: String
    var sellingPrice: String
    var emptStr: String
    var cresp: Cresp
    var selectedProd: SelectedProd
    var latitude: String
    var longitude: String
    var geolocation: String
    var dealerCategory: String

    init(details: NSDictionary) {
        nameTitle = details["nameTitle"] as? String ?? ""
        contactNo = details["contactNo"] as? String ?? ""
        name = details["name"] as? String ?? ""
        email = details["email"] as? String ?? ""
        currAdd = details["currAdd"] as? String ?? ""
        alternateNo = details["alternateNo"] as? String ?? ""
        state = details["state"] as? String ?? ""
        district = details["district"] as? String ?? ""
        city = details["city"] as? String ?? ""
        landmark = details["landmark"] as? String ?? ""
        pinCode = details["pinCode"] as? String ?? ""
        dealerName = details["dealerName"] as? String ?? ""
        dealerAdd = details["dealerAdd"] as? String ?? ""
        dealerState = details["dealerState"] as? String ?? ""
        dealerDist = details["dealerDist"] as? String ?? ""
        dealerCity = details["dealerCity"] as? String ?? ""
        dealerPinCode = details["dealerPinCode"] as? String ?? ""
        dealerNumber = details["dealerNumber"] as? String ?? ""
        addedBy = details["addedBy"] as? Int ?? 0
        billDetails = details["billDetails"] as? String ?? ""
        warrantyPhoto = details["warrantyPhoto"] as? String ?? ""
        sellingPrice = details["sellingPrice"] as? String ?? ""
        emptStr = details["emptStr"] as? String ?? ""
        cresp = Cresp(details: details["cresp"] as? NSDictionary ?? NSDictionary())
        selectedProd = SelectedProd(details: details["selectedProd"] as? NSDictionary ?? NSDictionary())
        latitude = details["latitude"] as? String ?? ""
        longitude = details["longitude"] as? String ?? ""
        geolocation = details["geolocation"] as? String ?? ""
        dealerCategory = details["dealerCategory"] as? String ?? ""
    }
}

class Cresp: Codable {
    var custIdForProdInstall: String
    var modelForProdInstall: String
    var errorCode: Int
    var errorMsg: String
    var statusType: Int
    var balance: String
    var currentPoints: String
    var couponPoints: String
    var promotionPoints: String
    var transactId: String
    var schemePoints: String
    var basePoints: String
    var clubPoints: String
    var scanDate: String
    var scanStatus: String
    var copuonCode: String
    var bitEligibleScratchCard: Bool
    var partId: Int
    var partNumber: String
    var partName: String
    var couponCode: String
    var skuDetail: String
    var purchaseDate: String
    var categoryId: String
    var category: String
    var anomaly: Int
    var warranty: String

    init(details: NSDictionary) {
        custIdForProdInstall = details["custIdForProdInstall"] as? String ?? ""
        modelForProdInstall = details["modelForProdInstall"] as? String ?? ""
        errorCode = details["errorCode"] as? Int ?? 0
        errorMsg = details["errorMsg"] as? String ?? ""
        statusType = details["statusType"] as? Int ?? 0
        balance = details["balance"] as? String ?? ""
        currentPoints = details["currentPoints"] as? String ?? ""
        couponPoints = details["couponPoints"] as? String ?? ""
        promotionPoints = details["promotionPoints"] as? String ?? ""
        transactId = details["transactId"] as? String ?? ""
        schemePoints = details["schemePoints"] as? String ?? ""
        basePoints = details["basePoints"] as? String ?? ""
        clubPoints = details["clubPoints"] as? String ?? ""
        scanDate = details["scanDate"] as? String ?? ""
        scanStatus = details["scanStatus"] as? String ?? ""
        copuonCode = details["copuonCode"] as? String ?? ""
        bitEligibleScratchCard = details["bitEligibleScratchCard"] as? Bool ?? false
        partId = details["partId"] as? Int ?? 0
        partNumber = details["partNumber"] as? String ?? ""
        partName = details["partName"] as? String ?? ""
        couponCode = details["couponCode"] as? String ?? ""
        skuDetail = details["skuDetail"] as? String ?? ""
        purchaseDate = details["purchaseDate"] as? String ?? ""
        categoryId = details["categoryId"] as? String ?? ""
        category = details["category"] as? String ?? ""
        anomaly = details["anomaly"] as? Int ?? 0
        warranty = details["warranty"] as? String ?? ""
    }
}

class SelectedProd: Codable {
    var specs: String
    var pointsFormat: String
    var product: String
    var productName: String
    var productCategory: String
    var productCode: String
    var points: Int
    var imageUrl: String
    var userId: String
    var productId: String
    var paytmMobileNo: String

    init(details: NSDictionary) {
        specs = details["specs"] as? String ?? ""
        pointsFormat = details["pointsFormat"] as? String ?? ""
        product = details["product"] as? String ?? ""
        productName = details["productName"] as? String ?? ""
        productCategory = details["productCategory"] as? String ?? ""
        productCode = details["productCode"] as? String ?? ""
        points = details["points"] as? Int ?? 0
        imageUrl = details["imageUrl"] as? String ?? ""
        userId = details["userId"] as? String ?? ""
        productId = details["productId"] as? String ?? ""
        paytmMobileNo = details["paytmMobileNo"] as? String ?? ""
    }
}
