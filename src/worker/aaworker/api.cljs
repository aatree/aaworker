(ns aaworker.api
  (:require [cljs.core.async :refer [chan close! timeout put!]]
            [cljs.reader :refer [read-string]])
  (:require-macros [cljs.core.async.macros :as m :refer [go go-loop]]))

(def worker-fn-map (atom {}))

(defn register-fn [fn-name f]
  (swap! worker-fn-map assoc (keyword fn-name) f))

(defn process-request [data]
  (let [data (read-string data)
        fn-key (first data)
        args (rest data)]
    (try
      (let [fn (fn-key @worker-fn-map)
            rv (apply fn args)
            msg (prn-str [fn-key true rv])]
        (.postMessage js/self msg))
      (catch :default e
        (.postMessage js/self (prn-str [fn-key false e]))))))

(defn process-requests []
  (let [requests (chan 1)]
    (set! (.-onmessage js/self)
          (fn [event]
            (let [data (.-data event)]
              (put! requests data))))
    (go-loop []
             (process-request (<! requests))
             (recur))))