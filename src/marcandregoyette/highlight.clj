(ns marcandregoyette.highlight
  (:require [clygments.core :refer [highlight]]
            [net.cgrand.enlive-html :as en]))

(defn- extract-code
  [highlighted]
  (-> highlighted
      java.io.StringReader.
      en/html-resource
      (en/select [:pre])
      first
      :content))

(defn- highlight-code [node]
  (let [code (->> node
                  :content
                  (apply str))
        lang (->> node
                  :attrs
                  :class
                  keyword)]
    (assoc node :content (-> code
                             (highlight lang :html)
                             extract-code))))

(defn highlight-code-blocks [page]
  (en/sniptest page
               [:pre :code] highlight-code
               [:pre :code] #(assoc-in % [:attrs :class] "highlight")
               [:pre] (en/wrap :div {:class "ui segment code"})))
