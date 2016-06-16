# reader-links
Problem: when reading an article, I may find links that I want to open.
However, if I open the links now, I'll lose the
context of the article I'm reading and if I open it in the background,
I won't remember the context of the article that my links came from.

Solution: repeat article links at the end an article with additional
context. For simplicity in implementation, this is done in reader mode.

![screenshot](http://raw.githubusercontent.com/mcomella/reader-links/master/readme-res/screenshot.png)

This prototype does not work across all pages (see below) but seems to
work well for articles on:
* http://mobile.nytimes.com
* http://theguardian.com
* http://mcomella.xyz

## User experience caveats
There are a few bugs that affect the UX:
* Most errors are not bubbled up to the client. Only occassionally are errors
logged (either on the server or the client)
* The server does not always retrieve the page
* The DOM is not always parsed correctly
* The code that deals with the DOM to pull out articles does not handle error
cases correctly and may just explode
* The code to retrieve article previews often gets blocked by remote servers,
leading to no results.

## Development
In a terminal:

    lein repl

A REPL will launch and in that:

    (run)
    (browser-repl)

Connect to:

    http://localhost:3449/

Once you connect, the cljs repl will be available in your shell and the
webpage should update automatically via figwheel.

### Implementation details
If these are important to you, let me know and I'll write up some docs.

## Credits
Built on the [Chestnut][] lein template.

[Chestnut]: https://github.com/plexus/chestnut
