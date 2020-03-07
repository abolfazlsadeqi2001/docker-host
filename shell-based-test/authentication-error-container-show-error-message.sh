echo "=============>>>>>>>>>>you must see the user panel"
curl -X POST -L --data "tel=09397534791" --data "password=ababdxoro12" http:/localhost:8080/login
echo "=============>>>>>>>>>>you must see a <div> line that contain an error message about telephone too short otherwise your model login authentication controller or html template has wrong!"
curl  -X POST -L --data "tel=0939753479" --data "password=ababdxoro12" http://localhost:8080/login
echo "=============>>>>>>>>>>you have to see a <div> line that contain an error message about wrong password otherwise your model login authentication controller or html template has wrong!"
curl  -X POST -L --data "tel=09397534791" --data "password=ababdxoro1" http://localhost:8080/login
echo "=============>>>>>>>>>>you have to see a <div> line that contain an error message about wrong password otherwise your model login authentication controller or html template has wrong!"
curl  -X POST -L --data "tel=09397534791" --data "password=ababdxoro1222" http://localhost:8080/login
echo "=============>>>>>>>>>>you have to see a <div> line that contain an error message about wrong password otherwise your model login authentication controller or html template has wrong!"
curl  -X POST -L --data "tel=09397534791" --data "password=a1" http://localhost:8080/login
echo "=============>>>>>>>>>>you must see a <div> line that contain an error message about telephone too short otherwise your model register authentication controller or html template has wrong!"
curl  -X POST -L --data "tel=0939753479" --data "password=ababdxoro12" http://localhost:8080/register
echo "=============>>>>>>>>>>you have to see a <div> line that contain an error message about wrong password otherwise your model register authentication controller or html template has wrong!"
curl  -X POST -L --data "tel=09397534791" --data "password=ababdxoro1" http://localhost:8080/register
echo "=============>>>>>>>>>>you have to see the user panel page"
curl  -X POST -L --data "tel=09397534790" --data "password=abab1212" http://localhost:8080/register
