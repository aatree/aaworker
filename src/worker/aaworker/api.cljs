(ns aaworker.api
  (:require [cljs.reader :refer [read-string]]))

(def worker-fn-map (atom {}))

(defn register-fn! [fn-name f]
  (swap! worker-fn-map assoc (keyword fn-name) f))

(defn send-notice [key & args]
  (.postMessage js/self (prn-str [key :notice args])))

(defn process-request [event]
  (let [data (read-string (.-data event))
        fn-key (first data)
        data (rest data)
        req-key (first data)
        args (rest data)]
    (try
      (let [[sf f] (fn-key @worker-fn-map)]
        (if sf
          (.postMessage js/self (prn-str [req-key :success (apply f args)]))
          (let [success
                (fn [result]
                  (.postMessage js/self (prn-str [req-key :success result])))
                failure
                (fn [error]
                  (.postMessage js/self (prn-str [req-key :failure (str error)]))
                  )]
            (apply f success failure args))))
      (catch :default e
        (.postMessage js/self (prn-str [req-key :failure (str e)]))))))

(defn process-requests []
  (set! (.-onmessage js/self) process-request)
  (send-notice :ready))