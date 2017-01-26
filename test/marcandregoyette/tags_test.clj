(ns marcandregoyette.tags-test
  (:require [marcandregoyette.tags :refer :all]
            [clojure.test :refer :all]))

(deftest build-tag-url-test
  (is (= "/tags/java/"
         (build-tag-url "Java")))
  (is (= "/tags/google_guava/"
         (build-tag-url "Google Guava"))))
