(ns marcandregoyette.tags-test
  (:require [marcandregoyette.tags :refer :all]
            [clojure.test :refer :all]))

(deftest build-tag-url-test
  (is (= "/en/tags/java/"
         (build-tag-url "Java" "en")))
  (is (= "/fr/tags/google_guava/"
         (build-tag-url "Google Guava" "fr"))))
