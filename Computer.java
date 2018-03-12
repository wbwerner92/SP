import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class Computer extends Mob
{
  boolean dLock;
  JButton cOpen;
  
  LinkedList<Mob> units = new LinkedList<Mob>();
  
  Computer()
  {
    setComputer();
  }
  
  public void scanArea(Area a)
  {
    for (Area.Panel[] pa : a.spaces)
    {
      for (Area.Panel p : pa)
      {
        if (p.mob != null && !p.mob.equals(this))
        {
          if (p.mob.getIsDoor()) dLock = true; // sets Lock function to locked if door is connected to comp matrix
          units.push(p.mob);
          System.out.println("Adding " + p.mob.getName() + " to unit matrix...");
        }
      }
    }
  }
  
  // Use Computer options
  public void useComp()
  {
    getLocal().pArea.bs.showPanel(getLocal());
    
    final JDialog frame = new JDialog();
    frame.setSize(100, 200);
    frame.setLocationRelativeTo(getLocal());
    frame.setUndecorated(true);
    frame.toFront();
    
    JPanel panel = new JPanel(new GridLayout(2, 1));
    
    cOpen = new JButton();
    if (dLock) cOpen.setText("Unlock Doors");
    else cOpen.setText("Lock Doors");
    cOpen.addActionListener(new ActionListener()
                              {
      public void actionPerformed(ActionEvent e)
      {
        if (dLock == true)
        {
          if (JOptionPane.showConfirmDialog(getLocal(), "Open all Security Doors?") != 0) return;
          for (Mob m: units)
          {
            if (m.getIsDoor())
            {
              m.setPowLvl(0);
              m.broke = true;
              
              Door d = (Door)m;
              d.pOne.updateMob();
              d.pTwo.updateMob(); 
            }
          }
          dLock = false;
          cOpen.setText("Lock Doors");
          return;
        }
        
        if (dLock == false)
        {
          if (JOptionPane.showConfirmDialog(getLocal(), "Lock all Security Doors?") != 0) return;
          for (Mob m: units)
          {
            if (m.getIsDoor())
            {
              m.setPowLvl(m.getPowLvlMax());
              m.broke = false;
              
              Door d = (Door)m;
              d.pOne.updateMob();
              d.pTwo.updateMob(); 
            }
          }
          dLock = true;
          cOpen.setText("Unlock Doors");
          return;
        }
      }
    });
    panel.add(cOpen);
    
    JButton cExit = new JButton("Exit Computer");
    cExit.addActionListener(new ActionListener()
                              {
      public void actionPerformed(ActionEvent e)
      {
        frame.dispose();
      }
    });
    panel.add(cExit);
    
    frame.add(panel);
    frame.setModal(true);
    frame.setVisible(true);
  }
  
  public String getStats()
  {
    String cMsg = ("Security Level: " + getPowLvl() +"\n");
    
    for (int i = 0; i < units.size(); i++)
    {
      cMsg += units.get(i).getName() + " - SL: " + units.get(i).getPowLvl() + "\n";
    }
    
    return cMsg;
  }
}
