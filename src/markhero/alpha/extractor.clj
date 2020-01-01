(ns markhero.alpha.extractor
  (:require [clojure.java.io :as io]))

(defn delimiter
  ([]
   (delimiter 32))
  ([times]
   (clojure.string/join (repeat times "`"))))

(defn- to-string [lines]
  (str
    (clojure.string/replace
      (clojure.string/join "\n" lines) "→" "\t")))

(defn- markdown-from [example]
  (loop [line (first example)
         remaining (rest example)
         markdown []]
    (if (= line ".")
      (str (to-string markdown) "\n")
      (recur (first remaining) (rest remaining) (conj markdown line)))))

(defn- html-from [example]
  (loop [line (first example)
         remaining (rest example)
         html nil]
    (if (nil? line)
      (str (to-string html) "\n")
      (let [current-html (if (= line ".") [] (conj html line))]
      (recur (first remaining) (rest remaining) current-html)))))

(defn to-json [example line-count example-count section]
  {
   :markdown (markdown-from example)
   :html (html-from example)
   :example example-count
   ;; total number of lines until now minues
   ;; number of lines in the example + open and closing delimiters
   :start-line (- line-count (+ (count example) 2))
   ;; total number of lines until now minus the one after delimiter
   :end-line (dec line-count)
   :section section
   }
  )

(defn- example-started? [line]
  (= line (str (delimiter) " example")))

(defn- example-finished? [line]
  (= line (delimiter)))

(defn- section-from [line current-section]
  (let [[_ _ title] (re-matches #"\A(##\s)(.+)" line)]
    (if (nil? title)
      current-section
      title)))

(defn- example-with [line current-example]
  (if (nil? current-example)
    nil
    (conj current-example line)))

(defn examples-to-json [file]
  (with-open [rdr (io/reader file)]
    (loop [remaining (line-seq rdr)
           examples []
           line-count 1
           section nil
           current-example nil]
      (if (empty? remaining)
        examples
        (let [line (first remaining)
              remaining (rest remaining)
              line-count (inc line-count)]
          (if (example-started? line)
            (recur remaining examples line-count section [])
            (if (example-finished? line)
              (let [example-count (inc (count examples))
                    example (to-json current-example line-count example-count section)]
                (recur remaining (conj examples example) line-count section []))
              (recur remaining examples line-count
                     (section-from line section)
                     (example-with line current-example)))))))))
