import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

class BScreen extends JFrame
{
  public JPanel getBScreen()
  {
    JPanel re = new JPanel();
    re.setSize(900, 900);
    re.setMaximumSize(new Dimension(900, 850));
    re.setLayout(new BorderLayout());
    
    re.add(jsp);
    re.add(sideJS, BorderLayout.EAST);
    re.add(bPanel, BorderLayout.SOUTH);
    
    return re;
  }
  public void runBS()
  {
    scanning = false;
    setVisible(true);
    
    // Centers starting screen to anchor
    CardLayout cl = (CardLayout)display.getLayout();
    cl.show(display, area.name);
    showPanel(area.anchor);
  }
  
  // Instance Variables
  JPanel sCopy;
  Area area; // Current area
  JPanel display; // Main display area
  JScrollPane jsp;
  JScrollPane sideJS;
  JScrollPane botJSP;
  JPanel bPanel;
  
  Border blackline = BorderFactory.createLineBorder(Color.black);
  Animator ani = new Animator();
  
  JLabel sName;
  JLabel sClass;
  JLabel sImg;
  JLabel bodHP;
  JLabel bodSH;
  JButton[] hbar;
  JButton[] sbar;
  JLabel sAP;
  JLabel sMov;
  JTextArea sStats;
  JButton eName;
  JLabel eImg;
  JLabel ePow;
  JLabel eUse;
  JButton end;
  JButton aiCont;
  Boolean aiActive;
  JTextArea bottom;
  
  Stack<Bod> peeps = new Stack<Bod>(); // All acting bods
  LinkedList<Squad> squads = new LinkedList<Squad>();
  Stack<Bot> bots = new Stack<Bot>();  // All availabel bots
  LinkedList<Area> areas = new LinkedList<Area>();
  
  boolean acting;
  boolean scanning;
  
  int sw = 2;
  
