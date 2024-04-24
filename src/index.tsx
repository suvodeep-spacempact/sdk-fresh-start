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
export function sampletry(requestData: any): Promise<number> {
  console.log('inside sampletryyyyy4 function', requestData);

  return VgRetailerSdk.sampletry(requestData);
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
