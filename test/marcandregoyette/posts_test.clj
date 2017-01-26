(ns marcandregoyette.posts-test
  (:require [marcandregoyette.categories :refer :all]
            [marcandregoyette.posts :refer :all]
            [clojure.test :refer :all]))

(def a-post
  "---
  {:title \"Iterables.concat\"
  :date \"2014-10-12T16:00:00Z\"
  :lang \"en\"
  :category \"Programming\"
  :tags [\"Java\" \"Guava\" \"Clojure\"]}
  ---

  Example of a post.
  ")

(deftest read-post-metadata-test
  (is (= {:category (->Category "Programming" "en" false)
          :date "2014-10-12T16:00:00Z"
          :lang "en"
          :tags ["Java" "Guava" "Clojure"]
          :title "Iterables.concat"}
         (read-post-metadata a-post))))
