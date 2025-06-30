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

// Helper function for consistent logging
const logFunctionCall = (functionName: string, params?: any) => {
  console.log(
    `[VGRetailerSDK] ${functionName} called`,
    params ? { params } : ''
  );
};

export function multiply(a: number, b: number): Promise<any> {
  logFunctionCall('multiply', { a, b });
  return VgRetailerSdk.multiply(a, b);
}

export function verifyBankDetails(requestData: any): Promise<any> {
  logFunctionCall('verifyBankDetails', requestData);
  return VgRetailerSdk.verifyBankDetails(requestData);
}

export function checkIfUserExists(mobileNumber: string): Promise<any> {
  logFunctionCall('checkIfUserExists', { mobileNumber });
  return VgRetailerSdk.checkIfUserExists(mobileNumber);
}

export function rewardPointsHistory(requestData: any): Promise<any> {
  logFunctionCall('rewardPointsHistory', requestData);
  return VgRetailerSdk.rewardPointsHistory(requestData);
}

export function ScannedBalancePoints(requestData: any): Promise<any> {
  logFunctionCall('ScannedBalancePoints', requestData);
  return VgRetailerSdk.ScannedBalancePoints(requestData);
}

export function userScanOutPointSummary(requestData: any): Promise<any> {
  logFunctionCall('userScanOutPointSummary', requestData);
  return VgRetailerSdk.userScanOutPointSummary(requestData);
}

export function InitializeSDK(requestData: any): Promise<any> {
  logFunctionCall('InitializeSDK', requestData);
  return VgRetailerSdk.InitializeSDK(requestData);
}

export function getCategoriesList(requestData: any): Promise<any> {
  logFunctionCall('getCategoriesList', requestData);
  return VgRetailerSdk.getCategoriesList(requestData);
}

export function getUserBasePoints(requestData: any): Promise<any> {
  logFunctionCall('getUserBasePoints', requestData);
  return VgRetailerSdk.getUserBasePoints(requestData);
}

export function getUserScanHistory(requestData: any): Promise<any> {
  logFunctionCall('getUserScanHistory', requestData);
  return VgRetailerSdk.getUserScanHistory(requestData);
}

export function captureCustomerDetails(requestData: any): Promise<any> {
  logFunctionCall('captureCustomerDetails', requestData);
  return VgRetailerSdk.captureCustomerDetails(requestData);
}

export function registerWarranty(requestData: any): Promise<any> {
  logFunctionCall('registerWarranty', requestData);
  return VgRetailerSdk.registerWarranty(requestData);
}

export function getEligibleProducts(requestData: any): Promise<any> {
  logFunctionCall('getEligibleProducts', requestData);
  return VgRetailerSdk.getEligibleProducts(requestData);
}

export function getSchemeSlabBasedSlab(requestData: any): Promise<any> {
  logFunctionCall('getSchemeSlabBasedSlab', requestData);
  return VgRetailerSdk.getSchemeSlabBasedSlab(requestData);
}

export function getSchemeCrossBasedSlab(requestData: any): Promise<any> {
  logFunctionCall('getSchemeCrossBasedSlab', requestData);
  return VgRetailerSdk.getSchemeCrossBasedSlab(requestData);
}

export function RewardsPoints(): Promise<any> {
  logFunctionCall('RewardsPoints');
  return VgRetailerSdk.userScanOutPointSummary({});
}

export function getCrossSchemesDetails(requestData: any): Promise<any> {
  logFunctionCall('getCrossSchemesDetails', requestData);
  return VgRetailerSdk.getCrossSchemesDetails(requestData);
}

export function getSlabBasedSchemes(requestData: any): Promise<any> {
  logFunctionCall('getSlabBasedSchemes', requestData);
  return VgRetailerSdk.getSlabBasedSchemes(requestData);
}

export function validateRetailerCoupon(requestData: any): Promise<any> {
  logFunctionCall('validateRetailerCoupon', requestData);
  return VgRetailerSdk.validateRetailerCoupon(requestData);
}

export function registerCustomer(requestData: any): Promise<any> {
  logFunctionCall('registerCustomer', requestData);
  return VgRetailerSdk.registerCustomer(requestData);
}

export function processForPin(requestData: any): Promise<any> {
  logFunctionCall('processForPin', requestData);
  return VgRetailerSdk.processForPin(requestData);
}

export function processCoupon(requestData: any): Promise<any> {
  logFunctionCall('processCoupon', requestData);
  return VgRetailerSdk.processCoupon(requestData);
}

export function getProductCrossSellScheme(requestData: any): Promise<any> {
  logFunctionCall('getProductCrossSellScheme', requestData);
  return VgRetailerSdk.getProductCrossSellScheme(requestData);
}

export function getProductSlabBasedScheme(requestData: any): Promise<any> {
  logFunctionCall('getProductSlabBasedScheme', requestData);
  return VgRetailerSdk.getProductSlabBasedScheme(requestData);
}

export function bankTransfer(requestData: any): Promise<any> {
  logFunctionCall('bankTransfer', requestData);
  return VgRetailerSdk.bankTransfer(requestData);
}

export function scanIn(requestData: any): Promise<any> {
  logFunctionCall('scanIn', requestData);
  return VgRetailerSdk.scanIn(requestData);
}

export function getFile(requestData: any): Promise<any> {
  logFunctionCall('getFile', requestData);
  return VgRetailerSdk.getFile(requestData);
}

export function uploadFile(requestData: any): Promise<any> {
  logFunctionCall('uploadFile', requestData);
  return VgRetailerSdk.uploadFile(requestData);
}

export function getSchemeFileList(requestData: any): Promise<any> {
  logFunctionCall('getSchemeFileList', requestData);
  return VgRetailerSdk.getSchemeFileList(requestData);
}

export function getTdsCertificate(requestData: any): Promise<any> {
  logFunctionCall('getTdsCertificate', requestData);
  return VgRetailerSdk.getTdsCertificate(requestData);
}

export function GetPrimarySchemeFileList(): Promise<any> {
  logFunctionCall('GetPrimarySchemeFileList');
  return VgRetailerSdk.GetPrimarySchemeFileList();
}

export function getCurrentSlabOnSlabBased(requestData: any): Promise<any> {
  logFunctionCall('getCurrentSlabOnSlabBased', requestData);
  return VgRetailerSdk.getCurrentSlabOnSlabBased(requestData);
}

export function getCurrentSlabOnCrossSell(requestData: any): Promise<any> {
  logFunctionCall('getCurrentSlabOnCrossSell', requestData);
  return VgRetailerSdk.getCurrentSlabOnCrossSell(requestData);
}
