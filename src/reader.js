const ArticleLinks = require('./article_links.js').ArticleLinks;
const React = require('react');
const ReactDOM = require('react-dom');

// TODO: Move to separate module. DOM?
function getReaderContentNode(doc) {
  return doc.getElementById('container')
      .getElementsByClassName('content')
      .item(0);
}

// TODO: Move to DOM module.
function getAllLinkNodes(node) {
  const linkHTMLColl = node.getElementsByTagName('a');
  const linkArr = Array.prototype.slice.call(linkHTMLColl);
  return linkArr.filter(val => !val.href.startsWith('#'));
}

function onReaderContentReady() {
  const contentNode = getReaderContentNode(document);
  const linkNodes = getAllLinkNodes(contentNode);
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
