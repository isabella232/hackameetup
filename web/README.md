> *This project was bootstrapped with [Create React App](https://github.com/facebookincubator/create-react-app) +
  [backpack-react-scripts](https://github.com/Skyscanner/backpack-react-scripts/tree/master/packages/react-scripts).*
  It is very similar to one that you would create using [Create React App](https://github.com/facebookincubator/create-react-app)
  without [backpack-react-scripts](https://github.com/Skyscanner/backpack-react-scripts/tree/master/packages/react-scripts),
  so please refer to [it's documentation](https://github.com/facebookincubator/create-react-app/blob/master/packages/react-scripts/template/README.md)
  for more usage information.

# TL;DR;
To have the project up and running, first of all make sure you have node >= 8.11.1 and npm > 5.8.0, the run:

```sh
git clone https://github.com/Skyscanner/hackameetup.git
cd hackameetup/web
npm i
npm start
```

the commands above will clone the repository, install the dependencies and run the project locally.

## npm script commands

`backpack-react-scripts` is listed as a `devDependency` in your [package.json](./package.json) and exposes the following
npm scripts:

- `npm start`: Runs the app in development mode. Open [http://localhost:3000/](http://localhost:3000/) to
  view it in the browser. The page will reload if you make edits. You will see the build errors in the console.
- `npm test`: Runs the test watcher in an interactive mode. By default, runs tests related to files changes
  since the last commit.
- `npm run build`: Builds the app for production to the build folder. It correctly bundles React in production mode
  and optimizes the build for the best performance. The build is minified and the filenames include the hashes.
  Your app is ready to be deployed!
- `npm run lint`: Lints all javascript using [`eslint-config-skyscanner`](https://www.npmjs.com/package/eslint-config-skyscanner).


