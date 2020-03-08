echo "======================>>>>>>>>>>>>>you have to see authentication page because of invalidNubmer"
curl -L --cookie "telephone=0939753471;password=ababdxoro12;" http://localhost:8080/mamad
echo "======================>>>>>>>>>>>>>you have to see authentication page because of short password"
curl -L --cookie "telephone=09397534791;password=ab2;" http://localhost:8080/panel
echo "======================>>>>>>>>>>>>>you have to see authentication page because of short password"
curl -L --cookie "telephone=09397534791;password=ababdxoro122;" http://localhost:8080/panel
echo "======================>>>>>>>>>>>>>you have to see authentication page because of invalid password"
curl -L --cookie "telephone=09397534791;password=adadbxoroq;" http://localhost:8080/mamad
echo "======================>>>>>>>>>>>>>you have to see authentication page because of invalid password"
curl -L --cookie "telephone=09397534791;password=123241112;" http://localhost:8080/panel
echo "======================>>>>>>>>>>>>>you have to see a panel page"
curl -v -X POST -L --cookie "telephone=09397534791; password=ababdxoro12;" http:/localhost:8080/panel
