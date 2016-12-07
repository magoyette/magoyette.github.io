(ns marcandregoyette.feed-test
  (:require [marcandregoyette.feed :refer :all]
            [clojure.test :refer :all]))

;; The h2 title will be removed by feed/remove-post-title
(def post-content
  "<html><body><h2>A title</h2><p>A post about something.</p></body></html>")

(def post-title "Iterables.concat")
(def post-date "2014-10-12T16:00:00Z")

(def posts-by-url
  (seq
   {"/iterables.concat"
    {:metadata {:date post-date
                :title post-title
                :lang "en"}
     :content post-content}}))

(def expected-author
  "Marc-André Goyette")

(def expected-feed-id
  "urn:marcandregoyette-com:feed")

(def expected-feed-url
  "http://www.marcandregoyette.com/atom.xml")

(def expected-post-feed-entry-id
  "urn:marcandregoyette-com:feed:post:iterables.concat-en")

(def expected-post-feed-entry-url
  "http://www.marcandregoyette.com/iterables.concat")

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
   "<title>" post-title "</title>"
   "<updated>" post-date "</updated>"
   "<author><name>" expected-author "</name></author>"
   "<link href=\"" expected-post-feed-entry-url "\"></link>"
   "<id>" expected-post-feed-entry-id "</id>"
   "<content type=\"html\">"
   expected-post-entry-content
   "</content>"
   "</entry>"
   "</feed>"))

(deftest generate-feed-test
  (is (= (generate-feed posts-by-url)
         expected-feed-xml)))
