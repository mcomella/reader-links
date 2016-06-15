(ns reader-links.core
  (:require [reader-links.reader :refer [readerify]]
            [reader-links.dom :as dom]
            [clojure.string :as str]
            [reagent.core :as r]
            [ajax.core :refer [GET]]))

(enable-console-print!)

(defonce article (r/atom nil))
(defonce rich-links (r/atom nil))

(defn get-rich-links [_key _atom old-state new-state]
  (reset! rich-links [])
  (doseq [link (dom/get-all-links (:content new-state))]
    (readerify (.-href link)
               #(when-let [prev @rich-links] ; TODO: core.async
                  (when-let [article %]
                    (reset! rich-links (conj prev article))))
               #(prn %))))
(add-watch article :rich-links get-rich-links)

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
    [:article
     [:h2 title]
     [:h3 byline]
     [:div {:dangerouslySetInnerHTML
            #js {:__html content}}]])) ; TODO: make safe.

(defn link-node-to-markup [node ctxt]
  (let [title (.-innerText node)
        [before after] (str/split ctxt title)
        href (.-href node)]
    [:li
     [:em before]
      [:a {:href href} title]
      [:em after]]))

(defn links []
  (let [{:keys [content]} @article
        links (dom/get-all-links content)
        link-ctxt (map dom/get-containing-sentence-from-link-node links)
        links-markup-seq (map link-node-to-markup links link-ctxt)
        links-markup (into [:ul] links-markup-seq)]
    [:div
     [:div#xyz-mcomella-links
      [:h2 "Some links you may have missed..."]
       links-markup]]))

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
