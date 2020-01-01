(ns markhero-test
  (:require [clojure.test]))

(defn run []
  (clojure.test/run-tests 'markhero.alpha.extractor-test
                          'markhero.alpha.cli-test))

(defn -main []
  (run))
