(ns clojurehackers.template
  (:require [hiccup2.core :as h]))

(str (h/html
      [:meta {:charset "UTF-8"}]
      [:script {:src "/main.js", :type "text/javascript"}]
      [:h1 "Clojure Hackers"]))
