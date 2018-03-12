import javax.swing.*;

class Equipment extends Item
{
  private ImageIcon eImg;
  private ImageIcon aImg;   // Action image
  private ImageIcon effImg; // Effect image
  private String ePow;
  private String eUse;
  
  private int minR;
  private int maxR;
  
  Equipment(){}
  
  public void equip(Bod b){setUser(b);}
  
  public boolean isValidTar(){return false;}
  public boolean isValidTar(Area.Panel p){return false;}
  public boolean isValidTar(Bod b){return false;}
  public boolean isValidTar(Mob m){return false;}
  
  public void maint(){}
  
  public boolean usePrimary(){return false;}
  public boolean usePrimary(Area.Panel p){return false;}
  public boolean usePrimary(Mob m)
  {
    boolean valid = true;
      
    if (getIsGun())
    {
      Gun g = (Gun)this;
      valid = g.shoot((Bod)m);
    }
    if (getIsTool())
    {
      System.out.println("Is a tool");
      System.out.println(m.getName());
      if (m.getIsBot()) System.out.println("Okay, it's a bot at least");
      Tool t = (Tool)this;
      if (m.getIsBod() || m.getIsBot() || m.getIsMon()) {System.out.println("Using " + getName() + " on " + m.getName()); t.useTool((Bod)m);}
      else t.useTool(m);
    }
    
    return valid;
  }
  
  // Getter/Setter Methods
  public ImageIcon getEImg(){return eImg;}
  public void setEImg(ImageIcon i){eImg = i;}
  public ImageIcon getAImg(){return aImg;}
  public void setAImg(ImageIcon i){aImg = i;}
  public ImageIcon getEffImg(){return effImg;}
  public void setEffImg(ImageIcon i){effImg = i;}
  public String getEPow(){return ePow;}
  public void setEPow(String s){ePow = s;}
  public String getEUse(){return eUse;}
  public void setEUse(String s){eUse = s;}
  public void setMinR(int i){minR = i;}
  public int getMinR(){return minR;}
  public void setMaxR(int i){maxR = i;}
  public int getMaxR(){return maxR;}
}