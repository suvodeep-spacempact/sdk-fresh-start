# react-native-vg-retailer-sdk

VGUARD SDK Retailer Program

## Installation

```sh
npm install react-native-vg-retailer-sdk
```

## Usage

```js
import { rewardPointsHistory,ScannedBalancePoints,userScanOutPointSummary,InitializeSDK } from 'react-native-vg-retailer-sdk';

// ...

 let data = await InitializeSDK({
        baseurl: <<FROM_EVOLVE>>,
        accesstoken:<<ACCESS_TOKEN HERE>>
        refreshtoken:<<REFRESH_TOKEN_HERE>>
      });
      console.log(data);
```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)
