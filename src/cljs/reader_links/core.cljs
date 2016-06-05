(ns reader-links.core
  (:require [reader-links.reader :refer [readerify]]
            [reagent.core :as r]
            [ajax.core :refer [GET]]))

(enable-console-print!)

(def slurp-url "/slurp")

(defonce article (r/atom {}))

(defn err [& _] (prn "req failed " _)) ; TODO

(defn on-url-submit [url e]
  (.preventDefault e)
  (GET slurp-url {:params {:url url}
                  :handler #(reset! article (readerify url %))
                  :error-handler err}))

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

(defn reader []
  (when @article
    (let [{:keys [title content byline]} @article]
      [:div
       [:h2 title]
       [:h3 byline]
       [:div {:dangerouslySetInnerHTML
              #js {:__html content}}]]))) ; TODO: make safe.

(defn app []
  (let [url (r/atom "")]
    [:div
     [url-input url]
     [reader]]))

(r/render [app]
          (.getElementById js/document "xyz-mcomella-app"))
