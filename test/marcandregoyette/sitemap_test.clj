(ns marcandregoyette.sitemap-test
  (:require [marcandregoyette.sitemap :as sitemap]
            [clojure.test :refer [deftest is]]))

(def expected-sitemap-xml
  (str
   "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
   "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\" "
   "xmlns:xhtml=\"http://www.w3.org/1999/xhtml\">"
   "<url>"
   "<loc>https://marcandregoyette.com</loc>"
   "<xhtml:link rel=\"alternate\" hreflang=\"en\" "
   "href=\"https://marcandregoyette.com/en\"></xhtml:link>"
   "<xhtml:link rel=\"alternate\" hreflang=\"fr\" "
   "href=\"https://marcandregoyette.com\"></xhtml:link>"
   "</url>"
   "<url>"
   "<loc>https://marcandregoyette.com/en/introduction/</loc>"
   "<xhtml:link rel=\"alternate\" hreflang=\"en\" "
   "href=\"https://marcandregoyette.com/en/introduction/\"></xhtml:link>"
   "<xhtml:link rel=\"alternate\" hreflang=\"fr\" "
   "href=\"https://marcandregoyette.com/fr/introduction/\"></xhtml:link>"
   "</url>"
   "<url>"
   "<loc>https://marcandregoyette.com/fr/introduction/</loc>"
   "<xhtml:link rel=\"alternate\" hreflang=\"fr\" "
   "href=\"https://marcandregoyette.com/fr/introduction/\"></xhtml:link>"
   "<xhtml:link rel=\"alternate\" hreflang=\"en\" "
   "href=\"https://marcandregoyette.com/en/introduction/\"></xhtml:link>"
   "</url>"
   "<url>"
   "<loc>https://marcandregoyette.com/en/colophon/</loc>"
   "<xhtml:link rel=\"alternate\" hreflang=\"en\" "
   "href=\"https://marcandregoyette.com/en/colophon/\"></xhtml:link>"
   "<xhtml:link rel=\"alternate\" hreflang=\"fr\" "
   "href=\"https://marcandregoyette.com/fr/colophon/\"></xhtml:link>"
   "</url>"
   "<url>"
   "<loc>https://marcandregoyette.com/fr/colophon/</loc>"
   "<xhtml:link rel=\"alternate\" hreflang=\"fr\" "
   "href=\"https://marcandregoyette.com/fr/colophon/\"></xhtml:link>"
   "<xhtml:link rel=\"alternate\" hreflang=\"en\" "
   "href=\"https://marcandregoyette.com/en/colophon/\"></xhtml:link>"
   "</url>"
   "<url>"
   "<loc>https://marcandregoyette.com/tags/java/</loc>"
   "</url>"
   "<url>"
   "<loc>https://marcandregoyette.com/tags/guava/</loc>"
   "</url>"
   "<url>"
   "<loc>https://marcandregoyette.com/tags/clojure/</loc>"
   "</url>"
   "</urlset>"))

(def articles-by-url
  (seq
   {"/en/introduction/"
    {:metadata
     {:lang "en"
      :translations [{:lang "fr" :path "/introduction/"}]}}
    "/fr/introduction/"
    {:metadata
     {:lang "fr"
      :translations [{:lang "en" :path "/introduction/"}]}}}))

(def pages-by-url
  (seq
   {"/en/colophon/"
    {:metadata
     {:lang "en"
      :translations [{:lang "fr" :path "/colophon/"}]}}
    "/fr/colophon/"
    {:metadata
     {:lang "fr"
      :translations [{:lang "en" :path "/colophon/"}]}}}))

(def tag-pages-by-url
  (seq
   {"/tags/java/" ""
    "/tags/guava/" ""
    "/tags/clojure/" ""}))

(deftest generate-sitemap-test
  (is (= (sitemap/generate-sitemap articles-by-url pages-by-url tag-pages-by-url)
         expected-sitemap-xml)))
