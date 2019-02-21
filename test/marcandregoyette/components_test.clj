(ns marcandregoyette.components-test
  (:require [marcandregoyette.categories :as categories]
            [marcandregoyette.components :refer :all]
            [clojure.test :refer :all]))

(def metadata
  {:date "2019-02-19T20:05:00Z"
   :category (categories/->Category "Programming" "en" false)
   :tags ["Java" "Guava"]
   :title "A post"})

(def post-content-expected-html
  (str
   "<div class=\"post-content\">"
   "<a href=\"/post\" class=\"post-title\">"
   "<h2 class=\"ui large header\">A post</h2>"
   "</a>"
   "<p>Content</p>"
   "</div>"))

(deftest test-render-post-content
  (is (= (render-post-content "/post" metadata "<p>Content</p>")
         post-content-expected-html)))

(def post-layout-expected-html
  (str
   "<div class=\"ui segment\">"
   "<div class=\"ui ribbon large label post-category\">"
   "<a href=\"/categories/programming/\">Programming</a>"
   "</div>"
   "<div class=\"ui top right attached large label post-date\">"
   "19 February 2019"
   "</div>"
   "<div class=\"post-content\">"
   "<a href=\"/a-post\" class=\"post-title\"><h2 class=\"ui large header\">A post</h2></a>"
   "<p>Some content</p>"
   "</div>"
   "<div class=\"post-tags\">"
   "<div class=\"ui large label\"><a href=\"/tags/java/\" class=\"label\">Java</a></div>"
   "<div class=\"ui large label\"><a href=\"/tags/guava/\" class=\"label\">Guava</a></div>"
   "</div>"
   "</div>"))

(deftest test-render-layout-html
  (is (= (render-post-layout "/a-post" metadata "<p>Some content</p>")
         post-layout-expected-html)))
