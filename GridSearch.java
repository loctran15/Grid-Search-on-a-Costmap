import java.util.Random;
import java.io.*; 
import java.util.Scanner;
import java.lang.Object;
import java.util.ArrayList;
import java.lang.Math;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
/**
 * 
 * @author (Loc Tran) 
 */
public class GridSearch {
    public static void main(String[] args) throws IOException,FileNotFoundException
    {
        String method = args[0];
        String gridFile = args[1];
        int rstart = Integer.parseInt(args[2]);
        int cstart = Integer.parseInt(args[3]);
        int rend = Integer.parseInt(args[4]);
        int cend = Integer.parseInt(args[5]);
        String pathFile = args[6];
        Grid[][] matrix = initialize(gridFile, rstart, cstart, rend, cend, method);
        if(method.equals("BFS"))
        {
            BFSMethod(matrix, pathFile, rstart, cstart);
        }
        if(method.equals("DFS"))
        {
            DFSMethod(matrix, pathFile, rstart, cstart);
        }
        if(method.equals("AStarZero") || method.equals("AStarEuclidean") || method.equals("AStarManhattan"))
        {
            AStar(matrix, pathFile, rstart, cstart, rend, cend, method);
        }
        if(method.equals("FringeSearch"))
        {
            fringeMethod(matrix, pathFile, rstart, cstart, rend, cend);
        }
    }
    
    public static void BFSMethod(Grid[][] matrix, String pathFile, int rstart, int cstart) throws IOException
    {
        Queue<Grid> F = new Queue<Grid>();
        Grid[][] parent = new Grid[matrix.length][matrix[0].length];
        Grid gridV = matrix[rstart][cstart];
        Grid gridU;
        F.enqueue(gridV);
        parent[gridV.positionX][gridV.positionY] = gridV;
        while (!F.isEmpty())
        {
            gridU = F.dequeue();
            if (isGoal(gridU))
            {
                path(gridU, parent, pathFile);
                return;
            }
            for (Grid grid : outEdges(gridU, matrix))
            {
                if (parent[grid.positionX][grid.positionY] == null)
                {
                    F.enqueue(grid);
                    parent[grid.positionX][grid.positionY] = gridU;
                }
            }
        }     
    }
    
    public static void DFSMethod(Grid[][] matrix, String pathFile, int rstart, int cstart) throws IOException
    {
        Stack<Grid> F = new Stack<Grid>();
        Grid[][] parent = new Grid[matrix.length][matrix[0].length];
        Grid gridV = matrix[rstart][cstart];
        Grid gridU;
        F.push(gridV);
        parent[gridV.positionX][gridV.positionY] = gridV;
        while (!F.isEmpty())
        {
            gridU = F.pop();
            if (isGoal(gridU))
            {
                path(gridU, parent, pathFile);
                return;
            }
            for (Grid grid : outEdges(gridU, matrix))
            {
                if (parent[grid.positionX][grid.positionY] == null)
                {
                    F.push(grid);
                    parent[grid.positionX][grid.positionY] = gridU;
                }
            }
        }     
    }
    
    public static void AStar(Grid[][] matrix, String pathFile, int rstart, int cstart, int rend, int cend, String method) throws IOException
    {
        HashSet<Grid> closedSet = new HashSet<Grid>();
        HashSet<Grid> openHashSet = new HashSet<Grid>();
        MinHeap<Grid> openSet = new MinHeap<Grid>();
        Grid[][] parent = new Grid[matrix.length][matrix[0].length];
        openSet.add(matrix[rstart][cstart]);
        openHashSet.add(matrix[rstart][cstart]);
        matrix[rstart][cstart].fScore = getHScore(matrix[rstart][cstart], matrix[rend][cend], method);
        Grid u;
        parent[rstart][cstart] = matrix[rstart][cstart];
        while (!openSet.isEmpty())
        {
            u = openSet.remove();
            if (u.isGoal)
            {
                path(u, parent, pathFile);
            }
            closedSet.add(u);
            for (Grid neighborGrid : outEdges(u, matrix))
            {
                if (closedSet.contains(neighborGrid))
                {
                    continue;
                }
                double g = u.gScore + neighborGrid.cost;
                if (!openHashSet.contains(neighborGrid))
                {
                    openSet.add(neighborGrid);
                    openHashSet.add(neighborGrid);
                }
                else if (g >= neighborGrid.gScore)
                {
                    continue;
                }
                parent[neighborGrid.positionX][neighborGrid.positionY] = u;
                neighborGrid.gScore = g;
                updateFScore(neighborGrid, matrix[rend][cend], method);
            }
        }
    }
    
