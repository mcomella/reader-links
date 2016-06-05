(ns reader-links.net)

(def slurp-url "/slurp")

(defn slurp [url handler error-handler] ; TODO: err
  "Calls the handler function with an HTML document."
  (let [xhr (js/XMLHttpRequest.)
        param (js/encodeURIComponent url)
        req-url (str slurp-url "?url=" param)]
    (set! (.-onload xhr) #(this-as this
                                   (handler (.-responseXML this))))
    (.open xhr "GET" req-url)
    (set! (.-responseType xhr) "document")
    (.send xhr)
    xhr))
