class Account
{
  private int ac_Num;
  private Bod ac_SpBod;
  private Ship ac_Ship;
  
  Account()
  {
    ac_SpBod = new Bod();
    ac_SpBod.generateCaptain(true);
    
    ac_Ship = new Ship();
    
    RVCalc calc = new RVCalc();
    ac_Num = calc.rInt(0, 10000);
    
    System.out.println("Account #: " + ac_Num + "\n"
                         + "Captain: " + ac_SpBod.getName() + "\n"
                         + "Ship: " + ac_Ship.getName());
  }
  
  
  // Setter & Getter Methods
  public Bod getUser(){return ac_SpBod;}
}