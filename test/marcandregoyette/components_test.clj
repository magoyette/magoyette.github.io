(ns marcandregoyette.components-test
  (:require [marcandregoyette.components :refer :all]
            [clojure.test :refer :all]
            [rum.core :as rum]))

(def metadata
  {:date "2019-02-19T20:05:00Z"
   :lang "en"
   :tags ["Java" "Guava"]
   :title "A post"})

(def post-content-expected-html
  (str
   "<div class=\"content is-family-secondary\">"
   "<a href=\"/post\">"
   "<h1 class=\"title\">A post</h1>"
   "</a>"
   "<p>Content</p>"
   "</div>"))

(deftest test-render-post-content
  (is (= (rum/render-static-markup
          (post-content "/post" metadata "<p>Content</p>"))
         post-content-expected-html)))

(def post-layout-expected-html
  (str
   "<div class=\"card post\">"
   "<div class=\"card-content\">"
   "<div class=\"has-text-grey-dark has-text-right\">"
   "Written on 19 February 2019"
   "</div>"
   "<div class=\"content is-family-secondary\">"
   "<a href=\"/a-post\"><h1 class=\"title\">A post</h1></a>"
   "<p>Some content</p>"
   "</div>"
   "<div class=\"tags\">"
   "<a href=\"/tags/java/\" class=\"tag is-medium\">Java</a>"
   "<a href=\"/tags/guava/\" class=\"tag is-medium\">Guava</a>"
   "</div>"
   "</div>"
   "</div>"))

(deftest test-render-layout-html
  (is (= (rum/render-static-markup
          (post-layout "/a-post" metadata "<p>Some content</p>"))
         post-layout-expected-html)))
