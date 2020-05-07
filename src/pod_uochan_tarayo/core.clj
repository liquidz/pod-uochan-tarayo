(ns pod-uochan-tarayo.core
  (:gen-class)
  (:require
   [bencode.core :as bencode]
   [clojure.edn :as edn]
   [tarayo.core :as core])
  (:import
   (java.io
    PushbackInputStream)))

(def stdin (PushbackInputStream. System/in))

(defn write* [v]
  (bencode/write-bencode System/out v)
  (.flush System/out))

(defn read-string* [^"[B" v]
  (String. v))

(defn read* []
  (bencode/read-bencode stdin))

(def pool (atom {}))

(defn -main [& _args]
  (loop []
    (let [message (try (read*)
                       (catch java.io.EOFException _
                         ::EOF))]
      (when-not (identical? ::EOF message)
        (let [op (some-> message (get "op") read-string* keyword)
              id (some-> message (get "id") read-string*)
              id (or id "unknown")]
          (case op
            :describe (do (write* {"format" "edn"
                                   "namespaces" [{"name" "pod.uochan.tarayo"
                                                  "vars" [{"name" "send"}]}]
                                   "id" id})
                          (recur))
            :invoke (do (let [var (some-> message (get "var") read-string* symbol)
                              [smtp-server message] (some-> message (get "args") read-string* edn/read-string)]
                          (case var
                            pod.uochan.tarayo/send
                            (with-open [conn (core/connect smtp-server)]
                              (let [value (pr-str (core/send! conn message))
                                    reply {"value" value, "id" id, "status" ["done"]}]
                                (write* reply)))))
                        (recur))
            (recur)))))))
