(ns reader-links.core
  (:require [reader-links.reader :refer [readerify]]
            [reader-links.dom :as dom]
            [clojure.string :as str]
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

(defn link-node-to-markup [node ctxt]
  (let [title (.-innerText node)
        [before after] (str/split ctxt title)
        href (.-href node)]
    [:li
     [:em before]
      [:a {:href href} title]
      [:em after]]))

(defonce ldev (atom nil))

; TODO: make not awful
; TODO: can re-use?
(defn get-start-of-sentence-ind [s]
  (let [[before after] (str/split (str/reverse s)
                                  #"\s+[!.,?]\S"
                                  2)]
    (if (empty? after) ; TODO: off-by one
      0
      (+ (count after)
        2)))) ; TODO: explain

; TODO: re-use of regex
(defn get-end-of-sentence-ind [s]
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

(defn links []
  (let [{:keys [content]} @article
        links (dom/get-all-links content)
        link-ctxt (map get-containing-sentence-from-link-node links)
        links-markup-seq (map link-node-to-markup links link-ctxt)
        links-markup (into [:ul] links-markup-seq)]
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
