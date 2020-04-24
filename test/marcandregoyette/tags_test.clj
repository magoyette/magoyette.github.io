(ns marcandregoyette.tags-test
  (:require [marcandregoyette.tags :as tags]
            [clojure.test :refer [deftest is]]))

(deftest get-tag-for-url-test
  (is (= "java"
         (tags/get-tag-for-url "Java")))
  (is (= "google-guava"
         (tags/get-tag-for-url "Google Guava"))))

(deftest build-tag-url-test
  (is (= "/en/tags/java/"
         (tags/build-tag-url "Java" "en")))
  (is (= "/fr/tags/google-guava/"
         (tags/build-tag-url "Google Guava" "fr"))))
