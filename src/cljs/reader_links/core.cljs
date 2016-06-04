(ns reader-links.core
  (:require [reagent.core :as r]
            [ajax.core :refer [GET]]))

(enable-console-print!)

(def slurp-url "/slurp")

(defn on-url-submit [url e]
  (.preventDefault e)
  (let [site-contents (GET slurp-url {:params {:url url}})]
    (prn site-contents)))

(defn url-input [value]
  [:div
   [:form {:className "url-input"
           :onSubmit (partial on-url-submit @value)}
    [:input {:type "text"
             :value @value
             :placeholder "URL to readerify"
             :on-change #(reset! value (-> % .-target .-value))
             :size 80}] ; TODO: in css
    [:input {:type "submit"
             :value "Load"}]]])

(defn reader [title]
  [:div "testing"])

(defn app []
  (let [url (r/atom "")]
    [:div
     [url-input url]
     [reader]]))

(r/render [app]
          (.getElementById js/document "xyz-mcomella-app"))