  BScreen() // BScreen Constructor --------------------------------------------------------
  {
    setLayout(new BorderLayout());
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(900, 900);
    setMaximumSize(new Dimension(900, 850));
    toFront();
    
    display = new JPanel(new CardLayout());
    
    jsp = new JScrollPane(display);
    jsp.setPreferredSize(new Dimension(700, 700));
    jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    
    add(jsp);
    
    // Side info. ------------------------------------------------------------------
    JPanel sideInf = new JPanel();
    sideInf.setPreferredSize(new Dimension(300, 700));
    sideInf.setLayout(new BoxLayout(sideInf, BoxLayout.Y_AXIS));
    
    // Filler
    sideInf.add(new JLabel(""));
    
    // Side name label
    sName = new JLabel("Name:          ");
    sName.setAlignmentX(Component.CENTER_ALIGNMENT);
    sName.setFont(new Font("Papyrus", Font.PLAIN, 20));
    sideInf.add(sName);
    
    // Side Class label
    sClass = new JLabel("Class:         ");
    sClass.setAlignmentX(Component.CENTER_ALIGNMENT);
    sClass.setFont(new Font("Papyrus", Font.PLAIN, 15));
    sideInf.add(sClass);
    
    // Side icon label
    sImg = new JLabel();
    sImg.setAlignmentX(Component.CENTER_ALIGNMENT);
    sImg.setPreferredSize(new Dimension(185, 185));
    sideInf.add(sImg);
    
    //Side HP bar
    JPanel sideHP = new JPanel();
    sideHP.setBorder(blackline);
    JLabel sHP = new JLabel("HP:");
    sHP.setFont(new Font("Papyrus", Font.PLAIN, 20));
    sideHP.add(sHP, BorderLayout.WEST);
    JPanel health = new JPanel(new GridLayout(1, 10));
    hbar = new JButton[10];
    for (int i = 0; i < 10; i++)
    {
      hbar[i] = new JButton();
      hbar[i].setPreferredSize(new Dimension(10, 10));
      hbar[i].setBackground(Color.RED);
      hbar[i].setOpaque(true);
      hbar[i].setBorderPainted(false);
      hbar[i].setVisible(false);
      health.add(hbar[i]); 
    } 
    sideHP.add(health, BorderLayout.CENTER);
    bodHP = new JLabel("___");
    bodHP.setFont(new Font("Papyrus", Font.PLAIN, 20));
    sideHP.add(bodHP, BorderLayout.EAST);
    sideInf.add(sideHP);
    
    // Side Shield Bar
    JPanel sideSH = new JPanel();
    sideSH.setBorder(blackline);
    JLabel sSH = new JLabel("SH:");
    sSH.setFont(new Font("Papyrus", Font.PLAIN, 20));
    sideSH.add(sSH, BorderLayout.WEST);
    JPanel shielding = new JPanel(new GridLayout(1, 10));
    sbar = new JButton[10];
    for (int i = 0; i < 10; i++)
    {
      sbar[i] = new JButton();
      sbar[i].setPreferredSize(new Dimension(10, 10));
      sbar[i].setBackground(Color.CYAN);
      sbar[i].setOpaque(true);
      sbar[i].setBorderPainted(false);
      sbar[i].setVisible(false);
      shielding.add(sbar[i]); 
    }
    sideSH.add(shielding, BorderLayout.CENTER);
    bodSH = new JLabel("___");
    bodSH.setFont(new Font("Papyrus", Font.PLAIN, 20));
    sideSH.add(bodSH, BorderLayout.EAST);
    sideInf.add(sideSH);
    
    // AP Info.
    JPanel sideAP = new JPanel(new GridLayout(1, 2));
    sideAP.setBorder(blackline);
    JLabel jlap = new JLabel("AP: ", SwingConstants.RIGHT);
    jlap.setFont(new Font("Papyrus", Font.PLAIN, 20));
    sideAP.add(jlap);
    sAP = new JLabel("___");
    sAP.setFont(new Font("Papyrus", Font.PLAIN, 20));
    sideAP.add(sAP);
    sideInf.add(sideAP);
    
    // Move Info.
    JPanel sideMov = new JPanel(new GridLayout(1, 2));
    sideMov.setBorder(blackline);
    JLabel jlmov = new JLabel("Move: ", SwingConstants.RIGHT);
    jlmov.setFont(new Font("Papyrus", Font.PLAIN, 20));
    sideMov.add(jlmov);
    sMov = new JLabel("___");
    sMov.setFont(new Font("Papyrus", Font.PLAIN, 20));
    sideMov.add(sMov);
    sideInf.add(sideMov);
    
    // Stats Info.
    sStats = new JTextArea("\n\n\n\n\n");
    sStats.setFont(new Font("Papyrus", Font.PLAIN, 20));
    sStats.setEditable(false);
    sStats.setMaximumSize(new Dimension(250, 400));
    sideInf.add(sStats);
    
    // Equipment Info.
    JPanel eNamePanel = new JPanel(new BorderLayout());
    eNamePanel.setBorder(blackline);
    eName = new JButton("_____");
    eName.addActionListener(new ActionListener()
                              {
      public void actionPerformed(ActionEvent e)
      {
        if (area.active != null && area.active.occupant.getAP() > 0) 
        {
          area.active.occupant.getPrimary().maint();
          area.active.occupant.setAP(area.active.occupant.getAP() - 1);
          updateSide(area.active);
        }
      }
    });
    eName.setFont(new Font("Papyrus", Font.PLAIN, 18));
    eNamePanel.add(eName);
    // Tool swap button
    JButton swap = new JButton();
    swap.setPreferredSize(new Dimension(30, 30));
    ImageIcon i = new ImageIcon(getClass().getResource("Recycle.png"));
    Image ii = i.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT);
    swap.setIcon(new ImageIcon(ii));   
    swap.addActionListener(new ActionListener()
                             {
      public void actionPerformed(ActionEvent e)
      {
        if (area.active != null) 
        {
          area.active.occupant.swapEq();
          Area.Panel p = area.active;
          area.resetPanels();
          updateSide(p);
          if (!p.occupant.getIsBot()) showActable(p);     
        }
      }
    });
    eNamePanel.add(swap, BorderLayout.EAST);
    sideInf.add(eNamePanel);
    // Equipment stats
    JPanel ePanel = new JPanel(new GridLayout(1, 2));
    ePanel.setBorder(blackline);
    eImg = new JLabel("");
    ePanel.add(eImg);
    JPanel ePanel2 = new JPanel(new GridLayout(2, 1));
    ePow = new JLabel("_____");
    ePow.setFont(new Font("Papyrus", Font.PLAIN, 18));
    ePanel2.add(ePow);
    eUse = new JLabel("_____");
    eUse.setFont(new Font("Papyrus", Font.PLAIN, 18));
    ePanel2.add(eUse);
    ePanel.add(ePanel2);
    sideInf.add(ePanel);
    
    
    // End Turn Button
    end = new JButton ("End Round");
    end.setPreferredSize(new Dimension(100, 50));
    end.setAlignmentX(Component.CENTER_ALIGNMENT);
    end.setFont(new Font("Papyrus", Font.PLAIN, 20));
    end.addActionListener(new ActionListener()
                            {
      public void actionPerformed(ActionEvent e)
      {
        if (area.active != null && area.active.occupant != null)
        {
          area.active.occupant.setAP(0);
          updateSide(area.active);
          area.resetPanels();
          setEmpty();
          checkActive();
        }
        else
        {
          if (JOptionPane.showConfirmDialog(null, "End Round Early?") == 0) resetPeeps(); 
        }
      }
    });
    sideInf.add(end);
    
