import javax.swing.*;

class Turret extends Mob
{
  int turTeam;
  TurretGun gun;
  
  Turret()
  {
    setTurret();
    gun = new TurretGun();
  }
  
  
  // SET TURRET GUNS POSTION WHEN YOU SET THE TURRET POSTION FOR DIST CALC IN SHOOTING
  
  public class TurretGun extends Bod
  { 
    TurretGun()
    {
      setJClass("Turret Gun");
      setPrimary(null);
      
      setEnd(2);
      setAP(getEnd());
      
      setGuns(1);
      
      Gun g = new Gun();
      g.generateHorizonRifle();
      g.equip(this);
    }
  }
}