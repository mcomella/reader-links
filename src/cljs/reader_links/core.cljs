(ns reader-links.core
  (:require [reader-links.reader :refer [readerify]]
            [clojure.string :as str]
            [reagent.core :as r]
            [ajax.core :refer [GET]]))

(enable-console-print!)

(def slurp-url "/slurp")

(defonce article (r/atom nil))

(defn on-url-submit [url e]
  (.preventDefault e)
  (readerify url
             #(reset! article %)
             #(prn %))) ; TODO: err

(defn url-input [value]
  [:div
   [:form {:className "url-input"
           :onSubmit (partial on-url-submit @value)} ; TODO: clears text
    [:input {:type "text"
             :value @value
             :placeholder "URL to readerify"
             :on-change #(reset! value (-> % .-target .-value))
             :size 80}] ; TODO: in css
    [:input {:type "submit"
             :value "Load"}]]])

(defn reader []
  (let [{:keys [title content byline]} @article]
    [:div
     [:h2 title]
     [:h3 byline]
     [:div {:dangerouslySetInnerHTML
            #js {:__html content}}]])) ; TODO: make safe.

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

(defn link-node-to-markup [node]
  (let [title (.-innerText node)
        href (.-href node)]
    [:div
     [:a {:href href} title]]))

(defn links []
  (let [{:keys [content]} @article
        links (get-all-links content)
        links-markup-seq (map link-node-to-markup links)
        links-markup (into [:div] links-markup-seq)]
    [:div
     [:hr]
     [:h2 "Some links you may have missed..."]
      links-markup]))

(defn app []
  (let [url (r/atom "")]
    [:div
     [url-input url]
     (when @article
       [:div ; TODO: rm div w/ macro?
        [reader]
        [links]])]))

(r/render [app]
          (.getElementById js/document "xyz-mcomella-app"))
