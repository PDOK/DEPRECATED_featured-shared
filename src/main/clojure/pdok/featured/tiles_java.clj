(ns pdok.featured.tiles-java
  (:require [pdok.featured.tiles :as tiles])
  (:import  [pdok.featured GeometryAttribute]))

(gen-class
  :name pdok.featured.tiles.TilesHelper
  :prefix "-"
  :methods [^{:static true} [nl [pdok.featured.GeometryAttribute] java.util.Set]])

(defn- -nl [^GeometryAttribute geometry]
  "Function to expose the Clojure 'tiles/nl' function"
       (tiles/nl geometry))
