(ns pdok.featured.feature
    (:refer-clojure :exclude [type])
    (:require [clojure.string :as str]
      [clojure.core.cache :as cache]
      [clojure.java.io :as io]
      [clojure.tools.logging :as log])
    (:import [pdok.featured NilAttribute GMLParser]
      [pdok.featured.xslt TransformXSLT]
      [pdok.featured.converters Transformer]
      [com.vividsolutions.jts.geom Geometry]
      [pdok.featured GeometryAttribute]
      [com.vividsolutions.jts.io WKTWriter WKTReader]
      (org.joda.time LocalDateTime)))

(def lower-case
  (fnil str/lower-case ""))

(defn nilled [clazz]
      (NilAttribute. clazz))

(def xslt-simple-gml (io/resource "pdok/featured/xslt/imgeo2simple-gml.xsl"))

(def simple-gml-transfomer (TransformXSLT. (io/input-stream xslt-simple-gml)))

(defn gml3-as-jts [gml]
  (GMLParser/parse ^String gml))

(def wkt-writer (WKTWriter.))
(def wkt-reader (WKTReader.))

(defn jts-as-wkt [jts]
      (if-not (nil? jts)
              (.write ^WKTWriter wkt-writer jts)))

(defn- geometry-attribute-dispatcher [^GeometryAttribute obj]
  (when obj (-> obj .getType lower-case)))

(defmulti valid-geometry? (fn [^GeometryAttribute obj]
                            (geometry-attribute-dispatcher obj)))

(defmethod valid-geometry? :default [_] nil)

(defmethod valid-geometry? "gml" [^GeometryAttribute obj]
  (if (.getGeometry obj) true false))

(defmethod valid-geometry? "wkt" [^GeometryAttribute obj]
  (if (.getGeometry obj) true false))

(defmethod valid-geometry? "jts" [^GeometryAttribute obj]
  (if (.getGeometry obj) true false))


(defmulti as-gml (fn [^GeometryAttribute obj]
                   (geometry-attribute-dispatcher obj)))

(defmethod as-gml :default [obj] nil)

