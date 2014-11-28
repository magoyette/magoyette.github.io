(ns marcandregoyette.page-layout
  (:require [hiccup.element :refer [javascript-tag]]
            [hiccup.page :refer [html5 include-css include-js]]))

(def css-files
  ["button" "grid" "header" "icon" "label" "list" "menu" 
   "monokai" "popup" "segment" "solarized-light" "custom-styles"])

(defn- css-paths []
  (map #(str "/styles/" % ".css") css-files))

(def js-files
  ["jquery" "popup" "main"])

(defn- js-paths []
  (map #(str "/scripts/" % ".js") js-files))

(defn- include-meta []
  (seq [[:meta {:charset "utf-8"}]
        [:meta {:http-equiv "X-UA-Compatible"
                :content "IE=edge,chrome=1"}]
        [:meta {:name "viewport"
                :content "width=device-width, initial-scale=1.0"}]
        [:meta {:name "author"
                :content "Marc-Andr\u00E9 Goyette"}]]))

(def head
  [:head
   [:title]
   (include-meta)
   (apply include-css (css-paths))
   (apply include-js (js-paths))
   (include-css "http://fonts.googleapis.com/css?family=Lato")])

(def menu-items
  [{:name "Programming" :link "/categories/programming"}
   {:name "Programmation" :link "/categories/programmation"}
   {:name "About" :link "/about"}
   {:name "À propos" :link "/apropos"}
   {:icon [:i.rss.large.icon {:data-content "Atom feed"}] :link "/atom.xml"}
   {:icon [:i.linkedin.large.icon {:data-content "LinkedIn"}]
    :link "http://www.linkedin.com/in/marcandregoyette"}
   {:icon [:i.github.large.icon {:data-content "GitHub"}]
    :link "https://github.com/magoyette"}])

(defn- build-menu-item [active-item current-item]
  [(if (= active-item current-item) :a.active.item :a.item)
   {:href (:link current-item)}
   (if (contains? current-item :name)
     (:name current-item)
     (:icon current-item))])

(defn- menu []
  [:nav.ui.inverted.menu.navbar.page.grid
   [:div.page-container
    [:div.item
     [:div.site-title
      [:a {:href "/"} "Marc-Andr\u00E9 Goyette"]]]
    (map (partial build-menu-item (first menu-items)) menu-items)]])

(defn- footer []
  [:div.footer.ui.segment.secondary
   [:div "Copyright \u00A9 Marc-Andr\u00E9 Goyette"
    [:div.right
     "Built with Clojure, Stasis and Semantic UI ("
     [:a {:href "/source"} "Source"]
     ")"]]])

(defn- post-grid []
  [:div.ui.page.stackable.grid
   [:div.one.wide.column]
   [:div.fourteen.wide.column
    [:div#posts-container]
    (footer)]
   [:div.one.wide.column]])

(def google-analytics-code
  "(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
    (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
    m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
    })(window,document,'script','//www.google-analytics.com/analytics.js','ga');
    ga('create', 'UA-46742437-1', 'auto');
    ga('send', 'pageview');")

(def disqus-code
  "var disqus_shortname = 'marcandregoyette';
  (function() {
  var dsq = document.createElement('script');
  dsq.type = 'text/javascript';
  dsq.async = true;
  dsq.src = '//' + disqus_shortname + '.disqus.com/embed.js';
  (document.getElementsByTagName('head')[0]
  || document.getElementsByTagName('body')[0]).appendChild(dsq);})();
  (function () {
  var s = document.createElement('script'); s.async = true;
  s.type = 'text/javascript';
  s.src = 'http://' + disqus_shortname + '.disqus.com/count.js';
  (document.getElementsByTagName('HEAD')[0]
  || document.getElementsByTagName('BODY')[0]).appendChild(s);}());")

(defn get-page-layout []
  (html5 head
         [:body
          (menu)
          [:div.ui.hidden.divider]
          (post-grid)
          [:div.additional-scripts]
          (javascript-tag "$('.large.icon').popup();")
          (javascript-tag google-analytics-code)
          (javascript-tag disqus-code)
          [:div.disqus-config]]))

