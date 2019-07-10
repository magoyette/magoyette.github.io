const jsBeautify = require('js-beautify').html;
const fs = require('fs');
const glob = require('glob');

const htmlOptions = {
  indent_size: 2
};

const encoding = 'utf8';

glob('**/dist/**/*.html', { absolute: true }, (er, files) => {
  files.forEach(file => {
    const formattedHtml = jsBeautify(fs.readFileSync(file, encoding), htmlOptions);
    fs.writeFileSync(file, formattedHtml, encoding);
  });
});
