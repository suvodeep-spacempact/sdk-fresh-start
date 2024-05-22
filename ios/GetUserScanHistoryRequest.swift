import Foundation

class GetUserScanHistoryRequest: Codable {
    var status: [String]?
    var scanType: String?
    var fromDate: String?
    var couponCode: String?

    // Custom initializer to initialize the object with values from the NSDictionary
    init(details: NSDictionary) {
        status = details["status"] as? [String]
        scanType = details["scanType"] as? String
        fromDate = details["fromDate"] as? String
        couponCode = details["couponCode"] as? String
    }
}
