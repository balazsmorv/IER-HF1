// Agent charger1 in project helloworld

/* Initial beliefs and rules */

/* Initial goals */

!start.

/* Plans */

+!start <- .print("I'm charger1.");
		   !place_bid.
		   
+!place_bid : can_bid(0) <- !place_bid.
					
+!place_bid : can_bid(1) <- ?pos(car,X,Y);
							bid(X,Y,1);
							!sleep.

+!place_bid <- !place_bid.
							
+!sleep <- !sleep.



