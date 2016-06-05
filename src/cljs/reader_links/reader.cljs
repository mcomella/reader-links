(ns reader-links.reader)

; NOTE: assumes Readability.js is loaded into the DOM
; because I don't know how to load it via cljs.

(defn get-reader-uri-from-str [uri]
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

(defn readerify [uri-str html-str]
  (let [parser (js/DOMParser.)
        uri (get-reader-uri-from-str uri-str)
        document (.parseFromString parser html-str "application/xml")
        js-article (.parse (js/Readability. uri document))]
    (js->clj js-article :keywordize-keys true)))
