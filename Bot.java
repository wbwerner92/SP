import javax.swing.*;
/*
 * Bot.java: An extended Bod object representing mechanical units on the game board. These Robot units are capable
 * of following directions specified by a Technician. (Both Engineering and Security skills are required to set 
 * directions for a Robot unit). Basic functions include Opening Doors/Computers, Attacking units, etc. To set a 
 * driection, a Tech unit must move to an adjacent square (Or use a wireless remote) and set a simple instruction. The
 * Robot will automatically fulfill this request to the best of its ability. Once complete, the Robot will not act
 * until further instruction. More advanced robots can perform more than one task.
 */

class Bot extends Bod
{
  private Area.Panel target;
  private Mob mTar;
  Bod aTar; // Aggro Tar
  private String directive;
  private char dType;
  
  private ImageIcon oImg;
  private boolean scanning;
  boolean packed;
  Bod owner;
  
  String paMsg;
  
  Bot(){setIsBot(true); generateBot();}
  
  public void generateBot() // Basic Robot
  {
    generateBod();
    
    setIsBod(false);
    setIsBot(true);
    
    setJob("Service-Bot V.1.0");
    setPrimary(null);
    
    setEng(1); setSec(1);
    
    setIH(new ImageIcon(getClass().getResource("SpBot.gif")));
    setOImg(new ImageIcon(getClass().getResource("SpBot_Off.png")));
    setImg(getOImg());
    
    setMovMsg("Moving to Target");
    
    Tool t = new Tool();
    t.generateOmniTool();
    t.equip(this);
    
    Shield s = new Shield(10);
    s.equip(this);
    
    setDirective("Wait");
    setTType('x');
    paMsg = "Performing Action";
    
    packed = false;
  }
  
  public void generateSecBot() // Basic Robot
  {
    generateBod();
    
    setIsBod(false);
    setIsBot(true);
    
    setJob("Security-Bot V.1.0");
    setPrimary(null);
    
    setGuns(1);
    
    setIH(new ImageIcon(getClass().getResource("SpBot_Security.gif")));
    setOImg(new ImageIcon(getClass().getResource("SpBot_Security_Off.png")));
    setImg(getOImg());
    
    Gun g = new Gun();
    g.generateHorizonSubRifle();
    g.equip(this);
    
    Shield s = new Shield(10);
    s.equip(this);
    
    setDirective("Wait");
    setTType('x');
    paMsg = "Performing Action";
    
    packed = false;
  }
  
  public void resetBot()
  {
    setTarget(null);
    setDirective("Wait");
    scanning = false;
    setImg(getOImg());
    Area.Panel pa = getLocal();
    pa.exit();
    pa.enter(this);
  }
  
  // BOT SETTER/GETTER METHODS
  public void setTarget(Area.Panel p)
  {
    String tarString = "Setting Target: ";
    
    target = p;  
    if (p == null) {setTType('x'); setMTar(null); tarString += "ERROR NO TARGET";}
    else if (p.mob != null) {setTType('m'); setMTar(p.mob); tarString += p.mob.getName();}
    else if (p.occupant != null) {setTType('b'); setMTar(p.occupant); tarString += p.occupant.getName();}
    else {setTType('p'); setMTar(null); tarString += ("Area: " + p.posX + ", " + p.posY);}
    
    Animator a = new Animator();
    if (!getDirective().equals("Wait")) 
    {
      if (p == null) setImg(getOImg());
      else setImg(getIH());
      a.runAnimationTalk(getLocal(), tarString, 2, (tarString.length() * 10), 30);
    }
    else setImg(getOImg());
  }
  public Area.Panel getTarget(){return target;}
  public void findTarget(Area a)
  {
    // Aggro Target check
    if (aTar != null && aTar.getDead() == true) aTar = null;
    
    int close = 100;
    Area.Panel pan = null;
    
    for (Area.Panel[] pa : a.spaces)
    {
      for (Area.Panel p : pa)
      {
        int dist = Math.abs(getLocal().posX - p.posX) + Math.abs(getLocal().posY - p.posY);
        if (p.occupant != null && p.occupant.getDead() == false && !p.occupant.equals(this) && dist < close){pan = p; close = dist;}
        if (p.occupant != null && p.occupant.getDead() == false && p.occupant.equals(aTar))
        {
          pan = p; close = 0; break;
        }
      }
    }
    
    if (pan != null) {setDirective("Act"); setTarget(pan);}
  }
  public void setMTar(Mob m){mTar = m;}
  public Mob getMTar(){return mTar;}
  public void setDirective(String s){directive = s; scanning = true;}
  public String getDirective(){return directive;}
  public void setTType(char c){dType = c;}
  public char getTType(){return dType;}
  public boolean getScanning(){System.out.println("Scanning status: " + scanning); return scanning;}
  public void setOImg(ImageIcon oi){oImg = oi;}
  public ImageIcon getOImg(){return oImg;}
}