    // AI On/Off switch
    aiActive = false;
    aiCont = new JButton("AI Off");
    aiCont.setPreferredSize(new Dimension(100, 50));
    aiCont.setAlignmentX(Component.CENTER_ALIGNMENT);
    aiCont.setFont(new Font("Papyrus", Font.PLAIN, 20));
    aiCont.addActionListener(new ActionListener()
                               {
      public void actionPerformed(ActionEvent e)
      {
        if (aiActive) {aiActive = false; aiCont.setText("AI Off");}
        else {aiActive = true; aiCont.setText("AI On");}
      }
    });
    sideInf.add(aiCont);
    
    sideJS = new JScrollPane(sideInf);
    sideJS.setPreferredSize(new Dimension(350, 700));
    sideJS.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    sideJS.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    
    add(sideJS, BorderLayout.EAST);
    
    // Message Panel (South Border)
    bPanel = new JPanel(new BorderLayout());
    bottom = new JTextArea();
    bottom.setFont(new Font("Papyrus", Font.PLAIN, 12));
    bottom.setEditable(false);
    botJSP = new JScrollPane(bottom);
    botJSP.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    botJSP.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    botJSP.setPreferredSize(new Dimension(500, 100));
    bPanel.add(botJSP, BorderLayout.WEST);
    JButton vA = new JButton("View Areas");
    vA.addActionListener(new ActionListener()
                           {
      public void actionPerformed(ActionEvent e)
      { 
        area.resetPanels();
        area.vp = jsp.getViewport().getViewPosition();
        if (areas.indexOf(area) == (areas.size() - 1)) // Last Area in list
        {
          showArea((Area)areas.getFirst());
        }
        else
        {
          showArea((Area)areas.get(areas.indexOf(area) + 1));
        } 
      }
    });
    bPanel.add(vA, BorderLayout.CENTER);
    add(bPanel, BorderLayout.SOUTH);
    
