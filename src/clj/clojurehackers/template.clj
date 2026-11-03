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
   [:link {:rel "icon" :type "image/svg+xml" :href "assets/new-logo.svg"}]
   [:link {:rel "preconnect" :href "https://fonts.googleapis.com"}]
   [:link {:rel "preconnect" :href "https://fonts.gstatic.com" :crossorigin true}]
   [:link {:href "https://fonts.googleapis.com/css2?family=Orbitron:wght@400;500;600;700;800;900&display=swap"
           :rel "stylesheet"}]
   [:title "Clojurehackers"]
   [:meta {:name "description"
           :content "Um espaço hacker para trocar conhecimento sobre Clojure e programação funcional"}]
   [:link {:rel "stylesheet" :href "assets/styles.css"}]])

(defn- hero []
  [:section.hero
   [:div.container
    [:div.hero-logo
     [:img {:src "assets/logo-with-title.svg" :alt "Logo do Clojurehackers"}]]
    [:p.description "Um " [:strong "espaço hacker"] " para trocar conhecimento sobre " [:strong "Clojure"] " e programação funcional"]
    [:div.meetups {:aria-labelledby "meetups-title"}
     [:h2#meetups-title "ENCONTROS PRESENCIAIS"]
     [:p.meetups-copy
      "a cada 2 semanas" [:br]
      "em Campinas/SP, Brasil" [:br]
      "na UNICAMP."]
     [:a.signup (merge external-attrs {:href "https://discord.gg/UTxeA9tHnd"}) "JUNTE-SE A NÓS"]]]])

(defn- event-item [{:keys [number title date host link time]}]
  (let [label (str "#" number ": " title)]
    [:li {:data-date date}
     [:span.event-info
      [:span.event-name (if link [:a (merge external-attrs {:href link}) label] label)]
      [:span.event-host host]]
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
         "Próximos"]
        [:button.event-tab {:id "tab-past" :role "tab" :aria-selected "false"
                             :aria-controls "panel-past" :tabindex "-1"}
         "Anteriores"]]
       [:div.event-panel.is-active {:id "panel-upcoming" :role "tabpanel" :aria-labelledby "tab-upcoming"}
        [:ul.event-list {:id "upcoming-events"}]]
       [:div.event-panel {:id "panel-past" :role "tabpanel" :aria-labelledby "tab-past" :hidden true}
        [:ul.event-list {:id "past-events"}]]]
      [:ul.event-list {:id "all-events"}
       (map event-item all-events)]]]))

(defn- footer []
  [:footer
   [:div.container
    [:p.footer-line
     "© 2026 Clojurehackers. Todos os direitos reservados."
     [:span.footer-divider "|"]
     [:a (merge external-attrs {:href "https://github.com/clojurehackers"}) "GitHub"]
     [:span.footer-divider "|"]
     [:a (merge external-attrs {:href "https://discord.gg/UTxeA9tHnd"}) "Discord"]]]])

(defn page [events-path]
  (str
   (h/html
    {:mode :html}
    (h/raw "<!doctype html>")
    [:html {:lang "pt-BR"}
     (head)
     [:body
      [:div.page
       [:main
        (hero)
        (events-section events-path)]
       (footer)]
      [:script {:src "main.js"}]]])))

(defn -main [& args]
  (let [target (or (first args) "resources/public")
        events-path "resources/events.edn"]
    (.mkdirs (java.io.File. target))
    (spit (str target "/index.html") (page events-path))))
