(ns marcandregoyette.tags-test
  (:require [marcandregoyette.tags :refer :all]
            [clojure.test :refer :all]))

(deftest get-tag-for-url-test
  (is (= "java"
         (get-tag-for-url "Java")))
  (is (= "google-guava"
         (get-tag-for-url "Google Guava"))))

(deftest build-tag-url-test
  (is (= "/en/tags/java/"
         (build-tag-url "Java" "en")))
  (is (= "/fr/tags/google-guava/"
         (build-tag-url "Google Guava" "fr"))))
