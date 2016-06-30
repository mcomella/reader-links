function getReaderContentNode(doc) {
  return doc.getElementById('container')
      .getElementsByClassName('content')
      .item(0);
}
exports.getReaderContentNode = getReaderContentNode;

function getAllLinkNodes(node) {
  const linkHTMLColl = node.getElementsByTagName('a');
  const linkArr = Array.prototype.slice.call(linkHTMLColl);
  return linkArr.filter(val => !val.href.startsWith('#'));
}
exports.getAllLinkNodes = getAllLinkNodes;