    public static void fringeMethod (Grid[][] matrix, String pathFile, int rstart, int cstart, int rend, int cend) throws IOException
    {
        String method = "AStarZero";
        
        double fmin;
        double g;
        double g_cached;
        double g_child;
        double f;
        Grid parent;
        
        Grid[][] parents = new Grid[matrix.length][matrix[0].length];
        DoublyLinkedList<Grid> fringe = new DoublyLinkedList<Grid>();
        matrix[rstart][cstart].fScore = getHScore(matrix[rstart][cstart], matrix[rend][cend], method);
        
        fringe.addFirst(matrix[rstart][cstart]);
        
        parents[rstart][cstart] = matrix[rstart][cstart];
        double fLimit = getHScore(matrix[rstart][cstart], matrix[rend][cend], method);
        boolean found = false;
        
        while (found == false && !fringe.isEmpty())
        {
            fmin = Double.POSITIVE_INFINITY;
            for (Grid grid : fringe)
            {
                g = grid.gScore;
                parent = parents[grid.positionX][grid.positionY];
                
                f = g + getHScore(grid, matrix[rend][cend], method);
                if (f > fLimit)
                {
                    fmin = Math.min(f, fmin);
                    continue;
                }
                if (isGoal(grid))
                {
                    found = true;
                    break;
                }
                for (Grid child : outEdges(grid, matrix))
                {
                    g_child = g + child.cost;
                    if (parents[child.positionX][child.positionY] != null)
                    {
                        g_cached = child.gScore;
                        parent = parents[child.positionX][child.positionY];
                        if (g_child >= g_cached)
                        {
                            continue;
                        }
                    }
                    if (fringe.contains(child))
                    {
                        fringe.remove(child);
                    }
                    fringe.addAfter(child, grid);
                    child.gScore = g_child;
                    parents[child.positionX][child.positionY] = grid;
                }
                fringe.remove(grid);
            }
            fLimit = fmin;
        }
        if (found = true)
        {
            path(matrix[rend][cend], parents, pathFile);
        }
    }
    //
    public static double fScore(Grid currentGrid, Grid previousGrid)
    {
        return previousGrid.gScore + currentGrid.cost;
    }
    
    public static Grid[][] initialize(String gridFile, int rstart, int cstart, int rend, int cend, String method) throws FileNotFoundException
    {
        boolean isGoal = false;
        boolean isStart = false;
        //input the gridFile
        File file = new File(gridFile);
        Scanner scanner = new Scanner(file);
        //analyze the first line of the file to get the number of columns and rows of the matrix
        int rows = scanner.nextInt();
        int columns = scanner.nextInt();
        Grid[][] matrix = new Grid[rows][columns];
        // use double for loop to initialize each grid with positionX,Y,cost,value, isGoal
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < columns; j++)
            {
                if (i == rend && j == cend)
                {
                    isGoal = true;
                }
                if (i == rstart && j == cstart)
                {
                    isStart = true;
                }
                matrix[i][j] = new Grid(i, j, scanner.nextDouble(), method, isGoal, isStart);
                isGoal = false;
                isStart = false;
                if (j < columns - 1 && !scanner.hasNext())
                {
                    System.out.print ("error: grid.txt");
                }
            }
        }
        
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < columns; j++)
            {
                Grid grid = matrix[i][j];
            }
        }
        
        //return matrix.
        return matrix;
    }
    
    public static boolean isGoal(Grid grid)
    {
        return grid.isGoal;
    }
    
    public static Stack<Grid> outEdges(Grid grid, Grid[][] matrix)
    {
        //same as what i wrote in the paper sheet
        Stack<Grid> neighborGrids = new Stack<Grid>();
        if(grid.positionX + 1 <= matrix.length - 1 && !matrix[grid.positionX + 1][grid.positionY].isObstacle)
        {
            neighborGrids.push(matrix[grid.positionX + 1][grid.positionY]);
        }
        if(grid.positionX - 1 >= 0 && !matrix[grid.positionX - 1][grid.positionY].isObstacle)
        {
            neighborGrids.push(matrix[grid.positionX - 1][grid.positionY]);
        } 
        if(grid.positionY + 1 <= matrix[0].length - 1 && !matrix[grid.positionX][grid.positionY + 1].isObstacle)
        {
            neighborGrids.push(matrix[grid.positionX][grid.positionY + 1]);
        }
        if(grid.positionY - 1 >= 0 && !matrix[grid.positionX][grid.positionY -1].isObstacle)
        {
            neighborGrids.push(matrix[grid.positionX][grid.positionY - 1]);
        }
        return neighborGrids;
        // try to think in case of grid is the goal
    }
    
    public static void path (Grid grid, Grid[][] parent, String pathFile) throws IOException
    {
        //link the pathFile with the code
        PrintWriter outputFile = new PrintWriter(pathFile);
        Grid tempGrid;
        //
        Stack<Grid> grids = new Stack<Grid>();
        while(grid != parent[grid.positionX][grid.positionY])
        {
            grids.push(grid);
            grid = parent[grid.positionX][grid.positionY];
        }
        grids.push(grid);
        // print the result by pop grid from grids stack
        while (!grids.isEmpty())
        {
            tempGrid = grids.pop();
            outputFile.print(tempGrid.positionX);
            outputFile.println("      " + tempGrid.positionY);
        }
        outputFile.close();
    }
    
    public static void updateGScore(Grid currentGrid, Grid previousGrid)
    {
        currentGrid.gScore = previousGrid.gScore + currentGrid.cost;
    }
    
    public static void updateFScore(Grid currentGrid, Grid goalGrid, String nameOfMethod)
    {
        currentGrid.fScore = currentGrid.gScore + getHScore(currentGrid, goalGrid, nameOfMethod);
    }
    
    public static double getHScore(Grid currentGrid, Grid goalGrid, String nameOfMethod)
    {
        double hScore = 0.0;
        if (nameOfMethod.equals("AStarZero"))
        {
            return 0.0;
        }
        else if (nameOfMethod.equals("AStarEuclidean"))
        {
            double a = goalGrid.positionX * goalGrid.positionX - currentGrid.positionX * currentGrid.positionX;
            double b = goalGrid.positionY * goalGrid.positionY - currentGrid.positionY * currentGrid.positionY;
            hScore = Math.sqrt(a + b);
            return hScore;
        }
        else if (nameOfMethod.equals("AStarManhattan"))
        {
            return Math.abs(goalGrid.positionX - currentGrid.positionX) + Math.abs(goalGrid.positionY - currentGrid.positionY);
        }
        return -1;
    }
   
    
}
