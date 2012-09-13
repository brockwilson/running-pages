(ns running-pages.views.intervals
  (:require [running-pages.views.common :as common]
            [noir.content.getting-started])
  (:use [noir.core :only [defpage defpartial]]
        [analemma.charts :only [emit-svg xy-plot add-points y-axis default-chart-props]]
        [analemma.svg]
        [analemma.xml]))

(def abw-interval-data [[2012 8 13 250]
                    [2012 8 23 224]
                    [2012 8 28 229]
                    [2012 8 31 208]
                    [2012 9 4 214]
                    [2012 9 7 200]])

(def abw-reduced-interval-data (map-indexed (fn [index item] [index (last item)]) abw-interval-data))

(defpage "/intervals" []
  (common/layout
   (let [chart (-> (xy-plot :height 500 :width 500
                 :xmin 0 :xmax 10
                 :ymin 0 :ymax 300)
        (add-points abw-reduced-interval-data))]
   (emit-svg chart))))
