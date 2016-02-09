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
        args (rest data)]
    (println "process" fn-key)
    (try
      (let [[sf f] (fn-key @worker-fn-map)]
        (println :sf sf fn-key)
        (if sf
          (.postMessage js/self (prn-str [fn-key :success (apply f args)]))
          (let [success
                (fn [result]
                  (println 12333)
                  (println fn-key :success result)
                  (println :postMessage (prn-str [fn-key :success result]))
                  (.postMessage js/self (prn-str [fn-key :success result]))
                  (println :posted))
                failure
                (fn [error]
                  (println 99998)
                  (let [error (if (instance? js/Event error)
                                (-> error .-target .-errorCode)
                                error)])
                  (.postMessage js/self (prn-str [fn-key :failure error]))
                  )]
            (println :what success)
            (apply f success failure args))))
      (catch :default e
        (println :catch e)
        (.postMessage js/self (prn-str [fn-key :failure e]))))))

(defn process-requests []
  (set! (.-onmessage js/self) process-request)
  (send-notice :ready)
  (println "sent ready"))