import javax.swing.*;

class BotPack extends Equipment
{
  Bot bot;
  boolean packed;
  
  BotPack() 
  {
    setIsGear(true);
    bot = new Bot();
    bot.setName("Bot");
    bot.generateSecBot();
    bot.packed = true;
    setName(bot.getName() + " Pack");
    setMinR(1); setMaxR(1);
    java.net.URL img1 = getClass().getResource("SpBot_Pack.png");
    setEImg(new ImageIcon(img1));
    setEPow("FULL");
    packed = true;
  }
  
  public void equip(Bod b)
  {
    if (b.getPrimary() == null) {b.setPrimary(this); bot.setName(b.getName() + "-Bot"); bot.owner = b;}
    else if (b.getSecondary() == null) {b.setSecondary(this); bot.setName(b.getName() + "-Bot"); bot.owner = b;}
    else return;
    
    setUser(b);
  }
  
  public boolean isValidTar(Bod b)
  {
    if (b == null || b.getDead() || b.equals(getUser()) || packed == true) return false;
    int dist = Math.abs(getUser().getLocal().posX - b.getPX()) + Math.abs(getUser().getLocal().posY - b.getPY());
    
    if (dist == 1 && bot == null && b.getIsBot()) return true;
    else return false;
  }
  public boolean isValidTar(Area.Panel p)
  {
    if (packed == false) return false;
    
    int dist = Math.abs(getUser().getLocal().posX - p.posX) + Math.abs(getUser().getLocal().posY - p.posY);
    
    if (dist == 1 && p.occupant == null && p.mob == null) return true;
    else return false;
  }
  
  public boolean usePrimary(Bod b)
  {
    System.out.println("Retrieving Bot");
    Bot tar = (Bot)b;
    
    if (tar.owner != null && !tar.owner.equals(getUser())) 
    {
      return false;
    }
    else bot = tar;
    
    bot.resetBot();
    bot.getLocal().exit();
    bot.setName(getUser().getName() + "-Bot");
    bot.owner = getUser();
    packed = true; 
    bot.packed = true;
    java.net.URL img1 = getClass().getResource("SpBot_Pack.png");
    setEImg(new ImageIcon(img1));
    setEPow("FULL");
    return true;
  }
  public boolean usePrimary(Area.Panel p)
  {
    p.enter(bot);
    bot.resetBot();
    bot.packed = false;
    bot = null;
    packed = false;
    //setEImg(null);
    setEPow("EMPTY");
    return true;
  }
  
}