(ns marcandregoyette.post-layout-test
  (:require [marcandregoyette.post-layout :refer :all]
            [clojure.test :refer :all]))

(def post-layout-expected-html
  (str
   "<div class=\"ui segment\">"
   "<div class=\"ui ribbon large label\" id=\"category\"></div>"
   "<div class=\"ui top right attached large label\" id=\"post-date\"></div>"
   "<div class=\"post-content\"></div>"
   "<div id=\"tags\"></div>"
   "</div>"))

(deftest test-post-layout-html
  (is (= (get-post-layout)
         post-layout-expected-html)))
