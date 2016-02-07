# aaworker
An Extensible Worker for [Hoplon](https://github.com/hoplon/hoplon)

[![Clojars Project](https://img.shields.io/clojars/v/aatree/aaworker.svg)](https://clojars.org/aatree/aaworker)

Some things, like IndexedDB and crypto tasks, may interfere with the responsiveness
of a web page. These tasks then are best run using a web worker.

Responses from a web worker are typically an updated state.
Hoplon is a nice solution for processing updated state for a reactive web page cells.

AAWorker's deflpc and mklocal have been modeled after 
[Castra](https://github.com/hoplon/castra)'s defrpc and mkremote.
A mechanism like castra's is necessary here as aaworker does not use shared web workers.

Demo: [tworker](https://github.com/aatree/aademos/tree/master/tworker)

## Change Log

**0.0.2** - Dropped async.core, added worker notifications.

**0.0.1** - Initial release.