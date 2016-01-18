;; ## Custom styles
;;
;; Custom styles defined with Garden.
(ns marcandregoyette.custom-styles
  (:require [clojure.string :as string]
            [garden.core :as css]
            [garden.stylesheet :as css-stylesheet]
            [garden.units :as css-units]))

(def coral "#FF7F50")
(def blue "#008EAC")
(def grey "#E8E8E8")
(def green "#03B255")
(def white "#FFFFFF")
(def dark-grey "#2E2E2E")
(def solarized-light-background "#eee8d5")
(def solarized-dark-background "#073642")

(def body
  [:body {:background-color grey
          :font-family ["Lato" "sans-serif"]
          :font-size (css-units/em 1)
          :margin (css-units/px 0)
          :padding (css-units/px 0)}])

(def inverted-menu
  [:.ui.inverted.menu {:background-color blue}])

(def menu
  [:.ui.menu
   [:.header.item {:background-color blue
                   :color white}]
   [".item > .custom-blue.label" {:background-color coral}]
   [:.item {:padding-left (css-units/em 0.8)
            :padding-right (css-units/em 0.8)}]
   [:.item.icon {:padding-left (css-units/em 0.6)
                 :padding-right (css-units/em 0.6)}]])

(def vertical-menu
  [:.ui.stackable.menu
   [".icon.item > .icon" {:margin (css-units/px 0)}]])

(def post-content
  [:.post-content {:padding-bottom "2.5%"
                   :padding-left "5%"
                   :padding-right "5%"
                   :padding-top "2.5%"}
   [:p {:font-family "'Open Sans', sans-serif"
        :font-size (css-units/em 0.8)}]])

(def footer
  [:.footer {:font-family "'Open Sans', sans-serif"
             :font-size (css-units/em 0.7)
             :padding-top (css-units/em 1)}])

(def site-title
  [:.site-title [:a {:color white
                     :font-size (css-units/px 20)
                     :font-weight "bold"
                     :text-decoration "none"}]])

(def code
  [:.ui.segment.code {:background-color solarized-light-background
                      :overflow "auto"}
   [:code {:font-family (str "Menlo, Monaco, 'DejaVu Sans Mono', Consolas, "
                             "'Courier New', monospace")
           :font-size (css-units/px 13)}]])

(def label
  [:.ui.label {:color white
               :background-color coral
               :font-weight "normal"}
   [:a {:opacity "1"
        :text-decoration "none"}]])

(def comments
  [:div.disqus-comments {:padding-top (css-units/px 10)}])

(def css-lines
  [body
   inverted-menu
   menu
   vertical-menu
   post-content
   footer
   label
   site-title
   code
   comments
   [:.main.container {:margin-top (css-units/em 2)}]
   [:.ui.large.header {:color dark-grey}]
   [:.ui.orange.label {:background-color coral}]
   [:a.post-title {:text-decoration "none"}]
   [:.right {:float "right"}]
   [(css-stylesheet/at-media {:screen :only :max-width (css-units/px 768)}
                             [:.right {:float "none"}])]])

(defn load-custom-styles
  "Load the custom CSS of the site as a string."
  []
  (string/join "\n\n" (map css/css css-lines)))
