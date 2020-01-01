(ns markhero.alpha.cli-options)

(defn- show-help []
  (prn "will show message beased on known-options :name and :desc"))

(defn parse-options [args known-options]
  (reduce
    (fn [options arg]
      (let [idx (.indexOf known-options arg)]
        (if (>= idx 0)
          (let [fun (:fun (nth known-options (inc idx)))]
            ;; if option has args (-x=xpto), args will be ["xpto"]
            ;; they will be passed to the function
            (conj options {:fun fun :args []}))
          options)))
    []
    args))

(defn run-with-options
  ([options]
    (if (empty? options)
      (show-help)
      (doseq [option options]
        (let [fun (:fun option)
              args (:args option)]
        (fun)))))
  ([args known-options]
   (run-with-options (parse-options args known-options))))
