import Foundation

class TdsCertificate: Codable {
    var quater: [String]
    var fiscalYear: [String]

    // Custom initializer to initialize the object with values from the NSDictionary
    init(details: NSDictionary) {
        quater = details["quater"] as? [String] ?? []
        fiscalYear = details["fiscalYear"] as? [String] ?? []
    }
}

