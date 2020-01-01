(ns markhero.alpha.cli
  (:require [markhero.alpha.cli-options :as options]
            [markhero.alpha.extractor :as xt]
            [clojure.java.io :as io]
            [clojure.data.json :as json])
  (:gen-class))

(defn- show-help []
  (prn "will show message beased on known-options :name and :desc"))

(defn- dump-test []
  (let [file
        (if (.exists (io/file "commonmark-spec/spec.txt"))
          "commonmark-spec/spec.txt"
          "https://raw.githubusercontent.com/commonmark/commonmark-spec/master/spec.txt")]
    (json/pprint (xt/examples-to-json file))))

(defn run [args]
  (let [known-options
        ["-d" {:fun dump-test
         :desc "dump tests in JSON format"}
         ;; this overrides the default show-help
         "-h" {:fun show-help
         :desc "show the this help message"}]]
  (options/run-with-options args known-options)))

(defn -main [& args]
  ;; (run (parse-options args known-options)))
  (run args))
