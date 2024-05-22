import Foundation

class RewardHistoryRequest: Codable {
    var mode: [String]
    var status: [String]
    var fromDate: String
    var toDate: String
    var userId: String

    // Custom initializer to initialize the object with values from the NSDictionary
    init(details: NSDictionary) {
        mode = details["mode"] as? [String] ?? []
        status = details["status"] as? [String] ?? []
        fromDate = details["fromDate"] as? String ?? ""
        toDate = details["toDate"] as? String ?? ""
        userId = details["userId"] as? String ?? ""
    }
}
