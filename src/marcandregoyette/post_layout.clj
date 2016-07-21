(ns marcandregoyette.post-layout
  (:require [hiccup.core :as hiccup]))

(defn get-post-layout []
  (hiccup/html (seq [[:div.ui.segment
                      [:div#category.ui.ribbon.large.label]
                      [:div#post-date.ui.top.right.attached.large.label]
                      [:div.post-content]
                      [:div#tags]]])))
