;; ## Code highlight
;;
;; Syntaxic highlight of code blocks.
(ns marcandregoyette.highlight
  (:require [net.cgrand.enlive-html :as enlive])
  (:import java.lang.String
           org.python.util.PythonInterpreter))

(defn- pygments-script
  [lang]
  (str "from pygments import highlight\n"
       "from pygments.lexers import get_lexer_by_name\n"
       "from pygments.formatters import HtmlFormatter\n"
       "\nlexer = get_lexer_by_name('"
       lang
       "')\n"
       "\nresult = highlight(code, lexer, HtmlFormatter(nowrap=True))"))

(def java-string-class
  (class (String. "")))

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
    (-> node
        (assoc :content (-> code
                            (highlight-with-pygments lang)
                            enlive/html-snippet))
        (assoc-in [:attrs :class] "highlight"))))

(defn highlight-code-blocks
  "Highlight the code blocks (a code element in a pre element) of the
  HTML of a page. The class highlight is added to the code element
  and the ui, segment and code classes are added to the pre element."
  [page]
  (enlive/sniptest page
                   [:pre :code] highlight-code
                   [:pre] (enlive/wrap :div {:class "ui segment code"})))
