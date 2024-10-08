import Foundation

class ValidateRetailerCouponRequest: Codable {
    var category: String
    var couponCode: String
    var from: String
    var geolocation: String
    var latitude: String
    var longitude: String
    var retailerCoupon: Bool
    var pin: String

    // Custom initializer to initialize the object with values from the NSDictionary
    init(details: NSDictionary) {
        category = details["category"] as? String ?? ""
        couponCode = details["couponCode"] as? String ?? ""
        from = details["from"] as? String ?? ""
        geolocation = details["geolocation"] as? String ?? ""
        latitude = details["latitude"] as? String ?? ""
        longitude = details["longitude"] as? String ?? ""
        retailerCoupon = details["retailerCoupon"] as? Bool ?? false
        pin = details["pin"] as? String ?? ""
    }
}