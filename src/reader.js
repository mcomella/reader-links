const ArticleLinks = require('./article_links.js').ArticleLinks;
const React = require('react');
const ReactDOM = require('react-dom');

const container = document.createElement('div');
document.body.appendChild(container);
ReactDOM.render(
  <ArticleLinks/>,
  container
);
