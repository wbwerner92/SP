import java.util.*;

class Ship
{
  String name;
  LinkedList<Area> rooms = new LinkedList<Area>();
  Area bridge;
  Area engineRoom;
  
  public enum ShipType {SMALL, MEDIUM, LARGE}
  ShipType st;
  
  Ship()
  {
    name = "Ship Mk.1";
    st = ShipType.SMALL;
    sayShipType();
    
    makeBridge();
    
    engineRoom = new Area(name + " Engine Room", null);
    engineRoom.createSmallRoom();
    rooms.push(engineRoom);
  }
  
  public void makeBridge()
  {
    bridge = new Area(name + " Bridge", null);
    bridge.createSmallRoom();
    
    Computer comp = new Computer();
    bridge.spaces[3][3].setMob(comp);
    comp.scanArea(bridge);
    
    rooms.push(bridge);
  }
  
  public void sayShipType()
  {
    switch (st)
    {
      case SMALL:
        System.out.println("It's a wee lil' ship");
        break;
        
      case MEDIUM:
        System.out.println("Medium sized and proud");
        break;
        
      case LARGE:
        System.out.println("Set it to W for Wambo");
        break;
        
      default:
        System.out.println("IDK my BFF Jill");
        break;
    }
  }
  
  // Setter & Getter Methods
  public String getName(){return name;}
}