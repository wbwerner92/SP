import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SPApplet extends Applet
{
  private BScreen bs;
  
  public void init()
  {
    bs = new BScreen();
    
    Area area2 = new Area("Area 2", bs);
    area2.createHallway();

    Bod bo2 = new Bod();
    bo2.setName("Wheeler");
    bo2.generateSpy();
    bo2.setHP(bo2.getVi() * 10);
    bo2.setAP(bo2.getEnd());
    area2.spaces[2][1].enter(bo2);
    
    bs.addArea(area2);
    bs.showArea(area2);
    
    add(bs.getContentPane());
  }
  
  
}