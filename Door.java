import java.util.Stack;

class Door extends Mob
{
  Area aOne;
  Area.Panel pOne;
  Area aTwo;
  Area.Panel pTwo;
  
  Door()
  {
    setDoor();
    setName("T Door");
  }

  // Links two Area with a Door object
  public void linkArea(Area a, Area b)
  {
    if (a.dEast != null && a.dEast.mob == null)
    {
      if (b.dWest != null && b.dWest.mob == null)
      {
        aOne = a; aTwo = b;
        pOne = a.dEast; pTwo = b.dWest;
        a.dEast.setVisible(true);
        a.dEast.setMob(this);
        b.dWest.setVisible(true);
        b.dWest.setMob(this);
      }
    }
    else if (a.dWest != null && a.dWest.mob == null)
    {
      if (b.dEast != null && b.dEast.mob == null)
      {
        aOne = a; aTwo = b;
        pOne = a.dWest; pTwo = b.dEast;
        a.dWest.setVisible(true);
        a.dWest.setMob(this);
        b.dEast.setVisible(true);
        b.dEast.setMob(this);
      }
    }
    else if (a.dNorth != null && a.dNorth.mob == null)
    {
      if (b.dSouth != null && b.dSouth.mob == null)
      {
        aOne = a; aTwo = b;
        pOne = a.dNorth; pTwo = b.dSouth;
        a.dNorth.setVisible(true);
        a.dNorth.setMob(this);
        b.dSouth.setVisible(true);
        b.dSouth.setMob(this);
      }
    }
    else if (a.dSouth != null && a.dSouth.mob == null)
    {
      if (b.dNorth != null && b.dNorth.mob == null)
      {
        aOne = a; aTwo = b;
        pOne = a.dSouth; pTwo = b.dNorth;
        a.dSouth.setVisible(true);
        a.dSouth.setMob(this);
        b.dNorth.setVisible(true);
        b.dNorth.setMob(this);
      }
    }
  }
  
  public void moveDoor(Bod b)
  {
    if (b.getLocal().pArea.equals(aOne)){shiftBod(b, pTwo, aTwo);}
    else if (b.getLocal().pArea.equals(aTwo)){shiftBod(b, pOne, aOne);}
  }
  public void shiftBod(Bod b, Area.Panel pTo, Area to)
  {
    for (Area.Panel[] pa : to.spaces)
    {
      for (Area.Panel p : pa)
      {
        int dist = Math.abs(pTo.posX - p.posX) + Math.abs(pTo.posY - p.posY);
        
        if (p.isVisible() && dist < 3 && dist > 0 && p.occupant == null)
        {
          p.enter(b.getLocal().exit());
          to.bs.showArea(to);
          to.bs.showPanel(p);
          return;
        }
      }
    }
  }
}