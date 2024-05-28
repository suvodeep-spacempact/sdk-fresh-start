import * as React from 'react';

import { launchImageLibrary } from 'react-native-image-picker';
import {
  StyleSheet,
  View,
  Text,
  Button,
  ScrollView,
  SafeAreaView,
} from 'react-native';
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
  getSchemeSlabBasedSlab,
  RewardsPoints,
  getSlabBasedSchemes,
  getCrossSchemesDetails,
  validateRetailerCoupon,
  registerCustomer,
  processForPin,
  processCoupon,
  getProductCrossSellScheme,
  getProductSlabBasedScheme,
  bankTransfer,
  scanIn,
  getFile,
  uploadFile,
  getTdsCertificate,
  getSchemeFileList,
  GetPrimarySchemeFileList,
  getSchemeCrossBasedSlab,
  getCurrentSlabOnSlabBased,
  getCurrentSlabOnCrossSell,
} from 'react-native-vg-retailer-sdk';

export default function App() {
  const [result, setResult] = React.useState<string | number | undefined>();
  React.useEffect(() => {
    Promise.resolve(intializesdk());
  }, []);

  async function bankdetails() {
    try {
      let data = await verifyBankDetails({
        bankIfsc: 'PUNB0601500',
        bankAccNo: '6015001500010225',
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

  async function get_Current_Slab_On_Slab_Based() {
    try {
      let data = await getCurrentSlabOnSlabBased({ schemeCode: 'VGSCHA3025' });
      console.log(data, '>>>>>>>>>>>>>>>>');
      setResult(data.toString());
    } catch (err) {
      console.log(err);
      setResult((err as Error).toString());
    }
  }
  async function get_Current_Slab_On_Cross_Sell() {
    try {
      let data = await getCurrentSlabOnCrossSell({ schemeCode: 'VGSCHFC60D' });
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
        mode: ['paytm'],
        status: [],
        fromDate: '2022-09-01',
        toDate: '2022-09-30',
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
        categories: [3],
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
        categories: [3],
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
        categories: [4],
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
        status: [],
        scanType: '',
        fromDate: '2021-10-10',
        couponCode: '',
        toDate: '2021-10-11',
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
        categoryId: '',
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
        subCategoryIds: [8],
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
        subCategoryIds: [8],
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
  async function get_Scheme_Slab_Based_Slab() {
    try {
      let data = await getSchemeSlabBasedSlab({ schemeId: 'VGSCH4E8FB' });
      console.log(data, '--------------');
      setResult(data.toString());
    } catch (err) {
      console.log(err, '');
      setResult((err as Error).toString());
    }
  }
  async function get_Scheme_Cross_Based_Slab() {
    try {
      let data = await getSchemeCrossBasedSlab({ schemeId: 'VGSCHFC60D' });
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
        contactNo: '6565656565',
        name: 'SHIVAKAR',
        email: 'test65@gmail.com',
        currAdd: 'Central Delhi',
        alternateNo: '7575757575',
        state: 'Delhi',
        district: 'Delhi',
        city: 'Central Delhi',
        landmark: 'Delhi',
        pinCode: '110006',
        dealerName: 'Mohit test',
        dealerAdd: 'Central Delhi',
        dealerState: 'Delhi',
        dealerDist: 'Central Delhi',
        dealerCity: 'Central Delhi',
        dealerPinCode: '110006',
        dealerNumber: '9873608820',
        addedBy: 22390, //Retailer UserId
        billDetails: '4b34eb9a-6e4c-4314-b408-31f6623b0a71.jpg',
        warrantyPhoto: '4b34eb9a-6e4c-4314-b408-31f6623b0a71.jpg',
        sellingPrice: '1500',
        emptStr: '',
        cresp: {
          custIdForProdInstall: '',
          modelForProdInstall: '',
          errorCode: 0,
          errorMsg: '',
          statusType: 1,
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
          copuonCode: '5362224187701942',
          bitEligibleScratchCard: false,
          partId: '141',
          pardId: 123,
          partNumber: '3002751',
          partName: '',
          skuDetail: 'JAADOO1050',
          purchaseDate: '2024-05-11', //also Manufacturing Date
          categoryId: '1',
          category: 'Digital Ups',
          anomaly: 0,
          warranty: '365',
        },
        selectedProd: {
          specs: '',
          pointsFormat: '',
          product: '',
          productName: '',
          productCategory: 'Digital Ups',
          productCode: '3002751',
          points: 0.0,
          imageUrl: '',
          userId: '22390',
          productId: '',
          paytmMobileNo: '',
        },
        latitude: '28.6798562',
        longitude: '77.0622139',
        geolocation: 'Central Delhi',
        dealerCategory: 'Customer',
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
  async function get_Product_Slab_Sell_Scheme() {
    try {
      let data = await getProductSlabBasedScheme({
        subCategory: [],
        category: [],
        schemeNumber: 'VGSCH6CD58',
      });
      console.log(data, '--------------');
      setResult(data.toString());
    } catch (err) {
      console.log(err, '');
      setResult((err as Error).toString());
    }
  }
  async function get_Product_Cross_Sell_Scheme() {
    try {
      let data = await getProductCrossSellScheme({
        subCategory: [],
        category: [],
        schemeNumber: 'VGSCH6CD58',
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
        amount: '0',
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
        uuid: 'bbcc2b15-047c-409a-8786-4ea50e8ac2c9.jpg',
        imageRelated: 'BILL',
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
      let data = await getTdsCertificate({
        quater: ['Quarter 2'],
        fiscalYear: ['2024-2026', '2022-2023'],
      });
      console.log(data, '--------------');
      setResult(data.toString());
    } catch (err) {
      console.log(err, '');
      setResult((err as Error).toString());
    }
  }
  async function get_Scheme_File_List() {
    try {
      let data = await getSchemeFileList({
        schemeId: 'VGSCHFC60D',
      });
      console.log(data, '--------------');
      setResult(data.toString());
    } catch (err) {
      console.log(err, '');
      setResult((err as Error).toString());
    }
  }

  async function Get_Primary_Scheme_File_List() {
    try {
      let data = await GetPrimarySchemeFileList();
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
        baseurl: 'http://35.207.195.181:5000/vguard/api',
        accesstoken:
          'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjIyMzkwLCJyb2xlSWQiOiIyIiwidXNlckNvZGUiOiJWR0lMMDEyMTg5OCIsImlzQWN0aXZlIjoiMSIsIm1vYmlsZSI6Ijk4MTE1NTU3ODkiLCJkaXNwbGF5TmFtZSI6IlN1bWl0IFRlc3QiLCJpYXQiOjE3MTY0NjAxOTQsImV4cCI6MTcxOTA1MjE5NH0.b0iu-p4puV3E8PblHwwkKOl8DfU1AqiCHezf7HKNHf8',
        refreshtoken:
          'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjIyMzkwLCJyb2xlSWQiOiIyIiwidXNlckNvZGUiOiJWR0lMMDEyMTg5OCIsImlzQWN0aXZlIjoiMSIsIm1vYmlsZSI6Ijk4MTE1NTU3ODkiLCJkaXNwbGF5TmFtZSI6IlN1bWl0IFRlc3QiLCJpYXQiOjE3MTY0NjAxOTQsImV4cCI6MTcxOTA1MjE5NH0.erJFQoHU_ePYbeOo5lzU1bfFiXsZ8vvWcZvIU_d8MSc',
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
            console.log(data);
            setResult(data.toString());
          })
          .catch((er) => {
            setResult((er as Error).toString());
          });
        //setResult(data.toString());
      })
      .catch((er) => {
        console.log(er);
        setResult((er as Error).toString());
      });

    console.log('adfsdvjksdvjkdjkvda');
  };

  return (
    <SafeAreaView>
      <ScrollView>
        <View style={styles.container}>
          <Button title="Bank details" onPress={bankdetails} />

          <Text>Result: {result}</Text>
          {/* <Button title="Check if user Exists" onPress={userExists} /> */}
          <Button title="User Reward Historys" onPress={userRewardHistory} />
          <Button
            title="Scanned Balance Points"
            onPress={ScannedBalancePoint}
          />
          <Button
            title="user Scan Out Point Summary"
            onPress={userScanOutPointSummaryfunction}
          />
          <Button title="Get Categories list" onPress={get_Categories_List} />
          <Button
            title="Get User Base Points list"
            onPress={get_User_Base_Points}
          />
          <Button
            title="Get User Scan History"
            onPress={get_User_Scan_History}
          />
          <Button
            title="Capture Customer Details"
            onPress={capture_Customer_Details}
          />
          <Button title="Register warranty" onPress={register_Warranty} />
          <Button
            title="get Eligible Products"
            onPress={get_Eligible_Products}
          />

          {/* <Button
        title="get Combo slab scheme"
        onPress={get_ComboSlab_Schemes}
      /> */}
          <Button
            title="get_Scheme_Slab_Based_Slab"
            onPress={get_Scheme_Slab_Based_Slab}
          />
          <Button
            title="get_Scheme_Cross_Based_Slab"
            onPress={get_Scheme_Cross_Based_Slab}
          />
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
            title="get_Product_Cross_Sell_Scheme"
            onPress={get_Product_Cross_Sell_Scheme}
          />
          <Button
            title="get_Product_Slab_Sell_Scheme"
            onPress={get_Product_Slab_Sell_Scheme}
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
          <Button
            title="Get get_Scheme_File_List"
            onPress={get_Scheme_File_List}
          />
          <Button
            title="Get_Primary_Scheme_File_List "
            onPress={Get_Primary_Scheme_File_List}
          />
          <Button
            title="get_Current_Slab_On_Cross_Sell "
            onPress={get_Current_Slab_On_Cross_Sell}
          />
          <Button
            title="get_Current_Slab_On_Slab_Based "
            onPress={get_Current_Slab_On_Slab_Based}
          />
        </View>
      </ScrollView>
    </SafeAreaView>
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
