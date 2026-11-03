# Clojurehackers

Source for [clojurehackers.org](https://clojurehackers.org).

The site is a static page generated with [Hiccup](https://github.com/weavejester/hiccup) from `src/clj`, plus a small ClojureScript file for the Upcoming/Past meetup tabs. `resources/public` is the web root: it holds the static assets and is where the generated `index.html`/`main.js` land.

## Running locally

Requires the [Clojure CLI](https://clojure.org/guides/install_clojure).

Build the HTML:

```sh
clojure -M:build resources/public
```

Build the ClojureScript:

```sh
clojure -M:cljs
```

Then open `resources/public/index.html` in a browser.

Meetups are listed in `resources/events.edn`.
