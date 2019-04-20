(ns marcandregoyette.posts-test
  (:require [marcandregoyette.posts :refer :all]
            [clojure.test :refer :all]
            [rum.core :as rum]))

(def an-unparsed-post-without-code-blocks
  {"/a-post-without-code-blocks"
   "---
{:title \"A post without code blocks\"
 :date \"2018-10-12T16:00:00Z\"
 :tags [\"General\"]}
---

A simple post without code blocks."})

(def a-post-without-code-blocks-html
  (str (rum/render-static-markup [:p "A simple post without code blocks."])
       "\n"))

(def a-parsed-post-without-code-blocks
  {"/a-post-without-code-blocks"
   (map->Post
    {:metadata
     (map->PostMetadata
      {:title "A post without code blocks"
       :date "2018-10-12T16:00:00Z"
       :tags ["General"]})
     :content a-post-without-code-blocks-html})})

(deftest read-post-without-code-blocks-test
  (is (= a-parsed-post-without-code-blocks
         (build-posts-from-stasis-map
          "" an-unparsed-post-without-code-blocks))))


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
       [:span.o ";"]
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
