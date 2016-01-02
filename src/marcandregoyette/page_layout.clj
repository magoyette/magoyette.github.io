(ns marcandregoyette.page-layout
  (:require [hiccup.element :as hiccup-element]
            [hiccup.page :as hiccup-page]
            [optimus.link :as link]
            [marcandregoyette.urls :as urls]))

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
   [:link {:type "text/css"
           :rel "stylesheet"
           :href "http://fonts.googleapis.com/css?family=Lato|Open+Sans"}]
   (hiccup-page/include-css "/bundles/styles.css")])

(def menu-items-without-categories
  [{:type :icon-link
    :icon [:i.help.large.icon]
    :link "/about"
    :title "About / À propos"
    :id (urls/build-category-id "About")}
   {:type :icon-link
    :icon [:i.rss.large.icon]
    :link "http://feeds.feedburner.com/marcandregoyette"
    :title "Subscribe to the feed of this blog"}
   {:type :icon-link
    :icon [:i.code.large.icon]
    :link "/source"
    :title "Source code"}
   {:type :icon-link
    :icon [:i.linkedin.large.icon]
    :link "http://www.linkedin.com/in/marcandregoyette"
    :title "LinkedIn"}
   {:type :icon-link
    :icon [:i.github.alternate.large.icon]
    :link "https://github.com/magoyette"
    :title "GitHub"}])

(defn- category-menu-item
  [category]
  {:type :text-link
   :name category
   :link (urls/build-category-url category)
   :id (urls/build-category-id category)})

(defn- build-menu-item [item]
  (let [href {:href (:link item) :title (:title item) :id (:id item)}]
    (case (:type item)
      :text-link [:a.item href (:name item)]
      :icon-link [:a.icon.item href (:icon item)])))

(defn- menu-items []
  (concat
   (map category-menu-item urls/categories)
   menu-items-without-categories))

(defn- menu []
  [:div.ui.inverted.stackable.borderless.menu
   [:div.ui.text.container
    [:div.item
     [:div.site-title
      [:a {:href "/"} "Marc-Andr\u00E9 Goyette"]]]
    (map build-menu-item (menu-items))]])

(defn- footer []
  [:div.footer
   [:div.ui.segment.secondary
    [:div "Copyright \u00A9 " [:a {:href "/about"} "Marc-Andr\u00E9 Goyette"]
     [:div.right
      "Built with Clojure, Stasis and Semantic UI ("
      [:a {:href "/source"} "Source code"]
      ")"]]]])

(defn- post-grid []
  [:div.ui.main.text.container
   [:div#posts-container]
   [:div (footer)]])

(def google-analytics-code
  "(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];
  a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
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
  (hiccup-page/html5 head
                     [:body
                      (menu)
                      (post-grid)
                      [:div.additional-scripts]
                      (hiccup-element/javascript-tag google-analytics-code)
                      (hiccup-element/javascript-tag disqus-code)
                      [:div.disqus-config]]))