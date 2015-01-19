(ns guess-colors.core
  [:use [clojure.string :only [trim]]]
  (:gen-class))

(def files "abcdefgh")
(def ranks "12345678")

(def file (comp str first))
(def rank (comp str second))
(def file-idx (comp (partial (memfn indexOf s) files) file))
(def rank-idx (comp (partial (memfn indexOf s) ranks) rank))

(defn- random-square []
  (str (rand-nth files) (rand-nth ranks)))

(defn- square-color [square]
    (if (even? (file-idx square))
      (if (even? (rank-idx square)) :dark :light)
      (if (even? (rank-idx square)) :light :dark)))

(defn- user-guess [line]
  (let [fc (first (.toLowerCase line))]
    (cond
      (#{\d \b} fc) :dark
      (#{\l \w} fc) :light
      :default nil)))

(defn- read-guess []
  (first (->> (line-seq (java.io.BufferedReader. *in*))
              (map trim)
              (map user-guess)
              (filter #{:dark :light}))))

(defn -main
  [& args]

  (loop [square (random-square)]
    (print (str square ": "))
    (flush)
    (let [guess (read-guess)
          color (square-color square)]
      (when-not (= guess color)
       (println (format "Incorrect! %s is %s not %s." square (name color) (name guess)))))
    (recur (random-square))))
