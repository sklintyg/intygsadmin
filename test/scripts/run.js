const cypress = require('cypress');
const marge = require('mochawesome-report-generator');
const { merge } = require('mochawesome-merge');
const fs = require('fs');


const dir = 'build/test-results';

if (!fs.existsSync(dir)){
  fs.mkdirSync(dir, { recursive: true });
}

// https://github.com/cypress-io/cypress/issues/1946

const config = {};

process.argv.forEach(function (val) {
  if (val.indexOf('=') > -1) {
    const parts = val.split('=');

    config[parts[0]] = parts[1];
  }
});

const options = {
  "reportDir": dir
};

const cypressOption = {
  "config": config
};

cypress.run(cypressOption).then(
  (results) => {
    //console.log(results);
    generateReport(options).finally(
      () => {
        if (results.totalFailed > 0) {
          process.exit(1)
        }
      }
    );
  }
).catch(error => {
  generateReport(options);
  console.error(error);
  process.exit(1);
});

function generateReport(options) {
  return merge(options).then(report => marge.create(report, options));
}
