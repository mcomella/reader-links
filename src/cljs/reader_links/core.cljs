(ns reader-links.core
  (:require [reader-links.reader :refer [readerify]]
            [reader-links.dom :as dom]
            [reagent.core :as r]
            [ajax.core :refer [GET]]))

(enable-console-print!)

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

(defn link-node-to-markup [node]
  (let [title (.-innerText node)
        href (.-href node)]
    [:div
     [:a {:href href} title]]))

(defn links []
  (let [{:keys [content]} @article
        links (dom/get-all-links content)
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
