(ns marcandregoyette.post-layout
  (:require [hiccup.core :as hiccup]))

(defn get-post-layout []
  (hiccup/html (seq [[:div.ui.segment
                      [:div#category.ui.ribbon.label]
                      [:div#post-date.ui.top.right.attached.label]
                      [:div.post-content]
                      [:div#tags]
                      [:div.disqus-comments]]])))