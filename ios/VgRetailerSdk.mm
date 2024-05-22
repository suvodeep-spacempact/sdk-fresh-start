#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(VgRetailerSdk, NSObject)

RCT_EXTERN_METHOD(multiply:(float)a withB:(float)b
                 withResolver:(RCTPromiseResolveBlock)resolve
                 withRejecter:(RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(verifyBankDetails:(NSDictionary *)details
                  withResolver:(RCTPromiseResolveBlock)resolve
                  withRejecter:(RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(InitializeSDK:(NSDictionary *)details
                  withResolver:(RCTPromiseResolveBlock)resolve
                  withRejecter:(RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(getCategoriesList:(NSDictionary *)details
                  withResolver:(RCTPromiseResolveBlock)resolve
                  withRejecter:(RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(getUserBasePoints:(NSDictionary *)details
                  withResolver:(RCTPromiseResolveBlock)resolve
                  withRejecter:(RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(getUserScanHistory:(NSDictionary *)details
                  withResolver:(RCTPromiseResolveBlock)resolve
                  withRejecter:(RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(rewardPointsHistory:(NSDictionary *)details
                  withResolver:(RCTPromiseResolveBlock)resolve
                  withRejecter:(RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(ScannedBalancePoints:(NSDictionary *)details
                  withResolver:(RCTPromiseResolveBlock)resolve
                  withRejecter:(RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(userScanOutPointSummary:(NSDictionary *)details
                  withResolver:(RCTPromiseResolveBlock)resolve
                  withRejecter:(RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(captureCustomerDetails:(NSDictionary *)details
                  withResolver:(RCTPromiseResolveBlock)resolve
                  withRejecter:(RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(registerWarranty:(NSDictionary *)details
                  withResolver:(RCTPromiseResolveBlock)resolve
                  withRejecter:(RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(getEligibleProducts:(NSDictionary *)details
                  withResolver:(RCTPromiseResolveBlock)resolve
                  withRejecter:(RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(getComboSlabSchemes:(NSDictionary *)details
                  withResolver:(RCTPromiseResolveBlock)resolve
                  withRejecter:(RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(getSlabView:(NSDictionary *)details
                  withResolver:(RCTPromiseResolveBlock)resolve
                  withRejecter:(RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(getCrossSchemesDetails:(NSDictionary *)details
                  withResolver:(RCTPromiseResolveBlock)resolve
                  withRejecter:(RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(getSlabBasedSchemes:(NSDictionary *)details
                  withResolver:(RCTPromiseResolveBlock)resolve
                  withRejecter:(RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(validateRetailerCoupon:(NSDictionary *)details
                  withResolver:(RCTPromiseResolveBlock)resolve
                  withRejecter:(RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(registerCustomer:(NSDictionary *)details
                  withResolver:(RCTPromiseResolveBlock)resolve
                  withRejecter:(RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(processForPin:(NSDictionary *)details
                  withResolver:(RCTPromiseResolveBlock)resolve
                  withRejecter:(RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(processCoupon:(NSDictionary *)details
                  withResolver:(RCTPromiseResolveBlock)resolve
                  withRejecter:(RCTPromiseRejectBlock)reject)

+ (BOOL)requiresMainQueueSetup
{
  return NO;
}

@end
