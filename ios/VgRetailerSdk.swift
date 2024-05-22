import Alamofire
import SwiftyJSON
import Foundation
import CoreData

struct APIConfig {
    var baseurl: String
    var accesstoken: String
    var refreshtoken: String
}


@objc(VgRetailerSdk)
class VgRetailerSdk: NSObject {
    var config: APIConfig?
  @objc(multiply:withB:withResolver:withRejecter:)
  func multiply(a: Float, b: Float, resolve:RCTPromiseResolveBlock,reject:RCTPromiseRejectBlock) -> Void {
      //print(APIConfiguration.)
      guard let config = config else {
              reject("SDK_NOT_INITIALIZED", "SDK not initialized", nil)
              return
          }

          // Accessing the values of APIConfig structure
          print("Base URL: \(config.baseurl)")
          print("Access Token: \(config.accesstoken)")
          print("Refresh Token: \(config.refreshtoken)")
      let url = "https://dummyjson.com/products/1"
      AF.request(url).responseJSON { response in
        switch response.result {
        case .success(let value):
          let json = JSON(value)
          print("Product response: \(json)")
        case .failure(let error):
          print("Error: \(error)")
        }
      }
    resolve(a*b)
  }

    @objc(verifyBankDetails:withResolver:withRejecter:)
    func verifyBankDetails(details: NSDictionary, resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock) {
        guard let config = config else {
                reject("SDK_NOT_INITIALIZED", "SDK not initialized", nil)
                return
            }
        let bankDetails = VerifyBankDetailsRequest(details: details)
        guard let jsonData = try? JSONEncoder().encode(bankDetails) else {
                reject("CONVERSION_FAILED", "Failed to convert BankDetails to JSON", nil)
                return
            }
        print(config.accesstoken)
        print("Bearer \(config.accesstoken)")
        let headers: HTTPHeaders = [
            "Content-Type": "application/json",
            "Authorization": "Bearer \(config.accesstoken)"
        ]

        AF.request(config.baseurl + "/banks/verifyBankDetails",
                   method: .post,
                   parameters: try? JSONSerialization.jsonObject(with: jsonData, options: []) as? Parameters,
                   encoding: JSONEncoding.default,
                   headers: headers)
            .responseString { response in
                switch response.result {
                case .success(let value):
                    print("Response: \(value)")
                    resolve(value)
                case .failure(let error):
                    print("Error: \(error)")
                    reject("REQUEST_FAILED", error.localizedDescription, nil)
                }
            }
    }
    

