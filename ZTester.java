import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class ZTester
{
  public static void main(String[] args)
  {
    Account a = new Account();
    
    //mainTest();
    RVCalc calc = new RVCalc();
    calc.showCalc();
  }
  
  public static void mainTest()
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
    
    bs.showArea(area3);
    
    //Ship s = new Ship();
    //bs.addShip(s);
    
    bs.runBS();
  }
  
  public static void animatorTest()
  {
    Animator ar = new Animator();
    ar.runAnimation(null, new ImageIcon("GunShot.gif"), 2, 100, 100);
  }
  public static void stackTest()
  {
    Stack<Bod> st = new Stack();
    Bod a = new Bod();
    Bod b = new Bod();
    Bod c = new Bod();
    
    st.push(a);
    st.push(b);
    
    System.out.println(st.search(a));
    System.out.println(st.search(b));
    System.out.println(st.search(c));
  }
  
  
  
  // Experiment
  class LabelSetter
  {
    JLabel[] labels;
    LabelSetter(JLabel[] jl){labels = jl;}
    
    public void setLabels(String[] sa)
    {
      for (int i = 0; i < labels.length; i++)
      {
        labels[i].setText(sa[i]);
      }
    }
  }
  
  
  public static void shootTest()
  {
    Bod a = new Bod();
    a.setName("Guy A");
    a.generateSoldier();
    a.setPX(1); a.setPY(1);
    Bod b = new Bod();
    b.setName("Guy B");
    b.generateSoldier();
    b.setPX(0); b.setPY(1);
    
    Gun g = new Gun("Pistol", 5, 2, 3, 5, true, false);
    g.equip(a);
    System.out.println(g.shoot(b)); 
  }
  
  // Tests the Random Value Calculator (RVCalc.java) 25 times
  public static void testRand25()
  {
    for (int i = 0; i < 25; i++)
    {
      System.out.println(new RVCalc().rInt(1, 5));
    }
  }
  
  // Animation
  public static void animate(ImageIcon ic, int i)
  {
    JFrame frame = new JFrame();
    frame.setSize(500, 500);
    frame.setLocationRelativeTo(null);
    JPanel panel = new JPanel();
    JLabel jl = new JLabel();
    jl.setIcon(ic);
    panel.add(jl);
    
    frame.add(panel);
    frame.setVisible(true);
    
    // Timer
    long start = testTime();
    
    int counter = 0;
    while (true)
    {
      long test = testTime();
      
      if ((test - start) == (long)1000)
      {
        counter ++;
        start = test;
      }
      
      if (counter == i) {frame.dispose(); break;}
      
    }
  }
  public static long testTime()
  {
    return new Date().getTime();
  }
  // Timer
  public static void loadBar(int i)
  {
    JFrame frame = new JFrame();
    frame.setSize(500, 100);
    frame.setLocationRelativeTo(null);
    JPanel panel = new JPanel(new GridLayout(1, i));
    
    JButton[] bars = new JButton[i];
    for (int j = 0; j < i; j++)
    {
      bars[j] = new JButton("" + (j + 1));
      bars[j].setVisible(false);
      panel.add(bars[j]);
    }
    
    frame.add(panel);
    frame.setVisible(true);
    
    // Timer
    long start = testTime();
    
    int counter = 0;
    while (true)
    {
      long test = testTime();
      
      if ((test - start) == (long)1000)
      {
        bars[counter].setVisible(true);
        counter ++;
        start = test;
      }
      
      if (counter == i) break;
    }
  }
}