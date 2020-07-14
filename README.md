# DoctorInfoMngmt
1. Make sure that the build.xml file has the correct directory for Tomcat. 
2. Compile/deploy in the usual way with the Ant script: ant -Dwar.name=doctors deploy
3. The base URL is: http://localhost:8080/doctors/resourcesD/
4. All CRUD operations are supported. The following curl scripts can be used for testing:

GET - will display all doctors or a specific doctor in plain/xml/json formats.

	curl --request GET localhost:8080/doctors/resourcesD/plain  
	curl --request GET localhost:8080/doctors/resourcesD/plain/2 

PUT - updates doctor's name when given and ID

	curl --request PUT --data "id=1&&name=DrTom" localhost:8080/doctors/resourcesD/update

DELETE - deletes doctor and all their patients based on doctor ID

	curl --request DELETE localhost:8080/doctors/resourcesD/delete/2

POST - creates a new doctor and their patients. Data must be passed as an XML document to support an arbitrary number of patients

	curl -v POST -H "Content-Type: application/xml" --data "<doctor><name>Doctor Strange</name><patients><patient><name>Iron Man</name><ins>1234</ins><id>6</id></patient><patient><name>Captain Marvel</name><ins>5678</ins><id>7</id></patient></patients></doctor>" localhost:8080/doctors/resourcesD/create
