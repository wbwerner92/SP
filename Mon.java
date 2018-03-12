import javax.swing.*;
/*
 * Mon.java
 */

class Mon extends Bot
{
  private Area.Panel target;
  
  Mon(){setIsMon(true);}
  
  public void generateMon() // Basic Mon
  {
    generateBod();
    
    setIsBod(false);
    setIsBot(true);
    setIsMon(true);
    
    setName("Space Slug");
    setJob("Monster");
    setPrimary(null);
    
    setMovMsg("*Snarl*");
    
    setIH(new ImageIcon(getClass().getResource("SpMon_Slug.gif")));
    setImg(getIH());
    
    Tool t = new Tool();
    t.generateMonsterClaw(this, "Claw", 20);
    
    setShield(null);
    
    setDirective("Wait");
    setTType('x');
    
    paMsg = "*RAWR*";
  }
  
  public void setTarget(Area.Panel p)
  {
    String tarString = "";
    
    target = p;  
    if (p == null) {setTType('x'); setMTar(null); tarString += "...";}
    else if (p.mob != null) {setTType('m'); setMTar(p.mob); tarString += "*Growl*";}
    else if (p.occupant != null) {setTType('b'); setMTar(p.occupant); tarString += "*Growl*";}
    else {setTType('p'); setMTar(null); tarString += "*pit pat* *pit pat*";}
  }
  public Area.Panel getTarget(){return target;}
}