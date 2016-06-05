(ns reader-links.dom
  (:require [clojure.string :as str]))

(defn html-str-to-dummy-node [html-str]
  "Can't use dom parser because not correctly formatted. assumes called in doc"
  (let [dummy (.createElement js/document "div")]
    (set! (.-innerHTML dummy) html-str)
    dummy))

(defn html-coll->clj [html-coll]
  (let [js (.call (-> js/Array .-prototype .-slice) html-coll)]
    (js->clj js)))

(defn get-all-links [html-str] ; TODO: going to want to get context too.
  (let [node (html-str-to-dummy-node html-str)
        html-coll (.getElementsByTagName node "a")
        clj (html-coll->clj html-coll)] ; TODO: remove anchors?
    (filter (fn [node] (let [href (.-href node)]
                         (not (str/starts-with? href "#"))))
            clj)))
