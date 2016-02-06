(ns aaworker.worker-macros)

(defmacro deflpc [fn-sym args & body]
  `(do
     (defn ~fn-sym ~args ~@body)
     (aaworker.api/register-fn ~(name fn-sym) ~fn-sym)))
