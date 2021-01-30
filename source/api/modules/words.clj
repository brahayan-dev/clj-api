(ns api.modules.words
  (:require [clojure.string :refer [lower-case split]]))

(defn- make-answer [b original scrambled]
  {:has-match? b :original original :scrambled scrambled})

(defn- str-to-map [s]
  (let [count-letter (fn [word-map c] (if (get word-map c)
                                        (assoc word-map c (+ (get word-map c) 1))
                                        (assoc word-map c 1)))]
    (reduce count-letter {} (split s #""))))

(defn- match-maps? [om sm]
  (let [f (fn [b-vec k v] (cond
                               (nil? (get sm k)) (conj b-vec false)
                               (> v (get sm k)) (conj b-vec false)
                               :else (conj b-vec true)))
        bool-vec (reduce-kv f [] om)]
    (every? true? bool-vec)))

(defn- analyze-words [sw ow]
  (let [s (lower-case sw)
        o (lower-case ow)
        o-map (str-to-map o)
        s-map (str-to-map s)
        scrambled? (if (> (count o) (count s)) false
                       (match-maps? o-map s-map))]
    (make-answer scrambled? o s)))

(defn handler [params]
  (let [ow (get params :original)
        sw (get params :scrambled)
        exists-params? (every? (comp not nil?) [sw ow])
        are-string? (every? string? [sw ow])]
    {:status 200
     :body (if (and exists-params? are-string?)
             (analyze-words sw ow)
             (make-answer false "" ""))}))
