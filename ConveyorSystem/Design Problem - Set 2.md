## Design/Coding Exercise
* We have three design problems outlined in this document below. We require you to provide a solution to `only one` of these problems. 
However, you may choose to solve more than one if you so wish.
*	We are flexible in your choice of target runtime/language. However, do note that a majority of our production infrastructure
 makes use of the JRE/.NET runtimes.
*	You may not use any external libraries to solve the problem itself, but you may use external libraries or tools for building and/or testing purposes.

## How we evaluate your code
We will be looking at a number of things including the design aspect of your solution and your object oriented programming skills.
 Whilst these are small problems, we expect you to submit what you believe is “production-quality” code that you would be able to run, 
 maintain and evolve. You do not need to “gold plate” your solution, but we are looking for something more than a bare-bones algorithm. 
 You should submit code that you would be happy to produce in a real project, or that you would be happy to receive from a colleague. 
 We prefer code submissions that exhibit loose coupling, a high degree of encapsulation with cyclomatic complexity distributed relatively evenly. 
 A quick `README` explaining how to run your program will no doubt help.  

### Problem 1: Airport Baggage

Denver International Airport has decided to give an automated baggage system another shot. 
The hardware and tracking systems from the previous attempt are still in place, they just need a system to route the baggage. 
 The system will route baggage checked, connecting, and terminating in Denver.

You have been asked to implement a system that will route bags to their flights or the proper baggage claim.  
The input describes the airport conveyor system, the departing flights, and the bags to be routed.  
The output is the optimal routing to get bags to their destinations.  
Bags with a flight id of “ARRIVAL” are terminating in Denver are routed to Baggage Claim.

Input: The input consists of several sections.  The beginning of each section is marked by a line starting: “# Section:”
```
Section 1: A weighted bi-directional graph describing the conveyor system.
Format: <Node 1> <Node 2> <travel_time>

Section 2: Departure list Format:
<flight_id> <flight_gate> <destination> <flight_time>
Section 3: Bag list Format:
<bag_number> <entry_point> <flight_id>
```

Output: The optimized route for each bag
```
<Bag_Number> <point_1> <point_2> [<point_3>, …] : <total_travel_time>
```

The output should be in the same order as the Bag list section of the input.

Example Input:
```
# Section: Conveyor System
Concourse_A_Ticketing A5 5
A5 BaggageClaim 5
A5 A10 4
A5 A1 6
A1 A2 1
A2 A3 1
A3 A4 1
A10 A9 1
A9 A8 1
A8 A7 1
A7 A6 1
# Section: Departures
UA10 A1 MIA 08:00
UA11 A1 LAX 09:00
UA12 A1 JFK 09:45
UA13 A2 JFK 08:30
UA14 A2 JFK 09:45
UA15 A2 JFK 10:00
UA16 A3 JFK 09:00
UA17 A4 MHT 09:15
UA18 A5 LAX 10:15
# Section: Bags
0001 Concourse_A_Ticketing UA12
0002 A5 UA17
0003 A2 UA10
0004 A8 UA18
0005 A7 ARRIVAL
```

Example Output:
```
0001 Concourse_A_Ticketing A5 A1 : 11
0002 A5 A1 A2 A3 A4 : 9
0003 A2 A1 : 1
0004 A8 A9 A10 A5 : 6
0005 A7 A8 A9 A10 A5 BaggageClaim : 12
```

### Coding problem: Traffic Signals

Write a program that controls the traffic signals for a four-way intersection. Initially, we consider traffic flowing in straight lines only, no turns. The four directions are S(outhbound) and N(orthbound) on Snell Rd; and W(estbound) and E(astbound) on Weaver Rd. The traffic lights should obey the following rules:

1. Cars arrive in each direction on both roads (Snell and Weaver) at the rate of 1 car per second. That is, 4 cars approach the intersection each second.

2. Only one road (Snell or Weaver) can have a "green" light at one time.

3. It is acceptable for both roads to have the "red" light at the same time. Of course, traffic backs up on both roads if this happens.

4. Start by turning on the traffic on Snell Rd "green" in both directions for 3 seconds; then turn it "red" for one second; then turn Weaver "green" for 3 seconds; and then red for one second.

5. When the light turns from red to green at any intersection, it takes the first car 2 seconds to start moving and cross the intersection. Subsequent cars take 1 second each.

6. At the instant the light turns from "green" to "red", a car may not start moving to cross the intersection; whether that car just arrived at the intersection or was waiting at that intersection.

7. The output should be the number of cars that are waiting at the intersection in each direction at each second, for the first 20 seconds. Do not make the program wait 20 seconds to produce the output: this is only a simulation, so print the output when it's ready.

8. Expected output
```
 	0: N = 0; S = 0; E = 0; W = 0
 	1: N = 0; S = 0; E = 1; W = 1
 	2: N = 0; S = 0; E = 2; W = 2
 	3: N = 0; S = 0; E = 3; W = 3
 	4: N = 1; S = 1; E = 4; W = 4
 	5: N = 2; S = 2; E = 5; W = 5
 	6: N = 3; S = 3; E = 5; W = 5
 	7: N = 4; S = 4; E = 5; W = 5
 	8: N = 5; S = 5; E = 6; W = 6
```
 
### Problem 3: Theatre Seating

You run a small theater and each month, you have patrons mail in requests for pre-sale tickets.  You need to process these ticket requests and either tell them where their party will sit or explain to the patron why you can't complete their order.

You have a few rules that you need to follow when you fill the orders:
1.	Fill as many orders as possible
2.	Put parties as close to the front as possible.
3.	If there are not enough seats available in the theater to handle a party, tell them "Sorry, we can't handle your party."
4.	Each party must sit in a single row in a single section.  If they won't fit, tell them "Call to split party".

Your program must parse a theater layout and a list of ticket requests and produce a list of tickets or explanations in the same order as the requests.

The theater layout is made up of 1 or more rows.  Each row is made up of 1 or more sections separated by a space.

After the theater layout, there is one empty line, followed by 1 or more theater requests.  The theater request is made up of a name followed by a space and the number of requested tickets.


Sample input:
```
6 6
3 5 5 3
4 6 6 4
2 8 8 2
6 6

Smith 2
Jones 5
Davis 6
Wilson 100
Johnson 3
Williams 4
Brown 8
Miller 12
```

Your program must produce results to standard output in the same order as the requests, with the name of the person who requested the ticket and either the row and section of the ticket or the explanations "Sorry, we can't handle your party" or "Call to split party."


Sample output:

```
Smith Row 1 Section 1
Jones Row 2 Section 2
Davis Row 1 Section 2
Wilson Sorry, we can't handle your party.
Johnson Row 2 Section 1
Williams Row 1 Section 1
Brown Row 4  Section 2
Miller Call to split party.
```
