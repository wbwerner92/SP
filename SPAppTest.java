import javax.swing.*;
import junit.framework.TestCase;

/**
 * A JUnit test case class.
 * Every method starting with the word "test" will be called when running
 * the test with JUnit.
 */
public class SPAppTest extends TestCase 
{
  /**
   * A test method.
   * (Replace "X" with a name describing the test.  You may write as
   * many "testSomething" methods in this class as you wish, and each
   * one will be called when running JUnit over this class.)
   */
  
  public void testBScreen() 
  {
    BScreen bs = new BScreen();
    
    // Area 1 Test Bods -------------------------------------------------------------------
    Area area1 = new Area("Area 1", bs);
    area1.createBigRoom();
    
    Bod b = new Bod();
    b.setName("Bob");
    b.generateSoldier();
    area1.spaces[1][1].enter(b);
    
    Bod b2 = new Bod();
    b2.setName("Owen");
    b2.generateSoldier();
    b2.setIH(new ImageIcon("SpBod_BigGun_Blue.gif"));  
    b2.setImg(b2.getIH());
    b2.setPrimary(null);
    Gun g1 = new Gun();
    g1.generateFlameRifle();
    g1.equip(b2);
    area1.spaces[1][2].enter(b2);
    
    Bod c = new Bod();
    c.setName("Guy");
    c.generateScout();
    c.setPrimary(null);
    Gun g = new Gun("Saber Elec. Pistol", 5, 2, 3, 4, true, false);
    g.generateElecPistol();
    g.equip(c);
    area1.spaces[1][3].enter(c);
    
    Bod d = new Bod();
    d.setName("Hal");
    d.generateTech();
    area1.spaces[6][10].enter(d);
    
    Bod q = new Bod();
    q.setName("Drew");
    q.generateTech();
    area1.spaces[4][1].enter(q);
    
    Bod f = new Bod();
    f.setName("Sal");
    f.generateSpy();
    area1.spaces[6][2].enter(f);
    
    Bod m = new Bod();
    m.setName("Mickey");
    m.generateFieldMed();
    area1.spaces[3][3].enter(m);
    
    Bod x = new Bod();
    x.setName("Cap'n");
    x.generateCaptain(false);
    area1.spaces[1][4].enter(x);
    
    Bod psy = new Bod();
    psy.setName("Psy Guy");
    psy.generatePsy();
    area1.spaces[2][3].enter(psy);
    
    Bot bot = new Bot();
    bot.setName("Rad-Bot");
    area1.spaces[4][2].enter(bot);
    
    Bot bot2 = new Bot();
    bot2.setName("Dell-Bot");
    bot2.generateSecBot();
    area1.spaces[4][3].enter(bot2);
    
    Mon mon1 = new Mon();
    mon1.generateMon();
    mon1.setHP(mon1.getVi() * 10);
    mon1.setAP(mon1.getEnd());
    area1.spaces[6][6].enter(mon1);
    
    Bod guy1 = new Bod();
    guy1.setName("Jo");
    area1.spaces[3][8].enter(guy1);
    Bod guy2 = new Bod();
    guy2.setName("Lu");
    area1.spaces[3][9].enter(guy2);
    Bod guy3 = new Bod();
    guy3.setName("Ed");
    area1.spaces[4][8].enter(guy3);
    
    Computer comp = new Computer();
    area1.spaces[3][1].setMob(comp);
    
    bs.addArea(area1);
    
    
    // Hallway Test ------------------------------------------------------------------
    Area area2 = new Area("Area 2", bs);
    area2.createHallway();

    Bod bo2 = new Bod();
    bo2.setName("Wheeler");
    bo2.generateSpy();
    bo2.setHP(bo2.getVi() * 10);
    bo2.setAP(bo2.getEnd());
    area2.spaces[2][1].enter(bo2);
    
    bs.addArea(area2);
    bs.showArea(area2);
    
    // Linking areas via Door ------------
    Door door = new Door();
    door.linkArea(area1, area2);
    
    Area area3 = new Area("Area 3", bs);
    area3.createSmallRoom();
   
    String[] names = {"Rudy", "Dude", "Loyd", "Cal", "Kris", "Rayman", "Burns", "Maxx", "Irons", "Gray",
      "Walt", "Stuart", "Deeds", "Raj", "Mac", "Cory", "Wyatt", "Redd", "Duke", "Marlin", "Stone", "Eli"};
    Squad sq = new Squad();
    RVCalc calc = new RVCalc();
    for (int i = 0; i < 8; i++)
    {
      Bod bo = new Bod();
      int nI = calc.rInt(0, names.length - 1);
      bo.setName(names[nI]);
      int jI = calc.rInt(0, 5);
      if (jI == 0) bo.generateBod();
      if (jI == 1) bo.generateScout();
      if (jI == 2) bo.generateSoldier();
      if (jI == 3) bo.generateTech();
      if (jI == 4) bo.generateSpy();
      if (jI == 5) bo.generateFieldMed();
      sq.addMem(bo);
    }

    sq.setTeam(1);
    area3.enterSquad(sq);
    bs.addArea(area3);
    
    Door door2 = new Door();
    door2.linkArea(area1, area3);
    
    // Comp Scanning
    comp.scanArea(area1);
    
    bs.showArea(area1);
    
    //Ship s = new Ship();
    //bs.addShip(s);
    
    bs.runBS();
  } 
}
