(ns reader-links.core
  (:require [reagent.core :as r]))

(enable-console-print!)

(defn on-input-submit [e & _]
  (.preventDefault e)
  (prn _))

(defn url-input [value]
  [:div
   [:form {:className "url-input"
           :onSubmit on-input-submit}
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
