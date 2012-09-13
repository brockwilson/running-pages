(ns running-pages.views.welcome
  (:require [running-pages.views.common :as common]
            [noir.content.getting-started])
  (:use [noir.core :only [defpage]]))

(defpage "/welcome" []
         (common/layout
           [:p "Welcome to running-pages"]))
