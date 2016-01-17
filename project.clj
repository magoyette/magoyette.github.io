(defproject marcandregoyette "0.6.0"
  :description "Personal website of Marc-Andr\u00E9 Goyette (http://marcandregoyette.com)."
  :url "http://www.marcandregoyette.com"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0-RC5"]
                 [org.clojure/data.xml "0.0.8"]
                 [org.clojure/tools.namespace "0.2.11"]
                 [clj-time "0.11.0"]
                 [clygments "0.1.1"]
                 [enlive "1.1.6"]
                 [garden "1.3.0" :exclusions [org.clojure/tools.nrepl]]
                 [hiccup "1.0.5"]
                 [me.raynes/cegdown "0.1.1"]
                 [me.raynes/fs "1.4.6"]
                 [optimus "0.18.3"]
                 [ring "1.4.0"]
                 [stasis "2.3.0"]]
  :ring {:handler marcandregoyette.core/app}
  :aliases {"export"
            ["do"
             ["run" "-m" "marcandregoyette.core/export"]
             ["marg"
              "src/marcandregoyette/core.clj"
              "src/marcandregoyette/pages.clj"
              "src/marcandregoyette/posts.clj"
              "src/marcandregoyette/highlight.clj"
              "src/marcandregoyette/templates.clj"
              "src/marcandregoyette/post_layout.clj"
              "src/marcandregoyette/page_layout.clj"
              "src/marcandregoyette/urls.clj"
              "src/marcandregoyette/custom_styles.clj"
              "src/marcandregoyette/feed.clj"
              "-d" "dist/source" "-f" "index.html"]]}
  :profiles {:dev
             {:dependencies [[midje "1.8.3"]
                             [prone "0.8.3"]]
              :plugins [[michaelblume/lein-marginalia "0.9.0"]
                        [lein-midje "3.1.3"]
                        [lein-ring "0.9.7" :exclusions [org.clojure/clojure]]]
              :ring {:stacktrace-middleware prone.middleware/wrap-exceptions}}})
