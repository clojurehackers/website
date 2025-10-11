(ns clojurehackers.template
  (:require
   [hiccup2.core :as h]))

(->> (h/html
      [:meta {:charset "UTF-8"}]
      [:script {:src "/main.js", :type "text/javascript"}]
      [:body {:style {:background-color :black, :text-align :center}}
       [:img {:src "assets/logo.jpg", :align :center}]
       [:h1  {:style {:color :red}} "ClojureHackers"]
       [:h2  {:style {:color :red}} "A HACKER SPACE ABOUT CLOJURE"]])
     str
     (spit "index.html"))
