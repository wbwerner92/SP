import javax.swing.*;

class Gun extends Equipment
{
  private int dmg;
  private int heat;
  private int cap;
  
  private boolean small;
  private boolean heavy;
  
  private int eff, ePow;
  
  // Gu object constructors
  Gun(){}
  Gun(String n, int d, int nR, int xR, int c, boolean s, boolean h)
  {
    setName(n);
    dmg = d; setMinR(nR); setMaxR(xR); cap = c;
    if (s) small = true;
    if (h) heavy = true;
    setIsGun(true);
    setEPow("Pow: " + dmg);
    setAImg(new ImageIcon(getClass().getResource("GunShot.gif")));
  }
  
  public void equip(Bod b)
  {
    setUser(b);
    if (heavy) getUser().setSpd(getUser().getSpd() - 1);
    getUser().setPrimary(this);
  }
  
  public boolean isValidTar(Bod b)
  {
    if (b == null || b.getDead() || b.equals(getUser())) return false;
    else return true;
  }
  public boolean isValidTar(Mob m) {return false;}
  
  public void maint(){heat = 0;}
  
  public boolean shoot(Bod t)
  {
    // Action message
    setMsg("");
    
    // Reload weapon if overheated
    if (heat == cap){setMsg("Reload"); return false;}
    else heat++;
      
    // Aim rating
    int aim = 0;
    
    // Aim bonus based on user gun skill
    Bod user = getUser();
    if (user.getGuns() == 1) aim += 5;
    if (user.getGuns() == 2) aim += 5;
    if (user.getGuns() == 3) aim += 5;
    // Gun Skill aim calc
    
    // Distance calc/aim calc
    int dist = Math.abs(user.getPX() - t.getPX()) + Math.abs(user.getPY() - t.getPY());
    setMsg("Distance = " + dist);
    if (dist < getMinR()) aim -= (getMinR() - dist);
    if (dist > getMaxR()) aim -= (dist - getMaxR());
    if (aim < 1) aim = 1;
       
    // Aim/Dodge rolls
    RVCalc calc = new RVCalc();
    int aimRoll = calc.rInt(1, aim);
    int dodRoll = calc.rInt(0, t.getDex()); 
    addMsg("Aim % = (" + aim + "), Dodge % = (" + t.getDex() + ")");
    addMsg("Aim = " + aimRoll + ", Dodge = " + dodRoll);
    
    Animator ani = new Animator();
    if (getEff() == 0) ani.runAnimation(t.getLocal(), getAImg(), 2, 100, 100); 
    else ani.run2Animation(t.getLocal(), getAImg(), 2, getEffImg(), 1, 100, 100);
    
    // Miss/Hit
    if (aimRoll > dodRoll)
    {
      addMsg("Hit! x" + dmg + " damage");
      boolean shBro = true;
      if (t.getShield() != null && t.getShield().getSh() > 0) shBro = false;
      t.takeDamage(dmg);
      if (!shBro && t.getShield().getSh() <= 0) shBro = false;
      effect(t, shBro);
      ani.runAnimation(t.getLocal(), new ImageIcon(getClass().getResource("Hit.png")), 1, 100, 100);
      
      // Sets Monsters to Aggro self
      if (t.getIsMon())
      {
        Mon m = (Mon)t;
        m.aTar = getUser();
      }
      
      return true;
    }
    else 
    {
      addMsg("Miss!"); 
      ani.runAnimation(t.getLocal(), new ImageIcon(getClass().getResource("Miss.png")), 1, 100, 100); 
      return false;
    }
  }
  
  public void effect(Bod t, boolean shBro)
  {
    if (eff == 0) return;
    if (eff == 1)
    {
      if (t.getShield() != null && t.getShield().getBroke() == false)
      {
        t.getShield().intercept(ePow);
        addMsg("x3 electrical damage");
      }
    }
    if (eff == 2)
    {
      if (t.getShield() == null || t.getShield().getSh() <= 0)
      {
        if (shBro) {t.takeDamage(ePow); addMsg("x2 fire damage");}
      }
    }
  }
  
  // SETTER/GETTER METHODS
  // Stat field setter/getter methods
  public int getHeat(){return heat;}
  public int getCap(){return cap;}
  public void setEff(int e, int ep){eff = e; ePow = ep;}
  public int getEff(){return eff;}
  public String getEUse(){return "Cap: " + heat + "/" + cap;}
  
  // Gun type getter methods --------------------------------------------------------------
  public void generateHorizonPistol()
  {
    setName("Horizon Pistol Mk.1");
    dmg = 5; setMinR(2); setMaxR(3); cap = 5;
    small = true; heavy = false;
    setIsGun(true);
    setEPow("Pow: " + dmg);
    setEImg(new ImageIcon(getClass().getResource("SmGun.png")));
    setAImg(new ImageIcon(getClass().getResource("GunShot.gif")));
  }
  public void generateHorizonSubRifle()
  {
    setName("Horizon Sub-Rifle Mk.1");
    dmg = 5; setMinR(2); setMaxR(4); cap = 7;
    small = true; heavy = false;
    setIsGun(true);
    setEPow("Pow: " + dmg);
    setEImg(new ImageIcon(getClass().getResource("SmGun_SubRifle.png")));
    setAImg(new ImageIcon(getClass().getResource("GunShot.gif")));
  }
  public void generateHorizonRifle()
  {
    setName("Horizon Rifle Mk.1");
    dmg = 7; setMinR(2); setMaxR(4); cap = 8;
    small = false; heavy = true;
    setIsGun(true);
    setEPow("Pow: " + dmg);
    setEImg(new ImageIcon(getClass().getResource("BigGun.png")));
    setAImg(new ImageIcon(getClass().getResource("GunShot.gif")));
  }
  public void generateElecPistol()
  {
    setName("Sabre Elec. Pistol");
    dmg = 5; setMinR(2); setMaxR(3); cap = 4;
    small = true; heavy = false;
    setIsGun(true);
    setEff(1, 3);
    setEPow("Pow: " + dmg + " (+3 Elec.)");
    setEImg(new ImageIcon(getClass().getResource("SmGun_Shocker.png")));
    setAImg(new ImageIcon(getClass().getResource("GunShot.gif")));
    setEffImg(new ImageIcon(getClass().getResource("Shock.gif")));
  }
  public void generateFlameRifle()
  {
    setName("Apex Flame Rifle");
    dmg = 8; setMinR(2); setMaxR(3); cap = 3;
    small = false; heavy = true;
    setIsGun(true);
    setEff(2, 2);
    setEPow("Pow: " + dmg + " (+2 Fire)");
    setEImg(new ImageIcon(getClass().getResource("BigGun_Flamer.png")));
    setAImg(new ImageIcon(getClass().getResource("GunShot.gif")));
    setEffImg(new ImageIcon(getClass().getResource("Flame.gif")));
  }
  
}