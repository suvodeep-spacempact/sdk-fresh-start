import Foundation

class ScanInRequest: Codable {
    var couponCode: String
    var pin: String
    var smsText: String
    var from: String
    var userType: String
    var userId: String
    var apmID: String
    var userCode: String
    var latitude: String
    var longitude: String
    var geolocation: String
    var category: String
    var source: String
    // Custom initializer to initialize the object with values from the NSDictionary
    init(details: NSDictionary) {
        couponCode = details["couponCode"] as? String ?? ""
        pin = details["pin"] as? String ?? ""
        smsText = details["smsText"] as? String ?? ""
        from = details["from"] as? String ?? ""
        userType = details["userType"] as? String ?? ""
        userId = details["userId"] as? String ?? ""
        apmID = details["apmID"] as? String ?? ""
        userCode = details["userCode"] as? String ?? ""
        latitude = details["latitude"] as? String ?? ""
        longitude = details["longitude"] as? String ?? ""
        geolocation = details["geolocation"] as? String ?? ""
        category = details["category"] as? String ?? ""
        source = "SDK"
    }
}


