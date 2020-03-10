# dockerhost
<hr>
dockerhost just a simple web application which buy some docker services like mongo mysql etc.
For example if you want a secure mongodb and you don't like to install mongodb on your server you could rent a mongodb secure docker container from us.
After your request and pay something for that our website make a docker container then give you host address and port number.

## about projects
### docker-host
This is the main website which include autherization for all clients also selling service of docker services (include authentication page and user panel to buy a docker service)
### docker-host-selenium-integration-test
This is created for test our html pages (just ensure that our patterns work or some important attributes and elements exists).
It is very flexible so you can change the server IP very easy,It means you can run it in our personal computer for tes on the server.
### docker-host-client
This is the main project.As the services will be a lot also a server cannot handle the memory and cpu for deploying website and hosting docker services I decided to make a client which get request from main server (docker-host) then make a docker service after that inform user about port and ip (on docker-host panel page).
You can have many docker-host-client on different servers but you cannot have more docker-host nubmer than 1 (just one host for deploy website)
### shell-based-test
This folder include .sh files for test by command line apps like curl (which give you some information about what will must happen after run this shell script and if it not where is the code might have wrong)

## TODO
- [x] Implements authentication system
- [ ] Implements cachable memory for autherization (0.041s performance foreach request)
- [ ] Implements more fields on register like name and family and repassword
- [ ] Implements user panel
- [ ] Implements a css framework
- [ ] Implements send code using Kavenegar to mobile for confirm and forget password
- [ ] Write docker-host-client project
- [ ] Implements selling service system
