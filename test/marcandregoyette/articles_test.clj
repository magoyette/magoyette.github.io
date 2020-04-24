(ns marcandregoyette.articles-test
  (:require [marcandregoyette.articles :as articles]
            [clojure.test :refer [deftest is]]
            [rum.core :as rum]))

(def an-unparsed-article-with-footnote
  {"/an-article-with-footnote"
   "---
{:title \"An article with a footnote\"
 :date \"2018-10-12T16:00:00Z\"
 :tags [\"General\"]}
---

An article with a footnote[^1].

[^1]: A footnote"})

(def an-article-with-footnote-html
  (str
   "<p>An article with a footnote"
   "<sup id=\"fnref-1\">"
   "<a class=\"footnote-ref\" href=\"#fn-1\">1</a>"
   "</sup>"
   ".</p>\n"
   "<div class=\"footnotes\">\n"
   "<hr />\n"
   "<ol>\n"
   "<li id=\"fn-1\">\n"
   "<p>A footnote</p>\n"
   "<a href=\"#fnref-1\" class=\"footnote-backref\">↩</a>\n"
   "</li>\n"
   "</ol>\n"
   "</div>\n"))

(def a-parsed-article-with-footnote
  {"/an-article-with-footnote"
   (articles/map->Article
    {:metadata
     (articles/map->ArticleMetadata
      {:title "An article with a footnote"
       :date "2018-10-12T16:00:00Z"
       :tags ["General"]})
     :content an-article-with-footnote-html})})

(deftest read-article-with-footnote-test
  (is (= a-parsed-article-with-footnote
         (articles/build-articles-from-stasis-map
          "" an-unparsed-article-with-footnote))))

(def an-unparsed-article-with-table
  {"/an-article-with-table"
   "---
{:title \"An article with a table\"
 :date \"2018-10-12T16:00:00Z\"
 :tags [\"General\"]}
---

| A column | Another column |
|----------|----------------|
| A value  | Another value  |"})

(def an-article-with-table-html
  (str
   "<table>\n"
   "<thead>\n"
   "<tr><th>A column</th><th>Another column</th></tr>\n"
   "</thead>\n"
   "<tbody>\n"
   "<tr><td>A value</td><td>Another value</td></tr>\n"
   "</tbody>\n"
   "</table>\n"))

(def a-parsed-article-with-table
  {"/an-article-with-table"
   (articles/map->Article
    {:metadata
     (articles/map->ArticleMetadata
      {:title "An article with a table"
       :date "2018-10-12T16:00:00Z"
       :tags ["General"]})
     :content an-article-with-table-html})})

(deftest read-article-with-table-test
  (is (= a-parsed-article-with-table
         (articles/build-articles-from-stasis-map
          "" an-unparsed-article-with-table))))

(def an-unparsed-article-with-code-blocks
  {"/a-parsed-article-with-code-blocks"
   "---
{:title \"An article with code blocks\"
 :date \"2018-10-12T16:00:00Z\"
 :tags [\"Java\" \"Clojure\"]}
---

Try Java syntax highlight.

```java
int count = 5 * 2;
```

Try Clojure syntax highlight.

```clojure
(def expr (+ 2 3))
```"})

(def an-article-with-code-blocks-html
  (str
   (rum/render-static-markup
    [:p "Try Java syntax highlight."])
   "\n"
   (rum/render-static-markup
    [:div.ui.segment.code
     [:pre
      [:code.highlight
       [:span.kt "int"]
       " "
       [:span.n "count"]
       " "
       [:span.o "="]
       " "
       [:span.mi "5"]
       " "
       [:span.o "*"]
       " "
       [:span.mi "2"]
       [:span.p ";"]
       "\n"]]])
    "\n"
    (rum/render-static-markup
     [:p "Try Clojure syntax highlight."])
    "\n"
    (rum/render-static-markup
     [:div.ui.segment.code
      [:pre
       [:code.highlight
        [:span.p "("]
        [:span.k "def "]
        [:span.nv "expr"]
        " "
        [:span.p "("]
        [:span.nb "+ "]
        [:span.mi "2"]
        " "
        [:span.mi "3"]
        [:span.p "))"]
        "\n"]]])
    "\n"))

(def a-parsed-article-with-code-blocks
  {"/a-parsed-article-with-code-blocks"
   (articles/map->Article
    {:metadata
     (articles/map->ArticleMetadata
      {:title "An article with code blocks"
       :date "2018-10-12T16:00:00Z"
       :tags ["Java" "Clojure"]})
     :content an-article-with-code-blocks-html})})

(deftest read-article-with-code-blocks-test
  (is (= a-parsed-article-with-code-blocks
         (articles/build-articles-from-stasis-map
          "" an-unparsed-article-with-code-blocks))))
