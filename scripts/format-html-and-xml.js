const jsBeautify = require('js-beautify').html;
const fs = require('fs');
const glob = require('glob');
const xmlFormatter = require('xml-formatter');

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

const xmlOptions = {collapseContent: true};

glob('**/dist/**/*.xml', { absolute: true }, (er, files) => {
  files.forEach(file => {
    const formattedXml = xmlFormatter(fs.readFileSync(file, encoding), xmlOptions);
    fs.writeFileSync(file, formattedXml, encoding);
  });
});
