(defproject marcandregoyette "0.20.0"
  :description "Personal website of Marc-Andr\u00E9 Goyette (https://marcandregoyette.com)."
  :url "https://marcandregoyette.com"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/data.xml "0.0.8"]
                 [org.clojure/tools.reader "1.3.2"]
                 [clygments "1.0.0"]
                 [com.vladsch.flexmark/flexmark "0.40.18"]
                 [enlive "1.1.6"]
                 [garden "1.3.6"]
                 [optimus "0.20.2"]
                 [ring "1.7.1"]
                 [rum "0.11.3" :exclusions [cljsjs/react cljsjs/react-dom sablono]]
                 [stasis "2.4.0"]]
  :ring {:handler marcandregoyette.core/app}
  :aliases {"build-semantic" ["shell" "./build-semantic"]
            "build-source-page" ["do"
                                 ["marg"
                                  "src/marcandregoyette/core.clj"
                                  "src/marcandregoyette/pages.clj"
                                  "src/marcandregoyette/posts.clj"
                                  "src/marcandregoyette/highlight.clj"
                                  "src/marcandregoyette/templates.clj"
                                  "src/marcandregoyette/components.clj"
                                  "src/marcandregoyette/categories.clj"
                                  "src/marcandregoyette/tags.clj"
                                  "src/marcandregoyette/custom_styles.clj"
                                  "src/marcandregoyette/feed.clj"
                                  "-d" "dist/source" "-f" "index.html"]]
            "start" ["do"
                     ["test"]
                     ["build-source-page"]
                     ["ring" "server"]]
            "export" ["do"
                      ["run" "-m" "marcandregoyette.core/export"]
                      ["build-source-page"]]
            "deploy" ["do"
                      ["test"]
                      ["export"]
                      ["shell" "./deploy-to-github-pages"]]}
  :profiles {:dev
             {:dependencies [[prone "1.6.1"]]
              :plugins [[lein-marginalia "0.9.1"]
                        [lein-npm "0.6.2"]
                        [lein-ring "0.12.5"]
                        [lein-shell "0.5.0"]]
              :ring {:stacktrace-middleware prone.middleware/wrap-exceptions}}}
  :npm {:dependencies [[semantic-ui "2.4.2"]]})
