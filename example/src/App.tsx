import * as React from 'react';

import { StyleSheet, View, Text, Button } from 'react-native';
import {
  multiply,
  sampletry,
  rewardPointsHistory,
  ScannedBalancePoints,
  userScanOutPointSummary,
  InitializeSDK,
} from 'react-native-vg-retailer-sdk';

export default function App() {
  const [result, setResult] = React.useState<string | number | undefined>();
  React.useEffect(() => {

  }, []);


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
  // const volleyu = async () => {
  //   let data = await postData();
  //   console.log(data);
  // };
  async function intializesdk() {
    try {
      let data = await InitializeSDK({
        baseurl: 'http://34.93.239.251:5000/vguard/api',
        accesstoken:
          'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjIyMzkwLCJyb2xlSWQiOiIyIiwidXNlckNvZGUiOiJWR0lMMDEyMTg5OCIsImlzQWN0aXZlIjoiMSIsIm1vYmlsZSI6Ijk4MTE1NTU3ODkiLCJkaXNwbGF5TmFtZSI6IlJldGFpbGVyIFRlc3Q0IiwiaWF0IjoxNzE0MDk5ODEyLCJleHAiOjE3MTY2OTE4MTJ9.JwtVxVS4usJzc9yzhvG9IgS8IQ3pJI1skTHNIaUwuzQ',
        refreshtoken:
          'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjIyMzkwLCJyb2xlSWQiOiIyIiwidXNlckNvZGUiOiJWR0lMMDEyMTg5OCIsImlzQWN0aXZlIjoiMSIsIm1vYmlsZSI6Ijk4MTE1NTU3ODkiLCJkaXNwbGF5TmFtZSI6IlJldGFpbGVyIFRlc3Q0IiwiaWF0IjoxNzE0MDk5ODEyLCJleHAiOjE3MTY2OTE4MTJ9.lyse71L2mbUeyK0cZLarfxsorDZ-YQN2h8nJS78ImBg',
      });
      console.log(data);
      setResult(data.toString());
    } catch (err) {
      console.log(err);
    }
  }
  return (
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
      <Button title="Initialize SDK" onPress={intializesdk} />
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
