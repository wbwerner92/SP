import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;

class Animator
{
  JDialog frame;
  
  Animator(){}
  
  // Animates a single gif for a specified duration
  public void runAnimation(Component c, ImageIcon i, int t, int x, int y)
  {
    if (i == null) return;
    
    frame = new JDialog();
    frame.setUndecorated(true);
    frame.setSize(x, y);
    frame.setLocationRelativeTo(c);
    frame.toFront();
    JPanel panel = new JPanel();
    JLabel jl = new JLabel();
    
    // ImageIcon adjustment
    ImageIcon ii = i;
    Image im = ii.getImage().getScaledInstance(x, y, Image.SCALE_DEFAULT);
    ii = new ImageIcon(im);
    jl.setIcon(ii);
    
    // Timer
    ActionListener al = new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        frame.dispose();
      }
    };
    Timer ti = new Timer((t * 900), al);
    ti.setRepeats(false);
    ti.start();
    
    panel.add(jl); 
    frame.add(panel);
    
    frame.setModal(true);
    frame.setVisible(true);     
  }
  
  // Animates two gifs one after the other for specified durations
  public void run2Animation(Component c, ImageIcon i, int t, ImageIcon i2, int t2, int x, int y)
  {
    frame = new JDialog();
    frame.setUndecorated(true);
    frame.setSize(x, y);
    frame.setLocationRelativeTo(c);
    frame.toFront();
    JPanel panel = new JPanel();
    JLabel jl = new JLabel();
    
    // First ImageIcon adjustment
    ImageIcon ii = i;
    Image im = ii.getImage().getScaledInstance(x, y, Image.SCALE_DEFAULT);
    ii = new ImageIcon(im);
    jl.setIcon(ii);
    
    final Component cN = c;
    final ImageIcon iN = i2;
    final int tN = t2;
    final int xN = x;
    final int yN = y;
    
    // Timer
    ActionListener al = new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        frame.dispose();
        runAnimation(cN, iN, tN, xN, yN);
      }
    };
    
    Timer ti = new Timer((t * 900), al);
    ti.setRepeats(false);
    ti.start();
    
    panel.add(jl); 
    frame.add(panel);
    
    frame.setModal(true);
    frame.setVisible(true);     
  }
  
  
  
  // Animates Two bars based on input integers
  public void runBars(Component c, int hb, int sb, int t, int x, int y)
  {
    frame = new JDialog();
    frame.setUndecorated(true);
    frame.setSize(x, y);
    frame.setLocationRelativeTo(c);
    frame.toFront();
    JPanel panel = new JPanel(new GridLayout(2, 1));
    
    JPanel hPanel = new JPanel(new GridLayout(1, 10));
    JButton[] h = new JButton[10];
    for (int i = 0; i < 10; i++)
    {
      h[i] = new JButton();
      h[i].setPreferredSize(new Dimension(7, 7));
      h[i].setBackground(Color.RED);
      h[i].setOpaque(true);
      h[i].setBorderPainted(false);
      if (i >= hb) h[i].setVisible(false);
      hPanel.add(h[i]); 
    }
    if (hb > 0) h[0].setVisible(true);
    panel.add(hPanel);
    JPanel sPanel = new JPanel(new GridLayout(1, 10));
    JButton[] s = new JButton[10];
    for (int i = 0; i < 10; i++)
    {
      s[i] = new JButton();
      s[i].setPreferredSize(new Dimension(7, 7));
      s[i].setBackground(Color.CYAN);
      s[i].setOpaque(true);
      s[i].setBorderPainted(false);
      if (i >= sb) s[i].setVisible(false);
      sPanel.add(s[i]); 
    }
    if (sb > 0) s[0].setVisible(true);
    panel.add(sPanel);
    
    // Timer
    ActionListener al = new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        frame.dispose();
      }
    };
    Timer ti = new Timer((t * 900), al);
    ti.setRepeats(false);
    ti.start();
   
  
    frame.add(panel);    
    frame.setModal(true);
    frame.setVisible(true);     
  }
  
  
  public void runAnimationTalk(Component c, String text, int t, int x, int y)
  {
    frame = new JDialog();
    frame.setUndecorated(true);
    frame.setSize(x, y);
    frame.setLocationRelativeTo(c);
    frame.toFront();
    JPanel panel = new JPanel();
    JLabel jl = new JLabel("\"" + text + "\"");
    jl.setFont(new Font("Papyrus", Font.PLAIN, 12));
    panel.add(jl);

    // Timer
    ActionListener al = new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        frame.dispose();
      }
    };
    Timer ti = new Timer((t * 900), al);
    ti.setRepeats(false);
    ti.start();
   
  
    frame.add(panel);    
    frame.setModal(true);
    frame.setVisible(true);
  }
}