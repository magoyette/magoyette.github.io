(ns marcandregoyette.post-layout
  (:require [hiccup.core :as hiccup]))

(defn get-post-layout []
  (hiccup/html (seq [[:div.ui.segment
                      [:div.ui.ribbon.large.label.post-category]
                      [:div.ui.top.right.attached.large.label.post-date]
                      [:div.post-content]
                      [:div.post-tags]]])))
