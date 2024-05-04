import * as React from 'react';

import { StyleSheet, View, Text, Button, ScrollView } from 'react-native';
import {
  multiply,
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
} from 'react-native-vg-retailer-sdk';

export default function App() {
  const [result, setResult] = React.useState<string | number | undefined>();
  React.useEffect(() => {

  }, []);

  async function bankdetails() {
    try {
      let data = await verifyBankDetails({
        bankIfsc: 'SBIN0010790',
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
        //mode: ['paytm', 'bank transfer'],
        status: ['pending', 'success'],
        //userId: '22390',
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
        subCategories: [5],
        userId: '22390',
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
        subCategories: [5],
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
        categories: [2],
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
        subCategoryIds: ['8'],
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
      let data = await captureCustomerDetails('9039128610');
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
        addedBy: 1,
        errorCode: 1,
        statusType: 1,
        pardId: 1,
        anomaly: 1,
        points: 1,
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
      let data = await getEligibleProducts('s', 's');
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
  async function get_Slab_View() {
    try {
      let data = await getSlabView('dfhjk');
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
  // const volleyu = async () => {
  //   let data = await postData();
  //   console.log(data);
  // };
  async function intializesdk() {
    try {
      let data = await InitializeSDK({
        baseurl: 'http://34.93.239.251:5000/vguard/api',
        accesstoken:
          'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjIyMzkwLCJyb2xlSWQiOiIyIiwidXNlckNvZGUiOiJWR0lMMDEyMTg5OCIsImlzQWN0aXZlIjoiMSIsIm1vYmlsZSI6Ijk4MTE1NTU3ODkiLCJkaXNwbGF5TmFtZSI6IlJldGFpbGVyIFRlc3Q0IiwiaWF0IjoxNzE0NzM4ODMzLCJleHAiOjE3MTczMzA4MzN9.lbfD-850A91qSufMr2Dt8_Mmof2ikYhRcVtCxBb27zg',
        refreshtoken:
          'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjIyMzkwLCJyb2xlSWQiOiIyIiwidXNlckNvZGUiOiJWR0lMMDEyMTg5OCIsImlzQWN0aXZlIjoiMSIsIm1vYmlsZSI6Ijk4MTE1NTU3ODkiLCJkaXNwbGF5TmFtZSI6IlJldGFpbGVyIFRlc3Q0IiwiaWF0IjoxNzE0NzM4ODMzLCJleHAiOjE3MTczMzA4MzN9.GOLLeDmQ2EOEMPdyInBIwTgGtmyvYA8nJU3kjm0FTe0',
      });
      console.log(data);
      setResult(data.toString());
    } catch (err) {
      console.log(err);
      setResult((err as Error).toString());
    }
  }
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
        <Button title="Initialize SDK" onPress={intializesdk} />
        <Button title="Clear" onPress={() => setResult('')} />
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
