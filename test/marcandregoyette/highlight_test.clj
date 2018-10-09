(ns marcandregoyette.highlight-test
  (:require [marcandregoyette.highlight :refer :all]
            [clojure.test :refer :all]
            [hiccup.core :as hiccup]))

(def raw-clj-html
  (hiccup/html [:pre [:code.language-clojure "(+ 2 3)"]]))

(def highlighted-clj-html
  (hiccup/html [:div.ui.segment.code
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
