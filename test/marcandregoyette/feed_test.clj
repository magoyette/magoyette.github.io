(ns marcandregoyette.feed-test
  (:require [marcandregoyette.feed :refer :all]
            [clojure.test :refer :all]))

(def post-content
  "<html><body><p>A post about something.</p></body></html>")

(def post-date "2014-10-12T16:00:00Z")

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

(def expected-author "Marc-André Goyette")

(def expected-post-entry-content
  (str
   "&lt;html&gt;&lt;body&gt;&lt;p&gt;"
   "A post about something."
   "&lt;/p&gt;&lt;/body&gt;&lt;/html&gt;"))

(def expected-feed-xml
  (str
   "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
   "<feed xmlns=\"http://www.w3.org/2005/Atom\">"
   "<id>urn:marcandregoyette-com:feed:languages:en</id>"
   "<updated>" post-date "</updated>"
   "<title type=\"text\">Marc-André Goyette</title>"
   "<link rel=\"self\" href=\""
   "https://marcandregoyette.com/feeds/languages/en/atom.xml\"></link>"
   "<entry>"
   "<title>Iterables.concat</title>"
   "<updated>" post-date "</updated>"
   "<author><name>" expected-author "</name></author>"
   "<link href=\"https://marcandregoyette.com/en/iterables.concat\"></link>"
   "<id>urn:marcandregoyette-com:feed:languages:en:iterables.concat</id>"
   "<content type=\"html\">"
   expected-post-entry-content
   "</content>"
   "</entry>"
   "<entry>"
   "<title>Iterables.concat Part 2</title>"
   "<updated>" post-date "</updated>"
   "<author><name>" expected-author "</name></author>"
   "<link href=\"https://marcandregoyette.com/en/iterables.concat-part-2\"></link>"
   "<id>urn:marcandregoyette-com:feed:languages:en:iterables.concat-part-2</id>"
   "<content type=\"html\">"
   expected-post-entry-content
   "</content>"
   "</entry>"
   "</feed>"))

(deftest generate-feed-test
  (is (= (generate-feed "/feeds/languages/en/atom.xml" posts-by-url "en" nil)
         expected-feed-xml)))

(def posts-by-url-for-tag
  (seq
   {"/fr/iterables.concat"
    {:metadata {:date post-date
                :title "Iterables.concat"
                :lang "fr"}
     :content post-content}}))

(def expected-feed-xml-for-tag
  (str
   "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
   "<feed xmlns=\"http://www.w3.org/2005/Atom\">"
   "<id>urn:marcandregoyette-com:feed:languages:fr:tags:java</id>"
   "<updated>" post-date "</updated>"
   "<title type=\"text\">Marc-André Goyette - Java</title>"
   "<link rel=\"self\" href=\""
   "https://marcandregoyette.com/feeds/languages/fr/tags/java/atom.xml\"></link>"
   "<entry>"
   "<title>Iterables.concat</title>"
   "<updated>" post-date "</updated>"
   "<author><name>" expected-author "</name></author>"
   "<link href=\"https://marcandregoyette.com/fr/iterables.concat\"></link>"
   "<id>urn:marcandregoyette-com:feed:languages:fr:iterables.concat</id>"
   "<content type=\"html\">"
   expected-post-entry-content
   "</content>"
   "</entry>"
   "</feed>"))

(deftest generate-feed-test-with-tag
  (is (= (generate-feed "/feeds/languages/fr/tags/java/atom.xml" posts-by-url-for-tag
                        "fr" "Java")
         expected-feed-xml-for-tag)))
