class Item
{
  private String name;
  private Bod user;
  
  private String msg;
  
  private boolean isGun;
  private boolean isTool;
  private boolean isGear;
  private boolean isImp;
  
  Item(){}
  public void setName(String n){name = n;}
  public String getName(){return name;}
  public void setUser(Bod b){user = b;}
  public Bod getUser(){return user;}
  public String getMsg(){return msg;}
  public void setMsg(String s){msg = s;}
  public void addMsg(String s){msg += ("\n" + s);}
  public void setIsGun(boolean b){isGun = b;}
  public boolean getIsGun(){return isGun;}
  public void setIsTool(boolean b){isTool = b;}
  public boolean getIsTool(){return isTool;}
  public void setIsGear(boolean b){isGear = b;}
  public boolean getIsGear(){return isGear;}
  public void setIsImp(boolean b){isImp = b;}
  public boolean getIsImp(){return isImp;}
}