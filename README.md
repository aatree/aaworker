# aaworker
An Extensible Transactional Worker for [Hoplon](https://github.com/hoplon/hoplon)

When dealing with side-effects, a web worker needs to process one update at a time.
This is easily achieved with clojure script's async.core.

Responses from a transactional web worker are typically an updated state.
Hoplon is a nice solution for processing updated state for a reactive web page.
