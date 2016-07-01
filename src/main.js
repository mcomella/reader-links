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

const sentenceEndRegexStr = '\w[,.?!]\s'; // TODO: non-english
const firstSentenceEndRegex = new Regexp(sentenceEndRegexStr);
const lastSentenceEndRegex = new Regexp(sentenceEndRegexStr + '(?!.*' + sentenceEndRegexStr + ')'); // via http://stackoverflow.com/a/4313994

function getEndOfSentenceIndexHelper(regexp, str) {
  const index = str.search(firstSentenceEndRegex);
  if (index < 0) {
    return index;
  }
  return index + 2; // offset for word character & punctuation.
}

function getFirstEndOfSentenceIndex(str) {
  return getEndOfSentenceIndexHelper(firstSentenceEndRegex, str);
}

function getLastEndOfSentenceIndex(str) {
  return getEndOfSentenceIndexHelper(lastSentenceEndRegex, str);
}

function getContextForLinkNode(node) {
  const linkTitle = node.innerText;
  const parentNodeText = node.parentNode.innerText;
  const [beforeLink, afterLink] = parentNodeText.split(linkTitle); // TODO: linkTitle may appear more than once.

  // before

  // after
  let afterKeep;
  const linkTitleEndIndex = getFirstEndOfSentenceIndex(linkTitle);
  if (linkTitleEndIndex == linkTitle.length) {
    afterKeey = '';
  }
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