    @objc(getCategoriesList:withResolver:withRejecter:)
    func getCategoriesList(details: NSDictionary, resolve: @escaping (Any?) -> Void, reject: @escaping (String, String, Error?) -> Void){
        guard let config = config else {
                reject("SDK_NOT_INITIALIZED", "SDK not initialized", nil)
                return
            }
        let getCategoriesListRequest = GetCategoriesListRequest(details: details)

                // Here you can use getCategoriesListRequest object for further processing
        print("Categories: \(getCategoriesListRequest.categories ?? [])")

                // For example purposes, resolve with the categories array
        guard let jsonData = try? JSONEncoder().encode(getCategoriesListRequest) else {
                reject("CONVERSION_FAILED", "Failed to convert getCategoriesListRequest to JSON", nil)
                return
            }
        print(config.accesstoken)
        print("Bearer \(config.accesstoken)")
        let headers: HTTPHeaders = [
            "Content-Type": "application/json",
            "Authorization": "Bearer \(config.accesstoken)"
        ]

        AF.request(config.baseurl + "/product/getCategoriesList",
                   method: .post,
                   parameters: try? JSONSerialization.jsonObject(with: jsonData, options: []) as? Parameters,
                   encoding: JSONEncoding.default,
                   headers: headers)
            .responseString { response in
                switch response.result {
                case .success(let value):
                    print("Response: \(value)")
                    resolve(value)
                case .failure(let error):
                    print("Error: \(error)")
                    reject("REQUEST_FAILED", error.localizedDescription, nil)
                }
            }
    }
    @objc(getUserBasePoints:withResolver:withRejecter:)
    func getUserBasePoints(details: NSDictionary, resolve: @escaping (Any?) -> Void, reject: @escaping (String, String, Error?) -> Void){
        guard let config = config else {
                reject("SDK_NOT_INITIALIZED", "SDK not initialized", nil)
                return
            }
        let GetUserBasePointsRequest = GetUserBasePointsRequest(details: details)
        guard let jsonData = try? JSONEncoder().encode(GetUserBasePointsRequest) else {
                reject("CONVERSION_FAILED", "Failed to convert getCategoriesListRequest to JSON", nil)
                return
            }
        print(config.accesstoken)
        print("Bearer \(config.accesstoken)")
        let headers: HTTPHeaders = [
            "Content-Type": "application/json",
            "Authorization": "Bearer \(config.accesstoken)"
        ]

        AF.request(config.baseurl + "/coupon/getUserBasePoints",
                   method: .post,
                   parameters: try? JSONSerialization.jsonObject(with: jsonData, options: []) as? Parameters,
                   encoding: JSONEncoding.default,
                   headers: headers)
            .responseString { response in
                switch response.result {
                case .success(let value):
                    print("Response: \(value)")
                    resolve(value)
                case .failure(let error):
                    print("Error: \(error)")
                    reject("REQUEST_FAILED", error.localizedDescription, nil)
                }
            }
    }
    @objc(getUserScanHistory:withResolver:withRejecter:)
    func getUserScanHistory(details: NSDictionary, resolve: @escaping (Any?) -> Void, reject: @escaping (String, String, Error?) -> Void){
        guard let config = config else {
                reject("SDK_NOT_INITIALIZED", "SDK not initialized", nil)
                return
            }
        let GetUserScanHistoryRequest = GetUserScanHistoryRequest(details: details)
        guard let jsonData = try? JSONEncoder().encode(GetUserScanHistoryRequest) else {
                reject("CONVERSION_FAILED", "Failed to convert getCategoriesListRequest to JSON", nil)
                return
            }
        print(config.accesstoken)
        print("Bearer \(config.accesstoken)")
        let headers: HTTPHeaders = [
            "Content-Type": "application/json",
            "Authorization": "Bearer \(config.accesstoken)"
        ]

        AF.request(config.baseurl + "/coupon/getUserScanHistory",
                   method: .post,
                   parameters: try? JSONSerialization.jsonObject(with: jsonData, options: []) as? Parameters,
                   encoding: JSONEncoding.default,
                   headers: headers)
            .responseString { response in
                switch response.result {
                case .success(let value):
                    print("Response: \(value)")
                    resolve(value)
                case .failure(let error):
                    print("Error: \(error)")
                    reject("REQUEST_FAILED", error.localizedDescription, nil)
                }
            }
    }
    @objc(rewardPointsHistory:withResolver:withRejecter:)
    func rewardPointsHistory (details: NSDictionary, resolve: @escaping (Any?) -> Void, reject: @escaping (String, String, Error?) -> Void){
        guard let config = config else {
                reject("SDK_NOT_INITIALIZED", "SDK not initialized", nil)
                return
            }
        let RewardHistoryRequest = RewardHistoryRequest(details: details)
        guard let jsonData = try? JSONEncoder().encode(RewardHistoryRequest) else {
                reject("CONVERSION_FAILED", "Failed to convert getCategoriesListRequest to JSON", nil)
                return
            }
        print(config.accesstoken)
        print("Bearer \(config.accesstoken)")
        let headers: HTTPHeaders = [
            "Content-Type": "application/json",
            "Authorization": "Bearer \(config.accesstoken)"
        ]

        AF.request(config.baseurl + "/product/userRewardHistory",
                   method: .post,
                   parameters: try? JSONSerialization.jsonObject(with: jsonData, options: []) as? Parameters,
                   encoding: JSONEncoding.default,
                   headers: headers)
            .responseString { response in
                switch response.result {
                case .success(let value):
                    print("Response: \(value)")
                    resolve(value)
                case .failure(let error):
                    print("Error: \(error)")
                    reject("REQUEST_FAILED", error.localizedDescription, nil)
                }
            }
    }
    @objc(ScannedBalancePoints:withResolver:withRejecter:)
    func ScannedBalancePoints(details: NSDictionary, resolve: @escaping (Any?) -> Void, reject: @escaping (String, String, Error?) -> Void){
        guard let config = config else {
                reject("SDK_NOT_INITIALIZED", "SDK not initialized", nil)
                return
            }
        let ScannedBalancePointsRequest = ScannedBalancePointsRequest(details: details)
        guard let jsonData = try? JSONEncoder().encode(ScannedBalancePointsRequest) else {
                reject("CONVERSION_FAILED", "Failed to convert getCategoriesListRequest to JSON", nil)
                return
            }
        print(config.accesstoken)
        print("Bearer \(config.accesstoken)")
        let headers: HTTPHeaders = [
            "Content-Type": "application/json",
            "Authorization": "Bearer \(config.accesstoken)"
        ]

        AF.request(config.baseurl + "/product/userScannedBalancePoints",
                   method: .post,
                   parameters: try? JSONSerialization.jsonObject(with: jsonData, options: []) as? Parameters,
                   encoding: JSONEncoding.default,
                   headers: headers)
            .responseString { response in
                switch response.result {
                case .success(let value):
                    print("Response: \(value)")
                    resolve(value)
                case .failure(let error):
                    print("Error: \(error)")
                    reject("REQUEST_FAILED", error.localizedDescription, nil)
                }
            }
    }
    @objc(userScanOutPointSummary:withResolver:withRejecter:)
    func userScanOutPointSummary(details: NSDictionary, resolve: @escaping (Any?) -> Void, reject: @escaping (String, String, Error?) -> Void){
        guard let config = config else {
                reject("SDK_NOT_INITIALIZED", "SDK not initialized", nil)
                return
            }
        let userScanOutPointSummaryRequest = ScannedBalancePointsRequest(details: details)
        guard let jsonData = try? JSONEncoder().encode(userScanOutPointSummaryRequest) else {
                reject("CONVERSION_FAILED", "Failed to convert getCategoriesListRequest to JSON", nil)
                return
            }
        print(config.accesstoken)
        print("Bearer \(config.accesstoken)")
        let headers: HTTPHeaders = [
            "Content-Type": "application/json",
            "Authorization": "Bearer \(config.accesstoken)"
        ]

        AF.request(config.baseurl + "/product/userScanOutPointSummary",
                   method: .post,
                   parameters: try? JSONSerialization.jsonObject(with: jsonData, options: []) as? Parameters,
                   encoding: JSONEncoding.default,
                   headers: headers)
            .responseString { response in
                switch response.result {
                case .success(let value):
                    print("Response: \(value)")
                    resolve(value)
                case .failure(let error):
                    print("Error: \(error)")
                    reject("REQUEST_FAILED", error.localizedDescription, nil)
                }
            }
    }
    @objc(captureCustomerDetails:withResolver:withRejecter:)
    func captureCustomerDetails(details: NSDictionary, resolve: @escaping (Any?) -> Void, reject: @escaping (String, String, Error?) -> Void){
        guard let config = config else {
                reject("SDK_NOT_INITIALIZED", "SDK not initialized", nil)
                return
            }
        guard let mobileNo = details["mobileNo"] as? String else {
                reject("INVALID_PARAMETERS", "Product ID is missing", nil)
                return
            }
        let url = "\(config.baseurl)/product/getCustomerDetails/\(mobileNo)"
        let headers: HTTPHeaders = [
                "Content-Type": "application/json",
                "Authorization": "Bearer \(config.accesstoken)"
            ]
        AF.request(url, method: .get, headers: headers)
                .responseString { response in
                    switch response.result {
                    case .success(let value):
                        print("Response: \(value)")
                        resolve(value)
                    case .failure(let error):
                        print("Error: \(error)")
                        reject("REQUEST_FAILED", error.localizedDescription, error)
                    }
                }
    }
    @objc(registerWarranty:withResolver:withRejecter:)
    func registerWarranty(details: NSDictionary, resolve: @escaping (Any?) -> Void, reject: @escaping (String, String, Error?) -> Void){
        guard let config = config else {
                reject("SDK_NOT_INITIALIZED", "SDK not initialized", nil)
                return
            }
        let RegisterWarrantyRequest = RegisterWarrantyRequest(details: details)
        guard let jsonData = try? JSONEncoder().encode(RegisterWarrantyRequest) else {
                reject("CONVERSION_FAILED", "Failed to convert getCategoriesListRequest to JSON", nil)
                return
            }
        print(config.accesstoken)
        print("Bearer \(config.accesstoken)")
        let headers: HTTPHeaders = [
            "Content-Type": "application/json",
            "Authorization": "Bearer \(config.accesstoken)"
        ]

        AF.request(config.baseurl + "/product/registerWarranty",
                   method: .post,
                   parameters: try? JSONSerialization.jsonObject(with: jsonData, options: []) as? Parameters,
                   encoding: JSONEncoding.default,
                   headers: headers)
            .responseString { response in
                switch response.result {
                case .success(let value):
                    print("Response: \(value)")
                    resolve(value)
                case .failure(let error):
                    print("Error: \(error)")
                    reject("REQUEST_FAILED", error.localizedDescription, nil)
                }
            }
    }
    @objc(getEligibleProducts:withResolver:withRejecter:)
    func getEligibleProducts(details: NSDictionary, resolve: @escaping (Any?) -> Void, reject: @escaping (String, String, Error?) -> Void){
        guard let config = config else {
                reject("SDK_NOT_INITIALIZED", "SDK not initialized", nil)
                return
            }

            let headers: HTTPHeaders = [
                "Content-Type": "application/json",
                "Authorization": "Bearer \(config.accesstoken)"
            ]
            
            // Convert NSDictionary to JSON dictionary
            guard let jsonDetails = details as? [String: Any] else {
                reject("CONVERSION_FAILED", "Failed to convert details to JSON object", nil)
                return
            }

            AF.request(config.baseurl + "/schemes/getEligibleProducts",
                       method: .post,
                       parameters: jsonDetails,
                       encoding: JSONEncoding.default,
                       headers: headers)
                .responseString { response in
                    switch response.result {
                    case .success(let value):
                        print("Response: \(value)")
                        resolve(value)
                    case .failure(let error):
                        print("Error: \(error)")
                        reject("REQUEST_FAILED", error.localizedDescription, nil)
                    }
                }
    }
    @objc(getComboSlabSchemes:withResolver:withRejecter:)
    func getComboSlabSchemes(details: NSDictionary, resolve: @escaping (Any?) -> Void, reject: @escaping (String, String, Error?) -> Void){
        guard let config = config else {
                reject("SDK_NOT_INITIALIZED", "SDK not initialized", nil)
                return
            }
        let GetComboSlabSchemesRequest = GetComboSlabSchemesRequest(details: details)
        guard let jsonData = try? JSONEncoder().encode(GetComboSlabSchemesRequest) else {
                reject("CONVERSION_FAILED", "Failed to convert getCategoriesListRequest to JSON", nil)
                return
            }
        print(config.accesstoken)
        print("Bearer \(config.accesstoken)")
        let headers: HTTPHeaders = [
            "Content-Type": "application/json",
            "Authorization": "Bearer \(config.accesstoken)"
        ]

        AF.request(config.baseurl + "/schemes/getComboSlabSchemes",
                   method: .post,
                   parameters: try? JSONSerialization.jsonObject(with: jsonData, options: []) as? Parameters,
                   encoding: JSONEncoding.default,
                   headers: headers)
            .responseString { response in
                switch response.result {
                case .success(let value):
                    print("Response: \(value)")
                    resolve(value)
                case .failure(let error):
                    print("Error: \(error)")
                    reject("REQUEST_FAILED", error.localizedDescription, nil)
                }
            }
    }
    @objc(getSlabView:withResolver:withRejecter:)
    func getSlabView(details: NSDictionary, resolve: @escaping (Any?) -> Void, reject: @escaping (String, String, Error?) -> Void){
        guard let config = config else {
                reject("SDK_NOT_INITIALIZED", "SDK not initialized", nil)
                return
            }

            let headers: HTTPHeaders = [
                "Content-Type": "application/json",
                "Authorization": "Bearer \(config.accesstoken)"
            ]
            
            // Convert NSDictionary to JSON dictionary
            guard let jsonDetails = details as? [String: Any] else {
                reject("CONVERSION_FAILED", "Failed to convert details to JSON object", nil)
                return
            }

            AF.request(config.baseurl + "/schemes/getSlabView",
                       method: .post,
                       parameters: jsonDetails,
                       encoding: JSONEncoding.default,
                       headers: headers)
                .responseString { response in
                    switch response.result {
                    case .success(let value):
                        print("Response: \(value)")
                        resolve(value)
                    case .failure(let error):
                        print("Error: \(error)")
                        reject("REQUEST_FAILED", error.localizedDescription, nil)
                    }
                }
    }
    @objc(getCrossSchemesDetails:withResolver:withRejecter:)
    func getCrossSchemesDetails(details: NSDictionary, resolve: @escaping (Any?) -> Void, reject: @escaping (String, String, Error?) -> Void){
        guard let config = config else {
                reject("SDK_NOT_INITIALIZED", "SDK not initialized", nil)
                return
            }
        let GetComboSlabSchemesRequest = GetComboSlabSchemesRequest(details: details)
        guard let jsonData = try? JSONEncoder().encode(GetComboSlabSchemesRequest) else {
                reject("CONVERSION_FAILED", "Failed to convert getCategoriesListRequest to JSON", nil)
                return
            }
        print(config.accesstoken)
        print("Bearer \(config.accesstoken)")
        let headers: HTTPHeaders = [
            "Content-Type": "application/json",
            "Authorization": "Bearer \(config.accesstoken)"
        ]

        AF.request(config.baseurl + "/schemes/getSchemes/comboDetails",
                   method: .post,
                   parameters: try? JSONSerialization.jsonObject(with: jsonData, options: []) as? Parameters,
                   encoding: JSONEncoding.default,
                   headers: headers)
            .responseString { response in
                switch response.result {
                case .success(let value):
                    print("Response: \(value)")
                    resolve(value)
                case .failure(let error):
                    print("Error: \(error)")
                    reject("REQUEST_FAILED", error.localizedDescription, nil)
                }
            }
    }
    @objc(getSlabBasedSchemes:withResolver:withRejecter:)
    func getSlabBasedSchemes(details: NSDictionary, resolve: @escaping (Any?) -> Void, reject: @escaping (String, String, Error?) -> Void){
        guard let config = config else {
                reject("SDK_NOT_INITIALIZED", "SDK not initialized", nil)
                return
            }
        let GetComboSlabSchemesRequest = GetComboSlabSchemesRequest(details: details)
        guard let jsonData = try? JSONEncoder().encode(GetComboSlabSchemesRequest) else {
                reject("CONVERSION_FAILED", "Failed to convert getCategoriesListRequest to JSON", nil)
                return
            }
        print(config.accesstoken)
        print("Bearer \(config.accesstoken)")
        let headers: HTTPHeaders = [
            "Content-Type": "application/json",
            "Authorization": "Bearer \(config.accesstoken)"
        ]

        AF.request(config.baseurl + "/schemes/getSchemes/slabDetails",
                   method: .post,
                   parameters: try? JSONSerialization.jsonObject(with: jsonData, options: []) as? Parameters,
                   encoding: JSONEncoding.default,
                   headers: headers)
            .responseString { response in
                switch response.result {
                case .success(let value):
                    print("Response: \(value)")
                    resolve(value)
                case .failure(let error):
                    print("Error: \(error)")
                    reject("REQUEST_FAILED", error.localizedDescription, nil)
                }
            }
    }
    @objc(validateRetailerCoupon:withResolver:withRejecter:)
    func validateRetailerCoupon(details: NSDictionary, resolve: @escaping (Any?) -> Void, reject: @escaping (String, String, Error?) -> Void){
        guard let config = config else {
                reject("SDK_NOT_INITIALIZED", "SDK not initialized", nil)
                return
            }
        let ValidateRetailerCouponRequest = ValidateRetailerCouponRequest(details: details)
        guard let jsonData = try? JSONEncoder().encode(ValidateRetailerCouponRequest) else {
                reject("CONVERSION_FAILED", "Failed to convert getCategoriesListRequest to JSON", nil)
                return
            }
        print(config.accesstoken)
        print("Bearer \(config.accesstoken)")
        let headers: HTTPHeaders = [
            "Content-Type": "application/json",
            "Authorization": "Bearer \(config.accesstoken)"
        ]

        AF.request(config.baseurl + "/coupon/validateRetailerCoupon",
                   method: .post,
                   parameters: try? JSONSerialization.jsonObject(with: jsonData, options: []) as? Parameters,
                   encoding: JSONEncoding.default,
                   headers: headers)
            .responseString { response in
                switch response.result {
                case .success(let value):
                    print("Response: \(value)")
                    resolve(value)
                case .failure(let error):
                    print("Error: \(error)")
                    reject("REQUEST_FAILED", error.localizedDescription, nil)
                }
            }
    }
    @objc(registerCustomer:withResolver:withRejecter:)
    func registerCustomer(details: NSDictionary, resolve: @escaping (Any?) -> Void, reject: @escaping (String, String, Error?) -> Void){
        guard let config = config else {
                reject("SDK_NOT_INITIALIZED", "SDK not initialized", nil)
                return
            }
        let RegisterWarrantyRequest = RegisterWarrantyRequest(details: details)
        guard let jsonData = try? JSONEncoder().encode(RegisterWarrantyRequest) else {
                reject("CONVERSION_FAILED", "Failed to convert getCategoriesListRequest to JSON", nil)
                return
            }
        print(config.accesstoken)
        print("Bearer \(config.accesstoken)")
        let headers: HTTPHeaders = [
            "Content-Type": "application/json",
            "Authorization": "Bearer \(config.accesstoken)"
        ]

        AF.request(config.baseurl + "/product/registerCustomer",
                   method: .post,
                   parameters: try? JSONSerialization.jsonObject(with: jsonData, options: []) as? Parameters,
                   encoding: JSONEncoding.default,
                   headers: headers)
            .responseString { response in
                switch response.result {
                case .success(let value):
                    print("Response: \(value)")
                    resolve(value)
                case .failure(let error):
                    print("Error: \(error)")
                    reject("REQUEST_FAILED", error.localizedDescription, nil)
                }
            }
    }
    
