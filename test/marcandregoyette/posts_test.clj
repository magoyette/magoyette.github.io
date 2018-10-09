(ns marcandregoyette.posts-test
  (:require [marcandregoyette.categories :refer :all]
            [marcandregoyette.posts :refer :all]
            [clojure.test :refer :all]
            [hiccup.core :as hiccup]))

(def an-unparsed-post-without-code-blocks
  {"/a-post-without-code-blocks"
   "---
{:title \"A post without code blocks\"
 :date \"2018-10-12T16:00:00Z\"
 :category \"Programming\"
 :tags [\"General\"]}
---

A simple post without code blocks."})

(def a-post-without-code-blocks-html
  (hiccup/html [:h2.ui.large.header "A post without code blocks"]
               [:p "A simple post without code blocks."]
               "\n"))

(def a-parsed-post-without-code-blocks
  {"/posts/a-post-without-code-blocks"
   (map->Post
    {:metadata
     (map->PostMetadata
      {:title "A post without code blocks"
       :date "2018-10-12T16:00:00Z"
       :category
       (map->Category
        {:name "Programming"
         :lang "en"
         :hidden false})
       :tags ["General"]})
     :content a-post-without-code-blocks-html})})

(deftest read-post-without-code-blocks-test
  (is (= a-parsed-post-without-code-blocks
         (build-posts-from-stasis-map
          "/posts" an-unparsed-post-without-code-blocks))))


(def an-unparsed-post-with-code-blocks
  {"/a-parsed-post-with-code-blocks"
   "---
{:title \"A post with code blocks\"
 :date \"2018-10-12T16:00:00Z\"
 :category \"Programming\"
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
  (hiccup/html [:h2.ui.large.header "A post with code blocks"]
               [:p "Try Java syntax highlight."]
               "\n"
               [:div.ui.segment.code
                [:pre
                 [:code.highlight
                  [:span]
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
                  "\n"]]]
               "\n"
               [:p "Try Clojure syntax highlight."]
               "\n"
               [:div.ui.segment.code
                [:pre
                 [:code.highlight
                  [:span]
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
                  "\n"]]]
               "\n"))

(def a-parsed-post-with-code-blocks
  {"/posts/a-parsed-post-with-code-blocks"
   (map->Post
    {:metadata
     (map->PostMetadata
      {:title "A post with code blocks"
       :date "2018-10-12T16:00:00Z"
       :category
       (map->Category
        {:name "Programming"
         :lang "en"
         :hidden false})
       :tags ["Java" "Clojure"]})
     :content a-post-with-code-blocks-html})})

(deftest read-post-with-code-blocks-test
  (is (= a-parsed-post-with-code-blocks
         (build-posts-from-stasis-map
          "/posts" an-unparsed-post-with-code-blocks))))
