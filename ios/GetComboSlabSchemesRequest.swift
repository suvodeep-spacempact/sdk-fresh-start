import Foundation

class GetComboSlabSchemesRequest: Codable {
    var categoryIds: [Int]
    var endDate: String
    var fromDate: String
    var status: String

    // Custom initializer to initialize the object with values from the NSDictionary
    init(details: NSDictionary) {
        categoryIds = details["categoryIds"] as? [Int] ?? []
        endDate = details["endDate"] as? String ?? ""
        fromDate = details["fromDate"] as? String ?? ""
        status = details["status"] as? String ?? ""
    }
}

