import Foundation

class TdsCertificate: Codable {
    var quarter: [String]
    var fiscalYear: [String]

    // Custom initializer to initialize the object with values from the NSDictionary
    init(details: NSDictionary) {
        quarter = details["quarter"] as? [String] ?? []
        fiscalYear = details["fiscalYear"] as? [String] ?? []
    }
}

