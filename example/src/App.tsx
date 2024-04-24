import * as React from 'react';

import { StyleSheet, View, Text, Button } from 'react-native';
import {
  multiply,
  sampletry,
  checkIfUserExists,
  rewardPointsHistory,
  ScannedBalancePoints,
  userScanOutPointSummary,
} from 'react-native-vg-retailer-sdk';

export default function App() {
  const [result, setResult] = React.useState<string | number | undefined>();
  React.useEffect(() => {
    multiply(3, 7).then(setResult);
    console.log('back here');
  }, []);
  async function bankdetails() {
    try {
      let data = await sampletry({
        bankIfsc: 'PUNB0601500',
        bankAccNo: '6015001500010225',
        bankAccHolderName: '',
        bankAccType: '',
        bankNameAndBranch: '',
        checkPhoto: '',
      });
      console.log(data, '>>>>>>>>>>>>>>>>');
      setResult(data.toString());
    } catch (err) {
      console.log(err);
    }
  }

  async function userExists() {
    try {
      let data = await checkIfUserExists('8088183747');
      console.log(data, '>>>>>>>>>>>>>>>>');
      setResult(data.toString());
    } catch (err) {
      console.log(err);
    }
  }

  async function userRewardHistory() {
    try {
      let data = await rewardPointsHistory({
        mode: ['paytm', 'bank transfer'],
        userId: '22390',
      });
      console.log(data, '>>>>>>>>>>>>>>>>');
      setResult(data.toString());
    } catch (err) {
      console.log(err);
    }
  }

  async function ScannedBalancePoint() {
    try {
      let data = await ScannedBalancePoints({
        categories: [],
        subCategories: [1],
        userId: '22390',
      });
      console.log(data, '>>>>>>>>>>>>>>>>');
      setResult(data.toString());
    } catch (err) {
      console.log(err);
    }
  }
  async function userScanOutPointSummaryfunction() {
    try {
      let data = await userScanOutPointSummary({
        categories: [],
        subCategories: [1],
        userId: '22390',
      });
      console.log(data, '>>>>>>>>>>>>>>>>');
      setResult(data.toString());
    } catch (err) {
      console.log(err);
    }
  }
  return (
    <View style={styles.container}>
      <Button title="Bank details" onPress={bankdetails} />

      <Text>Result: {result}</Text>
      <Button title="Check if user Exists" onPress={userExists} />
      <Button title="User Reward Historys" onPress={userRewardHistory} />
      <Button title="Scanned Balance Points" onPress={ScannedBalancePoint} />
      <Button
        title="user Scan Out Point Summary"
        onPress={userScanOutPointSummaryfunction}
      />
      <Button title="Clear" onPress={() => setResult('')} />
    </View>
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
