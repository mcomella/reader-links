var pageMod = require('sdk/page-mod');

pageMod.PageMod({
  include: 'about:reader*',
  contentScriptFile: './bundle.js',
});
