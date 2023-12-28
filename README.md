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
![Initial Fetch](https://github.com/AR-LABS-CPP/Flag-Manager/assets/70814565/dcff8ff8-7e97-40e9-b2da-8523e866d641)

### Here's some screens for you as well
#### 1.1 Project Creation
![Project_Creation](https://github.com/AR-LABS-CPP/Flag-Manager/assets/70814565/5b85b0f2-f405-4f37-97b7-e4a6bdf48a0d)

#### 1.2 Flag, Webhook, Bindings manager screen
![Flag_Manager](https://github.com/AR-LABS-CPP/Flag-Manager/assets/70814565/79527383-b7c3-4036-9e27-52c1ae74c1ac)

#### 1.3 Requests panel (Don't mind the colors 😅)
![Requests_Panel](https://github.com/AR-LABS-CPP/Flag-Manager/assets/70814565/55965cfa-f865-4666-8acc-debd6dc54a47)

### Have fun and keep building, cheers!
