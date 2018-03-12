/*
 * @class    RVCalc.java - This program is designed to provide a supplemental object for programs that utilize semmingly 
 * random chance rolls to include a dice roll component for variable results.
 * The object utilizes the Math.random call and an algorithm given to intake a minimum and maximum value and output a 
 * number between the two (inclusively). 
 * The object may be used repeatedly calling its public "rInt(int min, int max)" method to return the generated value.
 * This version includes a GUI component which creates an extended JPanel component called a RollPanel. This panel
 * contains a visual for users to perform random number rolls between any values 
 * ex) Roll = 13 from minimum 1 to maximum 100
 * 
 * @version   2.3
 * @updated   March 1, 2018
 * @author    William B. Werner
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class RVCalc
{
  RollPanel[] ePanels;
  
  // Empty constructor to create an instance of an RVCalc object
  RVCalc(){}
  
  /*
   * rInt - The central method of the RVCalc takes two integers as parameters and calculates an integer value between 
   * them inclusively. To call this method, an RVCalc object is created first which may then be used to call the rInt 
   * method as many times as necessary using a new instance of this method each time allowing for numerous values 
   * generated from the same set of minimum and maximum integers.
   * 
   * @param    int min - Minimum value (inclusive) of the range of the returned value
   * @param    int max - Maximum value (inclusive) of the range of the returned value
   * @return   int roll - Calculated seemingly random value ranging from the minimum value to the maximum. In cases 
   * where there is erroneous input from a user or program and the minimum value is greater than the maximum value, the 
   * maximum value is returned. In cases where there is a variant value outside the determined range, the method 
   * returns a recursive call of itself.
   */
  public int rInt(int min, int max)
  {
    if (min > max) return max;
    
    int roll = min + (int)(Math.random() * ((max - min) + 1));
    
    if (roll < min || roll > max) return rInt(min, max);
    else return roll;
  }
  
  /*
   * showCalc - This method creates 3 instances of the GUI object RollPanel in a JFrame object for use outside of a 
   * program. The JFrame comes with a clickable JButton which upon clicking generates the results from the input values. 
   * Results may be rolled as many times as necessary and the program ends when the RollPanel object is exited.
   * 
   * @param    none
   * @return   void
   */
  public void showCalc()
  {
    JFrame frame = new JFrame();
    frame.setTitle("RV Calculator");
    frame.setSize(200, 350);
    frame.setResizable(false);
    frame.setLocationRelativeTo(null);
    frame.toFront();
    
    JPanel panel = new JPanel(new GridLayout(4, 1));
    
    ePanels = new RollPanel[3];
    for (int i = 0; i < 3; i++)
    {
      RollPanel rp = new RollPanel();
      ePanels[i] = rp;
      panel.add(rp);
    }
    
    // JButton which generates values for all included RollPanels
    JButton roll = new JButton("Roll");
    roll.addActionListener(new ActionListener()
                             {
      public void actionPerformed(ActionEvent e)
      {
        for (RollPanel rp : ePanels)
        {
          rp.roll();
        }
      }
    });
    panel.add(roll);
    
    frame.add(panel);
    frame.setVisible(true);
  }
  
  /*
   * @innerclass  RollPanel extends JPanel
   * This inner class is intended for anyone looking to generate random values for boardgames and the like alone or in 
   * commmunication with others with needs beyond standard dice rolls. Users may dictate the range of values for their
   * rolls. A RollPanel allows for input of a minimum and maximum value and displays the result.
   * 
   * @param   UI - User interface/input affects the output of the panel
   * @return  JPanel extended object: RollPanel
   * 
   */
  class RollPanel extends JPanel
  {
    JTextField field;
    JTextField minF;
    JTextField maxF;
    
    RollPanel()
    {
      setLayout(new GridLayout(1, 3));
      minF = new JTextField("--");
      minF.setHorizontalAlignment(JTextField.CENTER);
      add(minF);
      maxF = new JTextField("--");
      maxF.setHorizontalAlignment(JTextField.CENTER);
      add(maxF);
      field = new JTextField("--");
      field.setHorizontalAlignment(JTextField.CENTER);
      field.setEditable(false);
      add(field); 
    }
    
    public void roll()
    {
      try
      {
        int minI = Integer.parseInt(minF.getText());
        int maxI = Integer.parseInt(maxF.getText());
        
        int roll = rInt(minI, maxI);
        if (roll < 0) field.setText("Error");
        else field.setText("" + roll);
      }
      catch(NumberFormatException nfe) {field.setText("--");}    
    }
  }
}