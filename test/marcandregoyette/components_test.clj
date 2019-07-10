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
   "<div class=\"content\">"
   "<a href=\"/post\">"
   "<h1 class=\"title is-family-secondary\">A post</h1>"
   "</a>"
   "<p>Content</p>"
   "</div>"))

(deftest test-render-post-content
  (is (= (rum/render-static-markup
          (post-content "/post" metadata "<p>Content</p>"))
         post-content-expected-html)))

(def post-layout-expected-html
  (str
   "<article class=\"card post\">"
   "<div class=\"card-content\">"
   "<div class=\"date has-text-grey-dark has-text-right\">"
   "Written on 19 February 2019"
   "</div>"
   "<div class=\"content\">"
   "<a href=\"/a-post\"><h1 class=\"title is-family-secondary\">A post</h1></a>"
   "<p>Some content</p>"
   "</div>"
   "<p>"
   "<div class=\"tags\">"
   "<a href=\"/en/tags/java/\" class=\"tag is-medium\">Java</a> "
   "<a href=\"/en/tags/guava/\" class=\"tag is-medium\">Guava</a> "
   "</div>"
   "</p>"
   "</div>"
   "</article>"))

(deftest test-render-layout-html
  (is (= (rum/render-static-markup
          (post-layout "/a-post" metadata "<p>Some content</p>"))
         post-layout-expected-html)))
