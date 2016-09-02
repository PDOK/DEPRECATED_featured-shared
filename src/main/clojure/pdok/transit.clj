(ns pdok.transit
  (:require [cognitect.transit :as transit])
  (:import (pdok.featured HandlerMaps)
           (java.io ByteArrayOutputStream ByteArrayInputStream)))

(def transit-readers (transit/read-handler-map HandlerMaps/readers))
(def transit-writers (transit/write-handler-map HandlerMaps/writers))

(defn to-json [obj]
  (let [out (ByteArrayOutputStream. 1024)
        writer (transit/writer out :json
                               {:handlers transit-writers})]
    (transit/write writer obj)
    (.toString out))
  )

(defn from-json [str]
  (if (clojure.string/blank? str)
    nil
    (let [in (ByteArrayInputStream. (.getBytes ^String str))
          reader (transit/reader in :json
                                 {:handlers transit-readers})]
      (transit/read reader))))