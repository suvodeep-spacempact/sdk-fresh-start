import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-vg-retailer-sdk' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const VgRetailerSdk = NativeModules.VgRetailerSdk
  ? NativeModules.VgRetailerSdk
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

export function multiply(a: number, b: number): Promise<number> {
  console.log('inside multiplyyyyy function');
  return VgRetailerSdk.multiply(a, b);
}
export function verifyBankDetails(requestData: any): Promise<any> {
  console.log('inside sampletryyyyy4 function', requestData);

  return VgRetailerSdk.verifyBankDetails(requestData);
}
export function checkIfUserExists(mobileNumber: string): Promise<number> {
  console.log('inside checkIfUserExists function', mobileNumber);

  return VgRetailerSdk.checkIfUserExists(mobileNumber);
}
export function rewardPointsHistory(requestData: any): Promise<number> {
  console.log('inside rewardpoints history function', requestData);

  return VgRetailerSdk.rewardPointsHistory(requestData);
}
export function ScannedBalancePoints(requestData: any): Promise<number> {
  console.log('inside scanned Balance points history function', requestData);

  return VgRetailerSdk.ScannedBalancePoints(requestData);
}

export function userScanOutPointSummary(requestData: any): Promise<number> {
  console.log('inside userScanOutPointSummary function', requestData);

  return VgRetailerSdk.userScanOutPointSummary(requestData);
}

export function InitializeSDK(requestData: any): Promise<number> {
  console.log('inside initialize sdk function', requestData);

  return VgRetailerSdk.InitializeSDK(requestData);
}

export function getCategoriesList(requestData: any): Promise<number> {
  console.log('inside get categories list function', requestData);
  return VgRetailerSdk.getCategoriesList(requestData);
}
export function getUserBasePoints(requestData: any): Promise<number> {
  console.log('inside getUserBasePoints function', requestData);
  return VgRetailerSdk.getUserBasePoints(requestData);
}

export function getUserScanHistory(requestData: any): Promise<number> {
  console.log('inside getUserBasePoints function', requestData);
  return VgRetailerSdk.getUserScanHistory(requestData);
}
export function captureCustomerDetails(mobileNo: string): Promise<number> {
  console.log('inside captureCustomerDetails function', mobileNo);
  return VgRetailerSdk.captureCustomerDetails(mobileNo);
}
export function registerWarranty(requestData: any): Promise<number> {
  console.log('inside requestData function', requestData);
  return VgRetailerSdk.registerWarranty(requestData);
}
export function getEligibleProducts(
  categoryId: String,
  schemeId: String
): Promise<number> {
  console.log('inside requestData function', categoryId, schemeId);
  return VgRetailerSdk.getEligibleProducts(categoryId, schemeId);
}
// export function getComboSlabSchemes(requestData: any): Promise<number> {
//   console.log('inside requestData function', requestData);
//   return VgRetailerSdk.getComboSlabSchemes(requestData);
// }
export function getSlabView(schemeId: String): Promise<number> {
  console.log('inside requestData function', schemeId);
  return VgRetailerSdk.getSlabView(schemeId);
}

export function RewardsPoints(): Promise<number> {
  console.log('inside Reward points function');
  return VgRetailerSdk.userScanOutPointSummary({});
}
