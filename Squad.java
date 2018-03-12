import java.util.LinkedList;

class Squad
{
  public String name;
  public LinkedList<Bod> memb = new LinkedList<Bod>();
  public int maxSize = 4;
  
  public int teamNum;
  
  Squad()
  {
    name = "Squad";
  }
  
  public boolean addMem(Bod b)
  {
    if (memb.size() >= maxSize) return false;
    
    memb.push(b);
    b.squad = this;
    return true;
  }
  
  public void setTeam(int i)
  {
    teamNum = i;
    for (int j = 0; j < memb.size(); j++)
    {
      Bod b = (Bod)memb.get(j);
      b.setCol(i);
    }
  }
  
  public int getSize(){return memb.size();}
  public Bod getMem(int i){return (Bod)memb.get(i);}
  
}