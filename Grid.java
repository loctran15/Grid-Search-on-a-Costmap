import java.lang.Comparable;
/**
 * Write a description of Grid here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Grid implements Comparable<Grid>{
    public int positionX;
    public int positionY;
    public double gScore;
    public double fScore;
    public double val;
    public double cost;
    public boolean isObstacle;
    public boolean isGoal;
    public boolean isStart;
    public Grid parentGrid;
    Grid(int positionX, int positionY, double val, String method, boolean isGoal, boolean isStart)
    {
        this.positionX = positionX;
        this.positionY = positionY;
        this.val = val;
        this.isGoal = isGoal;
        this.isStart = isStart;
        this.fScore = Double.POSITIVE_INFINITY;
        if (this.isStart)
        {
            gScore = 0;
        }
        else
        {
            gScore = Double.POSITIVE_INFINITY;
        }
        if (val == 0 && !isGoal)
        {
            isObstacle = true;
        }
        else
        {
            isObstacle = false;
        }
        if ((method == "DFS" || method == "BFS") && val != 0)
        {
            val = 1;
        }
        cost = 1/val;
    }
    
    public int compareTo(Grid grid)
    {
        if(this.fScore > grid.fScore) return 1;
        else if(this.fScore < grid.fScore) return -1;
        else return 0;
    }
    
    public void parent (Grid grid)
    {
       this.parentGrid = grid;
    }
}
