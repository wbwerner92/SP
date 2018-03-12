import java.util.LinkedList;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class Implant extends Tool
{
  public enum PType {DMG, HEAL, SHIELD};
  private int mem;  // Memory
  private LinkedList<Implant.Psy> psyTechs;
  private Implant.Psy activeP;
  
  Implant()
  {
    setIsImp(true);
    setName("Psy Implant V.1");
    pow = 5;
    charge = maxCharge = 5;
    mem = 3;
    psyTechs = new LinkedList<Implant.Psy>();
    setEImg(new ImageIcon(getClass().getResource("OmniTool.png")));
    setEPow("Pow: None");
    
    activeP = null;
    
    // Test Psy 
    Psy dmgPsy = new Psy("Psy Bolt", PType.DMG, 3, 3);
    Psy healPsy = new Psy("Psy Heal", PType.HEAL, 3, 2);
    Psy shieldPsy = new Psy("Psy Shield", PType.SHIELD, 3, 2);
    addPsy(dmgPsy);
    addPsy(healPsy);
    addPsy(shieldPsy);
  }
  
  public void equip(Bod b)
  {
    if (b.getPsy() == 0) return;
    
    if (b.getPrimary() == null) b.setPrimary(this);
    else if (b.getSecondary() == null) b.setSecondary(this);
    else return;
    
    setUser(b);
  }
  
  public void addPsy(Implant.Psy ip)
  {
    if (psyTechs.size() == mem) return;
    
    psyTechs.push(ip);
  }
  
  public void setActivePsy()
  {
    if (psyTechs.size() == 0) return;
    
    final JDialog frame = new JDialog();
    frame.setSize(100, 200);
    frame.setLocationRelativeTo(getUser().getLocal());
    frame.setUndecorated(true);
    frame.toFront();
    
    JPanel panel = new JPanel(new GridLayout(psyTechs.size(), 1));
    
    for (int i = 0; i < psyTechs.size(); i++)
    {
      final Psy p = psyTechs.get(i);
      JButton jb = new JButton(p.pName);
      jb.addActionListener(new ActionListener()
                             {
        public void actionPerformed(ActionEvent e)
        {
          activeP = p;
          setEPow("Pow: " + activeP.pName);
          getUser().getLocal().pArea.bs.updateSide(getUser().getLocal());
          frame.dispose();
        }
      });
      panel.add(jb);
    }
  
    frame.add(panel);
    frame.setModal(true);
    frame.setVisible(true);
    
    
    //if (psyTechs.contains(ip)) {activeP = ip; setEPow("Pow: " + activeP.pName);}
    //else activeP = null;
  }
  
  public boolean isValidTar(){return false;}
  public boolean isValidTar(Mob m)
  {
    return false;
  }
  public boolean isValidTar(Bod b)
  {
    if (b == null) return false;
    if (b.isWall) return false;
    
    if (activeP == null && b.equals(getUser())) return true;
    if (activeP == null && !b.equals(getUser())) return false;
    
    if (charge <= 0) return false;
    
    System.out.println("Validating " + b.getName());
    
    int dist = Math.abs(getUser().getPX() - b.getPX()) + Math.abs(getUser().getPY() - b.getPY());
    System.out.println("Dist = " + dist);
    
    switch (activeP.pType)
    {
      case DMG:
        if (dist > 0 && dist <= activeP.pRange) return true;
        break;
      case HEAL:
        if (dist <= activeP.pRange) return true;
        break;
      case SHIELD:
        if (dist <= activeP.pRange) return true;
        break;
      default:
        return false;
    }
    
    return false;
  } 
  public void useTool(Bod b)
  {  
    if (getUser().getAP() <= 0) return;
    
    switch (activeP.pType)
    {
      case DMG:
        System.out.println("Psy Bolt");
        setAImg(new ImageIcon(getClass().getResource("PsyBolt.gif")));
        b.takeDamage(activeP.pPow);
        break;
      case HEAL:
        System.out.println("Psy Heal");
        setAImg(new ImageIcon(getClass().getResource("Heal.gif")));      
        if ((b.getHP() < (b.getVi() * 10)) && !b.getDead())
        {
          b.setHP(b.getHP() + activeP.pPow);
          if (b.getHP() > b.getVi() * 10) b.setHP(b.getVi() * 10);
        }
        break;
      case SHIELD:
        System.out.println("Psy Shield");
        setAImg(new ImageIcon(getClass().getResource("PsyBolt.gif")));
        b.addShield(activeP.pPow);
        break;
      default:
        break;
    }
    
    charge --;
    activeP = null;
    
    Animator ani = new Animator();
    ani.runAnimation(b.getLocal(), getAImg(), 1, 100, 100);
  }
  
  public void resetImp()
  {
    activeP = null;
    setEPow("Pow: None");
  }
  
  // Getters & Setters
  public boolean emptyActiveP()
  {
    if (activeP == null) return true;
    else return false;
  }
  
  // Inner class: Psy ---------------------------------------------------------------
  class Psy
  {
    String pName;
    PType pType;
    int pPow;
    int pRange;
    
    Psy(String s, PType pt, int pp, int pr)
    {
      pName = s; pType = pt; pPow = pp; pRange = pr;
    }
  }
}