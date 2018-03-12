import javax.swing.*;

class Bod extends Mob
{
  // Stats
  private int vi;      // Fortitude stat determining Health Points (vi x 10)
  private int hp;  
  private int en;      // Fortitude stat determining Action Points
  private int ap;
  private int dx;      // Agility stat determining dodge rate
  private int sp;      // Agility stat determining movement
  
  // Equipment
  private Equipment primary;    // Currently held equipment
  private Equipment secondary;  // Side equipment
  private Shield shield;        // Equipped Shield
  
  // Skills
  private int guns;    // Guns Skill Tree
  //private int smGuns;  // Small Guns Skill
  //private int hvGuns;  // Heavy Guns Skill
  private int arm;     // Armor Skill Tree
  //private int liArm;   // Light Armor Skill 
  //private int heArm;   // Heavy Armor Skill
  private int sec;     // Security Skill Tree
  //private int locks;   // Locks Skill
  //private int comp;    // Computers Skill
  private int eng;     // Engineering Skill Tree
  //private int trp;     // Traps Skill
  //private int rep;     // Repair Skill
  private int med;     // Medical Skill Tree
  private int psy;     // Psyionic Skill Tree
  
  // Other
  private boolean dead;
  private String jClass;
  boolean isBot;
  Squad squad;
  
  Bod()
  {
    setName("Bod");
    jClass = "None";
    setIH(new ImageIcon(getClass().getResource("SpBod.gif")));
    setImg(getIH());
    generateBod();
    setIsBod(true);
    setIsBot(false);
    dead = false;
  }
  
