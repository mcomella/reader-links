(ns reader-links.reader
  (:require [reader-links.net :as net]))

; NOTE: assumes Readability.js is loaded into the DOM
; because I don't know how to load it via cljs.

(defn- get-reader-uri-from-url [uri]
  (let [loc (js/URL. uri)
        host (.-host loc)
        proto (.-protocol loc)
        pathname (.-pathname loc)]
    #js {:spec (.-href loc)
         :host host
         :prePath (str proto "//" host)
         :scheme (.substr proto 0 (.indexOf proto ":"))
         :pathBase (str proto "//" host
                        (.substr pathname
                                 0 (+ 1 (.lastIndexOf pathname "/"))))}))

(defn- doc-to-article [handler err-handler url doc]
  (let [reader-uri (get-reader-uri-from-url url)
        js-article (.parse (js/Readability. reader-uri doc))
        article (js->clj js-article :keywordize-keys true)]
    (handler article)))

; TODO: make it take a uri and use net.
(defn readerify [url handler err-handler]
  (net/slurp url
             (partial doc-to-article handler err-handler url)
             #(prn %))) ; TODO: error
