const React = require('react');

// TODO: names.
const Link = React.createClass({
  render: function () {
    return (
      <div className="mcomella-xyz-link">
        Here we put an article: {this.props.article.innerText}
      </div>
    );
  }
});

const ArticleList = React.createClass({
  render: function () {
    const articleNodes = this.props.articles.map((article) => {
      return (
        <Link article={article}/> // TODO: pass less than article?
      );
    });

    return (
      <div className="mcomella-xyz-article-list">
        {articleNodes}
      </div>
    );
  },
});

const ArticleLinks = React.createClass({
  getInitialState: function () {
    return { articles: [] };
  },
  render: function () {
    return (
      <div className="mcomella-xyz-article-links">
        <h1>Some links you may have missed...</h1>
        <ArticleList articles={this.props.articles}/>
      </div>
    );
  },
});
exports.ArticleLinks = ArticleLinks;
