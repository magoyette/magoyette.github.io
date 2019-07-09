(defproject marcandregoyette "0.31.0"
  :description "Personal website of Marc-Andr\u00E9 Goyette (https://marcandregoyette.com)."
  :url "https://marcandregoyette.com"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/data.xml "0.0.8"]
                 [org.clojure/tools.logging "0.4.1"]
                 [org.clojure/tools.reader "1.3.2"]
                 [ch.qos.logback/logback-classic "1.2.3"]
                 [clj-commons/fs "1.5.0"]
                 [com.vladsch.flexmark/flexmark "0.50.16"]
                 [com.vladsch.flexmark/flexmark-ext-footnotes "0.50.16"]
                 [com.vladsch.flexmark/flexmark-ext-tables "0.50.16"]
                 [enlive "1.1.6"]
                 [org.python/jython-standalone "2.7.1"]
                 [org.pygments/pygments "2.4.2"]
                 [ring "1.7.1"]
                 [rum "0.11.3" :exclusions [cljsjs/react cljsjs/react-dom sablono]]
                 [stasis "2.4.0"]
                 [tongue "0.2.6"]]
  :ring {:handler marcandregoyette.core/app}
  :jvm-opts ["-Dclojure.spec.check-asserts=true"]
  :aliases {"start" ["do"
                     ["test"]
                     ["ring" "server"]]
            "export" ["do"
                      ["run" "-m" "marcandregoyette.core/export"]]
            "deploy" ["do"
                      ["test"]
                      ["export"]
                      ["shell" "./deploy-to-github-pages"]]}
  :profiles {:dev
             {:dependencies [[prone "1.6.4"]]
              :plugins [[lein-ring "0.12.5"]
                        [lein-shell "0.5.0"]]
              :ring {:stacktrace-middleware prone.middleware/wrap-exceptions}}})
