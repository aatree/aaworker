(ns aaworker.lpc
  (:require [cljs.reader :refer [read-string]]))

(def worker-map (atom {}))

(defn process-message [file-name event]
  (let [[fn-key message-type result] (read-string(.-data event))]
    (cond
      (= message-type :success)
      (let [[state error loading counter] (get-in @worker-map [file-name 1 fn-key])]
        (reset! loading nil)
        (reset! state result)
        (if counter (swap! counter inc)))
      (= message-type :failure)
      (let [[state error loading counter] (get-in @worker-map [file-name 1 fn-key])]
        (reset! loading nil)
        (reset! error result)
        (if counter (swap! counter inc)))
      (= message-type :notice)
      (let [f (get-in @worker-map [file-name 2 fn-key])]
        (if f
          (if result
            (apply f result)
            (f)))))))

(defn register-responder! [file-name fn-name rsp-vec]
  (swap! worker-map assoc-in [file-name 1 (keyword fn-name)] rsp-vec))

(defn register-notice-processor! [file-name key f]
  (swap! worker-map assoc-in [file-name 2 key] f))

(defn new-worker! [file-name]
  (let [w (js/Worker. file-name)]
    (swap! worker-map assoc file-name
           [w {} {}])
    (set! (.-onmessage w) (partial process-message file-name)))
  (register-notice-processor! file-name :alert
                             (fn [msg]
                               (js/alert msg))))

(defn mklocal!
  ([fn-name file-name state error loading]
   (mklocal! fn-name file-name state error loading nil))
  ([fn-name file-name state error loading counter]
   (mklocal! fn-name file-name state error loading counter (keyword fn-name)))
  ([fn-name file-name state error loading counter req-key]
   (register-responder! file-name req-key [state error loading counter])
   (fn [& args]
     (reset! error nil)
     (reset! loading (str "Sending " fn-name " request to worker " file-name "."))
     (let [msg [(keyword fn-name) req-key]
           msg (reduce conj msg args)
           msg (prn-str msg)
           w (get-in @worker-map [file-name 0])]
       (.postMessage w msg)))))
