const ArticleLinks = require('./article_links.js').ArticleLinks;
const React = require('react');
const ReactDOM = require('react-dom');

function onReaderContentReady() {
  const container = document.createElement('div');
  document.body.appendChild(container);
  ReactDOM.render(
    <ArticleLinks/>,
    container
  );
}

// TODO: Use event like AboutReaderContentReady, or
// at least wait for reader dom structure to appear.
window.setTimeout(onReaderContentReady, 1000);
