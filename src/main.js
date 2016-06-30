const ArticleLinks = require('./article_links.js').ArticleLinks;
const dom = require('./dom.js');
const React = require('react');
const ReactDOM = require('react-dom');

function onReaderContentReady() {
  const contentNode = dom.getReaderContentNode(document);
  const linkNodes = dom.getAllLinkNodes(contentNode);
  const articles = linkNodes;

  const container = document.createElement('div');
  document.body.appendChild(container);
  ReactDOM.render(
    <ArticleLinks articles={articles}/>,
    container
  );
}

// TODO: Use event like AboutReaderContentReady, or
// at least wait for reader dom structure to appear.
window.setTimeout(onReaderContentReady, 1000);
