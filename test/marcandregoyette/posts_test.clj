(ns marcandregoyette.posts-test
  (:require [marcandregoyette.posts :refer :all]
            [clojure.test :refer :all]
            [rum.core :as rum]))

(def an-unparsed-post-with-footnote
  {"/a-post-with-footnote"
   "---
{:title \"A post with a footnote\"
 :date \"2018-10-12T16:00:00Z\"
 :tags [\"General\"]}
---

A simple post with a footnote[^1].

[^1]: A footnote"})

(def a-post-with-footnote-html
  (str
   "<p>A simple post with a footnote"
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

(def a-parsed-post-with-footnote
  {"/a-post-with-footnote"
   (map->Post
    {:metadata
     (map->PostMetadata
      {:title "A post with a footnote"
       :date "2018-10-12T16:00:00Z"
       :tags ["General"]})
     :content a-post-with-footnote-html})})

(deftest read-post-with-footnote-test
  (is (= a-parsed-post-with-footnote
         (build-posts-from-stasis-map
          "" an-unparsed-post-with-footnote))))

(def an-unparsed-post-with-table
  {"/a-post-with-table"
   "---
{:title \"A post with a table\"
 :date \"2018-10-12T16:00:00Z\"
 :tags [\"General\"]}
---

| A column | Another column |
|----------|----------------|
| A value  | Another value  |"})

(def a-post-with-table-html
  (str
   "<table>\n"
   "<thead>\n"
   "<tr><th>A column</th><th>Another column</th></tr>\n"
   "</thead>\n"
   "<tbody>\n"
   "<tr><td>A value</td><td>Another value</td></tr>\n"
   "</tbody>\n"
   "</table>\n"))

(def a-parsed-post-with-table
  {"/a-post-with-table"
   (map->Post
    {:metadata
     (map->PostMetadata
      {:title "A post with a table"
       :date "2018-10-12T16:00:00Z"
       :tags ["General"]})
     :content a-post-with-table-html})})

(deftest read-post-with-table-test
  (is (= a-parsed-post-with-table
         (build-posts-from-stasis-map
          "" an-unparsed-post-with-table))))

(def an-unparsed-post-with-code-blocks
  {"/a-parsed-post-with-code-blocks"
   "---
{:title \"A post with code blocks\"
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

(def a-post-with-code-blocks-html
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

(def a-parsed-post-with-code-blocks
  {"/a-parsed-post-with-code-blocks"
   (map->Post
    {:metadata
     (map->PostMetadata
      {:title "A post with code blocks"
       :date "2018-10-12T16:00:00Z"
       :tags ["Java" "Clojure"]})
     :content a-post-with-code-blocks-html})})

(deftest read-post-with-code-blocks-test
  (is (= a-parsed-post-with-code-blocks
         (build-posts-from-stasis-map
          "" an-unparsed-post-with-code-blocks))))
