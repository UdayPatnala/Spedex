// Basic polyfills needed for Jest and React Native
global.setTimeout = setTimeout;
global.clearTimeout = clearTimeout;
global.setImmediate = setImmediate;
global.clearImmediate = clearImmediate;
global.window = global;

jest.mock('expo-font', () => {
  return {
    loadAsync: jest.fn(),
    isLoaded: jest.fn().mockReturnValue(true),
  };
});

jest.mock('@expo/vector-icons', () => {
  const React = require('react');
  const { View } = require('react-native');
  return {
    MaterialIcons: (props) => React.createElement(View, { testID: `icon-${props.name}`, ...props }),
  };
});

jest.mock('expo-linear-gradient', () => {
  const React = require('react');
  const { View } = require('react-native');
  return {
    LinearGradient: (props) => React.createElement(View, { testID: "linear-gradient", ...props }),
  };
});

jest.mock('react-native/Libraries/Utilities/defineLazyObjectProperty', () => {
  return jest.fn();
});

jest.useFakeTimers();
