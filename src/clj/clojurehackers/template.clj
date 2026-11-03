(ns clojurehackers.template
  (:require
   [clojurehackers.events :as events]
   [hiccup2.core :as h])
  (:gen-class))

(def ^:private external-attrs {:target "_blank" :rel "noopener noreferrer"})

(defn- head []
  [:head
   [:meta {:charset "UTF-8"}]
   [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
   [:link {:rel "preconnect" :href "https://fonts.googleapis.com"}]
   [:link {:rel "preconnect" :href "https://fonts.gstatic.com" :crossorigin true}]
   [:link {:href "https://fonts.googleapis.com/css2?family=Orbitron:wght@400;500;600;700;800;900&display=swap"
           :rel "stylesheet"}]
   [:title "Clojurehackers"]
   [:meta {:name "description"
           :content "A hackerspace where we exchange ideas related to Clojure and Functional Programming."}]
   [:link {:rel "stylesheet" :href "assets/styles.css"}]])

(defn- hero []
  [:section.hero
   [:div.container
    [:div.hero-top
     [:h1.brand "Clojurehackers"]
     [:img.logo {:src "assets/new-logo.svg" :alt "Clojurehackers logo"}]]
    [:p.tagline "a \"Hackerspace\""]
    [:p.description "we exchange ideas related to Clojure and Functional Programming."]
    [:div.meetups {:aria-labelledby "meetups-title"}
     [:h2#meetups-title "IN PERSON MEETUPS"]
     [:p.meetups-copy
      "every 2 weeks" [:br]
      "in Campinas/SP, Brazil" [:br]
      "at UNICAMP."]
     [:a.signup (merge external-attrs {:href "https://discord.gg/UTxeA9tHnd"}) "JOIN US"]]]])

(defn- values []
  [:section.values
   [:div.container.values-list
    [:p "we are thinkers"]
    [:p "we are creative"]
    [:p "we are curious"]]])

(defn- event-item [{:keys [number title date host link time]}]
  (let [label (str "#" number ": " title)]
    [:li {:data-date date}
     [:span.event-info
      [:span.event-name (if link [:a (merge external-attrs {:href link}) label] label)]
      [:span.event-host (str "hosted by " host)]]
     [:span.event-date (str (events/format-date date) (when time (str " · " time)))]]))

(defn- events-section [events-path]
  (let [all-events (events/load-events events-path)]
    [:section.events
     [:div.container
      [:h2 "Meetups"]
      [:div#tabbed-events {:hidden true}
       [:div.event-tabs {:role "tablist" :aria-label "Meetups"}
        [:button.event-tab {:id "tab-upcoming" :role "tab" :aria-selected "true"
                             :aria-controls "panel-upcoming"}
         "Upcoming"]
        [:button.event-tab {:id "tab-past" :role "tab" :aria-selected "false"
                             :aria-controls "panel-past" :tabindex "-1"}
         "Past"]]
       [:div.event-panel.is-active {:id "panel-upcoming" :role "tabpanel" :aria-labelledby "tab-upcoming"}
        [:ul.event-list {:id "upcoming-events"}]]
       [:div.event-panel {:id "panel-past" :role "tabpanel" :aria-labelledby "tab-past" :hidden true}
        [:ul.event-list {:id "past-events"}]]]
      [:ul.event-list {:id "all-events"}
       (map event-item all-events)]]]))

(defn- footer []
  [:footer
   [:div.container
    [:span "Clojurehackers"]
    [:nav.community-links {:aria-label "Community links"}
     [:a (merge external-attrs {:href "https://github.com/clojurehackers"}) "GitHub"]
     [:a (merge external-attrs {:href "https://discord.gg/UTxeA9tHnd"}) "Discord"]]
    [:span "Copyright Clojurehackers 2026"]]])

(defn page [events-path]
  (str
   (h/html
    {:mode :html}
    (h/raw "<!doctype html>")
    [:html {:lang "en"}
     (head)
     [:body
      [:div.page
       [:main
        (hero)
        (values)
        (events-section events-path)]
       (footer)]
      [:script {:src "main.js"}]]])))

(defn -main [& args]
  (let [target (or (first args) "resources/public")
        events-path "resources/events.edn"]
    (.mkdirs (java.io.File. target))
    (spit (str target "/index.html") (page events-path))))
