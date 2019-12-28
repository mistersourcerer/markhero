(ns markhero-test
  (:require [clojure.test]
            [markhero.alpha-test]))

(defn run []
  (clojure.test/run-tests 'markhero.alpha-test))

(defn -main []
  (run))
