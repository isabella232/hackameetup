import React from "react";
import { Text, View, StyleSheet } from "react-native";

import BpkText from "react-native-bpk-component-text";
import BpkButtonLink from "react-native-bpk-component-button-link";
import BpkAlert from "react-native-bpk-component-alert";

import {
  colorBlue500,
  colorWhite,
  spacingLg,
  spacingXxl
} from "bpk-tokens/tokens/base.react.native";

const showAlert = () => {
  BpkAlert.alert(
    "Backpack App",
    "It works!",
    [
      {
        text: "Cancel",
        style: "cancel"
      },
      ,
    ],
    { cancelable: true }
  );
};

const App = () => (
  <View>
    <View style={styles.header}>
      <View style={styles.container}>
        <BpkText emphasize style={styles.text} textStyle="lg">
          Welcome to React Native + Backpack
        </BpkText>
      </View>
    </View>
    <View style={styles.container}>
      <BpkText>To get started, edit 'App.js' and save to reload.</BpkText>
    </View>
    <View style={styles.container}>
      <BpkButtonLink title="Tap Me" onPress={showAlert} />
    </View>
  </View>
);

const styles = StyleSheet.create({
  header: {
    paddingTop: spacingXxl * 2,
    height: spacingXxl * 5,
    backgroundColor: colorBlue500
  },
  container: {
    flex: 1,
    alignItems: "center",
    justifyContent: "center",
    padding: spacingLg
  },
  text: {
    color: colorWhite
  }
});

export default App;
