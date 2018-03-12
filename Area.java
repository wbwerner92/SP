import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class Area extends JPanel
{ 
  BScreen bs;
  
  String name;
  int length;
  int width;
  Panel[][] spaces;
  
  Panel active;
  
  boolean visited;
  
  Panel dNorth, dSouth, dEast, dWest; // Door panel
  Panel anchor; // Viewpoint anchor
  Point vp;  // Remembers where last view occured
  
  JPanel aDisplay;
  
  Area(String n, BScreen b)
  {
    name = n; 
    bs = b;
    aDisplay = new JPanel();
  }
  
  // Create a Room with spaces set to a specified size
  public void createBigRoom()
  { 
    length = 12; width = 12;
    
    aDisplay.setPreferredSize(new Dimension(1200, 1200));
    aDisplay.setLayout(new GridLayout(length, width));
    
    setRoomSpaces(); 
    setRoomDoors();
    
    anchor = spaces[length/2][width/2];
    
    add(aDisplay);
  }
  public void createSmallRoom()
  {
    length = 7; width = 7;
    
    aDisplay.setPreferredSize(new Dimension(700, 700));
    aDisplay.setLayout(new GridLayout(length, width));
    
    setRoomSpaces(); 
    setRoomDoors();
    
    anchor = spaces[0][width/2];
    
    add(aDisplay);
  }
  public void setRoomSpaces()
  {
    spaces = new Panel[length][width];
    
    for (int i = 0; i < length; i++)
    {
      for (int j = 0; j < width; j++)
      {
        spaces[i][j] = new Panel(i, j, this);
        aDisplay.add(spaces[i][j]);
        if (i == 0 || j == 0 || i == (length - 1) || j == (length - 1)) spaces[i][j].setVisible(false);
      }
    }
  }
  public void setRoomDoors()
  {
    // Assigns Doors
    int lHalf = length / 2;
    int wHalf = width / 2;
    
    dNorth = spaces[length - 1][wHalf];
    dSouth = spaces[0][wHalf];
    dEast = spaces[lHalf][width - 1];
    dWest = spaces[lHalf][0];
  }
  
  // Creates a Hallway
  public void createHallway()
  {
    aDisplay.setPreferredSize(new Dimension(1200, 500));
    aDisplay.setLayout(new GridLayout(5, 12));
    length = 5;
    width = 12;
    spaces = new Panel[5][12];
    for (int i = 0; i < 5; i++)
    {
      for (int j = 0; j < 12; j++)
      {
        spaces[i][j] = new Panel(i, j, this);
        aDisplay.add(spaces[i][j]);
        if (i == 0 || j == 0 || i == 11 || j == 11 || i == 4) spaces[i][j].setVisible(false);
      }
    }
    
    anchor = spaces[0][0];
    
    // Assigns Doors
    int lHalf = length / 2;
    int wHalf = width / 2;
    
    dNorth = null;
    dSouth = null;
    dEast = spaces[lHalf][width - 1];
    dWest = spaces[lHalf][0];
    
    add(aDisplay);
  }
  
  public void resetPanel(Panel p)
  {
    p.setText(" ");
    p.setFont(new Font("Papyrus", Font.PLAIN, 20));
    p.setForeground(Color.BLACK);
    p.movable = false;
  }
  public void resetPanels()
  {
    bs.acting = false;
    active = null;
    bs.scanning = false;
    
    for (Panel[] pa : spaces)
    {
      for (Panel p : pa)
      {
        resetPanel(p);
      }
    }
    
    bs.checkActive();
  }  
  
  public void enterSquad(Squad sq)
  {
    int eLen = length/2;
    int eWid = 2;
    Panel p1 = spaces[eLen][eWid];
    Panel p2 = spaces[eLen + 1][eWid];
    Panel p3 = spaces[eLen - 1][eWid];
    Panel p4 = spaces[eLen + 1][eWid - 1];
    Panel p5 = spaces[eLen - 1][eWid - 1];
    Panel[] pa = {p1, p2, p3, p4, p5};
    
    for (int i = 0; i < sq.getSize(); i++)
    {
      Bod b = (Bod)sq.getMem(i);
      System.out.println("Entering " + b.getName() + " at num " + i + " in Panel " + pa[i].posX + ", " + pa[i].posY);
      pa[i].enter(b);
    }
  }
  
  // Inner Panel Space Object ----------------------------------------------------------------------------------------
  class Panel extends JButton
  {
    int posX;
    int posY;
    Bod occupant;
    Bod dHold;
    Mob mob;
    
    JLabel bName;
    JLabel bImg;
    
    boolean movable;
    
    boolean entry;
    Area pArea;
    
    Panel(int x, int y, Area a)
    {
      pArea = a;
      posX = x; posY = y;
      //setPreferredSize(new Dimension(100, 100));
      setLayout(new BorderLayout());
      bName = new JLabel("", SwingConstants.CENTER);
      bImg = new JLabel("", SwingConstants.CENTER);
      add(bName, BorderLayout.NORTH);
      add(bImg, BorderLayout.CENTER);
      
      occupant = null;
      setVisible(true);
      
      addActionListener(new ActionListener()
                          {
        public void actionPerformed(ActionEvent e)
        { 
          // Prechecks
          if (dHold != null && active == null) // Dead thing image in place
          {
            System.out.println("Stage: Dead Image return");
            resetPanels(); bs.updateSide(Area.Panel.this); return;
          }
          if (active != null && active.occupant != null && active.occupant.getAP() <= 0 && 
              occupant != null && occupant.getIsBot())
          {
            System.out.println("Stage: Target Bot, No AP case return");
            resetPanels(); bs.updateSide(Area.Panel.this); return;
          }
          if (active != null && active.occupant != null && occupant != null && occupant.getIsBot() && 
              bs.scanning == false && active.occupant.getPrimary().isValidTar(occupant) == false)
          {
            System.out.println("Stage: Target Bot, Not Scanning return");
            resetPanels(); bs.updateSide(Area.Panel.this); return;
          }
          
          // Bot option set
          if (bs.scanning)
          {  
            System.out.println("Stage: Bot Scanning");
            Bot b = (Bot)active.occupant;
            
            b.setDirective("Act");
            b.setTarget(Area.Panel.this);
            
            b.getLocal().exit();
            b.getLocal().enter(b);
            
            resetPanels();
            bs.setEmpty();
            return;
          }
          // Empty Space
          if (occupant == null && mob == null)
          {
            System.out.println(posX + ", " + posY); 
            
            // Gear deployment
            if (active != null && active.occupant != null && active.occupant.getPrimary().isValidTar(Area.Panel.this))
            {
              active.occupant.getPrimary().usePrimary(Area.Panel.this);
              active.occupant.setAP(active.occupant.getAP() - 1);
              if (active.occupant.getAP() > 0 
                    && active.occupant.getAP() < active.occupant.getEnd())
              {
                bs.updateSide(active);
                bs.showActable(active); 
              }
              else {bs.setEmpty(); resetPanels();}
              return;
            }
            
            // Moving: Move, resetPanels, updateSide, showMovable ---------------------
            if (movable && active.occupant.getAP() > 0) 
            {
              setText(" ");
              active.occupant.setAP(active.occupant.getAP() - 1);
              enter(active.exit());
              resetPanels();
              if (occupant.getAP() > 0 && occupant.getAP() < occupant.getEnd())
              {
                bs.updateSide(Area.Panel.this);
                bs.showActable(Area.Panel.this); 
                return;
              }
              else bs.setEmpty();
            }
            else // resetPanels
            {
              resetPanels();
              bs.setEmpty();
            }
          }
          // Occupied space
          else
          {
            if (mob == null) System.out.println(occupant.getName());
            if (occupant == null) System.out.println(mob.getName());
            System.out.println("Stage: Occupied Space event");
            
            // Confirm action variable
            int conf = 10;
            
            System.out.println("Scanning Case 1");
            // #1 if Null set to active, showMovable (Turn acting on)-------------
            if (active == null) 
            {
              System.out.println("NULL");
              if (occupant != null && occupant.getIsBot()) {bs.updateSide(Area.Panel.this); return;}
              
              resetPanels(); 
              bs.updateSide(Area.Panel.this);
              bs.showActable(Area.Panel.this); 
              return;
            }
            System.out.println("Scanning Case 2");
            // #2 if self, reset panel (Turn acting off)--------------------------
            if (active.equals(Area.Panel.this) && !active.occupant.getPrimary().isValidTar(active.occupant))
            {
              System.out.println("Reset");
              resetPanels(); 
              bs.setEmpty();
              return;
            }
            else if (active.equals(Area.Panel.this) && active.occupant.getPrimary().isValidTar(active.occupant)
                       && active.occupant.getAP() > 0)
            {
              // Set Equipment type
              Equipment eq = active.occupant.getPrimary();
              // Self Tool use
              if (eq.getIsImp()) implantOperation(Area.Panel.this);      
              if (eq.getIsTool()) toolOperation(Area.Panel.this);
              
              bs.checkActive();
              return;
            }
            System.out.println("Scanning Case 3");
            // #3 if movable door, ask to shift (Act) ----------------------------------------------------
            // Open door check
            if (mob != null && mob.getIsDoor() && 
                mob.broke == true && active != null && 
                active.occupant != null && movable)
            {
              System.out.println("Moving through Door");
              Bod bod = active.occupant;
              if (JOptionPane.showConfirmDialog(active, "Move to next Area?") == 0)
              {
                System.out.println("Attempting an Area shift.");
                active.occupant.setAP(active.occupant.getAP() - 1);
                Door d = (Door)mob;
                d.moveDoor(active.occupant);
              }
              resetPanels();
              active = bod.getLocal();
              bs.updateSide(active);
              bs.showActable(active);
              bs.checkActive();
              return;
            }
            System.out.println("Scanning Case 4");
            if (mob != null && mob.getIsComp() && 
                mob.broke == true && active != null && 
                active.occupant != null && active.occupant.getAP() > 0)
            {
              Computer c = (Computer)mob;
              c.useComp();
            }
            System.out.println("Scanning Case 5");
            // #5 if other ask to act (Act)------------------------------------------------------------------
            if (!bs.scanning && bs.acting && 
                active.occupant.getAP() > 0 &&
                active.occupant.getPrimary() != null &&
                (active.occupant.getPrimary().isValidTar(occupant) ||
                 active.occupant.getPrimary().isValidTar(mob)))
            {
              System.out.println("Acting");
              
              // Set Equipment type
              Equipment eq = active.occupant.getPrimary();
              // Action methods
              if (eq.getIsGun()) gunOperation(Area.Panel.this);
              else if (eq.getIsGear()) botPackOperation(Area.Panel.this);
              else if (eq.getIsImp()) implantOperation(Area.Panel.this);
              else if (eq.getIsTool()) toolOperation(Area.Panel.this);
              else{}

              bs.checkActive();
              return;
            }
            else 
            {
              System.out.println("Didn't pass anything");
              resetPanels(); 
              bs.updateSide(Area.Panel.this); 
              bs.showActable(Area.Panel.this);
              bs.checkActive();
            }
          }
        }
      });
    }
    
    // Gun Use
    public void gunOperation(Area.Panel ap)
    {
      Gun g = (Gun) active.occupant.getPrimary();
      if (JOptionPane.showConfirmDialog(null, "Shoot " +ap.occupant.getName() + "?") != 0) return;
      
      // Cap check
      if (g.getHeat() >= g.getCap())
      {
        JOptionPane.showMessageDialog(null, "Weapon is overheated");
        return;
      }
      
      // Gunshot
      g.shoot(ap.occupant);
      bs.bottom.setText(g.getMsg());
      
      ap.deadCheck();
      
      apUse();
      useAnimation(ap);
    }
    // Tool use
    public void toolOperation(Area.Panel ap)
    { 
      Tool t = (Tool) active.occupant.getPrimary();
      
      if (JOptionPane.showConfirmDialog(null, "Use " + t.getName() + "?") != 0) return;
      
      // Charge check
      if (t.getCharge() <= 0)
      {
        JOptionPane.showMessageDialog(null, "Tool is out of charge");
        return;
      }
      // Tool animation
      if (ap.mob == null) t.useTool(ap.occupant);
      else if (ap.occupant == null) t.useTool(ap.mob);
      bs.bottom.setText(t.getMsg());
      
      if (ap.mob == null && ap.occupant.getIsBot())
      {
        Bot bot = (Bot)ap.occupant;
        
        // Ownership check
        if (bot.owner != null && !bot.owner.equals(active.occupant))
        {
          JOptionPane.showMessageDialog(bot.getLocal(), "Bot is not responding");
          return;
        }
        
        if (bot.getScanning())
        {
          active.occupant.setAP(active.occupant.getAP() - 1);
          bs.updateSide(active);
          bs.showActableBot(Area.Panel.this); 
          return;
        }
      }
      
      if (ap.occupant == null && ap.mob.broke) ap.updateMob();
      
      apUse();
      useAnimation(ap);
    }
    // BotPack Use
    public void botPackOperation(Area.Panel ap)
    {
      BotPack bp = (BotPack)active.occupant.getPrimary();
      if (JOptionPane.showConfirmDialog(null, "Use " + bp.getName() + "?") != 0) return;
      
      System.out.println("Using Gear");
      bp.usePrimary(ap.occupant);
      
      apUse();
      useAnimation(ap);
    }
    // Implant Use ----------
    public void implantOperation(Area.Panel ap)
    {
      Implant i = (Implant)active.occupant.getPrimary();
      
      // No Psy Set
      if (i.emptyActiveP()) {i.setActivePsy(); return;}
        
      // Use Psy on target
      if (JOptionPane.showConfirmDialog(null, "Use " + active.occupant.getPrimary().getEPow() + "?") != 0) return;
      i.useTool(ap.occupant);
      apUse();
      useAnimation(ap);
    }
    // AP Consumption
    public void apUse()
    {
      // AP drain & image updates 
      active.occupant.setAP(active.occupant.getAP() - 1);
      if (active.occupant.getAP() > 0 
            && active.occupant.getAP() < active.occupant.getEnd())
      {
        bs.updateSide(active);
        bs.showActable(active); 
      }
      else {bs.setEmpty(); resetPanels();} 
    }
    public void useAnimation(Area.Panel ap)
    {
      // Remaining health/shield animations
      if (ap.occupant != null && ap.occupant.getHP() > 0)
      {
        if (ap.mob == null && active != null && active.occupant.getPrimary().getIsTool() && ap.occupant.getIsBot())
        {
          Bot bot = (Bot)ap.occupant;
          if (bot.getScanning()){bs.showActableBot(ap); return;}
          else
          {
            int bSh = 0;
            if (ap.occupant.getShield() != null) 
              bSh = 10 * ap.occupant.getShield().getSh() / ap.occupant.getShield().getShMax();
            if (bSh == 0 && ap.occupant.getShield().getSh() > 0) bSh = 1;
            int bHp = ap.occupant.getHP() / ap.occupant.getVi();
            if (bHp == 0 && ap.occupant.getHP() > 0) bHp = 1;
            bs.ani.runBars(ap, bHp, bSh, 1, 50, 25); 
          }
        }
        else
        {
          int bSh = 0;
          if (ap.occupant.getShield() != null) 
            bSh = 10 * ap.occupant.getShield().getSh() / ap.occupant.getShield().getShMax();
          if (bSh == 0 && ap.occupant.getShield().getSh() > 0) bSh = 1;
          int bHp = ap.occupant.getHP() / ap.occupant.getVi();
          if (bHp == 0 && ap.occupant.getHP() > 0) bHp = 1;
          bs.ani.runBars(ap, bHp, bSh, 1, 50, 25); 
        }                    
      }
    }
    
    
    
    // Utility Methods ---------------------------------------------------------------------------------
    public Point getPanelPoint()
    {
      System.out.println(bs.jsp.getViewport().getViewPosition().getX() + ", " + bs.jsp.getViewport().getViewPosition().getY());
      double setX = ((double)posX/bs.area.length * (double)bs.jsp.getHorizontalScrollBar().getWidth());
      double setY = ((double)posY/bs.area.width * (double)bs.jsp.getVerticalScrollBar().getHeight());
      System.out.println("X percentage = " + setX + ", " + "Y percentage = " + setY);
      
      System.out.println("Vertical SB: " + bs.jsp.getVerticalScrollBar().getWidth() + ", " + bs.jsp.getVerticalScrollBar().getHeight());
      System.out.println("Horizontal SB: " + bs.jsp.getHorizontalScrollBar().getWidth() + ", " + bs.jsp.getHorizontalScrollBar().getHeight());
      
      return new Point((int)setY, (int)setX);
    }
    
    public void deadCheck()
    {
      // Dead Check
      if (occupant.getDead()) 
      {
        Bod m = exit();
        enter(m);
      }
    }
    
    // Set a Mob object into an available Panel
    public boolean setMob(Mob m)
    {
      if (occupant != null || mob != null) return false;
      
      mob = m;
      m.setLocal(this);
      
      ImageIcon ii = null;
      if (mob.broke) ii = mob.getIH();
      else ii = mob.getII();
      Image im = ii.getImage().getScaledInstance(90, 90, Image.SCALE_DEFAULT);
      ii = new ImageIcon(im);
      bImg.setIcon(ii); 
      
      mob.setPX(posX); mob.setPY(posY);
      
      return true;
    } 
    public void updateMob()
    {
      bImg.setIcon(null);
      
      ImageIcon in = null;
      if (mob.broke) in = mob.getIH();
      else in = mob.getII();
      
      Image in2 = in.getImage().getScaledInstance(90, 90, Image.SCALE_DEFAULT);
      bImg.setIcon(new ImageIcon(in2)); 
    }
    
    // Bod enters Panel
    public boolean enter(Bod b)
    {
      if (occupant != null || mob != null) return false;
      
      if (b.getDead() == true) dHold = b;
      else occupant = b;
      
      b.setLocal(this);
      
      //Sets Panel display     
      bName.setText(b.getName());
      
      ImageIcon ii = b.getII();
      Image im = ii.getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT);
      ii = new ImageIcon(im);
      bImg.setIcon(ii);
      
      // Sets position
      b.setPX(posX); b.setPY(posY);
      
      // Adds Bot/Bod to Stack if newly added
      if (b.getIsBot() && bs.bots.search(b) == -1) bs.bots.push((Bot)b);
      else if (bs.peeps.search(b) == -1) bs.peeps.push(b);
      
      return true;
    }
    
    public Bod exit()
    {
      Bod moving = occupant;
      occupant = null;
      
      bName.setText("");
      bImg.setIcon(null);
      
      if (dHold != null)
      {
        bName.setText(dHold.getName());
        ImageIcon ii = dHold.getII();
        Image im = ii.getImage().getScaledInstance(80, 80, Image.SCALE_DEFAULT);
        ii = new ImageIcon(im);
        bImg.setIcon(ii);
      }
      
      return moving;
    }
    
    public void moveBotTo(Panel p)
    {
      bs.ani.runAnimationTalk(this, occupant.getMovMsg(), 2, 150, 30);
      
      int x = posY; int y = posX;
      int x2 = p.posY; int y2 = p.posX;
      
      // Determines weather to move horizontally or vertically
      int hor = Math.abs(x - x2);
      int ver = Math.abs(y - y2);
      
      // Attempted Horizontal move
      if (hor >= ver)
      {
        // Attempted Left Move
        if (x > x2)
        {
          if (moveLeft()) return;
          else if (y > y2) {if (moveUp()) return; else if (moveDown()) return;}
          else if (y2 > y) {if (moveDown()) return; else if (moveUp()) return;}
          else if (y == y2)
          {
            RVCalc calc = new RVCalc();
            if (calc.rInt(1, 2) == 1) {if (moveUp()) return; else if (moveDown()) return;}
            else {if (moveDown()) return; else if (moveUp()) return;}
          }
        }
        // Attempted Right Move
        else if (x2 > x)
        {
          if (moveRight()) return;
          else if (y > y2) {if (moveUp()) return; else if (moveDown()) return;}
          else if (y2 > y) {if (moveDown()) return; else if (moveUp()) return;}
          else if (y == y2)
          {
            RVCalc calc = new RVCalc();
            if (calc.rInt(1, 2) == 1) {if (moveUp()) return; else if (moveDown()) return;}
            else {if (moveDown()) return; else if (moveUp()) return;}
          }
        }
      }
      // Attempted Vertical move
      else
      {
        // Attempted Up Move
        if (y > y2)
        {
          if (moveUp()) return;
          else if (x > x2) {if (moveLeft()) return; else if (moveRight()) return;}
          else if (x2 > x) {if (moveRight()) return; else if (moveLeft()) return;}
          else if (x == x2)
          {
            RVCalc calc = new RVCalc();
            if (calc.rInt(1, 2) == 1) {if (moveLeft()) return; else if (moveRight()) return;}
            else {if (moveRight()) return; else if (moveLeft()) return;}
          }
        }
        // Attempted Down Move
        if (y2 > y)
        {
          if (moveDown()) return;
          else if (x > x2) {if (moveLeft()) return; else if (moveRight()) return;}
          else if (x2 > x) {if (moveRight()) return; else if (moveLeft()) return;}
          else if (x == x2)
          {
            RVCalc calc = new RVCalc();
            if (calc.rInt(1, 2) == 1) {if (moveLeft()) return; else if (moveRight()) return;}
            else {if (moveRight()) return; else if (moveLeft()) return;}
          }
        }
      }
    }
    public void findPath(Area.Panel from, Area.Panel to, int d) // Recursively searches bordering panels until dist runs out
    {
      if (d == 0) return;
      if (to.mob != null && to.mob.getIsDoor())
      {
        System.out.println("Examining the mob");
      }
      else if (to.mob != null) return;
      
      // Sets Movable panels to green/movable && continues search through this panel
      if (to.occupant == null)
      {
        to.setText("\u25ce");
        to.setFont(new Font("Papyrus", Font.PLAIN, 50));
        to.setForeground(Color.GREEN.darker());
        
        to.movable = true;
      }
      
      Stack<Area.Panel> pans = validPanels(to, from);
      while (pans.empty() == false)
      {
        Panel p = pans.pop();
        findPath(to, p, (d - 1)); // Recursive call
      }
    }
    public Stack<Area.Panel> validPanels(Area.Panel check, Area.Panel not) // Returns a Stack containing all valid bordering panels
    {
      Stack<Area.Panel> vPans = new Stack<Panel>();
      
      int x = check.posX; int y = check.posY;
      
      if (x > 0)
      {
        Panel pot = spaces[x-1][y];
        if (!pot.equals(not) && pot.isVisible()) {vPans.push(pot);}
      }
      if (x < (length - 1))
      {
        Panel pot = spaces[x+1][y];
        if (!pot.equals(not) && pot.isVisible()) {vPans.push(pot);}
      }
      if (y > 0)
      {
        Panel pot = spaces[x][y-1];
        if (!pot.equals(not) && pot.isVisible()) {vPans.push(pot);}
      }
      if (y < (width - 1))
      {
        Panel pot = spaces[x][y+1];
        if (!pot.equals(not) && pot.isVisible()) {vPans.push(pot);}
      }
      
      return vPans;
    }
    
    public boolean canMove(Area.Panel p)
    {
      if (p.mob == null && p.occupant == null) return true;
      else return false;
    }
    public boolean moveUp() 
    {
      Panel p = spaces[posX-1][posY];
      if (canMove(p)) {p.enter(exit()); System.out.println("Moving Up"); return true;}
      else return false;
    }
    public boolean moveDown()
    {
      Panel p = spaces[posX+1][posY];
      if (canMove(p)) {p.enter(exit()); System.out.println("Moving Down"); return true;}
      else return false;
    }
    public boolean moveRight() 
    {
      Panel p = spaces[posX][posY+1];
      if (canMove(p)) {p.enter(exit()); System.out.println("Moving Right"); return true;}
      else return false;
    }
    public boolean moveLeft()
    {
      Panel p = spaces[posX][posY-1];
      if (canMove(p)) {p.enter(exit()); System.out.println("Moving Left"); return true;}
      else return false; 
    }
  }
}

