# aaworker
An Extensible Transactional Worker for [Hoplon](https://github.com/hoplon/hoplon)

When dealing with side-effects, a web worker needs to process one update at a time.
This is easily achieved with clojure script's async.core.

Responses from a transactional web worker are typically an updated state.
Hoplon is a nice solution for processing updated state for a reactive web page.

Two macros will be used, deflpc and mklocal. These will be modeled on the
[Castra](https://github.com/hoplon/castra) macros, defrpc and mkremote.
A mechanism like castra's in necessary here as we will not be using a shared web worker.
