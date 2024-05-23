import Alamofire
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
    func isTokenExpired(token: String) -> Bool {
        let parts = token.components(separatedBy: ".")
        
        func decode(jwtToken jwt: String) -> [String: Any] {
          let segments = jwt.components(separatedBy: ".")
          return decodeJWTPart(segments[1]) ?? [:]
        }

        func decodeJWTPart(_ value: String) -> [String: Any]? {
          guard let bodyData = base64UrlDecode(value),
            let json = try? JSONSerialization.jsonObject(with: bodyData, options: []), let payload = json as? [String: Any] else {
              return nil
          }
          return payload
        }
        
        func base64UrlDecode(_ value: String) -> Data? {
          var base64 = value
            .replacingOccurrences(of: "-", with: "+")
            .replacingOccurrences(of: "_", with: "/")

          let length = Double(base64.lengthOfBytes(using: String.Encoding.utf8))
          let requiredLength = 4 * ceil(length / 4.0)
          let paddingLength = requiredLength - length
          if paddingLength > 0 {
            let padding = "".padding(toLength: Int(paddingLength), withPad: "=", startingAt: 0)
            base64 = base64 + padding
          }
          return Data(base64Encoded: base64, options: .ignoreUnknownCharacters)
        }

        guard parts.count > 1,
              let payloadJson = decode(jwtToken: token) as? [String: Any],
              let expirationTime = payloadJson["exp"] as? TimeInterval else {
                  return true // Token decoding failed or expiration time not found
        }
        let currentTime = Date().timeIntervalSince1970
    

        return expirationTime < currentTime
    }
    
    func refreshAccessToken() {
        guard var config = config else {
            print("SDK not initialized")
            return
        }
        
        let baseURL = config.baseurl
        let refreshToken = config.refreshtoken
        
        let semaphore = DispatchSemaphore(value: 0)
        
        let parameters: [String: Any] = [
            "refreshToken": refreshToken
        ]
        
        var requestError: Error?
        
        AF.request(baseURL + "/user/refreshAccessToken", method: .post, parameters: parameters, encoding: JSONEncoding.default)
            .responseJSON { [self] response in
                switch response.result {
                case .success(let value):
                    if let jsonResponse = value as? [String: Any],
                       let accessToken = jsonResponse["accessToken"] as? String,
                       let refreshToken = jsonResponse["refreshToken"] as? String {
                        let newConfig = APIConfig(baseurl: config.baseurl, accesstoken: accessToken, refreshtoken: refreshToken)
                        self.config = newConfig // Use self to refer to the outer scope's config
                    }
                    
                    semaphore.signal() // Signal that the request is completed
                case .failure(let error):
                    
                    requestError = error
                    semaphore.signal() // Signal that the request is completed
                }
            }
        
        semaphore.wait() // Wait until the request is completed
        
        if let error = requestError {
            print("Error refreshing access token: \(error)")
        }
    }


    
    
    
  @objc(multiply:withB:withResolver:withRejecter:)
  func multiply(a: Float, b: Float, resolve:RCTPromiseResolveBlock,reject:RCTPromiseRejectBlock) -> Void {
      //print(APIConfiguration.)
      guard let config = config else {
              reject("SDK_NOT_INITIALIZED", "SDK not initialized", nil)
              return
          }

          
    resolve(a*b)
  }

    @objc(verifyBankDetails:withResolver:withRejecter:)
    func verifyBankDetails(details: NSDictionary, resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock) {
        guard var config = config else {
                reject("SDK_NOT_INITIALIZED", "SDK not initialized", nil)
                return
            }
        let token = config.accesstoken
        if(isTokenExpired(token: token)){
            refreshAccessToken()
            config = self.config!
            
        }

        let bankDetails = VerifyBankDetailsRequest(details: details)
        guard let jsonData = try? JSONEncoder().encode(bankDetails) else {
                reject("CONVERSION_FAILED", "Failed to convert BankDetails to JSON", nil)
                return
            }
       
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
                    print("headers: \(headers)")
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
        guard var config = config else {
                reject("SDK_NOT_INITIALIZED", "SDK not initialized", nil)
                return
            }
        let token = config.accesstoken
        if(isTokenExpired(token: token)){
            refreshAccessToken()
            config = self.config!
        }
        let getCategoriesListRequest = GetCategoriesListRequest(details: details)

        guard let jsonData = try? JSONEncoder().encode(getCategoriesListRequest) else {
                reject("CONVERSION_FAILED", "Failed to convert getCategoriesListRequest to JSON", nil)
                return
            }
        
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
        guard var config = config else {
                reject("SDK_NOT_INITIALIZED", "SDK not initialized", nil)
                return
            }
        let token = config.accesstoken
        if(isTokenExpired(token: token)){
            
            refreshAccessToken()
            config = self.config!
            
        }
        let GetUserBasePointsRequest = GetUserBasePointsRequest(details: details)
        guard let jsonData = try? JSONEncoder().encode(GetUserBasePointsRequest) else {
                reject("CONVERSION_FAILED", "Failed to convert getCategoriesListRequest to JSON", nil)
                return
            }
        
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
        guard var config = config else {
                reject("SDK_NOT_INITIALIZED", "SDK not initialized", nil)
                return
            }
        let token = config.accesstoken
        if(isTokenExpired(token: token)){
            
            refreshAccessToken()
            config = self.config!
            
        }
        let GetUserScanHistoryRequest = GetUserScanHistoryRequest(details: details)
        guard let jsonData = try? JSONEncoder().encode(GetUserScanHistoryRequest) else {
                reject("CONVERSION_FAILED", "Failed to convert getCategoriesListRequest to JSON", nil)
                return
            }
        
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
        guard var config = config else {
                reject("SDK_NOT_INITIALIZED", "SDK not initialized", nil)
                return
            }
        let token = config.accesstoken
        if(isTokenExpired(token: token)){
            
            refreshAccessToken()
            config = self.config!
            
        }
        let RewardHistoryRequest = RewardHistoryRequest(details: details)
        guard let jsonData = try? JSONEncoder().encode(RewardHistoryRequest) else {
                reject("CONVERSION_FAILED", "Failed to convert getCategoriesListRequest to JSON", nil)
                return
            }
        
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
        guard var config = config else {
                reject("SDK_NOT_INITIALIZED", "SDK not initialized", nil)
                return
            }
        let token = config.accesstoken
        if(isTokenExpired(token: token)){
            
            refreshAccessToken()
            config = self.config!
            
        }
        let ScannedBalancePointsRequest = ScannedBalancePointsRequest(details: details)
        guard let jsonData = try? JSONEncoder().encode(ScannedBalancePointsRequest) else {
                reject("CONVERSION_FAILED", "Failed to convert getCategoriesListRequest to JSON", nil)
                return
            }
        
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
        guard var config = config else {
                reject("SDK_NOT_INITIALIZED", "SDK not initialized", nil)
                return
            }
        let token = config.accesstoken
        if(isTokenExpired(token: token)){
            
            refreshAccessToken()
            config = self.config!
            
        }
        let userScanOutPointSummaryRequest = ScannedBalancePointsRequest(details: details)
        guard let jsonData = try? JSONEncoder().encode(userScanOutPointSummaryRequest) else {
                reject("CONVERSION_FAILED", "Failed to convert getCategoriesListRequest to JSON", nil)
                return
            }
        
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
        guard var config = config else {
                reject("SDK_NOT_INITIALIZED", "SDK not initialized", nil)
                return
            }
        let token = config.accesstoken
        if(isTokenExpired(token: token)){
            
            refreshAccessToken()
            config = self.config!
            
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
        guard var config = config else {
                reject("SDK_NOT_INITIALIZED", "SDK not initialized", nil)
                return
            }
        let token = config.accesstoken
        if(isTokenExpired(token: token)){
            
            refreshAccessToken()
            config = self.config!
            
        }
        let RegisterWarrantyRequest = RegisterWarrantyRequest(details: details)
        guard let jsonData = try? JSONEncoder().encode(RegisterWarrantyRequest) else {
                reject("CONVERSION_FAILED", "Failed to convert getCategoriesListRequest to JSON", nil)
                return
            }
        
        
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
        guard var config = config else {
                reject("SDK_NOT_INITIALIZED", "SDK not initialized", nil)
                return
            }
        let token = config.accesstoken
        if(isTokenExpired(token: token)){
            
            refreshAccessToken()
            config = self.config!
            
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
        guard var config = config else {
                reject("SDK_NOT_INITIALIZED", "SDK not initialized", nil)
                return
            }
        let token = config.accesstoken
        if(isTokenExpired(token: token)){
            
            refreshAccessToken()
            config = self.config!
            
        }
        let GetComboSlabSchemesRequest = GetComboSlabSchemesRequest(details: details)
        guard let jsonData = try? JSONEncoder().encode(GetComboSlabSchemesRequest) else {
                reject("CONVERSION_FAILED", "Failed to convert getCategoriesListRequest to JSON", nil)
                return
            }
        
        
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
        guard var config = config else {
                reject("SDK_NOT_INITIALIZED", "SDK not initialized", nil)
                return
            }
        let token = config.accesstoken
        if(isTokenExpired(token: token)){
            
            refreshAccessToken()
            config = self.config!
            
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
        guard var config = config else {
                reject("SDK_NOT_INITIALIZED", "SDK not initialized", nil)
                return
            }
        let token = config.accesstoken
        if(isTokenExpired(token: token)){
            
            refreshAccessToken()
            config = self.config!
            
        }
        let GetComboSlabSchemesRequest = GetComboSlabSchemesRequest(details: details)
        guard let jsonData = try? JSONEncoder().encode(GetComboSlabSchemesRequest) else {
                reject("CONVERSION_FAILED", "Failed to convert getCategoriesListRequest to JSON", nil)
                return
            }
        
        
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
        guard var config = config else {
                reject("SDK_NOT_INITIALIZED", "SDK not initialized", nil)
                return
            }
        let token = config.accesstoken
        if(isTokenExpired(token: token)){
            
            refreshAccessToken()
            config = self.config!
            
        }
        let GetComboSlabSchemesRequest = GetComboSlabSchemesRequest(details: details)
        guard let jsonData = try? JSONEncoder().encode(GetComboSlabSchemesRequest) else {
                reject("CONVERSION_FAILED", "Failed to convert getCategoriesListRequest to JSON", nil)
                return
            }
        
        
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
        guard var config = config else {
                reject("SDK_NOT_INITIALIZED", "SDK not initialized", nil)
                return
            }
        let token = config.accesstoken
        if(isTokenExpired(token: token)){
            
            refreshAccessToken()
            config = self.config!
            
        }
        let ValidateRetailerCouponRequest = ValidateRetailerCouponRequest(details: details)
        guard let jsonData = try? JSONEncoder().encode(ValidateRetailerCouponRequest) else {
                reject("CONVERSION_FAILED", "Failed to convert getCategoriesListRequest to JSON", nil)
                return
            }
        
        
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
        guard var config = config else {
                reject("SDK_NOT_INITIALIZED", "SDK not initialized", nil)
                return
            }
        let token = config.accesstoken
        if(isTokenExpired(token: token)){
            
            refreshAccessToken()
            config = self.config!
            
        }
        let RegisterWarrantyRequest = RegisterWarrantyRequest(details: details)
        guard let jsonData = try? JSONEncoder().encode(RegisterWarrantyRequest) else {
                reject("CONVERSION_FAILED", "Failed to convert getCategoriesListRequest to JSON", nil)
                return
            }
        
        
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
        guard var config = config else {
                reject("SDK_NOT_INITIALIZED", "SDK not initialized", nil)
                return
            }
        let token = config.accesstoken
        if(isTokenExpired(token: token)){
            
            refreshAccessToken()
            config = self.config!
            
        }
        let ProcessForPinRequest = ProcessForPinRequest(details: details)
        guard let jsonData = try? JSONEncoder().encode(ProcessForPinRequest) else {
                reject("CONVERSION_FAILED", "Failed to convert getCategoriesListRequest to JSON", nil)
                return
            }
        
        
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
        guard var config = config else {
                reject("SDK_NOT_INITIALIZED", "SDK not initialized", nil)
                return
            }
        let token = config.accesstoken
        if(isTokenExpired(token: token)){
            
            refreshAccessToken()
            config = self.config!
            
        }
        let ProcessForPinRequest = ProcessForPinRequest(details: details)
        guard let jsonData = try? JSONEncoder().encode(ProcessForPinRequest) else {
                reject("CONVERSION_FAILED", "Failed to convert getCategoriesListRequest to JSON", nil)
                return
            }
        
        
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
    @objc(getProductCrossSellScheme:withResolver:withRejecter:)
    func getProductCrossSellScheme(details: NSDictionary, resolve: @escaping (Any?) -> Void, reject: @escaping (String, String, Error?) -> Void){
        guard var config = config else {
                reject("SDK_NOT_INITIALIZED", "SDK not initialized", nil)
                return
            }
        let token = config.accesstoken
        if(isTokenExpired(token: token)){
            
            refreshAccessToken()
            config = self.config!
            
        }
        let GetProductDetailsRequest = GetProductDetailsRequest(details: details)
        guard let jsonData = try? JSONEncoder().encode(GetProductDetailsRequest) else {
                reject("CONVERSION_FAILED", "Failed to convert getCategoriesListRequest to JSON", nil)
                return
            }
        
        
        let headers: HTTPHeaders = [
            "Content-Type": "application/json",
            "Authorization": "Bearer \(config.accesstoken)"
        ]

        AF.request(config.baseurl + "/product/getCategoryProductDetails/comboBased",
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
    @objc(getProductSlabBasedScheme:withResolver:withRejecter:)
    func getProductSlabBasedScheme(details: NSDictionary, resolve: @escaping (Any?) -> Void, reject: @escaping (String, String, Error?) -> Void){
        guard var config = config else {
                reject("SDK_NOT_INITIALIZED", "SDK not initialized", nil)
                return
            }
        let token = config.accesstoken
        if(isTokenExpired(token: token)){
            
            refreshAccessToken()
            config = self.config!
            
        }
        let GetProductDetailsRequest = GetProductDetailsRequest(details: details)
        guard let jsonData = try? JSONEncoder().encode(GetProductDetailsRequest) else {
                reject("CONVERSION_FAILED", "Failed to convert getCategoriesListRequest to JSON", nil)
                return
            }
        
        
        let headers: HTTPHeaders = [
            "Content-Type": "application/json",
            "Authorization": "Bearer \(config.accesstoken)"
        ]

        AF.request(config.baseurl + "/product/getCategoryProductDetails/slabBased",
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
    @objc(bankTransfer:withResolver:withRejecter:)
    func bankTransfer(details: NSDictionary, resolve: @escaping (Any?) -> Void, reject: @escaping (String, String, Error?) -> Void){
        guard var config = config else {
                reject("SDK_NOT_INITIALIZED", "SDK not initialized", nil)
                return
            }
        let token = config.accesstoken
        if(isTokenExpired(token: token)){
            
            refreshAccessToken()
            config = self.config!
            
        }
        let paymentDetails = PaymentDetails(details: details)
        guard let jsonData = try? JSONEncoder().encode(paymentDetails) else {
                reject("CONVERSION_FAILED", "Failed to convert getCategoriesListRequest to JSON", nil)
                return
            }
        
        
        let headers: HTTPHeaders = [
            "Content-Type": "application/json",
            "Authorization": "Bearer \(config.accesstoken)"
        ]

        AF.request(config.baseurl + "/order/bankTransfer",
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
    
    @objc(scanIn:withResolver:withRejecter:)
    func scanIn(details: NSDictionary, resolve: @escaping (Any?) -> Void, reject: @escaping (String, String, Error?) -> Void){
        guard var config = config else {
                reject("SDK_NOT_INITIALIZED", "SDK not initialized", nil)
                return
            }
        let token = config.accesstoken
        if(isTokenExpired(token: token)){
            
            refreshAccessToken()
            config = self.config!
            
        }
        let ScanInRequest = ScanInRequest(details: details)
        guard let jsonData = try? JSONEncoder().encode(ScanInRequest) else {
                reject("CONVERSION_FAILED", "Failed to convert getCategoriesListRequest to JSON", nil)
                return
            }
        
        
        let headers: HTTPHeaders = [
            "Content-Type": "application/json",
            "Authorization": "Bearer \(config.accesstoken)"
        ]

        AF.request(config.baseurl + "/coupon/scanIn",
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
    
    @objc(getFile:withResolver:withRejecter:)
    func getFile(details: NSDictionary, resolve: @escaping (Any?) -> Void, reject: @escaping (String, String, Error?) -> Void){
        guard var config = config else {
                reject("SDK_NOT_INITIALIZED", "SDK not initialized", nil)
                return
            }
        let token = config.accesstoken
        if(isTokenExpired(token: token)){
            
            refreshAccessToken()
            config = self.config!
            
        }
        guard let uuid = details["uuid"] as? String else {
                reject("INVALID_PARAMETERS", "Product ID is missing", nil)
                return
            }
        guard let imageRelated = details["imageRelated"] as? String else {
                reject("INVALID_PARAMETERS", "Product ID is missing", nil)
                return
            }
        guard let userRole = details["userRole"] as? String else {
                reject("INVALID_PARAMETERS", "Product ID is missing", nil)
                return
            }
        let url = "\(config.baseurl)/file/\(uuid)/\(imageRelated)/\(userRole)"
        print(url)
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
    @objc(getSchemeFileList:withResolver:withRejecter:)
    func getSchemeFileList(details: NSDictionary, resolve: @escaping (Any?) -> Void, reject: @escaping (String, String, Error?) -> Void){
        guard var config = config else {
                reject("SDK_NOT_INITIALIZED", "SDK not initialized", nil)
                return
            }
        let token = config.accesstoken
        if(isTokenExpired(token: token)){
            
            refreshAccessToken()
            config = self.config!
            
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

            AF.request(config.baseurl + "/schemes/file/getSpecialSchemes",
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
    @objc(getTdsCertificate:withResolver:withRejecter:)
    func getTdsCertificate(details: NSDictionary, resolve: @escaping (Any?) -> Void, reject: @escaping (String, String, Error?) -> Void){
        guard var config = config else {
                reject("SDK_NOT_INITIALIZED", "SDK not initialized", nil)
                return
            }
        let token = config.accesstoken
        if(isTokenExpired(token: token)){
            
            refreshAccessToken()
            config = self.config!
            
        }
        let TdsCertificate = TdsCertificate(details: details)
        guard let jsonData = try? JSONEncoder().encode(TdsCertificate) else {
                reject("CONVERSION_FAILED", "Failed to convert getCategoriesListRequest to JSON", nil)
                return
            }
        
        
        let headers: HTTPHeaders = [
            "Content-Type": "application/json",
            "Authorization": "Bearer \(config.accesstoken)"
        ]

        AF.request(config.baseurl + "/user/tdsCertificate",
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
    
    @objc(GetPrimarySchemeFileList:withRejecter:)
    func GetPrimarySchemeFileList(_ resolve: @escaping (Any?) -> Void, withRejecter reject: @escaping (String, String, Error?) -> Void) {
        guard var config = config else {
                reject("SDK_NOT_INITIALIZED", "SDK not initialized", nil)
                return
            }
        let token = config.accesstoken
        if(isTokenExpired(token: token)){
            
            refreshAccessToken()
            config = self.config!
            
        }
        let url = "\(config.baseurl)/schemes/getActiveSchemeOffers"
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
   
    @objc(getCurrentSlabOnCrossSell:withResolver:withRejecter:)
    func getCurrentSlabOnCrossSell(details: NSDictionary, resolve: @escaping (Any?) -> Void, reject: @escaping (String, String, Error?) -> Void){
        guard var config = config else {
                reject("SDK_NOT_INITIALIZED", "SDK not initialized", nil)
                return
            }
        let token = config.accesstoken
        if(isTokenExpired(token: token)){
            
            refreshAccessToken()
            config = self.config!
            
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

            AF.request(config.baseurl + "/schemes/getSlabView/slabDetails",
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
    @objc(getCurrentSlabOnSlabBased:withResolver:withRejecter:)
    func getCurrentSlabOnSlabBased(details: NSDictionary, resolve: @escaping (Any?) -> Void, reject: @escaping (String, String, Error?) -> Void){
        guard var config = config else {
                reject("SDK_NOT_INITIALIZED", "SDK not initialized", nil)
                return
            }
        let token = config.accesstoken
        if(isTokenExpired(token: token)){
            
            refreshAccessToken()
            config = self.config!
            
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

            AF.request(config.baseurl + "/schemes/getUserCurrentSlab/slabBased",
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
    @objc(getSchemeSlabBasedSlab:withResolver:withRejecter:)
    func getSchemeSlabBasedSlab(details: NSDictionary, resolve: @escaping (Any?) -> Void, reject: @escaping (String, String, Error?) -> Void){
        guard var config = config else {
                reject("SDK_NOT_INITIALIZED", "SDK not initialized", nil)
                return
            }
        let token = config.accesstoken
        if(isTokenExpired(token: token)){
            refreshAccessToken()
            config = self.config!
            
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

            AF.request(config.baseurl + "/schemes/getSlabView/slabDetails",
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
    @objc(getSchemeCrossBasedSlab:withResolver:withRejecter:)
    func getSchemeCrossBasedSlab(details: NSDictionary, resolve: @escaping (Any?) -> Void, reject: @escaping (String, String, Error?) -> Void){
        guard var config = config else {
                reject("SDK_NOT_INITIALIZED", "SDK not initialized", nil)
                return
            }
        let token = config.accesstoken
        if(isTokenExpired(token: token)){
            refreshAccessToken()
            config = self.config!
            
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

            AF.request(config.baseurl + "/schemes/getSlabView/comboDetails",
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

    @objc(uploadFile:withResolver:withRejecter:)
    func uploadFile(details: NSDictionary, resolve: @escaping (Any?) -> Void, reject: @escaping (String, String, Error?) -> Void) {
        guard var config = config else {
                reject("SDK_NOT_INITIALIZED", "SDK not initialized", nil)
                return
            }
        let token = config.accesstoken
        if(isTokenExpired(token: token)){
            
            refreshAccessToken()
            config = self.config!
            
        }
        guard let imageRelated = details["imageRelated"] as? String,
              let userRole = details["userRole"] as? String,
              let file = details["file"] as? NSDictionary,
              let fileName = file["fileName"] as? String,
              let fileType = file["fileType"] as? String,
              let fileUriString = file["fileUri"] as? String,
              let fileUri = URL(string: fileUriString) else {
            reject("InvalidParameters", "Invalid parameters provided", nil)
            return
        }
        
        do {
            let inputStream = try Data(contentsOf: fileUri)
            
            let boundary = "Boundary-\(UUID().uuidString)"
            let url = config.baseurl  + "/file"
            let headers: HTTPHeaders = [
                "Content-Type": "multipart/form-data; boundary=\(boundary)"
            ]
            
            AF.upload(multipartFormData: { multipartFormData in
                multipartFormData.append(inputStream, withName: "file", fileName: fileName, mimeType: fileType)
                multipartFormData.append(Data(imageRelated.utf8), withName: "imageRelated")
                multipartFormData.append(Data(userRole.utf8), withName: "userRole")
            }, to: url, headers: headers)
            .response { response in
                switch response.result {
                case .success(let responseData):
                    if let data = responseData, let responseBody = String(data: data, encoding: .utf8) {
                        resolve(responseBody)
                    } else {
                        reject("InvalidResponse", "Invalid response from server", nil)
                    }
                case .failure(let error):
                    reject("ServerError", "Error in server response", error)
                }
            }
        } catch {
            reject("FileError", "Error reading file data", error)
        }
    }

    func getBaseURL() -> String {
        // Implement base URL retrieval logic here
        return "" // Return your base URL
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
