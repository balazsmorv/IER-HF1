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
  public static int chargeLeft = 100;
  
  /* Változtatható paraméterek */
  
  public static int consumption = 1; // fogyasztás/kocka
  
  // Töltők gyorsasága: mennyi energiát adjon körönként az autónak
  public static int chargeRate0 = 1;
  public static int chargeRate1 = 2;
  public static int chargeRate2 = 3;
  public static int chargeRate3 = 10;
  /*---------------------------*/
  
  public static final int CHARGER = 1;
  public static final int CHARGER1 = 2;
  public static final int CHARGER2 = 3;
  public static final int CHARGER3 = 4;
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
      Location chargerLoc = model.getAgPos(CHARGER);

      Literal carPos = Literal.parseLiteral("pos(car," + carLoc.x + "," + carLoc.y + ")");
      Literal chargerPos = Literal.parseLiteral("pos(charger1," + chargerLoc.x + "," + chargerLoc.y + ")");
      Literal destination = Literal.parseLiteral("pos(dest,15,0)");
      
      addPercept(carPos);
      addPercept(chargerPos);
      addPercept(destination);
      addPercept(Literal.parseLiteral("battery_charge(" + (chargeLeft) + ")"));

  }
  
  
  class WorldModel extends GridWorldModel {
	  
	 

	  protected WorldModel() {
		  super(gridSize, gridSize, 9); // width, height, number of agents
		
		  try {
			  setAgPos(CAR, gridSize/2, gridSize - 1);
			  setAgPos(CHARGER, 0, gridSize/2);
			
		  } catch(Exception e) {
			  e.printStackTrace();
		  }
		  
		  add(DEST, 15, 0);
		
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
		  setAgPos(CHARGER, getAgPos(CHARGER)); // just to draw it in the view
		  updatePercepts();
	  }
	  
	  void chargeCar(int chargerID) {
		  switch(chargerID) {
		  case 0:
			  chargeLeft += chargeRate0;
			  break;
		  case 1:
			  chargeLeft += chargeRate1;
			  break;
		  case 2:
			  chargeLeft += chargeRate2;
			  break;
		  case 3:
			  chargeLeft += chargeRate3;
			  break;
		  }
		  if(chargeLeft >= 100) {
			  chargeLeft = 100;
		  }
		  setAgPos(CAR, getAgPos(CAR)); // redraw with the current charge level
		  updatePercepts();
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
        } else if (id == CHARGER) {
        	c = Color.green;
        	label = "Charger";
        }
        super.drawAgent(g, x, y, c, -1);
        if (id == CAR) {
            g.setColor(Color.black);
        } else if(id == CHARGER){
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
















