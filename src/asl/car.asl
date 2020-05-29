// Agent car in project helloworld

/* Initial beliefs and rules */

at(P) :- pos(P,X,Y) & pos(car,X,Y).

/* Initial goals */

!start.

/* Plans */

+!start
		<- startAuction;
			!bidsArrive.

+!bidsArrive : not charger1_bid(0) & not charger2_bid(0) & not charger3_bid(0) & not charger4_bid(0)
		<-  decideWinner;
			!findWinner.
		
+!bidsArrive <- !bidsArrive.

+!findWinner : winner(1)
		<- !at(charger1).
	
+!findWinner : winner(2)
		<- !at(charger2).
		
+!findWinner : winner(3)
		<- !at(charger3).
		
+!findWinner : winner(4)
		<- !at(charger4).
		
+!at(dest) : at(dest).

+!at(dest)
		 <- ?pos(dest,X,Y);
		 	move_towards(X,Y);
		 	!at(dest).
		 	


+!at(charger1) : at(charger1) & battery_charge(100) <- !at(dest);
														.print("im going to destination :)").

+!at(charger2) : at(charger2) & battery_charge(100) <- !at(dest);
														.print("im going to destination :)").

+!at(charger3) : at(charger3) & battery_charge(100) <- !at(dest);
														.print("im going to destination :)").

+!at(charger4) : at(charger4) & battery_charge(100) <- !at(dest);
														.print("im going to destination :)").
														
+!at(charger1) : at(charger1) & not battery_charge(100)
				 <- charge(0);
				 .print("im charging :))")
				 !at(charger1).

+!at(charger2) : at(charger2) & not battery_charge(100)
				 <- charge(1);
				 .print("im charging :))")
				 !at(charger2).				 

+!at(charger3) : at(charger3) & not battery_charge(100)
				 <- charge(2);
				 .print("im charging :))")
				 !at(charger3).

+!at(charger4) : at(charger4) & not battery_charge(100)
				 <- charge(3);
				 .print("im charging :))")
				 !at(charger4).
				 
+!at(charger1) <- ?pos(charger1,X,Y);
           move_towards(X,Y);
           !at(charger1).
 
+!at(charger2) <- ?pos(charger2,X,Y);
           move_towards(X,Y);
           !at(charger2).		   
           
+!at(charger3) <- ?pos(charger3,X,Y);
           move_towards(X,Y);
           !at(charger3).	           
           
+!at(charger4) <- ?pos(charger4,X,Y);
           move_towards(X,Y);
           !at(charger4).	           
           