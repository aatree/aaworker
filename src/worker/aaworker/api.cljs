(ns aaworker.api
  (:require [cljs.reader :refer [read-string]]))

(def worker-fn-map (atom {}))

(defn register-fn [fn-name f]
  (swap! worker-fn-map assoc (keyword fn-name) f))

(defn process-request [event]
  (let [data (read-string (.-data event))
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
  (set! (.-onmessage js/self) process-request))