  public void generateBod()
  {
    vi = new RVCalc().rInt(1, 2);
    en = new RVCalc().rInt(1, 2);
    dx = new RVCalc().rInt(1, 2);
    sp = new RVCalc().rInt(1, 2);
    
    setHP(getVi() * 10);
    setAP(getEnd());
    
    Equipment e = new Equipment();
    primary = e;
  }
  public void generateCaptain(boolean userAc) // Captain: User unit
  {
    jClass = "Captain";
    setIsCap(true);
    primary = null;
    
    vi = en = dx = sp = 2;
    setHP(getVi() * 10);
    setAP(getEnd());
    
    guns = arm = eng = sec = med = psy = 1;
    
    setIH(new ImageIcon(getClass().getResource("SpBod_Cap.gif")));
    setImg(getIH());
    
    Gun g = new Gun();
    g.generateHorizonPistol();
    g.equip(this);
    
    Tool t = new Tool();
    t.generateOmniTool();
    t.equip(this);
    
    Shield s = new Shield(10);
    s.equip(this);
    
    // Captain Customization
    if (userAc)
    {
      System.out.println("Customize your captain");
    }
  }
  public void generateSoldier() // Soldiers: Main Fighters
  {
    jClass = "Soldier";
    primary = null;
    
    vi = 3; en = 3; dx = 2; sp = 2;
    setHP(getVi() * 10);
    setAP(getEnd());
    guns = 1;
    arm = 1;

    setIH(new ImageIcon(getClass().getResource("SpBod_BigGun.gif")));
    setImg(getIH());
    
    Gun g = new Gun();
    g.generateHorizonRifle();
    g.equip(this);
    
    Shield s = new Shield(10);
    s.equip(this);   
  }
  public void generateScout() // Scouts: Combat support
  {
    jClass = "Scout";
    primary = null;
    
    vi = 2; en = 2; dx = 2; sp = 4;
    setHP(getVi() * 10);
    setAP(getEnd());
    guns = 1;
    eng = 1;
      
    setIH(new ImageIcon(getClass().getResource("SpBod_SmGun.gif")));
    setImg(getIH());
    
    Gun g = new Gun();
    g.generateHorizonPistol();
    g.equip(this);
    
    Tool t = new Tool("Elec. Tool V.1", 3, 1, 1, 3, false, true, false, false);
    t.setEImg(new ImageIcon(getClass().getResource("ElecTool.png")));
    t.equip(this);
    
    Shield s = new Shield(10);
    s.equip(this);
  }
  public void generateSpy() // Spies: Infiltrators
  {
    jClass = "Spy";
    primary = null;
    
    vi = 1; en = 2; dx = 4; sp = 3;
    setHP(getVi() * 10);
    setAP(getEnd());
    guns = 1;
    sec = 1;

    setIH(new ImageIcon(getClass().getResource("SpBod_SmGun.gif")));
    setImg(getIH());
    
    Gun g = new Gun();
    g.generateHorizonPistol();
    g.equip(this);
    
    Tool t = new Tool("Data Pad V.1", 3, 1, 1, 3, true, false, false, false);
    t.setEImg(new ImageIcon(getClass().getResource("DataPad.gif")));
    t.equip(this);
    
    Shield s = new Shield(10);
    s.equip(this);
  }
  public void generateTech() // Technicians: Technology support
  {
    generateBod();
    
    jClass = "Tech";
    primary = null;
    sec = 1;
    eng = 1;
    
    setIH(new ImageIcon(getClass().getResource("SpBod_Tech.gif")));
    setImg(getIH());
    
    Tool t = new Tool();
    t.generateOmniTool();
    t.equip(this);
    
    BotPack bp = new BotPack();
    bp.equip(this);
    
    Shield s = new Shield(10);
    s.equip(this);
  }
  public void generateFieldMed() // Fieldmedic: Healing/field support
  {
    generateBod();
    
    jClass = "Field Medic";
    primary = null;
    med = 1; eng = 1;
    
    setIH(new ImageIcon(getClass().getResource("SpBod_Medic.gif")));
    setImg(getIH());
    
    Tool t1 = new Tool("Healing Wand V.1", 3, 0, 1, 5, false, false, true, false);
    t1.setEImg(new ImageIcon(getClass().getResource("HealWand.png")));
    t1.equip(this);
    
    Tool t2 = new Tool("Elec. Tool V.1", 3, 1, 1, 3, false, true, false, false);
    t2.setEImg(new ImageIcon(getClass().getResource("ElecTool.png")));
    t2.equip(this);
    
    Shield s = new Shield(10);
    s.equip(this);
  }
  public void generatePsy()
  {
    generateBod();
    
    jClass = "Psy Op";
    primary = null;
    psy = 2;
    
    setIH(new ImageIcon(getClass().getResource("SpBod_Psy.gif")));
    setImg(getIH());
    
    Implant imp = new Implant();
    imp.equip(this);
    
    Shield s = new Shield(10);
    s.equip(this);
  }
  
  
  // Take damage method
  public void takeDamage(int i)
  {
    if (shield != null) i = shield.intercept(i);
    hp -= i;
    if (hp <= 0) 
    {
      hp = 0;
      dead = true;
      setImg(new ImageIcon(getClass().getResource("SpBod_Dead.gif")));
    }
  }
  
  // Shield Adding
  public void addShield(int i)
  {
    shield.addShield(i);
  }
  
  // Swap Equipment method
  public void swapEq()
  {
    if (primary.getIsImp())
    {
      Implant i = (Implant)primary;
      i.resetImp();
    }
    if (secondary == null) return;
    Equipment swap = secondary;
    secondary = primary;
    primary = swap;
  }
  
  public void resetBod()
  {
    if (getPrimary().getIsImp())
    {
      Implant i = (Implant)getPrimary();
      i.resetImp();
    }
  }
  
