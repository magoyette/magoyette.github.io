(ns marcandregoyette.highlight-test
  (:require [marcandregoyette.highlight :refer :all]
            [clojure.test :refer :all]
            [rum.core :as rum]))

(def raw-clj-html
  (rum/render-static-markup [:pre [:code.clojure "(+ 2 3)"]]))

(def highlighted-clj-html
  (rum/render-static-markup [:div.ui.segment.code
                [:pre
                 [:code.highlight
                  [:span]
                  [:span.p "("]
                  [:span.nb "+ "]
                  [:span.mi "2"]
                  " "
                  [:span.mi "3"]
                  [:span.p ")"]
                  "\n"]]]))

(deftest test-highlight-code-blocks
  (is (= highlighted-clj-html
         (highlight-code-blocks raw-clj-html))))
