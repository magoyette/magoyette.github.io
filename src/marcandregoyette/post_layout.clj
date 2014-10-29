(ns marcandregoyette.post-layout
  (:require [hiccup.core :refer [html]]))

(defn get-post-layout []
   (html (seq [[:div#category.ui.ribbon.label]
         [:div#post-date.ui.top.right.attached.label]
         [:div.post-content]
         [:div#tags]])))
