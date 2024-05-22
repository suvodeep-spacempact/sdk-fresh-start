import Foundation

class GetCategoriesListRequest: Codable {
    var categories: [Int]?

    // Custom initializer to initialize the object with values from the NSDictionary
    init(details: NSDictionary) {
        categories = details["categories"] as? [Int]
    }
}
