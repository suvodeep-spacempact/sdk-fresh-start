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

export function multiply(a: number, b: number): Promise<any> {
  console.log('inside multiplyyyyy function');
  return VgRetailerSdk.multiply(a, b);
}
export function verifyBankDetails(requestData: any): Promise<any> {
  console.log('inside sampletryyyyy4 function', requestData);

  return VgRetailerSdk.verifyBankDetails(requestData);
}
export function checkIfUserExists(mobileNumber: string): Promise<any> {
  console.log('inside checkIfUserExists function', mobileNumber);

  return VgRetailerSdk.checkIfUserExists(mobileNumber);
}
export function rewardPointsHistory(requestData: any): Promise<any> {
  console.log('inside rewardpoints history function', requestData);

  return VgRetailerSdk.rewardPointsHistory(requestData);
}
export function ScannedBalancePoints(requestData: any): Promise<any> {
  console.log('inside scanned Balance points history function', requestData);

  return VgRetailerSdk.ScannedBalancePoints(requestData);
}

export function userScanOutPointSummary(requestData: any): Promise<any> {
  console.log('inside userScanOutPointSummary function', requestData);

  return VgRetailerSdk.userScanOutPointSummary(requestData);
}

export function InitializeSDK(requestData: any): Promise<any> {
  console.log('inside initialize sdk function', requestData);

  return VgRetailerSdk.InitializeSDK(requestData);
}

export function getCategoriesList(requestData: any): Promise<any> {
  console.log('inside get categories list function', requestData);
  return VgRetailerSdk.getCategoriesList(requestData);
}
export function getUserBasePoints(requestData: any): Promise<any> {
  console.log('inside getUserBasePoints function', requestData);
  return VgRetailerSdk.getUserBasePoints(requestData);
}

export function getUserScanHistory(requestData: any): Promise<any> {
  console.log('inside getUserBasePoints function', requestData);
  return VgRetailerSdk.getUserScanHistory(requestData);
}
export function captureCustomerDetails(requestData: any): Promise<any> {
  console.log('inside captureCustomerDetails function', requestData);
  return VgRetailerSdk.captureCustomerDetails(requestData);
}
export function registerWarranty(requestData: any): Promise<any> {
  console.log('inside requestData function', requestData);
  return VgRetailerSdk.registerWarranty(requestData);
}
export function getEligibleProducts(requestData: any): Promise<any> {
  console.log('inside requestData function', requestData);
  return VgRetailerSdk.getEligibleProducts(requestData);
}

export function getSchemeSlabBasedSlab(requestData: any): Promise<any> {
  console.log('inside requestData function', requestData);
  return VgRetailerSdk.getSchemeSlabBasedSlab(requestData);
}
export function getSchemeCrossBasedSlab(requestData: any): Promise<any> {
  console.log('inside requestData function', requestData);
  return VgRetailerSdk.getSchemeCrossBasedSlab(requestData);
}

export function RewardsPoints(): Promise<any> {
  console.log('inside Reward points function');
  return VgRetailerSdk.userScanOutPointSummary({});
}
export function getCrossSchemesDetails(requestData: any): Promise<any> {
  console.log('inside Reward points function');
  return VgRetailerSdk.getCrossSchemesDetails(requestData);
}
export function getSlabBasedSchemes(requestData: any): Promise<any> {
  console.log('inside Reward points function');
  return VgRetailerSdk.getSlabBasedSchemes(requestData);
}
export function validateRetailerCoupon(requestData: any): Promise<any> {
  console.log('inside Reward points function');
  return VgRetailerSdk.validateRetailerCoupon(requestData);
}

export function registerCustomer(requestData: any): Promise<any> {
  console.log('inside Reward points function');
  return VgRetailerSdk.registerCustomer(requestData);
}
export function processForPin(requestData: any): Promise<any> {
  console.log('inside Reward points function');
  return VgRetailerSdk.processForPin(requestData);
}
export function processCoupon(requestData: any): Promise<any> {
  console.log('inside Reward points function');
  return VgRetailerSdk.processCoupon(requestData);
}
export function getProductCrossSellScheme(requestData: any): Promise<any> {
  console.log('inside Reward points function');
  return VgRetailerSdk.getProductCrossSellScheme(requestData);
}
export function getProductSlabBasedScheme(requestData: any): Promise<any> {
  console.log('inside Reward points function');
  return VgRetailerSdk.getProductSlabBasedScheme(requestData);
}
export function bankTransfer(requestData: any): Promise<any> {
  console.log('inside Reward points function');
  return VgRetailerSdk.bankTransfer(requestData);
}

export function scanIn(requestData: any): Promise<any> {
  console.log('inside Reward points function');
  return VgRetailerSdk.scanIn(requestData);
}

export function getFile(requestData: any): Promise<any> {
  console.log('inside Reward points function');
  return VgRetailerSdk.getFile(requestData);
}

export function uploadFile(requestData: any): Promise<any> {
  console.log('inside upload file with params', requestData);
  return VgRetailerSdk.uploadFile(requestData);
}

export function getSchemeFileList(requestData: any): Promise<any> {
  console.log('inside getSchemeFileList', requestData);
  return VgRetailerSdk.getSchemeFileList(requestData);
}

export function getTdsCertificate(requestData: any): Promise<any> {
  console.log('inside getSchemeFileList', requestData);
  return VgRetailerSdk.getTdsCertificate(requestData);
}

export function GetPrimarySchemeFileList(): Promise<any> {
  return VgRetailerSdk.GetPrimarySchemeFileList();
}
export function getCurrentSlabOnSlabBased(requestData: any): Promise<any> {
  console.log('inside get current slab on slab based function');
  return VgRetailerSdk.getCurrentSlabOnSlabBased(requestData);
}
export function getCurrentSlabOnCrossSell(requestData: any): Promise<any> {
  console.log('inside get current slab on slab based function');
  return VgRetailerSdk.getCurrentSlabOnCrossSell(requestData);
}
