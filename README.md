# aaworker
An Extensible Transactional Worker for [Hoplon](https://github.com/hoplon/hoplon)

[![Clojars Project](https://img.shields.io/clojars/v/aatree/aaworker.svg)](https://clojars.org/aatree/aaworker)

When dealing with side-effects, a web worker needs to process one update at a time.
This is easily achieved with clojure script's async.core.

Responses from a transactional web worker are typically an updated state.
Hoplon is a nice solution for processing updated state for a reactive web page.

Two items, deflpc and mklocal, will be modeled on the
[Castra](https://github.com/hoplon/castra)'s defrpc and mkremote.
A mechanism like castra's is necessary here as we will not be using a shared web worker.

Demo: [tworker](https://github.com/aatree/aademos/tree/master/tworker)
