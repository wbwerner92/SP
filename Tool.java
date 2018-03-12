import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class Tool extends Equipment
{
  int pow;
  int charge;
  int maxCharge;
  boolean soft;
  boolean hard;
  boolean med;
  boolean rob;
  boolean mon;
  
  Tool(){}
  Tool(String n, int p, int nR, int xR, int c, boolean s, boolean h, boolean m, boolean r)
  {
    setName(n);
    pow = p;
    setMinR(nR); setMaxR(xR);
    charge = maxCharge = c;
    soft = s; hard = h; med = m; rob = r;
    setIsTool(true);
    setEPow("Pow: " + pow);
  }
  
  public String getEUse(){return ("Charge: " + charge + "/" + maxCharge);}
  
  public void equip(Bod b)
  {
    if (soft && b.getSec() == 0) return;
    if (hard && b.getEng() == 0) return;
    if (med && b.getMed() == 0) return;
    
    if (b.getPrimary() == null) b.setPrimary(this);
    else if (b.getSecondary() == null) b.setSecondary(this);
    else return;
    
    setUser(b);
  }
  
  // Valid Target methods
  public boolean isValidTar(Mob b)
  {   
    if (b == null) return false;
    if (b.isWall) return false;
    
    System.out.println("Validating " + b.getName());
    
    boolean valid = true;
    
    Bod user = getUser(); 
    int dist = Math.abs(user.getPX() - b.getPX()) + Math.abs(user.getPY() - b.getPY());
    System.out.println("The distance is " + dist);
    if (dist != 1)  valid = false;
    
    // Door check
    if (b.getIsDoor())
    {
      System.out.println("Verifying Door distance");
      Door d = (Door)b;
      int dist1 = Math.abs(user.getPX() - d.pOne.posX) + Math.abs(user.getPY() - d.pOne.posY);
      int dist2 = Math.abs(user.getPX() - d.pTwo.posX) + Math.abs(user.getPY() - d.pTwo.posY);
      
      if (dist1 == 1 || dist2 == 1) {valid = true; System.out.println("Distance passes");}
      else System.out.println("Still not close enough");
    }
    
    if (b.broke) valid = false;
    
    if ((b.getIsComp() || b.getIsDoor()) && !soft) {System.out.println("Error: Not a soft tool"); valid = false;}
    if (b.isGear && !hard) valid = false;
    
    if (valid == true) {System.out.println(b.getName() + " is a valid target"); return true;}
    else {System.out.println(b.getName() + " is NOT a valid target"); return false;}
  }
  public boolean isValidTar(Bod b)
  {
    if (b == null) return false;  // No target
    if (b.getIsBod() && hard && (b.getShield() == null || b.getShield().getBroke())) return false; // Cannot shock broken shields
    if (soft && !hard) return false; // Cannot use soft tools on living units
    if (med && (b.getDead() || b.getHP() == (b.getVi() * 10))) return false; // Can't heal dead or full health unit
    if (!med && b.getDead()) return false; // Cannot use non medical tools on dead units

    Bod user = getUser();
    int dist = Math.abs(user.getPX() - b.getPX()) + Math.abs(user.getPY() - b.getPY());
    if (dist >= getMinR() && dist <= getMaxR()) {System.out.println("Can Target Bod"); return true;}
    
    return false;
  } 
  
  public void useTool(Bod b)
  {
    System.out.println("Using " + getName());
    if (getUser().getIsBod() && b.getIsBot() && rob) setRoboTask((Bot)b);
    else if (hard) shock(b);
    else if (med) treatInjury(b);
    else if (mon) monsterAtk(b);
    
    Animator ani = new Animator();
    ani.runAnimation(b.getLocal(), getAImg(), 1, 100, 100);
  }
  public void useTool(Mob m)
  {
    if ((m.getIsComp() || m.getIsDoor()) && soft) hack(m);
    Animator ani = new Animator();
    ani.runAnimation(m.getLocal(), getAImg(), 1, 100, 100);
  }
  
  public void maint(){charge = maxCharge;}
  
  // Shock Lifeform
  public void shock(Bod b)
  {
    System.out.println("Shocking");
    setAImg(new ImageIcon(getClass().getResource("Shock.gif")));
    
    if (b.getShield().getBroke()) return;
    else
    {
      b.getShield().intercept(pow);
      charge --;
    }
  }
  // Repair Mechanical Items
  public void repair(Bod b)
  {
    System.out.println("Repairing");
    setAImg(new ImageIcon(getClass().getResource("Shock.gif")));
    Shield s = b.getShield();
    
    if (s.getSh() < s.getShMax()) s.setSh(s.getSh() + pow);
    if (s.getSh() > s.getShMax()) s.setSh(s.getShMax());
  }
  // Hack computers & security
  public void hack(Mob m)
  {
    setMsg("Hacking " + m.getName());
    setAImg(new ImageIcon(getClass().getResource("Jack.gif")));
    
    if (m.getPowLvl() > 0)
    {
      addMsg("Hack = (" + pow + "), Security % = (" + m.getPowLvl() + ")");
      RVCalc calc = new RVCalc();
      int secRoll = calc.rInt(0, m.getPowLvl());
      addMsg("Security = " + secRoll);
      
      if (secRoll < pow) {m.setPowLvl(m.getPowLvl() - (pow - secRoll)); addMsg("Hacked " + (pow - secRoll));}
      else addMsg("Hack blocked");
    }
    if (m.getPowLvl() <= 0) 
    {
      m.setPowLvl(0);
      m.broke = true;
      addMsg(m.getName() + " hacked!");
      
      if (m.getIsDoor())
      {
        Door d = (Door)m;
        d.pOne.updateMob();
        d.pTwo.updateMob();
      }
    }
    
    charge--;
  }
  public void treatInjury(Bod b)
  {
    System.out.println("Healing");
    setAImg(new ImageIcon(getClass().getResource("Heal.gif")));
    
    if ((b.getHP() < (b.getVi() * 10)) && !b.getDead())
    {
      b.setHP(b.getHP() + pow);
      if (b.getHP() > b.getVi() * 10) b.setHP(b.getVi() * 10);
    }
    
    charge--;
  }
  // Set Robot directives
  public void setRoboTask(Bot b)
  {
    setMsg("Programming " + b.getName() + "\n");
    setAImg(new ImageIcon(getClass().getResource("Jack.gif")));
    
    final JDialog frame = new JDialog();
    frame.setSize(180, 100);
    frame.setUndecorated(true);
    frame.setLocationRelativeTo(b.getLocal());
    
    JPanel north = new JPanel(new BorderLayout());
    ImageIcon ii = new ImageIcon(getClass().getResource("Jack.gif"));
    Image i = ii.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT);
    ii = new ImageIcon(i);
    JLabel jl2 = new JLabel();
    jl2.setIcon(ii);
    north.add(jl2, BorderLayout.WEST);
    JLabel jl = new JLabel("Set Robot Directive", SwingConstants.LEFT);
    north.add(jl, BorderLayout.EAST);
    
    
    String[] ops = {"Act", "Wait"};
    JPanel opsPanel = new JPanel(new GridLayout(ops.length, 1)); 
    final Bot bot = b;
    for (int j = 0; j < ops.length; j++)
    {
      final JButton bu = new JButton(ops[j]);
      bu.addActionListener(new ActionListener()
                            {
        public void actionPerformed(ActionEvent e)
        {
          bot.setDirective(bu.getText());
          System.out.println("Directive set: " + bot.getDirective());
          frame.setModal(false);
          frame.dispose();
        }
      });
      opsPanel.add(bu);
    }
    
    JPanel panel = new JPanel(new BorderLayout());
    panel.add(north, BorderLayout.NORTH);
    panel.add(opsPanel, BorderLayout.CENTER);
    frame.add(panel);
    frame.setModal(true);
    frame.setVisible(true);
    
    charge--;
  }
  
  public void monsterAtk(Bod b)
  {
    System.out.println("Monster Attack");
    
    setAImg(new ImageIcon(getClass().getResource("Claw")));
    
    b.takeDamage(pow);
  }
  
  public int getCharge(){return charge;}
  public int getMaxCharge(){return maxCharge;}
  
  public void generateOmniTool()
  {
    setName("OmniTool V.1");
    pow = 5;
    setMinR(1);
    setMaxR(1);
    charge = maxCharge = 5;
    soft = true; hard = true; med = false; rob = true;
    setEImg(new ImageIcon(getClass().getResource("OmniTool.png")));
    setAImg(new ImageIcon(getClass().getResource("Shock.gif")));
    setIsTool(true);
    setEPow("Pow: " + pow);
  }
  // Monster weapons
  public void generateMonsterClaw(Mon m, String n, int p)
  {
    setUser(m);
    m.setPrimary(this);
    setName(m.getName() + " " + n);
    pow = p;
    setMinR(1);
    setMaxR(1);
    // Charge handling
    soft = false; hard = false; med = false; rob = false; mon = true;
    setIsTool(true);
    setEPow("Pow: " + pow);
  }
}