import Foundation

class BankDetail: Codable {
    var bankAccHolderName: String
    var bankAccNo: String
    var bankAccType: String
    var bankIfsc: String
    var bankNameAndBranch: String
    var checkPhoto: String

    // Custom initializer to initialize the object with values from the NSDictionary
    init(details: NSDictionary) {
        bankAccHolderName = details["bankAccHolderName"] as? String ?? ""
        bankAccNo = details["bankAccNo"] as? String ?? ""
        bankAccType = details["bankAccType"] as? String ?? ""
        bankIfsc = details["bankIfsc"] as? String ?? ""
        bankNameAndBranch = details["bankNameAndBranch"] as? String ?? ""
        checkPhoto = details["checkPhoto"] as? String ?? ""
    }
}

class PaymentDetails: Codable {
    var amount: String
    var bankDetail: BankDetail

    // Custom initializer to initialize the object with values from the NSDictionary
    init(details: NSDictionary) {
        amount = details["amount"] as? String ?? ""
        if let bankDetailDict = details["bankDetail"] as? NSDictionary {
            bankDetail = BankDetail(details: bankDetailDict)
        } else {
            bankDetail = BankDetail(details: NSDictionary())
        }
    }
}
