var self = require("sdk/self");

var pageMod = require('sdk/page-mod');

pageMod.PageMod({
  include: 'about:reader*',
  contentScript: 'alert("lol");'
});

// a dummy function, to show how tests work.
// to see how to test this function, look at test/test-index.js
function dummy(text, callback) {
  callback(text);
}

exports.dummy = dummy;
