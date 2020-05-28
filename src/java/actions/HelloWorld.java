// Internal action code for project helloworld

package actions;

import jason.asSyntax.*;
import jason.environment.*;
import jason.environment.grid.GridWorldModel;
import jason.environment.grid.GridWorldView;
import jason.environment.grid.Location;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Random;
import java.util.logging.Logger;

public class HelloWorld extends Environment {

  private Logger logger = Logger.getLogger("testenv.mas2j."+HelloWorld.class.getName());
  public static final int gridSize = 30;
  public static int auction = 0;
  
  /* Valtoztathato parameterek */
  
  public static int consumption = 1; 							// fogyasztas/kocka
  public static int chargeLeft = 100;							// auto kezdeti toltottsege (max 100, toltok 100-ra toltik)
  public static int[] chargeRates = {1, 2, 3, 10};	 			// Toltok gyorsasaga sorrendben: mennyi energiat adjon koronkent az autonak
  public static Location destination = new Location(15, 0);		// cel helyzete
  public static Location auto = new Location(15, 29);			// auto helyzete
  
  public static Location c1 = new Location(0, 15);				// toltok helyzete
  public static Location c2 = new Location(29, 15);				//
  public static Location c3 = new Location(15, 15);				//
  public static Location c4 = new Location(8, 10);				//
  
  /*---------------------------*/
  
  public static final int CHARGER1 = 1;
  public static final int CHARGER2 = 2;
  public static final int CHARGER3 = 3;
  public static final int CHARGER4 = 4;
  public static final int CAR = 0;
  public static final int DEST = 7;
  
  
  
  
  private WorldModel model;
  private WorldView view;

  /** Called before the MAS execution with the args informed in .mas2j */
  @Override
  public void init(String[] args) { 
	  model = new WorldModel();
	  view = new WorldView(model);
	  model.setView(view);
	  updatePercepts();
  }

  @Override
  public boolean executeAction(String agName, Structure action) {
	  logger.info(agName + " " + action);
	  
	  try {
		  
		  if(action.getFunctor().equals("move_towards")) {
              int x = (int)((NumberTerm)action.getTerm(0)).solve();
              int y = (int)((NumberTerm)action.getTerm(1)).solve();
              model.moveTowards(x,y);
		  } 
		  
		  if(action.getFunctor().equals("charge")) {
			  model.chargeCar((int) ((NumberTerm)action.getTerm(0)).solve());
		  }
		  
		  if(action.getFunctor().equals("bid")) {
			  int x = (int)((NumberTerm)action.getTerm(0)).solve();
              int y = (int)((NumberTerm)action.getTerm(1)).solve();
			  int n = (int)((NumberTerm)action.getTerm(2)).solve();
              model.bid(x,y,n);
		  }
		  
		  if(action.getFunctor().equals("startAuction")) {
			  model.startAuction();
		  }
		  
	  } catch (Exception e) {
		  e.printStackTrace();
	  }

	  updatePercepts();
	  
	  try {
		  Thread.sleep(200);
	  } catch(Exception e) {
		  e.printStackTrace();
	  }
	  informAgsEnvironmentChanged();
	  return true;
  }

  /** Called before the end of MAS execution */
  @Override
  public void stop() {
    super.stop();
  }
  
  void updatePercepts() {
	  System.out.println("updating percepts.");
	  clearPercepts();
	  
	  Location carLoc = model.getAgPos(CAR);
      Location charger1Loc = model.getAgPos(CHARGER1);
	  Location charger2Loc = model.getAgPos(CHARGER2);
	  Location charger3Loc = model.getAgPos(CHARGER3);
	  Location charger4Loc = model.getAgPos(CHARGER4);

      Literal carPos = Literal.parseLiteral("pos(car," + carLoc.x + "," + carLoc.y + ")");
      Literal charger1Pos = Literal.parseLiteral("pos(charger1," + charger1Loc.x + "," + charger1Loc.y + ")");
	  Literal charger2Pos = Literal.parseLiteral("pos(charger2," + charger2Loc.x + "," + charger2Loc.y + ")");
	  Literal charger3Pos = Literal.parseLiteral("pos(charger3," + charger3Loc.x + "," + charger3Loc.y + ")");
	  Literal charger4Pos = Literal.parseLiteral("pos(charger4," + charger4Loc.x + "," + charger4Loc.y + ")");
      Literal dest = Literal.parseLiteral("pos(dest," + destination.x + "," + destination.y + ")");
      
      addPercept(carPos);
      addPercept(charger1Pos);
	  addPercept(charger2Pos);
	  addPercept(charger3Pos);
	  addPercept(charger4Pos);
      addPercept(dest);
      addPercept(Literal.parseLiteral("battery_charge(" + (chargeLeft) + ")"));
	  addPercept(Literal.parseLiteral("can_bid(" + (auction) + ")"));

  }
  
  
  class WorldModel extends GridWorldModel {
	  
	 

