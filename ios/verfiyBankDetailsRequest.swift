import Foundation
class VerifyBankDetailsRequest: Codable {
    var bankIfsc: String?
    var bankAccNo: String?
    var bankAccHolderName: String?
    var bankAccType: String?
    var bankNameAndBranch: String?
    var checkPhoto: String?

    // Custom initializer to initialize the object with values from the NSDictionary
    init(details: NSDictionary) {
        bankIfsc = details["bankIfsc"] as? String
        bankAccNo = details["bankAccNo"] as? String
        bankAccHolderName = details["bankAccHolderName"] as? String
        bankAccType = details["bankAccType"] as? String
        bankNameAndBranch = details["bankNameAndBranch"] as? String
        checkPhoto = details["checkPhoto"] as? String
    }
}
