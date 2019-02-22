Employee Operations Crud


All Employee details-
/employees
Method – GET

Input-
[{
	 "id": 100,
	 "name": "Aayush",
	 "leavesBalance": 500,
	 "joiningDate": "2019-02-01",
	 "gender": "Male",
	 "numberOfChildren": 0 
}]

Output-
[
    {
        "id": 100,
        "name": "Aayush",
        "leavesBalance": 5,
        "compOffBalance": [],
        "joiningDate": "2019-02-01",
        "gender": "Male",
        "numberOfChildren": 0,
        "optionalLeavesAvailed": [],
        "onBlanketCoverageLeave": false
    }
]



Single Employee detail:-
/employees/{id}
Method - GET

example-
http://localhost:8080/employees/100


Create Employee in the database
'/employees'
Method - POST



Update Employee By Id
'/employees/{id}'
Method - PUT


Delete Employee By Id
'/employees/{id}'
Method – DELETE,






Leave Operations

Format : json 

All the leaves of employee
Method - GET,
 '/leaves'



Apply for Leave for an employee
Method - POST,
'/leaves/apply'


Format -
{ 
	"employee": 100,
	 "typeOfLeaves": "MATERNITY", 
	"startDate": "2019-01-20",
	 "endDate": "2019-01-25",
	 "requestDate": "2019-01-16"(optional) 
}

Types of Leaves -
OUT_OF_OFFICE,
COMP_OFF,
MATERNITY,
PATERNITY,
SABBATICAL,
OPTIONAL_LEAVE

