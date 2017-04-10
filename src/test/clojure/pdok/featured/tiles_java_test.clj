(ns pdok.featured.tiles-java-test
  (:require [clojure.test :refer :all]
            [pdok.featured.feature :refer :all]
            [clojure.java.io :refer :all])
  (:import
    [pdok.featured.tiles TilesHelper]
    [pdok.featured GeometryAttribute]))


(deftest test-tiles-helper
         (let [ga (GeometryAttribute. "gml"
                                      "<gml:Point xmlns:gml=\"http://www.opengis.net/gml/3.2\" srsName=\"urn:ogc:def:crs:EPSG::28992\" gml:id=\"LOCAL_ID_1\"><gml:pos>131411.071 481481.517</gml:pos></gml:Point>"
                                      (Integer. 29822))
               tiles (TilesHelper/nl ga)]
           (is (= 1 (count tiles)))
           (is (= 38309 (first tiles)))))