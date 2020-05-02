// Agent car in project helloworld

/* Initial beliefs and rules */

at(P) :- pos(P,X,Y) & pos(car,X,Y).


/* Initial goals */

!at(charger1).

/* Plans */

+!at(dest) : at(dest).

+!at(dest)
		 <- ?pos(dest,X,Y);
		 	move_towards(X,Y);
		 	!at(dest).
		 	


+!at(charger1) : at(charger1) <- !at(dest).
 

+!at(charger1) <- ?pos(charger1,X,Y);
           move_towards(X,Y);
           !at(charger1).