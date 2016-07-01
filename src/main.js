const ArticleLinks = require('./ui/article_links.js').ArticleLinks;
const dom = require('./dom.js');
const net = require('./net.js');
const React = require('react');
const ReactDOM = require('react-dom');

function appendContainerToDocument(doc) {
  const container = doc.createElement('div');
  doc.body.appendChild(container);
  return container;
}

function render(container, articles) {
  ReactDOM.render(
    <ArticleLinks articles={articles}/>,
    container
  );
}

function onReaderContentReady() {
  const container = appendContainerToDocument(document);

  const contentNode = dom.getReaderContentNode(document);
  const linkNodes = dom.getAllLinkNodes(contentNode);

  const articles = Array(linkNodes.length);
  const docPromiseArr = linkNodes.map((node, i) => {
    return net.getDocumentForURLPromise(node.href).then((res) => {
      articles[i] = node; // TODO: do something with result.
      render(container, articles); // TODO: render should occur for content accessible in page, not just network request.
    }).catch((res) => {
      console.log("Unexpected error: " + res);
    });
  });
}

// TODO: Use event like AboutReaderContentReady, or
// at least wait for reader dom structure to appear.
window.setTimeout(onReaderContentReady, 1000);
