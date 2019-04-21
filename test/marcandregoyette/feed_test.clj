(ns marcandregoyette.feed-test
  (:require [marcandregoyette.feed :refer :all]
            [clojure.test :refer :all]))

(def post-content
  "<html><body><p>A post about something.</p></body></html>")

(def post-date "2014-10-12T16:00:00Z")

;; The same post in French and in English
(def posts-by-url
  (seq
   {"/en/iterables.concat"
    {:metadata {:date post-date
                :title "Iterables.concat"
                :lang "en"}
     :content post-content}
    "/en/iterables.concat-part-2"
    {:metadata {:date post-date
                :title "Iterables.concat Part 2"
                :lang "en"}
     :content post-content}}))

(def expected-author
  "Marc-André Goyette")

(def expected-feed-id
  "urn:marcandregoyette-com:feed")

(def expected-feed-url
  "https://marcandregoyette.com/feeds/languages/en/atom.xml")

(def expected-post-entry-content
  (str
   "&lt;html&gt;&lt;body&gt;&lt;p&gt;"
   "A post about something."
   "&lt;/p&gt;&lt;/body&gt;&lt;/html&gt;"))

(def expected-feed-xml
  (str
   "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
   "<feed xmlns=\"http://www.w3.org/2005/Atom\">"
   "<id>" expected-feed-id "</id>"
   "<updated>" post-date "</updated>"
   "<title type=\"text\">" expected-author "</title>"
   "<link rel=\"self\" href=\"" expected-feed-url "\"></link>"
   "<entry>"
   "<title>Iterables.concat</title>"
   "<updated>" post-date "</updated>"
   "<author><name>" expected-author "</name></author>"
   "<link href=\"https://marcandregoyette.com/en/iterables.concat\"></link>"
   "<id>urn:marcandregoyette-com:feed:post:en:iterables.concat</id>"
   "<content type=\"html\">"
   expected-post-entry-content
   "</content>"
   "</entry>"
   "<entry>"
   "<title>Iterables.concat Part 2</title>"
   "<updated>" post-date "</updated>"
   "<author><name>" expected-author "</name></author>"
   "<link href=\"https://marcandregoyette.com/en/iterables.concat-part-2\"></link>"
   "<id>urn:marcandregoyette-com:feed:post:en:iterables.concat-part-2</id>"
   "<content type=\"html\">"
   expected-post-entry-content
   "</content>"
   "</entry>"
   "</feed>"))

(deftest generate-feed-test
  (is (= (generate-feed "/feeds/languages/en/atom.xml" posts-by-url)
         expected-feed-xml)))