  // SETTER & GETTER METHODS
  // Equipment
  public Equipment getPrimary(){return primary;}
  public void setPrimary(Equipment e){primary = e;}
  public Equipment getSecondary(){return secondary;}
  public void setSecondary(Equipment e){secondary = e;}
  public Shield getShield(){return shield;}
  public void setShield(Shield s){shield = s;}
  // Stats
  public void setJClass(String c){jClass = c;}
  public String getJClass(){return jClass;}
  public int getVi(){return vi;}
  public void setHP(){hp = vi * 10;}
  public void setHP(int i){hp = i;}
  public int getHP(){return hp;}
  public int getDex(){return dx;}
  public void setAP(int i){ap = i;}
  public int getAP(){return ap;}
  public void setEnd(int i){en = i;}
  public int getEnd(){return en;}
  public void setSpd(int i){sp = i;}
  public int getSpd(){return sp;}
  public String getStats()
  {
    return("Vitality: " + vi + "\n" +
           "Dexterity: " + dx + "\n" +
           "Speed: " + sp + "\n" +
           "Endurance: " + en);
  }
  // Skills
  public void setGuns(int i){guns = i;}
  public int getGuns(){return guns;}
  public void setEng(int i){eng = i;}
  public int getEng(){return eng;}
  public void setSec(int i){sec = i;}
  public int getSec(){return sec;}
  public void setMed(int i){med = i;}
  public int getMed(){return med;}
  public void setPsy(int i){psy = i;}
  public int getPsy(){return psy;}
  // Other
  public void setDead(boolean b){dead = b;}
  public boolean getDead(){return dead;}
  public void setJob(String n){jClass = n;}
  public String getJob(){return jClass;}
  
  // Team Img setter
  public void setCol(int i)
  {
    if (i == 0)
    {
      if (jClass.equals("None")){
        setIH(new ImageIcon(getClass().getResource("SpBod.gif"))); setImg(getIH());
      }
      if (jClass.equals("Soldier")){
        setIH(new ImageIcon(getClass().getResource("SpBod_BigGun.gif"))); setImg(getIH());
      }
      if (jClass.equals("Scout") || jClass.equals("Spy")){
        setIH(new ImageIcon(getClass().getResource("SpBod_SmGun.gif"))); setImg(getIH());
      }
      if (jClass.equals("Tech")){
        setIH(new ImageIcon(getClass().getResource("SpBod_Tech.gif"))); setImg(getIH());
      }
      if (jClass.equals("Field Medic"))
      {
        setIH(new ImageIcon(getClass().getResource("SpBod_Medic.gif"))); setImg(getIH());
      }
    }
    if (i == 1)
    {
      if (jClass.equals("None")){
        setIH(new ImageIcon(getClass().getResource("SpBod.gif"))); setImg(getIH());
      }
      if (jClass.equals("Soldier")){
        setIH(new ImageIcon(getClass().getResource("SpBod_BigGun_Red.gif"))); setImg(getIH());
      }
      if (jClass.equals("Scout") || jClass.equals("Spy")){
        setIH(new ImageIcon(getClass().getResource("SpBod_SmGun_Red.gif"))); setImg(getIH());
      }
      if (jClass.equals("Tech")){
        setIH(new ImageIcon(getClass().getResource("SpBod_Tech_Red.gif"))); setImg(getIH());
      }
      if (jClass.equals("Field Medic"))
      {
        setIH(new ImageIcon(getClass().getResource("SpBod_Medic_Red.gif"))); setImg(getIH());
      }
    }
    if (i == 2)
    {
      if (jClass.equals("None")){
        setIH(new ImageIcon(getClass().getResource("SpBod.gif"))); setImg(getIH());
      }
      if (jClass.equals("Soldier")){
        setIH(new ImageIcon(getClass().getResource("SpBod_BigGun_Blue.gif"))); setImg(getIH());
      }
      if (jClass.equals("Scout") || jClass.equals("Spy")){
        setIH(new ImageIcon(getClass().getResource("SpBod_SmGun_Blue.gif"))); setImg(getIH());
      }
      if (jClass.equals("Tech")){
        setIH(new ImageIcon(getClass().getResource("SpBod_Tech_Blue.gif"))); setImg(getIH());
      }
      if (jClass.equals("Field Medic"))
      {
        setIH(new ImageIcon(getClass().getResource("SpBod_Medic_Blue.gif"))); setImg(getIH());
      }
    }
  }
}