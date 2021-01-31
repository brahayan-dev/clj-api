# API | Clojure | Pedestal

Of course, the project only has been developed to run in _LOCAL_ environment. It can be started with **lein run-dev** command, its unique endpoint is (/word?original=foo&scrambled=bar)[http://localhost:3000/word?original=foo&scrambled=bar]. Also it has a module with integration test, to launch that test you must run the command **lein test**.

## Details

- Default port: 3000.
- CORS are enabled to work with same origin (http:/localhost) by default. 
- The `:has-match?` body parameter only is returned when the query parameters are alpha-characters in lower case.
