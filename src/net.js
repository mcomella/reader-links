function getDocumentForURLPromise(url) {
  return new Promise((resolve, reject) => {
    const req = new XMLHttpRequest();
    // TODO: report abort.
    req.addEventListener('load', (res) => {
      resolve(req.responseXML); // TODO: nuance. res codes?
    });
    req.addEventListener('error', (res) => {
      // TODO: Can we make cross-site requests for wildcards?
      console.log('error: ' + res);
      resolve(res); // TODO: proper handling.
    });

    req.open('GET', url);
    req.responseType = 'document';
    req.send();
  });
}
exports.getDocumentForURLPromise = getDocumentForURLPromise;
