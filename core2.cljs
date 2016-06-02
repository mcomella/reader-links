(ns article-testing.core
  (:require [clojure.string :as str]
            [crate.core :as crate :refer [html]]))

(enable-console-print!)

(def context-size 40)
(def footer-id "xyz-mcomella-footer")

(defn get-all-links [doc]
  "Returns [HTMLLinkElement ...]"
  (let [html-coll (.getElementsByTagName doc "a")]
    (js->clj (.call (-> js/Array .-prototype .-slice) html-coll)
             {:kewordize-keys true})))

(defn get-context [link-elem]
  (let [text (.-text link-elem)
        parent (.-parentElement link-elem)
        all-text (.-innerText parent)
        index (str/index-of all-text text)]
    (if-not index
      text
      (let [[begin end] (split-at index all-text)]
        (str (apply str (take-last context-size begin))
             (apply str (drop-last context-size end)))))))

(defn article-node [link-elem]
  (let [href (.-href link-elem)
        text (.-text link-elem)
        context (get-context link-elem)]
    (html [:p
           [:a {:href href} (str text ":")]
           [:em (str " " context)]])))

(defn footer [links]
  (let [footer (html [:div {:id footer-id}
                      [:h2 "Some articles you may have missed..."]
                      [:hr]])]
    (doseq [elem (map article-node links)]
      (.appendChild footer elem)) ; optim: probably only want to add once
    footer))

; DOM injection
(defn remove-elem! []
  (when-let [elem (.getElementById js/document footer-id)]
    (.removeChild (.-body js/document) elem)))

(defn append-elem! []
  (.appendChild (.-body js/document)
                (footer (get-all-links js/document))))

(let [loc (.-location js/document)
      host (.-host loc)
      proto (.-protocol loc)
      pathname (.-pathname loc)
      uri #js {:spec (.-href loc)
               :host host
               :prePath (str proto "//" host)
               :scheme (.substr proto 0 (.indexOf proto ":"))
               :pathBase (str proto "//" host
                              (.substr pathname
                                       0 (+ 1 (.lastIndexOf pathname "/"))))}
      article (.parse (js/Readability. uri js/document))]
  (set! (-> js/document
            .-body
            .-innerHTML)
        (.-content article))
  (prn (.-content article)))

#_(remove-elem!) ; for reloading the extension
#_(append-elem!)
