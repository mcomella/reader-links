(ns reader-links.core
  (:require [reagent.core :as r]))

(enable-console-print!)

(defonce url (r/atom ""))

(defn on-input-submit [e & _]
  (.preventDefault e)
  (prn _))

(defn url-input []
  [:div
   [:form {:className "url-input"
           :onSubmit on-input-submit}
    [:input {:type "text"
             :value @url
             :placeholder "URL to readerify"
             :on-change #(reset! url (-> % .-target .-value))
             :size 80}]
    [:input {:type "submit"
             :value "Load"}]]])

(r/render [url-input]
          (.getElementById js/document "xyz-mcomella-app"))
