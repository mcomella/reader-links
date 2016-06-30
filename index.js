var pageMod = require('sdk/page-mod');

pageMod.PageMod({
  include: 'about:reader*',
  contentScriptWhen: 'ready',
  contentScriptFile: './bundle.js',
});
