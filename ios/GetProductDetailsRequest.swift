import Foundation

class GetProductDetailsRequest: Codable {
    var category: [Int]
    var subCategory: [Int]
    var schemeNumber: String
    var partNumber: String

    // Custom initializer to initialize the object with values from the NSDictionary
    init(details: NSDictionary) {
        category = details["category"] as? [Int] ?? []
        subCategory = details["subCategory"] as? [Int] ?? []
        schemeNumber = details["schemeNumber"] as? String ?? ""
        partNumber = details["PartNumber"] as? String ?? ""
    }
}
