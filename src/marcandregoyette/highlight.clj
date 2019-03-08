;; ## Code highlight
;;
;; Syntaxic highlight of code blocks.
(ns marcandregoyette.highlight
  (:require [clojure.string :as string]
            [net.cgrand.enlive-html :as enlive])
  (:import java.io.StringReader
           java.lang.String
           org.python.util.PythonInterpreter))

(defn- extract-code
  "Extract the highlighted code from the HTML generated by Pygments."
  [highlighted-code]
  (-> highlighted-code
      StringReader.
      enlive/html-resource
      (enlive/select [:pre])
      first
      :content))

(defn- pygments-script
  [lang]
  (str "from pygments import highlight\n"
       "from pygments.lexers import get_lexer_by_name\n"
       "from pygments.formatters import HtmlFormatter\n"
       "\nlexer = get_lexer_by_name('"
       lang
       "')\n"
       "\nresult = highlight(code, lexer, HtmlFormatter())"))

(def java-string-class
  (class (java.lang.String. "")))

(defn- highlight-with-pygments
  [code lang]
  (let [script (pygments-script lang)
        interpreter (doto (PythonInterpreter.)
                      (.set "code" code)
                      (.exec script))]
    (.get interpreter "result" java-string-class)))

(defn- highlight-code
  "Highlight the code contained in a node with Pygments."
  [node]
  (let [code (->> node
                  :content
                  (apply str))
        lang (-> node
                 :attrs
                 :class)]
    (assoc node :content (-> code
                             (highlight-with-pygments lang)
                             extract-code))))

(defn highlight-code-blocks
  "Highlight the code blocks (a code element in a pre element) of the
  HTML of a page. The class highlight is added to the code element
  and the ui, segment and code classes are added to the pre element."
  [page]
  (enlive/sniptest page
                   [:pre :code] highlight-code
                   [:pre :code] #(assoc-in % [:attrs :class] "highlight")
                   [:pre] (enlive/wrap :div {:class "ui segment code"})))
