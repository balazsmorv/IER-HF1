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
  public static final int CHARGER = 8; 
  public static final int CAR = 0;
  public static int chargeLeft = 100;
  
  
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
		  } else {
			  //return false;
			  System.out.println("else ág az executeActionben. return false lenne az elvárt viselkedés");
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
	  clearPercepts();
	  
	  Location carLoc = model.getAgPos(CAR);
      Location chargerLoc = model.getAgPos(CHARGER);

      Literal carPos = Literal.parseLiteral("pos(car," + carLoc.x + "," + carLoc.y + ")");
      Literal chargerPos = Literal.parseLiteral("pos(charger1," + chargerLoc.x + "," + chargerLoc.y + ")");
      Literal destination = Literal.parseLiteral("pos(dest,15,0)");
      
      addPercept(carPos);
      addPercept(chargerPos);
      
      

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
		  setAgPos(CAR, r1);
		  chargeLeft--;
		  setAgPos(CHARGER, getAgPos(CHARGER)); // just to draw it in the view
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
        } else {
        	c = Color.green;
        	label = "Charger";
        }
        super.drawAgent(g, x, y, c, -1);
        if (id == CAR) {
            g.setColor(Color.black);
        } else {
            g.setColor(Color.white);
        }
        super.drawString(g, x, y, defaultFont, label);
        repaint();
    }
  }
  
  
}
