	  protected WorldModel() {
		  super(gridSize, gridSize, 9); // width, height, number of agents
		
		  try {
			  setAgPos(CAR, auto.x, auto.y);
			  setAgPos(CHARGER1, c1.x, c1.y);
			  setAgPos(CHARGER2, c2.x, c2.y);
			  setAgPos(CHARGER3, c3.x, c3.y);
			  setAgPos(CHARGER4, c4.x, c4.y);
			
		  } catch(Exception e) {
			  e.printStackTrace();
		  }
		  
		  add(DEST, destination.x, destination.y);
		
	  }
	  
	  void moveTowards(int x, int y) throws Exception {
		  if(chargeLeft < 0) {
			  System.out.println("Out of charge");
		  }
		  Location r1 = getAgPos(CAR);
		  if (r1.x < x)
			  r1.x++;
		  else if (r1.x > x)
			  r1.x--;
		  if (r1.y < y)
			  r1.y++;
		  else if (r1.y > y)
			  r1.y--;
		  chargeLeft -= consumption;
		  setAgPos(CAR, r1);
		  setAgPos(CHARGER1, getAgPos(CHARGER1)); // just to draw it in the view
		  setAgPos(CHARGER2, getAgPos(CHARGER2));
		  setAgPos(CHARGER3, getAgPos(CHARGER3));
		  setAgPos(CHARGER4, getAgPos(CHARGER4));
		  updatePercepts();
	  }
	  
	  void chargeCar(int chargerID) {
		  switch(chargerID) {
		  case 0:
			  chargeLeft += chargeRates[0];
			  break;
		  case 1:
			  chargeLeft += chargeRates[1];
			  break;
		  case 2:
			  chargeLeft += chargeRates[2];
			  break;
		  case 3:
			  chargeLeft += chargeRates[3];
			  break;
		  }
		  if(chargeLeft >= 100) {
			  chargeLeft = 100;
		  }
		  setAgPos(CAR, getAgPos(CAR)); // redraw with the current charge level
		  updatePercepts();
	  }
	  
	  void bid(int x, int y, int n) {
		  Location ch = getAgPos(CAR);
		  switch(n) {
			case 1:
			  	ch = getAgPos(CHARGER1);
				break;
			case 2:
			  	ch = getAgPos(CHARGER2);
				break;
			case 3:
			  	ch = getAgPos(CHARGER3);
				break;
			case 4:
			  	ch = getAgPos(CHARGER4);
				break;
		  }
		  int batteryAtCharger = chargeLeft - (calculateDistance(ch.x, ch.y, x, y) * consumption);
		  int chargeTime = 0;
		  while (batteryAtCharger < 100) {
			  chargeTime++;
			  batteryAtCharger += chargeRates[n-1];
		  }
		  int bid_value = calculateDistance(ch.x, ch.y, x, y) + calculateDistance(destination.x, destination.y, x, y) + chargeTime;
		  System.out.println(n + " bid: " + bid_value);
		  updatePercepts();
	  }
	  
	  void startAuction() {
		  auction = 1;
		  updatePercepts();
	  }
	  
	  int calculateDistance(int x1, int y1, int x2, int y2) {
		  int d = 0;
		  while((x1 != x2 && y1 != y2) || (x1 != x2 && y1 == y2) || (x1 == x2 && y1 != y2)) {
			d++;
			if (x1 < x2)
			  x1++;
			else if (x1 > x2)
			  x1--;
			if (y1 < y2)
			  y1++;
			else if (y1 > y2)
			  y1--;  
		  }
		  return d;
	  }
	  
  }
  
  
  class WorldView extends GridWorldView {

	public WorldView(WorldModel model) {
		super(model, "Jason", 600);
		defaultFont = new Font("Arial", Font.PLAIN, 20);
		setVisible(true);
		repaint();
	}
	
	@Override
    public void drawAgent(Graphics g, int x, int y, Color c, int id) {
        String label = "";
        if (id == CAR) {
            c = Color.blue;
            label = "Car(" + chargeLeft + ")";
        } else if (id == CHARGER1 || id == CHARGER2 || id == CHARGER3 || id == CHARGER4) {
        	c = Color.green;
        	label = "Charger";
        }
        super.drawAgent(g, x, y, c, -1);
        if (id == CAR) {
            g.setColor(Color.black);
        } else if(id == CHARGER1 || id == CHARGER2 || id == CHARGER3 || id == CHARGER4){
            g.setColor(Color.white);
        }
        super.drawString(g, x, y, defaultFont, label);
        repaint();
    }
	
	@Override
	public void draw(Graphics g, int x, int y, int object) {
		System.out.println("draw");
		if(object == HelloWorld.DEST) {
			drawDest(g, x, y);
		}
	}
	
	public void drawDest(Graphics g, int x, int y) {
		super.drawObstacle(g, x, y);
		g.setColor(Color.red);
		drawString(g, x, y, defaultFont, "Destination");
	}
	
  }
  
  
}
















