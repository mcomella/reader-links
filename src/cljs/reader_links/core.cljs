(ns reader-links.core
  (:require [reader-links.reader :refer [readerify]]
            [reader-links.dom :as dom]
            [clojure.string :as str]
            [reagent.core :as r]
            [ajax.core :refer [GET]]))

(enable-console-print!)

(defonce url (r/atom ""))
(defonce article (r/atom nil))
(defonce rich-links (r/atom nil))

(defn get-rich-links [_key _atom old-state new-state]
  "Creates coll of {1 <article> 2 <article> ...} where <article> may be nil"
  (let [links (dom/get-all-links (:content new-state))
        i-links (map vector (range (count links)) links)]
    (reset! rich-links (into (sorted-map)
                             (map-indexed vector (repeat (count links) nil))))
    (doseq [[i link] i-links]
      (readerify (.-href link)
                 #(when-let [prev @rich-links] ; TODO: core.async
                    (when-let [article %]
                      (reset! rich-links (assoc prev i article))))
                 #(prn %)))))
(add-watch article :rich-links get-rich-links)

(defn on-url-submit [url e]
  (.preventDefault e)
  (readerify url
             #(reset! article %)
             #(prn %))) ; TODO: err

(defn url-input []
  [:div
   [:form {:className "url-input"
           :onSubmit (partial on-url-submit @url)}
    [:input {:type "text"
             :value @url
             :placeholder "URL to readerify"
             :on-change #(reset! url (-> % .-target .-value))
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

(defn link-node-to-markup [node ctxt rich-link]
  (prn (keys rich-link))
  (let [title (.-innerText node)
        [before after] (str/split ctxt title)
        href (.-href node)]
    [:div.xyz-mcomella-link-node
     [:h3 (:title rich-link)]
     [:p
      [:em before]
      [:a {:href href} title]
      [:em after]]
     [:p (:excerpt rich-link)]]))
     ; (:uri :title :byline :dir :content :textContent :length :excerpt)

(defn links []
  (let [{:keys [content]} @article ; TODO: rich-links make this re-render a lot
        links (dom/get-all-links content)
        link-ctxt (map dom/get-containing-sentence-from-link-node links)
        links-markup-seq (map link-node-to-markup links link-ctxt (vals @rich-links))
        links-markup (into [:ul] links-markup-seq)]
    [:div
     [:div#xyz-mcomella-links
      [:h2 "Some links you may have missed..."]
       links-markup]]))

(defn app []
  [:div
   [url-input]
   (when @article
     [:div ; TODO: rm div w/ macro?
      [reader]
      [links]])])

(r/render [app]
          (.getElementById js/document "xyz-mcomella-app"))
