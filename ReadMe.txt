-----Configuration-----
IMP:- When user download the zip file of project , extract project and rename it to ticketService (do this if need to use launch file to run program in eclispe)
--> Downloaded-project-name = ticketService-master.zip
--> Expected project-name = ticketService

1. Launch file is configured to execute main class [ExecuteTicketService.java] . 
2. Use launch file to run the project in eclipse. (ExecuteTicketService.launch)
3. Configure log file location (refer ticket-service-logback.xml)..update path "logBasePath" as per your log path folder location.
4. Thread configuration can be updated in ticketService.properties file.


-----------------Maven build command-----------------------------------------------------------
cd $project_path/ticketService

1. mvn clean compile install


-----------Maven Junit Test cases execution command------------------------------------------------
cd $project_path/ticketService
(note: please have patience while test cases are running.It takes One minute to complete)
1. mvn -Dtest=JunitTicketServiceApp test -Dlogback.configurationFile=ticket-service-logback.xml
2. mvn -Dtest=JunitTestITicketService test -Dlogback.configurationFile=ticket-service-logback.xml


-----Debugging application----------------------------------------------------------------------------

1. For detail level debugging update logging configuration in ticket-service-logback.xml file.

---------------Assumptions and data flow-----------------
1. numberOfSeatsAvailableQueue :- This Linked-blocking queue will contain user request for seat availability. Its a scheduled service which will be executed after specific time interval.
Currently seat availability request will be triggered every 3 seconds.

2. findAndHoldSeatsQueue :- This Linked-blocking queue will contain user request for whom system should find the seats and hold them for user.

3. Once the seats are on hold for user, a scheduled-executor service will be executed as value configured [ticket.hold.expire.time]. 
For e.g. if seat is booked, then user needs to decide
within 10 seconds(configurable value) whether he wants to finally confirm the seats or need to cancel the transaction.
	So after 10 seconds, status of seat will be either (isBooked = true and isOnHold = false) or (isOnHold = false).

4. reserveSeatsQueue:- If user finally confirms the seat within the time expiry interval, the booking will be  confirmed and seatHold object will be 
pushed to Linked-blocking named "reserveSeatsQueue". Here , seat status will be updated to BOOKED" and an dummy email will be sent.

5. Once all the requests are processed, a report will created which will contain details of confirmed seat reservation for every customer.
Refer customerOrderReport.txt file for details.

------------Data simulation to check in multi-threading environment---------
1. In order to execute request for seat availability , findAndHold , reserve-seats ; system will create dummy orders during application start-up.
2. Various Linked-blocking queue will be populated with dummy orders. These queues will be polled by multiple thread instances and accordingly request will be processed.
3. So eventually when all the requests in queue are processed, the program execution will complete, generating a report and will exit.

Note:- This program can be extended to a service which will poll the linked-blocking-queue to process user requests 24*7 , 
but for convenience of debugging and testing we will exit the program once all static user requests are processed.

------------------------------------------------------------------------------------------------------------------------------

::::::For Testing purpose, code was executed in multi-threaded environment.

Below are the log file, configuration file and seat reservation report--------
Go to folder \sampleOutput folder which has 3 files:-

1. test_Configuration.txt :- Contains thread configuration , ticket hold expiry timeframe etc.  for test case scenario.

2. customerOrderReport.txt:- After processing multiple request, final seat confirmation order report was generated. 

In sample test case below are the statistics:-
Input orders: 200
Confirmed orders: 103.
Rejected Orders: 97. 

3. ticketService.log :- Log file was generated in debug mode to track multiple request processed by threads to get availability count, findAndHold seat , reserve seats. 


 
