(ns aaworker.worker-macros)

(defmacro deflpc! [fn-sym args & body]
  `(do
     (defn ~fn-sym ~args ~@body)
     (aaworker.api/register-fn! ~(name fn-sym) [true ~fn-sym])))

(defmacro deflapc! [fn-sym args & body]
  `(do
     (defn ~fn-sym [~'success ~'failure ~@args] ~@body)
     (aaworker.api/register-fn! ~(name fn-sym) [true ~fn-sym])))
