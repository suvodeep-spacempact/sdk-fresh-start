import Foundation

class ScannedBalancePointsRequest: Codable {
    var categories: [Int]
    var subCategories: [Int]
    var userId: String
    var scheme:String
    // Custom initializer to initialize the object with values from the NSDictionary
    init(details: NSDictionary) {
        categories = details["categories"] as? [Int] ?? []
        subCategories = details["subCategories"] as? [Int] ?? []
        userId = details["userId"] as? String ?? ""
        scheme = details["scheme"] as? String ?? ""
    }
}

