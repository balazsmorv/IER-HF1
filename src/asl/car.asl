// Agent car in project helloworld

/* Initial beliefs and rules */

at(P) :- pos(P,X,Y) & pos(car,X,Y).

/* Initial goals */

!at(charger11).

/* Plans */

+!at(dest) : at(dest).

+!at(dest)
		 <- ?pos(dest,X,Y);
		 	move_towards(X,Y);
		 	!at(dest).
		 	


+!at(charger11) : at(charger11) & battery_charge(100) <- !at(dest);
														.print("im going to destination :)").

+!at(charger12) : at(charger12) & battery_charge(100) <- !at(dest);
														.print("im going to destination :)").

+!at(charger13) : at(charger13) & battery_charge(100) <- !at(dest);
														.print("im going to destination :)").

+!at(charger14) : at(charger14) & battery_charge(100) <- !at(dest);
														.print("im going to destination :)").
														
+!at(charger11) : at(charger11) & not battery_charge(100)
				 <- charge(0);
				 .print("im charging :))")
				 !at(charger11).

+!at(charger12) : at(charger12) & not battery_charge(100)
				 <- charge(1);
				 .print("im charging :))")
				 !at(charger12).				 

+!at(charger13) : at(charger13) & not battery_charge(100)
				 <- charge(2);
				 .print("im charging :))")
				 !at(charger13).

+!at(charger14) : at(charger14) & not battery_charge(100)
				 <- charge(3);
				 .print("im charging :))")
				 !at(charger14).
				 
+!at(charger11) <- ?pos(charger11,X,Y);
           move_towards(X,Y);
           !at(charger11).
 
+!at(charger12) <- ?pos(charger12,X,Y);
           move_towards(X,Y);
           !at(charger12).		   
           
+!at(charger13) <- ?pos(charger13,X,Y);
           move_towards(X,Y);
           !at(charger13).	           
           
+!at(charger14) <- ?pos(charger14,X,Y);
           move_towards(X,Y);
           !at(charger14).	           
           