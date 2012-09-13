(ns running-pages.views.intervals
  (:require [running-pages.views.common :as common]
            [noir.content.getting-started])
  (:use [noir.core :only [defpage defpartial]]
        [analemma.charts]
        [analemma.svg]
        [analemma.xml]))

(def abw-interval-data [[2012 8 13 250]
                    [2012 8 23 224]
                    [2012 8 28 229]
                    [2012 8 31 208]
                    [2012 9 4 214]
                    [2012 9 7 200]])

(def abw-reduced-interval-data (map-indexed (fn [index item] [index (last item)]) abw-interval-data))

(defn my-x-axis [{:keys [height width
		      xmin xmax
		      grid-lines
		      axis-font-family axis-font-size
		      axis-number-format]}]
  (let [grid-x-space (/ width grid-lines)]
    (for [i (range 0 (inc grid-lines)) :when (even? i)]
      (-> (text {:x (* i grid-x-space) :y (+ 20 height)}
		(axis-number-format
			(translate-value (* i grid-x-space)
					 0 width xmin xmax)))
	  (style :fill (rgb 150 150 150)
		 :font-family axis-font-family
		 :font-size axis-font-size
		 :text-anchor :middle)))))

(defn my-y-axis [{:keys [height width
		      ymin ymax
		      grid-lines
		      axis-font-family axis-font-size
		      axis-number-format]}]
  (let [grid-y-space (/ height grid-lines)]
    (for [i (range 1 (inc grid-lines)) :when (even? i)]
      (-> (text {:x 0 :y (- height (* i grid-y-space))}
		(axis-number-format
			(translate-value (* i grid-y-space)
					 0 height ymin ymax)))
	  (style :fill (rgb 150 150 150)
		 :font-family axis-font-family
		 :font-size axis-font-size
		 :text-anchor :end
		 :alignment-baseline :middle)))))


(defn my-xy-plot [& options]
  (let [my-options (apply hash-map options)
        props-to-pass-along (merge default-chart-props my-options)]
    {:properties props-to-pass-along
     :svg (-> (group (chart-background props-to-pass-along))
              (translate (:x props-to-pass-along) (:y props-to-pass-along))
              (concat (x-grid props-to-pass-along)
                      (y-grid props-to-pass-along)
                      (my-x-axis (assoc props-to-pass-along :axis-number-format (get props-to-pass-along :x-axis-number-format)))
                      (my-y-axis (assoc props-to-pass-along :axis-number-format (get props-to-pass-along :y-axis-number-format)))))}))


(defpage "/intervals" []
  (common/layout
   (emit-svg (-> (my-xy-plot :height 500 :width 500
                          :xmin 0 :xmax 10
                          :ymin 150 :ymax 300
                          :x-axis-number-format (fn [x] (format "%.1f" x))
                          :y-axis-number-format (fn [x] (let [minutes (int (/ x 60))
                                                             seconds (int (- x (* 60 minutes)))]
                                                         (format "%d:%02d" minutes seconds))))
                 (add-points abw-reduced-interval-data)))))