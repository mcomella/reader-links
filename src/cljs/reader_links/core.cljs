(ns reader-links.core
  (:require [reagent.core :as r]))

(enable-console-print!)

(defn component []
  [:div
   [:p "Hello world"]])

(r/render [component]
          (.getElementById js/document "xyz-mcomella-app"))
