# aaworker
An Extensible Web Worker for [Hoplon](https://github.com/hoplon/hoplon)

[![Clojars Project](https://img.shields.io/clojars/v/aatree/aaworker.svg)](https://clojars.org/aatree/aaworker)

1. [Introduction](#introduction)
1. [Client API](#client-api)
1. [Worker API](#worker-api)
1. [Change Log](#change-log)

## Introduction

Some things, like IndexedDB and crypto tasks, may interfere with the responsiveness
of a web page. These tasks then are best run using a web worker.

Responses from a web worker are typically an updated state.
Hoplon is a nice solution for processing updated state for a reactive web page cells.

AAWorker's deflpc and mklocal have been modeled after 
[Castra](https://github.com/hoplon/castra)'s defrpc and mkremote.
A mechanism like castra's is necessary here as aaworker does not use shared web workers.

Demos: [tworker](https://github.com/aatree/aademos/tree/master/tworker)
[duracell](https://github.com/aatree/aademos/tree/master/duracell)

## Client API

**```(aaworker.lpc/new-worker! "worker.js")```**

Create a web worker that executes the ```worker.js``` file.

**```(aaworker.lpc/mklocal! 'click "worker.js" state error loading)```**

**```(aaworker.lpc/mklocal! 'click "worker.js" state error loading req-key)```**

Returns a RPC function to call the ```click``` method in the web worker that is
executing ```worker.js```, while also associating the state/error/loading cells
with the name of the function for the given worker.

The ```state``` cell is where the results of calling ```click``` are returned,
assuming no exception was raised.

The ```error``` cell is set to ```nil``` if there is no error, or to the exception raised
when ```click``` was called in the web worker.

The ```loading``` cell contains a text message while the ```click``` method is executing,
otherwise it is ```nil```.

The optional ```req-key``` identifies the particular request, allowing multiple requests
with different state cells to the same worker api.
When not present, ```(keyword fn-name)```--```:click``` in this case--is used.

**```(aaworker.lpc/register-notice-processor! "worker.js" :alert (fn [msg] (js/alert msg)))```**

The ```register-notice-processor!``` is creates a handler for one type of notice from a worker.
In this case it creates a handler for alerts coming from the web worker executing ```worker.js```.
And this particular call to ```register-notice-processor!``` occurs when ```new-worker!``` is called,
so that any worker can send an ```:alert``` notice to the client.

## Worker API

**```(aaworker.api/process-requests)```**

Processes requests sent by the client.

Once request processing is initiated, a :ready notice is sent back to the client.

**```(aaworker.worker-macros/deflpc! click [] body)```**

Defines the ```click``` function and registers it to process ```click```
requests from the client.

**```(aaworker.worker-macros/deflapc! idb-read [key] body)```**

Defines the ```idb-read``` asynchronous function and registers it
to process ```idb-read``` requests from the client.

Two arguments are pre-pended to the function's arguments, success and failure.
These are callback functions which take a single argument, a result or
an error message, respectively.

**```(aaworker.api/send-notice :alert "Something somewhere has gone wrong!")```**

Sends a notice, in this case an ```:alert```, to the client.
The client must have already registered a handler for the given type of notice,
or the notice will be ignored.
Arguments after the notice type, e.g. after the ```:alert``` in this case, must match
the arguments of the registered handler function.

## Change Log

**0.1.2** - Added mkreq! for when multiple cells use the same worker api.
Fixed error handling and argument passing bugs.

**0.1.1** - Added deflapc! for asynchronous functions.
Added the :ready notice.

**0.1.0** - Dropped async.core, added worker notifications, 
added ! to the end of the names of funchtions which alter state.

**0.0.1** - Initial release.
