import Foundation

class GetUserBasePointsRequest: Codable {
    var categoryIds: [String]?
    var subCategoryIds: [String]?

    // Custom initializer to initialize the object with values from the NSDictionary
    init(details: NSDictionary) {
        categoryIds = details["categoryIds"] as? [String]
        subCategoryIds = details["subCategoryIds"] as? [String]
    }
}
