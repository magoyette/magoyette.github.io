(ns marcandregoyette.highlight-test
  (:require [marcandregoyette.highlight :refer :all]
            [hiccup.core :as hiccup]
            [midje.sweet :refer :all]))

(def raw-clj-html
  (hiccup/html [:pre [:code.clj "(+ 2 3)"]]))

(def highlighted-clj-html
  (hiccup/html [:div.ui.segment.code
                [:pre
                 [:code.highlight
                  [:span.p "("]
                  [:span.nb "+ "]
                  [:span.mi "2"]
                  " "
                  [:span.mi "3"]
                  [:span.p ")"]
                  "\n"]]]))

(fact "The html of the Clojure code should be highlighted
       and wrapped in a Semantic UI segment that has the class code"
      (highlight-code-blocks raw-clj-html) => highlighted-clj-html)
