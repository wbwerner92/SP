class Shield extends Equipment
{
  private int sh;
  private int shMax;
  
  private int rec;  // Recovery Rate
  private int cool; // Cool down
  
  private boolean broke;
  
  Shield(int i){sh = shMax = i; rec = shMax/5;}
  
  // Shield Preset Creator Methods

  
  public void equip(Bod b)
  {
    int engRating = b.getEng();
    
    if (engRating > 0) sh = shMax += (5 * engRating);
    
    setUser(b);
    getUser().setShield(this);
  }
  
  public int intercept(int i)
  {
    int remainder = i - sh;
    
    sh -= i;
    if (sh < 0) {sh = 0; broke = true;}
    
    if (remainder < 0) return 0;
    else return remainder;
  }
  
  public void recover()
  {
    if (broke) return;
    if (sh < shMax) sh += rec;
    if (sh > shMax && (sh - rec) < shMax) sh = shMax;
  }
  
  public void addShield(int i)
  {
    if (i <= 0) return;
    //if (broke) broke = false;
    sh += i;
    //if (sh > shMax) sh = shMax;
  }
  
  
  // Getter & Setter Methods
  public int getSh(){return sh;}
  public void setSh(int i){sh = i;}
  public int getShMax(){return shMax;}
  public boolean getBroke(){return broke;}
  public int getRec(){return rec;}
  public void setRec(int i){rec = i;}
    
}