(defmethod as-gml "gml" [^GeometryAttribute obj]
  (when-let [gml (.getGeometry obj)]
    (str/trim (reduce str (str/split-lines (str/trim-newline (str/replace gml #"<\?[^\?]*\?>" "")))))))



(defmulti as-jts (fn [^GeometryAttribute obj]
                   (geometry-attribute-dispatcher obj)))

(defmethod as-jts :default [_] nil)

(def gml->jts-cache (atom (cache/lu-cache-factory {} :threshold 30000)))

(defmethod as-jts "gml" [^GeometryAttribute obj]
           (when-let [gml (.getGeometry obj)]
                     (if (cache/has? @gml->jts-cache gml)
                       (cache/lookup @gml->jts-cache gml)
                       (let [jts (gml3-as-jts gml)
                             _ (swap! gml->jts-cache #(cache/miss % gml jts))]
                            jts))))

(defmethod as-jts "jts" [^GeometryAttribute obj]
           (.getGeometry obj))

(defmethod as-jts "wkt" [^GeometryAttribute obj]
           (let [jts (.read ^WKTReader wkt-reader ^String (.getGeometry obj))]
                (when-let [srid (.getSrid obj)]
                          ; srid should be an integer, but it might be a string
                          (doto jts (.setSRID (if (string? srid) (Integer/parseInt srid) srid))))
                jts))

(defn as-rd [^Geometry geometry]
      (when geometry
            (if (= 28992 (.getSRID geometry))
              geometry
              (.transform Transformer/ETRS89ToRD geometry))))

(defn as-etrs89 [^Geometry geometry]
      (when geometry
            (if (= 4258 (.getSRID geometry))
              geometry
              (.transform Transformer/RDToETRS89 geometry))))


(defmulti as-simple-gml (fn [^GeometryAttribute obj]
                          (geometry-attribute-dispatcher obj)))
(defmethod as-simple-gml "gml" [^GeometryAttribute obj]
           (when-let [gml (as-gml obj)]
                     (.transform ^TransformXSLT simple-gml-transfomer gml)))
(defmethod as-simple-gml :default [_] nil)

(defmulti as-wkt (fn [^GeometryAttribute obj]
                   (geometry-attribute-dispatcher obj)))
(defmethod as-wkt "gml" [^GeometryAttribute obj]
           (let [jts (as-jts obj)
                 wkt (jts-as-wkt jts)]
                wkt))
(defmethod as-wkt :default [_] nil)


(defn- geometry-group*
  ([point-types line-types test-value]
   (geometry-group* point-types line-types test-value get))
  ([point-types line-types test-value predicate]
   (condp predicate test-value
     point-types :point
     line-types :line
     :polygon)))

;; http://schemas.opengis.net/gml/3.1.1/base/geometryPrimitives.xsd

(def gml-point-types
  #{"Point" "MultiPoint"})

;; TODO alles toevoegen, of starts with
(def gml-line-types
  #{"Curve" "CompositeCurve" "Arc" "ArcString" "Circle" "LineString"})

(def jts-point-types
  "Geometry types of Point-category"
  #{"Point" "MultiPoint"})

(def jts-line-types
  "Geometry types of Line-category"
  #{"Line" "LineString" "MultiLine"})

(defmulti geometry-group
          "returns :point, :line or :polygon"
          (fn [^GeometryAttribute obj] (geometry-attribute-dispatcher obj)))

(defmethod geometry-group :default [_] nil)


(defmethod geometry-group "gml" [^GeometryAttribute obj]
           (when-let [gml-str (.getGeometry obj)]
                     (let [re-result (re-find #"^(<\?[^\?]*\?>)?<([a-zA-Z0-9]+:)?([^\s]+)" gml-str)
                           type (when re-result (nth re-result 3))]
                          (geometry-group* gml-point-types gml-line-types type))))

(defmethod geometry-group "jts" [^GeometryAttribute obj]
           (let [type (.getGeometryType ^Geometry (.getGeometry obj))]
                (geometry-group* jts-point-types jts-line-types type)))

(defmethod geometry-group "wkt" [^GeometryAttribute obj]
           (let [jts (as-jts obj)
                 type (.getGeometryType ^Geometry jts)]
                (geometry-group* jts-point-types jts-line-types type)))

(defmulti as-stufgeo-field clojure.core/type)
(defmethod as-stufgeo-field LocalDateTime [tijdstipregistratie]
           (.toString tijdstipregistratie "yyyyMMddHHmmss"))
(defmethod as-stufgeo-field :default [_] nil)

(defn- map-replace [content & replacements]
       (let [replacement-list (partition 2 replacements)]
            (reduce #(apply str/replace %1 %2) content replacement-list)))

(defmulti as-stufgeo-gml (fn [^GeometryAttribute obj]
                           (geometry-attribute-dispatcher obj)))
(defmethod as-stufgeo-gml "gml" [^GeometryAttribute obj]
           (when (.getGeometry obj)
                 (map-replace (as-gml obj) #"(<gml:Curve .+</gml:Curve>)" "<imgeo:lijn>$1</imgeo:lijn>"
                              #"(<gml:LineString .+</gml:LineString>)" "<imgeo:lijn>$1</imgeo:lijn>"
                              #"(<gml:Surface .+</gml:Surface>)" "<imgeo:vlak>$1</imgeo:vlak>"
                              #"(<gml:MultiSurface .+</gml:MultiSurface>)" "<imgeo:multiVlak>$1</imgeo:multiVlak>"
                              #"(<gml:Point .+</gml:Point>)" "<imgeo:punt>$1</imgeo:punt>"
                              #"(<gml:MultiPoint .+</gml:MultiPoint>)" "<imgeo:multiPunt>$1</imgeo:multiPunt>"
                              #"<gml:Polygon (.+?)>(.+)</gml:Polygon>"
                              "<imgeo:vlak><gml:Surface $1><gml:patches><gml:PolygonPatch>$2</gml:PolygonPatch></gml:patches></gml:Surface></imgeo:vlak>")))
(defmethod as-stufgeo-gml :default [_] nil)

; The StUF-Geo XSD contains a bug in the "Overig bouwwerk" BGT type. While all other types use camelCase field names in
; the GML part, Overig bouwwerk uses lowercase. As a workaround, this is a separate Mustache function for Overig
; bouwwerk that adds the lowercase bug after applying the regular GML transformation.
(defmulti as-stufgeo-gml-lc (fn [^GeometryAttribute obj]
                              (geometry-attribute-dispatcher obj)))
(defmethod as-stufgeo-gml-lc "gml" [^GeometryAttribute obj]
           (when (.getGeometry obj)
                 (map-replace (as-stufgeo-gml obj) #"(<imgeo:multiVlak>.+</imgeo:multiVlak>)" "<imgeo:multivlak>$1</imgeo:multivlak>"
                              #"(<imgeo:multiPunt>.+</imgeo:multiPunt>)" "<imgeo:multipunt>$1</imgeo:multipunt>")))
(defmethod as-stufgeo-gml-lc :default [_] nil)
