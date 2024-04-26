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

export function rewardPointsHistory(requestData: any): Promise<number> {

  return VgRetailerSdk.rewardPointsHistory(requestData);
}
export function ScannedBalancePoints(requestData: any): Promise<number> {

  return VgRetailerSdk.ScannedBalancePoints(requestData);
}

export function userScanOutPointSummary(requestData: any): Promise<number> {

  return VgRetailerSdk.userScanOutPointSummary(requestData);
}

export function InitializeSDK(requestData: any): Promise<number> {

  return VgRetailerSdk.InitializeSDK(requestData);
}

export function postData(): Promise<any> {
  return VgRetailerSdk.postData();
}
