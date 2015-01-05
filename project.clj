(defproject marcandregoyette "0.1.0-SNAPSHOT"
  :description "Personal website of Marc-Andr\u00E9 Goyette (http://marcandregoyette.com)."
  :url "http://www.marcandregoyette.com"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0-alpha4"]
                 [org.clojure/data.xml "0.0.8"]
                 [clj-time "0.9.0"]
                 [clygments "0.1.1"]
                 [enlive "1.1.5"]
                 [garden "1.2.5"]
                 [hiccup "1.0.5"]
                 [me.raynes/cegdown "0.1.1"]
                 [me.raynes/fs "1.4.6"]
                 [ring "1.3.2"]
                 [stasis "2.2.2"]]
  :ring {:handler marcandregoyette.core/app}
  :aliases {"export"
            ["do"
             ["run" "-m" "marcandregoyette.core/export"]
             ["marg" "-d" "dist/source" "-f" "index.html"]]}
  :profiles {:dev
             {:dependencies [[midje "1.6.3"]]
              :plugins [[lein-marginalia "0.8.0"]
                        [lein-ring "0.8.13"]]}})
