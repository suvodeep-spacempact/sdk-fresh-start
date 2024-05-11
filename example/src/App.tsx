import * as React from 'react';

import {
  StyleSheet,
  View,
  Text,
  Button,
  ScrollView,
} from 'react-native';
import {  launchImageLibrary } from 'react-native-image-picker';
import {
  verifyBankDetails,
  rewardPointsHistory,
  ScannedBalancePoints,
  userScanOutPointSummary,
  InitializeSDK,
  getCategoriesList,
  getUserBasePoints,
  getUserScanHistory,
  captureCustomerDetails,
  registerWarranty,
  getEligibleProducts,
  //getComboSlabSchemes,
  getSlabView,
  RewardsPoints,
  getSlabBasedSchemes,
  getCrossSchemesDetails,
  validateRetailerCoupon,
  registerCustomer,
  processForPin,
  processCoupon,
  getCategoryProductDetails,
  bankTransfer,
  scanIn,
  getFile,
  uploadFile,
  getTdsCerticateFiles,
} from 'react-native-vg-retailer-sdk';

export default function App() {
  const [result, setResult] = React.useState<string | number | undefined>();
  React.useEffect(() => {}, []);

  async function bankdetails() {
    try {
      let data = await verifyBankDetails({
        bankIfsc: 'SBIN0010792',
        bankAccNo: '31348186046',
        bankAccHolderName: '',
        bankAccType: '',
        bankNameAndBranch: '',
        checkPhoto: '',
      });
      console.log(typeof data, '****************');
      console.log(data, '---------------');
      console.log(data, '>>>>>>>>>>>>>>>>');
      setResult(data.toString());
    } catch (err) {
      console.log(err);
      setResult((err as Error).toString());
    }
  }

  async function userRewardHistory() {
    try {
      let data = await rewardPointsHistory({
        // "mode" : ["payt", "bank transfer"], //bank transfer or UPI
        status: ['success'], //success, pending, failed
        // "fromDate" : "2021-01-02",
        // "toDate" : "2022-04-12",
        //"userId":"22390"
      });
      console.log(data, '>>>>>>>>>>>>>>>>');
      setResult(data.toString());
    } catch (err) {
      console.log(err);
      setResult((err as Error).toString());
    }
  }

  async function ScannedBalancePoint() {
    try {
      let data = await ScannedBalancePoints({
        categories: [1, 3],
        subCategories: [],
        //userId: '22390',
      });
      console.log(data, '>>>>>>>>>>>>>>>>');
      setResult(data.toString());
    } catch (err) {
      console.log(err);
      setResult((err as Error).toString());
    }
  }
  async function userScanOutPointSummaryfunction() {
    try {
      let data = await userScanOutPointSummary({
        categories: [1, 3],
        subCategories: [6],
        userId: '22390',
      });
      console.log(data, '>>>>>>>>>>>>>>>>');
      setResult(data.toString());
    } catch (err) {
      console.log(err);
      setResult((err as Error).toString());
    }
  }
  async function get_Categories_List() {
    try {
      let data = await getCategoriesList({
        categories: [1, 5],
      });
      console.log(data, '--------------');
      setResult(data.toString());
    } catch (err) {
      console.log(err);
      setResult((err as Error).toString());
    }
  }
  async function get_User_Base_Points() {
    try {
      let data = await getUserBasePoints({
        categoryIds: ['1'],
        subCategoryIds: ['6'],
      });
      console.log(data, '--------------');
      setResult(data.toString());
    } catch (err) {
      console.log(err);
      setResult((err as Error).toString());
    }
  }
  async function get_User_Scan_History() {
    try {
      let data = await getUserScanHistory({
        status: ['success'],
        scanType: 'Scan-out',
        fromDate: '2023-12-04',
        couponCode: '5084089287556709',
      });
      console.log(data, '--------------');
      setResult(data.toString());
    } catch (err) {
      console.log(err);
      setResult((err as Error).toString());
    }
  }
  async function capture_Customer_Details() {
    try {
      let data = await captureCustomerDetails({ mobileNo: '9039128615' });
      console.log(data, '--------------');
      setResult(data.toString());
    } catch (err) {
      console.log(err, '');
      setResult((err as Error).toString());
    }
  }
  async function register_Warranty() {
    try {
      let data = await registerWarranty({
        nameTitle: '<string>',
        contactNo: '9811555789',
        name: '<string>',
        email: '<string>',
        currAdd: '<string>',
        alternateNo: '<string>',
        state: '<string>',
        district: '<string>',
        city: '<string>',
        landmark: '<string>',
        pinCode: '<string>',
        dealerName: '<string>',
        dealerAdd: '<string>',
        dealerState: '<string>',
        dealerDist: '<string>',
        dealerCity: '<string>',
        dealerPinCode: '<string>',
        dealerNumber: '<string>',
        addedBy: 2,
        billDetails: '<string>',
        warrantyPhoto: '<string>',
        sellingPrice: '<string>',
        emptStr: '<string>',
        cresp: {
          custIdForProdInstall: '<string>',
          modelForProdInstall: '<string>',
          errorCode: 0,
          errorMsg: '<string>',
          statusType: 1,
          balance: '<string>',
          currentPoints: '<string>',
          couponPoints: '<string>',
          promotionPoints: '<string>',
          transactId: '<string>',
          schemePoints: '<string>',
          basePoints: '<string>',
          clubPoints: '<string>',
          scanDate: '<string>',
          scanStatus: '<string>',
          copuonCode: '<string>',
          bitEligibleScratchCard: false,
          pardId: 123,
          partNumber: '<string>',
          partName: '<string>',
          couponCode: '<string>',
          skuDetail: '<string>',
          purchaseDate: '<string>',
          categoryId: '<string>',
          category: '<string>',
          anomaly: 1,
          warranty: '<string>',
        },
        selectedProd: {
          specs: '<string>',
          pointsFormat: '<string>',
          product: '<string>',
          productName: '<string>',
          productCategory: '<string>',
          productCode: '<string>',
          points: 0,
          imageUrl: '<string>',
          userId: '<string>',
          productId: '<string>',
          paytmMobileNo: '<string>',
        },
        latitude: '<string>',
        longitude: 'asd',
        geolocation: '<string>',
        dealerCategory: '<string>',
      });
      console.log(data, '--------------');
      setResult(data.toString());
    } catch (err) {
      console.log(err, '');
      setResult((err as Error).toString());
    }
  }
  async function get_Eligible_Products() {
    try {
      let data = await getEligibleProducts({
        //"categoryId":"1",
        schemeId: 'VGSCH4E8FB',
      });
      console.log(data, '--------------');
      setResult(data.toString());
    } catch (err) {
      console.log(err, '');
      setResult((err as Error).toString());
    }
  }
  // async function get_ComboSlab_Schemes() {
  //   try {
  //     let data = await getComboSlabSchemes({categoryIds:[],endDate:"",fromDate:"",status:""});
  //     console.log(data, '--------------');
  //     setResult(data.toString());
  //   } catch (err) {
  //     console.log(err,"");
  //     setResult((err as Error).toString());
  //   }
  // }
  async function get_Combo_Based_Schemes() {
    try {
      let data = await getCrossSchemesDetails({
        categoryIds: [],
        endDate: '',
        fromDate: '',
        status: '',
      });
      console.log(data, '--------------');
      setResult(data.toString());
    } catch (err) {
      console.log(err, '');
      setResult((err as Error).toString());
    }
  }
  async function get_Slab_Based_Schemes() {
    try {
      let data = await getSlabBasedSchemes({
        categoryIds: [],
        endDate: '',
        fromDate: '',
        status: '',
      });
      console.log(data, '--------------');
      setResult(data.toString());
    } catch (err) {
      console.log(err, '');
      setResult((err as Error).toString());
    }
  }
  async function get_Slab_View() {
    try {
      let data = await getSlabView({ schemeId: 'VGSCHFC60D' });
      console.log(data, '--------------');
      setResult(data.toString());
    } catch (err) {
      console.log(err, '');
      setResult((err as Error).toString());
    }
  }
  async function reward_points() {
    try {
      let data = await RewardsPoints();
      console.log(data, '--------------');
      setResult(data.toString());
    } catch (err) {
      console.log(err, '');
      setResult((err as Error).toString());
    }
  }
  async function validate_Retailer_Coupon() {
    try {
      let data = await validateRetailerCoupon({
        category: 'Customer',
        couponCode: '5362224187701942',
        from: 'APP',
        geolocation: '',
        latitude: '12.892242',
        longitude: '77.5976361',
        retailerCoupon: 'true',
      });
      console.log(data, '--------------');
      setResult(data.toString());
    } catch (err) {
      console.log(err, '');
      setResult((err as Error).toString());
    }
  }
  async function register_Customer() {
    try {
      let data = await registerCustomer({
        nameTitle: '',
        contactNo: '9435731954',
        name: 'B k singh',
        email: 'madhuranjankumar1983.mk@gmail.com',
        currAdd: 'Muzaffarpur',
        alternateNo: '',
        state: 'Bihar',
        district: 'Muzaffarpur',
        city: 'Muzaffarpur',
        landmark: '',
        pinCode: '843108',
        dealerName: 'Madhu Ranjan Kumar',
        dealerAdd: '',
        dealerState: 'Bihar',
        dealerDist: 'Muzaffarpur',
        dealerCity: 'Muzaffarpur',
        dealerPinCode: '843108',
        dealerNumber: '9934674364',
        addedBy: '',
        billDetails: '',
        warrantyPhoto: '4b34eb9a-6e4c-4314-b408-31f6623b0a71.jpg',
        sellingPrice: '',
        emptStr: '',
        cresp: {
          custIdForProdInstall: '',
          modelForProdInstall: '',
          errorCode: 0,
          errorMsg: '',
          statusType: '',
          balance: '',
          currentPoints: '',
          couponPoints: '',
          promotionPoints: '',
          transactId: '',
          schemePoints: '',
          basePoints: '',
          clubPoints: '',
          scanDate: '',
          scanStatus: '',
          copuonCode: '8467909055761994',
          bitEligibleScratchCard: 1,
          pardId: '',
          partNumber: '',
          partName: '',
          couponCode: '',
          skuDetail: 'VG 400',
          purchaseDate: '',
          categoryId: '',
          category: '',
          anomaly: 0,
          warranty: '183',
        },
        selectedProd: {
          specs: '',
          pointsFormat: '',
          product: '',
          productName: '',
          productCategory: '',
          productCode: '',
          points: 0.0,
          imageUrl: '',
          userId: '',
          productId: '',
          paytmMobileNo: '',
        },
        latitude: '',
        longitude: '',
        geolocation: '',
        dealerCategory: '',
      });
      console.log(data, '--------------');
      setResult(data.toString());
    } catch (err) {
      console.log(err, '');
      setResult((err as Error).toString());
    }
  }
  async function process_For_Pin() {
    try {
      let data = await processForPin({
        userMobileNumber: '',
        couponCode: '',
        pin: '',
        smsText: '',
        from: '',
        userType: '',
        userId: '453',
        apmID: '',
        userCode: '',
        latitude: '',
        longitude: '',
        geolocation: '',
        category: '',
      });
      console.log(data, '--------------');
      setResult(data.toString());
    } catch (err) {
      console.log(err, '');
      setResult((err as Error).toString());
    }
  }
  async function process_Coupon() {
    try {
      let data = await processCoupon({
        userMobileNumber: '9811555789',
        couponCode: '9802522723211275',
        pin: '1234',
        smsText: '',
        from: '',
        userType: '',
        userId: '453',
        apmID: '',
        userCode: '',
        latitude: '',
        longitude: '',
        geolocation: '',
        category: '',
      });
      console.log(data, '--------------');
      setResult(data.toString());
    } catch (err) {
      console.log(err, '');
      setResult((err as Error).toString());
    }
  }
  async function get_Category_Product_Details() {
    try {
      let data = await getCategoryProductDetails({
        subCategory: '6',
        category: '1',
        skuId: '',
      });
      console.log(data, '--------------');
      setResult(data.toString());
    } catch (err) {
      console.log(err, '');
      setResult((err as Error).toString());
    }
  }
  async function bank_Transfer() {
    try {
      let data = await bankTransfer({
        amount: '100',
        bankDetail: {
          bankAccHolderName: 'ehhegge',
          bankAccNo: '3461616',
          bankAccType: '',
          bankIfsc: 'zvzxbbx266',
          bankNameAndBranch: 'Andhra bank',
          checkPhoto: '451ac963-6290-4374-9881-9c54a76fee7b.jpg',
        },
      });
      console.log(data, '--------------');
      setResult(data.toString());
    } catch (err) {
      console.log(err, '');
      setResult((err as Error).toString());
    }
  }

  async function scan_In() {
    try {
      let data = await scanIn({
        couponCode: '7654367982156745',
        pin: '<string>',
        smsText: '<string>',
        from: '<string>',
        userType: '<string>',
        userId: '<long>',
        apmID: '<long>',
        userCode: '<string>',
        latitude: '<string>',
        longitude: '<string>',
        geolocation: '<string>',
        category: '<string>',
      });
      console.log(data, '--------------');
      setResult(data.toString());
    } catch (err) {
      console.log(err, '');
      setResult((err as Error).toString());
    }
  }
  async function get_File() {
    try {
      let data = await getFile({
        uuid: 'af0656a5-9a5e-4c4e-8da8-de91241a0dc3.png',
        imageRelated: 'PROFILE',
        userRole: '2',
      });
      console.log(data, '--------------');
      setResult(data.toString());
    } catch (err) {
      console.log(err, '');
      setResult((err as Error).toString());
    }
  }
  async function get_Tds_Certicate_Files() {
    try {
      let data = await getTdsCerticateFiles({
        fileId: '1',
        fiscalStartYear: '',
        fiscalEndYear: '',
        quater: '',
      });
      console.log(data, '--------------');
      setResult(data.toString());
    } catch (err) {
      console.log(err, '');
      setResult((err as Error).toString());
    }
  }
  // const volleyu = async () => {
  //   let data = await postData();
  //   console.log(data);
  // };
  async function intializesdk() {
    try {
      let data = await InitializeSDK({
        baseurl: 'http://34.93.239.251:5000/vguard/api',
        accesstoken:
          'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjIyMzkwLCJyb2xlSWQiOiIyIiwidXNlckNvZGUiOiJWR0lMMDEyMTg5OCIsImlzQWN0aXZlIjoiMSIsIm1vYmlsZSI6Ijk4MTE1NTU3ODkiLCJkaXNwbGF5TmFtZSI6IlJldGFpbGVyIFRlc3Q0IiwiaWF0IjoxNzE1NDE0MDg4LCJleHAiOjE3MTgwMDYwODh9.XlCtUWZw-6pu8gfdj69ZwbgBWq97dxC5TnaELFv7eac',
        refreshtoken:
          'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjIzNDIwLCJyb2xlSWQiOiIyIiwidXNlckNvZGUiOiJWR0lMMDIwMTAzNCIsImlzQWN0aXZlIjoiMSIsIm1vYmlsZSI6Ijk4NzM2MDg4MjAiLCJkaXNwbGF5TmFtZSI6Ik1vaGl0IHRlc3QiLCJpYXQiOjE3MTUzNDQzNjYsImV4cCI6MTcxNzkzNjM2Nn0.ykMbfFU-D0Pl3R6hOx3shUBD1N-AjNmTeeVO8vvdN08',
      });
      console.log(data);
      setResult(data.toString());
    } catch (err) {
      console.log(err);
      setResult((err as Error).toString());
    }
  }
  const handleCameraUpload = () => {
    launchImageLibrary({
      mediaType: 'photo',
      includeBase64: false,
    })
      .then((response: any) => {
        console.log(response);
        uploadFile({
          imageRelated: 'BILL',
          userRole: '2',
          file: {
            fileType: response.assets[0].type,
            fileUri: response.assets[0].uri,
            fileName: response.assets[0].fileName,
          },
        })
          .then((data: any) => {
            setResult(data.toString());
          })
          .catch((er) => {
            setResult((er as Error).toString());
          });
        setResult(data.toString());
      })
      .catch((er) => {
        console.log(er);
        setResult((er as Error).toString());
      });

    console.log('adfsdvjksdvjkdjkvda');
  };
  return (
    <ScrollView>
      <View style={styles.container}>
        <Button title="Bank details" onPress={bankdetails} />

        <Text>Result: {result}</Text>
        {/* <Button title="Check if user Exists" onPress={userExists} /> */}
        <Button title="User Reward Historys" onPress={userRewardHistory} />
        <Button title="Scanned Balance Points" onPress={ScannedBalancePoint} />
        <Button
          title="user Scan Out Point Summary"
          onPress={userScanOutPointSummaryfunction}
        />
        <Button title="Get Categories list" onPress={get_Categories_List} />
        <Button
          title="Get User Base Points list"
          onPress={get_User_Base_Points}
        />
        <Button title="Get User Scan History" onPress={get_User_Scan_History} />
        <Button
          title="Capture Customer Details"
          onPress={capture_Customer_Details}
        />
        <Button title="Register warranty" onPress={register_Warranty} />
        <Button title="get Eligible Products" onPress={get_Eligible_Products} />

        {/* <Button
        title="get Combo slab scheme"
        onPress={get_ComboSlab_Schemes}
      /> */}
        <Button title="Get Slab View" onPress={get_Slab_View} />
        <Button title="Get Reward Points" onPress={reward_points} />
        <Button
          title="Get Combo based schemes "
          onPress={get_Combo_Based_Schemes}
        />
        <Button
          title="Get slab based schemes "
          onPress={get_Slab_Based_Schemes}
        />
        <Button title="Get Reward Points" onPress={reward_points} />
        <Button
          title="validate retailer coupon"
          onPress={validate_Retailer_Coupon}
        />
        <Button title="register customer" onPress={register_Customer} />
        <Button title="Process for pin" onPress={process_For_Pin} />
        <Button title="Process Coupon" onPress={process_Coupon} />
        <Button
          title="Get Category Product details"
          onPress={get_Category_Product_Details}
        />
        <Button title="Bank Transfer" onPress={bank_Transfer} />
        <Button title="Scan In" onPress={scan_In} />
        <Button title="Get File" onPress={get_File} />
        <Button title="Initialize SDK" onPress={intializesdk} />
        <Button title="Clear" onPress={() => setResult('')} />
        <Button title="Test" onPress={handleCameraUpload} />
        <Button
          title="Get Tds Certificate Files"
          onPress={get_Tds_Certicate_Files}
        />
      </View>
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
