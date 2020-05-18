(ns marcandregoyette.components-test
  (:require [marcandregoyette.components :as components]
            [clojure.test :refer [deftest is]]
            [rum.core :as rum]))

(def metadata
  {:date "2019-02-19T20:05:00Z"
   :lang "en"
   :tags ["Java" "Guava"]
   :title "An article"})

(def article-content-expected-html
  (str
   "<div class=\"content\">"
   "<a href=\"/article\">"
   "<h1 class=\"title is-family-secondary\">An article</h1>"
   "</a>"
   "<p>Content</p>"
   "</div>"))

(deftest test-render-article-content
  (is (= (rum/render-static-markup
          (components/article-content "/article" metadata "<p>Content</p>"))
         article-content-expected-html)))

(def article-layout-expected-html
  (str
   "<article class=\"card\">"
   "<div class=\"card-content\">"
   "<div class=\"date has-text-grey-dark has-text-right\">"
   "Written on 19 February 2019"
   "</div>"
   "<div class=\"content\">"
   "<a href=\"/an-article\"><h1 class=\"title is-family-secondary\">An article</h1></a>"
   "<p>Some content</p>"
   "</div>"
   "<div class=\"article-footer\">"
   "<p><a href=\"/en/articles\">All articles</a></p>"
   "<p><a href=\"/en/tags/java/\">Articles with the tag Java</a></p>"
   "<p><a href=\"/en/tags/guava/\">Articles with the tag Guava</a></p>"
   "</div>"
   "</div>"
   "</article>"))

(deftest test-render-layout-html
  (is (= (rum/render-static-markup
          (components/article-layout "/an-article" metadata "<p>Some content</p>"))
         article-layout-expected-html)))