    setVisible(true);
  }
  
  // BScreen Methods --------------------------------------------------------------------------------------------------
  public void addArea(Area a)
  {
    display.add(a, a.name);
    areas.push(a);
  }
  
  public void remArea(Area a)
  {
    CardLayout cl = (CardLayout)display.getLayout();
    cl.removeLayoutComponent(a);
    areas.remove(a);
  }
  
  public void showArea(Area a)
  {
    CardLayout cl = (CardLayout)display.getLayout();
    cl.show(display, a.name);
    area = a;

    //if (area.length <= 7) jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
    //else jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    
    if (a.visited == false) {System.out.println("Visiting " + a.name + " for first time."); showPanel(area.anchor);}
    else {System.out.println("Been here before"); jsp.getViewport().setViewPosition(area.vp);}
    
    a.visited = true;
  }  
  
  public void showPanel(Area.Panel p)
  {
    if (!area.equals(p.pArea)) showArea(p.pArea);
    jsp.getViewport().setViewPosition(p.getPanelPoint());
  }
  
  // Removes other Area and adds Ship to BScreen
  public void addShip(Ship s)
  { 
    s.bridge.bs = this;
    display.add(s.bridge, s.bridge.name);
    areas.push(s.bridge);
    s.bridge.vp = null;
    showArea(s.bridge);
    
    for (int i = 0; i < areas.size(); i++)
    {
      Area a = areas.get(i);
      if (!a.equals(area)) {System.out.println("Deleting " + a.name); remArea(a); i = -1;}
    }

    System.out.println("Number of Areas: " + areas.size());
  }
  
  public void addSquad(Squad sq)
  {
    area.enterSquad(sq);
    squads.add(sq);
    sq.teamNum = squads.indexOf(sq);
  }
  
  // Alerts the sytem if there are no active Bods left in the Area
  public void checkActive()
  {
    Stack<Bod> temp = peeps;
    peeps = new Stack<Bod>();
    
    boolean reset = true;
    
    while (temp.empty() == false)
    {
      Bod b = temp.pop();
      if (b.getAP() > 0)
      {
        reset = false;
      }
      peeps.push(b);
    }
    
    if (reset) resetPeeps();
  }
  
  // Resets all action/movement points and refreshes shields
  public void resetPeeps()
  {
    // Runs the Bot stack
    runBots();
    
    JOptionPane.showMessageDialog(null, "New Round!");
    
    Stack<Bod> temp = peeps;
    peeps = new Stack<Bod>();
    
    while(temp.empty() == false)
    {
      Bod b = temp.pop();
      if (!b.getDead()) b.setAP(b.getEnd());
      if (b.getShield() != null) b.getShield().recover();
      peeps.push(b);
    }
    
    area.resetPanels();
    setEmpty();
  }
  
  public void setEmpty()
  {
    area.active = null;
    
    // Name update
    sName.setText("Name:          ");
    
    // Class update
    sClass.setText("Class:         ");
    
    // Image update
    sImg.setIcon(null);
    
    // HP Calc
    for (JButton jb : hbar){jb.setVisible(false);}
    bodHP.setText("___");
    
    // Shield Calc
    for (JButton jb : sbar){jb.setVisible(false);}
    bodSH.setText("___");
    
    // AP Calc
    sAP.setText("___");
    
    // Move Calc
    sMov.setText("___");
    
    // Stats update
    sStats.setText("\n\n\n\n\n");
    
    // Equip update
    eName.setText("_____");
    eImg.setIcon(null);
    ePow.setText("_____");
    eUse.setText("_____");
    
    // End Button update
    end.setText("End Round");
  }
  
  public void updateSide(Area.Panel ap)
  {
    area.resetPanels();
    setEmpty();
    
    area.active = ap;
    
    if (ap.mob != null)
    {
      // MOB UPDATE
      sName.setText(ap.mob.getName());
      if (ap.mob.broke) sName.setText(ap.mob.getName() + " (broke)");
      sClass.setText("Object");
      ImageIcon ii = null;
      if (ap.mob.broke) ii = ap.mob.getIH();
      else ii = ap.mob.getII();
      Image im = ii.getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT);
      ii = new ImageIcon(im);
      sImg.setIcon(ii);
      sStats.setText(ap.mob.getStats());
    }
    else
    {
      // BOD UPDATE
      
      if (ap.occupant == null && ap.dHold != null) ap.occupant = ap.dHold;
      
      // Name update
      sName.setText("Name: " + ap.occupant.getName());
      if (ap.occupant.getDead()) sName.setText(sName.getText() + " (Dead)");
      
      // Class update
      sClass.setText("Class: " + ap.occupant.getJClass());
      
      // Image update
      ImageIcon ii = ap.occupant.getIH();
      Image im = ii.getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT);
      ii = new ImageIcon(im);
      sImg.setIcon(ii);
      
      // HP Calc
      int hd = ap.occupant.getHP() / ap.occupant.getVi();
      for (JButton jb : hbar){jb.setVisible(true);}
      for (int i = 9; i >= hd; i--){hbar[i].setVisible(false);}   
      if (ap.occupant.getHP() > 0) hbar[0].setVisible(true);
      bodHP.setText(ap.occupant.getHP() + "/" + (ap.occupant.getVi() * 10));
      
      // Shield Calc
      int sd = 0;
      if (ap.occupant.getShield() != null) 
        sd = 10 * ap.occupant.getShield().getSh() / ap.occupant.getShield().getShMax();
      for (JButton jb : sbar){jb.setVisible(true);}
      for (int i = 9; i >= sd; i--){sbar[i].setVisible(false);}
      if (ap.occupant.getShield() != null && ap.occupant.getShield().getSh() > 0) 
      {
        sbar[0].setVisible(true);
        bodSH.setText(ap.occupant.getShield().getSh() + 
                      "/" + ap.occupant.getShield().getShMax());
      }
      else bodSH.setText(0 + "/" + 0);
      if (ap.occupant.getShield() != null &&
          ap.occupant.getShield().getBroke()) 
        bodSH.setText(bodSH.getText() + "(Broken)");
      
      // AP Calc
      if (ap.occupant.getDead()) ap.occupant.setAP(0);
      sAP.setText("" + ap.occupant.getAP());
      
      // Move Calc
      int steps = ap.occupant.getSpd() - (ap.occupant.getEnd() - ap.occupant.getAP());
      if (steps < 1) steps = 1;
      if (ap.occupant.getAP() == 0) steps = 0;
      sMov.setText("" + steps);
      
      // Stats update
      sStats.setText(ap.occupant.getStats());
      
      // Equip update
      if (ap.occupant.getPrimary().getEImg() != null)
      {
        eName.setText(ap.occupant.getPrimary().getName());
        if (ap.occupant.getPrimary().getIsGun()) eName.setText(eName.getText() + 
                                                               " (" + 
                                                               ap.occupant.getPrimary().getMinR() +
                                                               "-" +
                                                               ap.occupant.getPrimary().getMaxR() +
                                                               ")");
        ImageIcon ii2 = ap.occupant.getPrimary().getEImg();
        Image im2 = ii2.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT);
        ii2 = new ImageIcon(im2);
        eImg.setIcon(ii2);
        ePow.setText(ap.occupant.getPrimary().getEPow());
        eUse.setText(ap.occupant.getPrimary().getEUse()); 
      }
      
      if (ap.dHold != null && ap.dHold.equals(ap.occupant)) ap.occupant = null;
      
      // End Button udpate
      end.setText("End Turn");
      
      if (ap.occupant != null && ap.occupant.getAP() > 0 && !ap.occupant.getIsBot()) showActable(ap); 
    }
  }
  // Show movable/actable panels using recursive pathfinding -------------------------------------------------------
  public void showActable(Area.Panel ap)
  {
    if (ap.mob != null || ap.occupant.getIsBot() || ap.occupant.getAP() <= 0) return; 
    
    showMovable(ap);
    for (Area.Panel[] pa : area.spaces)
    {
      for (Area.Panel p : pa)
      {
        // Sets targetable panels to red
        if (((p.occupant != null || p.mob != null) && 
             (ap.occupant.getPrimary().isValidTar(p.occupant) || 
              ap.occupant.getPrimary().isValidTar(p.mob))) 
              || 
            ap.occupant.getPrimary().isValidTar(p))
        {
          p.setText("\u25ce");
          p.setFont(new Font("Papyrus", Font.PLAIN, 50));
          p.setForeground(Color.RED.darker());
          
          acting = true;
        }
      }
    }
  }
  public void showMovable(Area.Panel ap)
  {
    if (ap.mob != null) return;
    
    // Calculates the initial step ability of occupant
    int steps = ap.occupant.getSpd() - (ap.occupant.getEnd() - ap.occupant.getAP());
    if (steps < 1) steps = 1;
    if (ap.occupant.getAP() == 0) steps = 0;
    
    // Returns a Stack of valid panels to test
    Stack<Area.Panel> ps = ap.validPanels(ap, ap);
    
    while(ps.empty() == false)
    {
      Area.Panel p = ps.pop();
      ap.findPath(ap, p, steps);
    }
  }
  
  // BOT ACTIONS --------------------------------------------------------
  public void showActableBot(Area.Panel ap)
  {
    Bot b = (Bot)ap.occupant; if (b.getDirective().equals("Wait")) {b.resetBot(); return;}
    
    area.active = ap;
    scanning = true;
    
    for (Area.Panel[] pa : area.spaces)
    {
      for (Area.Panel p : pa)
      {
        if (p.occupant == null || !p.occupant.equals(ap.occupant))
        {
          p.setText("\u25ce");
          p.setFont(new Font("Papyrus", Font.PLAIN, 50));
          p.setForeground(Color.BLUE.brighter().brighter()); 
        }
      }
    }
  }
  
  
  public void actBot(Area.Panel ap)
  {
    if (ap.occupant == null) return;
    
    Bot bot = (Bot)ap.occupant;
    
    if (bot.packed == true) return;
    
    // Step 1: Gets Directive
    
    // Waiting for instructions
    if (bot.getDirective().equals("Wait")) return;
    
    // Sets the viewscreen
    updateSide(ap);
    showPanel(ap);
    
    // Acting on instructions
    Area.Panel p = bot.getTarget();
    
    // Analyzes current directive to determine action
    char c = bot.getTType();
    if (c == 'm' && p.mob.broke) {bot.resetBot(); return;}              // Mob object is already broken, reset
    // Bod target is dead
    if (c == 'b' && bot.getMTar().getIsBod())
    {
      Bod b = (Bod)bot.getMTar();
      if (b.getDead())
      {
        String t2Msg = "Target has been terminated";
        ani.runAnimationTalk(this, t2Msg, 1, (t2Msg.length() * 10), 30);
        bot.resetBot(); 
        return;
      }
    }
    // Bod has moved, resets target panel
    if (c == 'b' && (p.occupant == null || !p.occupant.equals(bot.getMTar())))                  
    {
      String tMsg = "Target has shifted";
      ani.runAnimationTalk(this, tMsg, 1, (tMsg.length() * 10), 30);
      bot.setTarget(bot.getMTar().getLocal());
      p = bot.getTarget();
    } 
    if (c == 'b' && (p.occupant == null || p.occupant.getDead())) {bot.resetBot(); return;}   // Bod is dead, reset
    // Useful Gun-toting actions (i.e. Reloading, targeting)
    if (c == 'b' && bot.getPrimary().getIsGun())
    {
      Gun g = (Gun)bot.getPrimary();
      
      // Reload if necessary
      if (g.getHeat() >= g.getCap()) 
      {
        g.maint(); 
        String gMsg = "Reloading";
        ani.runAnimationTalk(this, gMsg, 1, (gMsg.length() * 10), 30);
        return;
      }
      // Get closer if necessary
      int dist = Math.abs(bot.getPX() - p.occupant.getPX()) + Math.abs(bot.getPY() - p.occupant.getPY());
      if (dist > g.getMaxR())
      {
        ap.moveBotTo(bot.getTarget());
        return;
      }
    }
    if (c == 'b' && bot.getPrimary().getIsTool())
    {
      boolean validAct = true;
      if (p.mob == null)
      {
        validAct = bot.getPrimary().isValidTar(p.occupant);
        // Verifies that the problem isn't distance
        if (validAct == false)
        {
          int holdX = p.occupant.getPX();
          int holdY = p.occupant.getPY();
          p.occupant.setPX(bot.getPX() + 1);
          p.occupant.setPY(bot.getPY());
          validAct = bot.getPrimary().isValidTar(p.occupant); // Reverifies if act is valid
          p.occupant.setPX(holdX);
          p.occupant.setPY(holdY);
        }
      }
      if (p.occupant == null) validAct = bot.getPrimary().isValidTar(p.mob);
      
      if (validAct == false)
      {
        String iMsg = "Target is Invalid";
        ani.runAnimationTalk(this, iMsg, 1, (iMsg.length() * 10), 30);
        bot.resetBot(); 
        return;
      }
    }
    // Bot has reached destination
    if (c == 'p' && ap.equals(p)) 
    {
      String tmMsg = "Reached Target";
      ani.runAnimationTalk(ap, tmMsg, 1, (tmMsg.length() * 10), 30);
      bot.resetBot(); 
      return;
    } 
    
    // Acting options
    if ((p.mob != null && ap.occupant.getPrimary().isValidTar(p.mob)) || 
        (p.occupant != null && ap.occupant.getPrimary().isValidTar(p.occupant)))
    {
      String aMsg = bot.paMsg;
      ani.runAnimationTalk(ap, aMsg, 1, (aMsg.length() * 10), 30);
      
      // Action/Animation
      
      jsp.getViewport().setViewPosition(p.getPanelPoint());
      Boolean hitMiss = false;
      if (p.mob != null) {System.out.println("Attempting to use tool"); hitMiss = ap.occupant.getPrimary().usePrimary(p.mob);}
      if (p.occupant != null) {System.out.println("Attempting to use tool"); hitMiss = ap.occupant.getPrimary().usePrimary(p.occupant);}
      
      // Bars animation
      if (p.occupant != null)
      {
        int bSh = 0;
        if (p.occupant.getShield() != null) 
          bSh = 10 * p.occupant.getShield().getSh() / p.occupant.getShield().getShMax();
        int bHp = p.occupant.getHP() / p.occupant.getVi();
        ani.runBars(p, bHp, bSh, 1, 50, 25); 
        
        p.deadCheck();
      }
      
      bottom.setText(ap.occupant.getPrimary().getMsg());
      botJSP.getVerticalScrollBar().setValue(0);
      jsp.getViewport().setViewPosition(bot.getLocal().getPanelPoint());
      
      // Image Update
      if (bot.getTarget().occupant == null && bot.getTarget().mob != null && bot.getTarget().mob.broke)
      {
        System.out.println("EXITING");
        bot.getTarget().bImg.setIcon(null);
        
        ImageIcon in = bot.getTarget().mob.getIH();
        Image in2 = in.getImage().getScaledInstance(90, 90, Image.SCALE_DEFAULT);
        bot.getTarget().bImg.setIcon(new ImageIcon(in2));
      }
      
      botJSP.getVerticalScrollBar().setValue(0);
    }
    else {ap.moveBotTo(bot.getTarget());}
  }
  
  
  public void runBots() // Runs robots and monsters
  {
    Stack<Bot> temp = bots;
    bots = new Stack<Bot>();
    
    while (temp.empty() == false)
    {
      Bot b = temp.pop();
      if (b.getIsMon() && b.getDead() == false && aiActive)
      {
        Mon m = (Mon)b;
        m.findTarget(area);
        System.out.println("Monster targeting: " + m.getTarget().occupant.getName());
        actBot(m.getLocal());
      }
      else if (b.getIsMon() == false && b.getDead() == false)
      {   
        b.setAP(2);
        while (b.getAP() > 0)
        {
          actBot(b.getLocal());
          b.setAP(b.getAP() - 1);
        }
      }
      if (bots.search(b) == -1) bots.push(b);
    }
  }
}