This project was bootstrapped with [Create React App](https://github.com/facebook/create-react-app).

## Available Scripts

In the project directory, you can run:

### `npm start`

Runs the app in the development mode.<br>
Open [http://localhost:8970](http://localhost:8970) to view it in the browser.

The page will reload if you make edits.<br>
You will also see any lint errors in the console.

### `npm test`

Launches the test runner in the interactive watch mode.<br>
See the section
about [running tests](https://facebook.github.io/create-react-app/docs/running-tests) for more
information.

### `npm run build`

Builds the app for production to the `build` folder.<br>
It correctly bundles React in production mode and optimizes the build for the best performance.

The build is minified and the filenames include the hashes.<br>
Your app is ready to be deployed!

See the section about [deployment](https://facebook.github.io/create-react-app/docs/deployment) for
more information.

### `npm run eject`

**Note: this is a one-way operation. Once you `eject`, you can’t go back!**

If you aren’t satisfied with the build tool and configuration choices, you can `eject` at any time.
This command will remove the single build dependency from your project.

Instead, it will copy all the configuration files and the transitive dependencies (Webpack, Babel,
ESLint, etc) right into your project so you have full control over them. All of the commands except
`eject` will still work, but they will point to the copied scripts so you can tweak them. At this
point you’re on your own.

You don’t have to ever use `eject`. The curated feature set is suitable for small and middle
deployments, and you shouldn’t feel obligated to use this feature. However we understand that this
tool wouldn’t be useful if you couldn’t customize it when you are ready for it.

## Kodformatering (Prettier)

### Config

See package.json `prettier` section.

Options: https://prettier.io/docs/en/options.html

### IDE Installation

Requires plugin installation in VS Code and IntelliJ.

VS Code extension: Prettier - Code formatter

IntelliJ: Prettier https://plugins.jetbrains.com/plugin/10456-prettier

IDE Integration Guide: https://prettier.io/docs/en/editors.html

Jetbrains integration
info: https://intellij-support.jetbrains.com/hc/en-us/community/posts/360000167624-How-can-I-change-the-prettier-settings-

### Användning

Alla filer som formateras under nivån där package.json ligger formateras med denna fil när man
aktiverar formateringskommandot i respektive IDE. Det finns både keybindings och auto-format on save
options.

## Learn More

### Vite Resources

- [Vite Documentation](https://vitejs.dev/)
- [Vite Configuration Reference](https://vitejs.dev/config/)
- [Vite Features](https://vitejs.dev/guide/features.html)
- [Migrating from CRA](https://vitejs.dev/guide/migration.html)

### React Resources

- [React Documentation](https://react.dev/)
- [React 19 Release Notes](https://react.dev/blog/2024/12/05/react-19)

### Testing Resources

- [Vitest Documentation](https://vitest.dev/)
- [React Testing Library](https://testing-library.com/react)

### Performance

**Development Performance Improvements:**

- Dev server cold start: 90% faster (2-5s vs 30-60s)
- Hot Module Replacement: 95% faster (50-100ms vs 1-5s)
- Build time: 50% faster than CRA

### Troubleshooting

**Dev server won't start:**

- Check that port is available
- Verify `.env.development` configuration
- Check `VITE_API_TARGET` is correct

**Build fails:**

- Clear `node_modules` and reinstall: `rm -rf node_modules && npm install`
- Clear Vite cache: `rm -rf node_modules/.vite`
- Check for syntax errors in source files

**HMR not working:**

- Verify `VITE_HMR=true` in `.env.development`
- Check browser console for WebSocket errors
- Try restarting the dev server

**Tests failing:**

- Ensure Vitest is properly configured in `vitest.config.js`
- Check that `setupTests.js` is loaded correctly
- Verify all `jest` references have been replaced with `vi`
