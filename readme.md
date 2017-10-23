# Issue demonstration application

## Summary

This application was created to demonstrate the following issue: 
TraceFilter will never close the span for the error response (4xx and 5xx),
if it was returned with RequestEntity class. 

Looks like it was designed to work with controllers, which returns bad responses through exception throwing only.  

Link to the issue:

https://github.com/spring-cloud/spring-cloud-sleuth/issues/759

## Application detail

There is the rest controller class `SampleController`. 
It was created to demonstrate an issue.

It has two methods, that handle two requests:
* `/test_ok` - returns 200 response. Span for this method created and closed correctly: 

```
-o.s.c.sleuth.instrument.web.TraceFilter  : Received a request to uri [/test_ok] that should not be sampled [false]
-o.s.c.sleuth.instrument.web.TraceFilter  : No parent span present - creating a new span
-o.s.c.s.i.web.TraceHandlerInterceptor    : Handling span [Trace: 7c2e274c05c7c38c, Span: 7c2e274c05c7c38c, Parent: null, exportable:false]
-o.s.c.s.i.web.TraceHandlerInterceptor    : Adding a method tag with value [processOk] to a span [Trace: 7c2e274c05c7c38c, Span: 7c2e274c05c7c38c, Parent: null, exportable:false]
-o.s.c.s.i.web.TraceHandlerInterceptor    : Adding a class tag with value [SampleController] to a span [Trace: 7c2e274c05c7c38c, Span: 7c2e274c05c7c38c, Parent: null, exportable:false]
-o.s.c.sleuth.instrument.web.TraceFilter  : Closing the span [Trace: 7c2e274c05c7c38c, Span: 7c2e274c05c7c38c, Parent: null, exportable:false] since the response was successful
```

* `/test_bad_request` - returns 400 response. Span for this method will be created, but never closed:

```
-o.s.c.sleuth.instrument.web.TraceFilter  : Received a request to uri [/test_bad_request] that should not be sampled [false]
-o.s.c.sleuth.instrument.web.TraceFilter  : No parent span present - creating a new span
-o.s.c.s.i.web.TraceHandlerInterceptor    : Handling span [Trace: 7e4fb32891d4efdd, Span: 7e4fb32891d4efdd, Parent: null, exportable:false]
-o.s.c.s.i.web.TraceHandlerInterceptor    : Adding a method tag with value [processFail] to a span [Trace: 7e4fb32891d4efdd, Span: 7e4fb32891d4efdd, Parent: null, exportable:false]
-o.s.c.s.i.web.TraceHandlerInterceptor    : Adding a class tag with value [SampleController] to a span [Trace: 7e4fb32891d4efdd, Span: 7e4fb32891d4efdd, Parent: null, exportable:false]
-o.s.c.sleuth.instrument.web.TraceFilter  : Detaching the span [Trace: 7e4fb32891d4efdd, Span: 7e4fb32891d4efdd, Parent: null, exportable:false] since the response was unsuccessful
```

Application could be runned with `mvn spring-boot:run` command.