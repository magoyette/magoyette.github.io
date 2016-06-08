(ns marcandregoyette.urls_test
  (:require [marcandregoyette.urls :refer :all]
            [clojure.test :refer :all]))

(deftest build-category-url-test
  (is (= (build-category-url "Programming")
         "/categories/programming/")))

(deftest build-tag-url-test
  (is (= (build-tag-url "Java")
         "/tags/java/"))
  (is (= (build-tag-url "Google Guava")
         "/tags/google_guava/")))
