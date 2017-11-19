(ns defng.jdatascript.core
  (:require [clojure.walk :as walk]
            [datascript.core :as d]
            [defng.joculer.core :as joculer]))

;; Conversions

(defn- keywordize [s]
  (if (and (string? s) (= (subs s 0 1) ":"))
    (keyword (subs s 1))
    s))

(defn- schema->clj [schema]
  (->>  schema
        (reduce-kv
          (fn [m k v] (assoc m k (walk/postwalk keywordize v))) {})))

(defn- pull-result->java
  [result]
  (->> result
       (walk/postwalk #(if (keyword? %) (str %) %))))

(defn- ^:declared entities->clj [entities])

(defn- entity->clj [e]
  (cond (map? e)
        (-> e
            (dissoc ":db/id")
            (assoc  :db/id (e ":db/id")))
        (= (first e) ":db.fn/call")
        (let [[_ f & args] e]
          (concat [:db.fn/call (fn [& args] (entities->clj (apply f args)))] args))
        (sequential? e)
        (let [[op & entity] e]
          (concat [(keywordize op)] entity))))

(defn- entities->clj [entities]
  (->> (joculer/->clojure entities)
       (map entity->clj)))

;; Public API

(defn db-with [db entities]
  (d/db-with db (entities->clj entities)))

(defn transact [conn entities & [tx-meta]]
  (let [entities (entities->clj entities)
        report   (-> (d/-transact! conn entities tx-meta))]
    (doseq [[_ callback] @(:listeners (meta conn))]
      (callback report))
    report))
