### Why did I create it when there are better things out there?
Well, I was having some issues with managing environment variables while testing my projects backend. I was bored and had nothing to build so I thought why not create a utility for myself that I can use.

### What's the usage?
It's functioning is simple. You first create a project. After the project creation, we can create flags, web-hooks and bindings. Initially you will have to fetch the flags from database but subsequent updates are pushed through web-hooks that you have defined through the UI. One thing to note is that updates won’t be pushed if there is no binding created. A binding binds a web-hook with a set of flags (single or multiple), any change in the flag name or value will push updates through the web-hook it is bind with.

### Any Issues?
Um sure, there are minor issues here and there, I have tried to fix almost all of them because I use it myself (duh). I have added logs and requests panel to let the user know what’s happening behind the scenes.

### List down the tools & tech & libraries you have used please
- PostgreSQL for data storage
- Retrofit for API requests
- HikariCP & JDBC for database connectivity
- Swing for UI

### Here's a diagram for you


### Have fun and keep building, cheers!