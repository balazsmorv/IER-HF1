// Agent charger1 in project helloworld

/* Initial beliefs and rules */

at(P) :- pos(P,X,Y) & pos(charger1,X,Y).

/* Initial goals */

!start.

/* Plans */

+!start <- .print("I'm a charger.");
		   !bidding.
		   
+!bidding : can_bid(0) <- !bidding.
					
+!bidding : can_bid(1) <- bid(2);
							!sleep.

+!bidding <- !bidding.
							
+!sleep <- !sleep.



