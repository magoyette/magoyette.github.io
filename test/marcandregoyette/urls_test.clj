(ns marcandregoyette.urls_test
  (:require [marcandregoyette.urls :refer :all]
            [midje.sweet :refer :all]))

(fact "build-category-url generates a valid category url"
      (build-category-url "Programming") => "/categories/programming/")

(fact "build-tag-url generates a valid tag url"
      (build-tag-url "Java") => "/tags/java/"
      (build-tag-url "Google Guava") => "/tags/google_guava/")
