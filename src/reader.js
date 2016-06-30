const ArticleLinks = require('./article_links.js').ArticleLinks;
const React = require('react');
const ReactDOM = require('react-dom');

/*
const Cu = Components.utils;
Cu.import("resource://gre/modules/Services.jsm");
*/

window.addEventListener('DOMContentLoaded', function () {
	console.log('lol handled');
	return false;
});

function getAllLinks(node) {
  // TODO: only inside div .content
  let linksHTMLColl = document.getElementsByTagName('a');
  let linksArr = Array.prototype.slice.call(linksHTMLColl);
  debugger;
  return linksArr.filter((val) => !val.href.startsWith('#'));
}

window.addEventListener('AboutReaderContentReady', function () {
	console.log('reader handled');
	getAllLinks(document);
	return false;
});

/*
Services.obs.addObserver({
	observe: function (subject, topic, data) {
		console.log('handled: ' + subject);
		return false;
	},
}, "AboutReader:Ready", false);
*/

// TODO: wait until reader loads
/*
const container = document.createElement('div');
document.body.appendChild(container);
ReactDOM.render(
  <ArticleLinks/>,
  container
);
*/