    @objc(processForPin:withResolver:withRejecter:)
    func processForPin(details: NSDictionary, resolve: @escaping (Any?) -> Void, reject: @escaping (String, String, Error?) -> Void){
        guard let config = config else {
                reject("SDK_NOT_INITIALIZED", "SDK not initialized", nil)
                return
            }
        let ProcessForPinRequest = ProcessForPinRequest(details: details)
        guard let jsonData = try? JSONEncoder().encode(ProcessForPinRequest) else {
                reject("CONVERSION_FAILED", "Failed to convert getCategoriesListRequest to JSON", nil)
                return
            }
        print(config.accesstoken)
        print("Bearer \(config.accesstoken)")
        let headers: HTTPHeaders = [
            "Content-Type": "application/json",
            "Authorization": "Bearer \(config.accesstoken)"
        ]

        AF.request(config.baseurl + "/coupon/processForPin",
                   method: .post,
                   parameters: try? JSONSerialization.jsonObject(with: jsonData, options: []) as? Parameters,
                   encoding: JSONEncoding.default,
                   headers: headers)
            .responseString { response in
                switch response.result {
                case .success(let value):
                    print("Response: \(value)")
                    resolve(value)
                case .failure(let error):
                    print("Error: \(error)")
                    reject("REQUEST_FAILED", error.localizedDescription, nil)
                }
            }
    }
    @objc(processCoupon:withResolver:withRejecter:)
    func processCoupon(details: NSDictionary, resolve: @escaping (Any?) -> Void, reject: @escaping (String, String, Error?) -> Void){
        guard let config = config else {
                reject("SDK_NOT_INITIALIZED", "SDK not initialized", nil)
                return
            }
        let ProcessForPinRequest = ProcessForPinRequest(details: details)
        guard let jsonData = try? JSONEncoder().encode(ProcessForPinRequest) else {
                reject("CONVERSION_FAILED", "Failed to convert getCategoriesListRequest to JSON", nil)
                return
            }
        print(config.accesstoken)
        print("Bearer \(config.accesstoken)")
        let headers: HTTPHeaders = [
            "Content-Type": "application/json",
            "Authorization": "Bearer \(config.accesstoken)"
        ]

        AF.request(config.baseurl + "/coupon/processForPin",
                   method: .post,
                   parameters: try? JSONSerialization.jsonObject(with: jsonData, options: []) as? Parameters,
                   encoding: JSONEncoding.default,
                   headers: headers)
            .responseString { response in
                switch response.result {
                case .success(let value):
                    print("Response: \(value)")
                    resolve(value)
                case .failure(let error):
                    print("Error: \(error)")
                    reject("REQUEST_FAILED", error.localizedDescription, nil)
                }
            }
    }
    
    
    
    @objc(InitializeSDK:withResolver:withRejecter:)
    func InitializeSDK(details: NSDictionary, resolve: @escaping (Any?) -> Void, reject: @escaping (String, String, Error?) -> Void) {
            guard let baseurl = details["baseurl"] as? String,
                  let accesstoken = details["accesstoken"] as? String,
                  let refreshtoken = details["refreshtoken"] as? String else {
                reject("INVALID_DETAILS", "Invalid details provided", nil)
                return
            }

            let newConfig = APIConfig(baseurl: baseurl, accesstoken: accesstoken, refreshtoken: refreshtoken)
            config = newConfig

            // You can access the stored values within this function as well
            if let config = config {
                print("Initialized SDK with Base URL: \(config.baseurl)")
                print("Initialized SDK with Access Token: \(config.accesstoken)")
                print("Initialized SDK with Refresh Token: \(config.refreshtoken)")
            }

            resolve("SDK Initialized successfully")
        }
    


    


}
