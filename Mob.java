import javax.swing.*;

class Mob
{  
  private String name;
  private int posX;
  private int posY;
  private ImageIcon img;
  private ImageIcon iHold;
  
  // Mob Dialog messages
  private String atkMsg;
  private String movMsg;
  private String shfMsg;
  private String dmgMsg;
  
  private boolean isBod;
  private boolean isCap;
  private boolean isBot;
  private boolean isMon;
  private boolean isComp;
  boolean isGear;
  private boolean isDoor;
  boolean isWall;
  private boolean isTurret;
  
  private int powLevel;
  private int powLevelMax;
  
  boolean broke;
  
  private Area.Panel local;
  
  Mob(){}
  Mob(String n, ImageIcon i, ImageIcon i2, boolean c, boolean g, boolean d, int l)
  {
    setName(n);
    setImg(i);
    setIH(i2);
    isComp = c;
    isGear = g;
    isDoor = d;   
    isBod = false;
    isBot = false;

    if (isDoor || isComp) setPowLvl(1);;
  }
  
  //SETTERS & GETTERS
  public void setName(String n){name = n;}
  public String getName(){return name;}
  public void setIsBod(boolean b){isBod = b;}
  public boolean getIsBod(){return isBod;}
  public void setIsCap(boolean b){isCap = b;}
  public boolean getIsCap(){return isCap;}
  public void setIsBot(boolean b){isBot = b;}
  public boolean getIsBot(){return isBot;}
  public void setIsMon(boolean b){isMon = b;}
  public boolean getIsMon(){return isMon;}
  public void setIsDoor(boolean b){isDoor = b;}
  public boolean getIsDoor(){return isDoor;}
  public void setIsComp(boolean b){isComp = b;}
  public boolean getIsComp(){return isComp;}
  public void setIsTur(boolean b){isTurret = b;}
  public boolean getIsTur(){return isTurret;}
  public void setPowLvl(int i){powLevel = i;}
  public int getPowLvl(){return powLevel;}
  public void setPowLvlMax(int i){powLevelMax = i;}
  public int getPowLvlMax(){return powLevelMax;}
  // Position
  public void setPX(int x){posX = x;}
  public void setPY(int y){posY = y;}
  public int getPX(){return posX;}
  public int getPY(){return posY;}
  public void setLocal(Area.Panel p){local = p;}
  public Area.Panel getLocal(){return local;}
  // Image
  public void setImg(ImageIcon i){img = i;}
  public ImageIcon getII(){return img;}
  public void setIH(ImageIcon i){iHold = i;}
  public ImageIcon getIH(){return iHold;}
  // Dialog
  public void setAtkMsg(String s){atkMsg = s;}
  public String getAtkMsg(){return atkMsg;}
  public void setMovMsg(String s){movMsg = s;}
  public String getMovMsg(){return movMsg;}
  public void setDmgMsg(String s){dmgMsg = s;}
  public String getDmgMsg(){return dmgMsg;}
  
  public String getStats()
  {
    if (isDoor || isComp)
    {
      return ("Security Level: " + powLevel +"\n\n\n\n");
    }
    else return "\n\n\n\n\n";
  }
  
  // Custom Mob specs
  public void setDoor()
  {
    setName("Door");
    setImg(new ImageIcon(getClass().getResource("Door_Closed.gif")));
    setIH(new ImageIcon(getClass().getResource("Door_Open.gif")));
    isComp = false;
    isGear = false;
    isDoor = true;   
    isBod = false; 
    powLevel = powLevelMax = 5;
  }
  public void setComputer()
  {
    setName("Computer");
    setImg(new ImageIcon(getClass().getResource("Comp.gif")));
    setIH(new ImageIcon(getClass().getResource("Comp_Hacked.gif")));
    isComp = true;
    isGear = false;
    isDoor = false;
    isBod = false;
    powLevel = powLevelMax = 10;
  }
  public void setWall()
  {
    setName("Wall");
    setImg(new ImageIcon("Wall.png"));
    setIH(null);
    isComp = false;
    isGear = false;
    isDoor = false;
    isBod = false;
    isWall = true;
  }
  public void setTurret()
  {
    setName("Turret");
    setImg(new ImageIcon(getClass().getResource("Turret.gif")));
    setIH(new ImageIcon(getClass().getResource("Turret_Off.png")));
    isComp = false;
    isGear = false;
    isDoor = false;
    isBod = false;
    isWall = false;
    isTurret = true;
  }
}