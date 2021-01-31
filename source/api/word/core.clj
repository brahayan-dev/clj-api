(ns api.word.core
  (:require [clojure.data.json :refer [write-str]]
            [clojure.string :refer [split]]))

(defn- make-answer [msg b original scrambled]
  (let [ans-map {:message msg
                 :has-match? b
                 :original original
                 :scrambled scrambled}
        f (fn [ini-map k v] (if (not (nil? v))
                              (assoc ini-map k v)
                              ini-map))]
    (write-str (reduce-kv f {} ans-map))))

(defn- str-to-map [s]
  (let [char-counter (fn [word-map c]
                       (if (nil? (get word-map c))
                         (assoc word-map c 1)
                         (assoc word-map c (+ (get word-map c) 1))))]
    (reduce char-counter {} (split s #""))))

(defn- match-maps? [om sm]
  (let [f (fn [bool-vec k v] (cond
                               (nil? (get sm k)) (conj bool-vec false)
                               (> v (get sm k)) (conj bool-vec false)
                               :else (conj bool-vec true)))]
    (every? true? (reduce-kv f [] om))))

(defn- analyze-words [sw ow]
  (let [o-map (str-to-map ow)
        s-map (str-to-map sw)
        scrambled? (if (> (count ow) (count sw)) false
                       (match-maps? o-map s-map))]
    (make-answer nil scrambled? ow sw)))

(defn handler [params]
  (let [ow (get params :original)
        sw (get params :scrambled)
        all-params? (every? (comp not nil?) [sw ow])
        lower-case? (-> [sw ow]
                        ((partial map #(or % "")))
                        ((partial map #(re-matches #"^[a-z]+$" %)))
                        ((partial map string?))
                        ((partial every? true?)))]
    {:headers {"Content-Type" "application/json"}
     :status (if (and all-params? lower-case?) 200 400)
     :body (cond (not all-params?) (make-answer
                                    "'original' and 'scrambled' query parameters are required"
                                    false nil nil)
                 (not lower-case?) (make-answer
                                    "query parameters must have alphabetic characters"
                                    false nil nil)
                 :else (analyze-words sw ow))}))
