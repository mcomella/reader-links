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

; TODO: make not awful
; TODO: can re-use?
(defn- get-start-of-sentence-ind [s]
  (let [[before after] (str/split (str/reverse s)
                                  #"\s+[!.,?]\S"
                                  2)]
    (if (empty? after) ; TODO: off-by one
      0
      (+ (count after)
        2)))) ; TODO: explain

; TODO: re-use of regex
(defn- get-end-of-sentence-ind [s]
  (let [[before-end-of-sentence _] (str/split s #"\S[!.,?]\s+" 2)]
    (+ (count before-end-of-sentence)
       2))) ; for characters in split. TODO: 2 or 3?

(defn get-containing-sentence-from-link-node [link-node]
  (let [ctxt (-> link-node .-parentNode .-innerText)
        title (.-innerText link-node)
        [before after] (str/split ctxt title)
        working-after (str title after)
        start-of-sent-ind (get-start-of-sentence-ind before)
        end-of-sent-ind (get-end-of-sentence-ind working-after)] ; TODO: may end w/ sentence
    (subs ctxt start-of-sent-ind (+ (count before) ; TODO: expl offset
                                    end-of-sent-ind))))
