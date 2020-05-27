// Agent charger4 in project helloworld

/* Initial beliefs and rules */

/* Initial goals */

!start.

/* Plans */

+!start <- .print("I'm charger4.");
		   !place_bid.
		   
+!place_bid : can_bid(0) <- !place_bid.
					
+!place_bid : can_bid(1) <- ?pos(car,X,Y);
							bid(X,Y,4);
							!sleep.

+!place_bid <- !place_bid.
							
+!sleep <- !sleep.