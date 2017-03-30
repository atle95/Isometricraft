package application;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


class NodeMap
{
  Node[][] map;
  double angle = Math.PI/6;
  Random r = new Random();
    
  Boolean[] cubeTall = new Boolean[] {false, true,  false, true,  false, true };
  Boolean[] cubeDeep = new Boolean[] {true,  false, true,  false, true,  false};
  Boolean[] cubeNone = new Boolean[] {false, false, false, false, false, false};
  Boolean[] cubeAll  = new Boolean[] {true,  true,  true,  true,  true,  true };
  int cubeTallInt = 0b010101;
  int cubeDeepInt = 0b101010;
  int cubeNoneInt = 0b000000;
  int cubeAllInt  = 0b111111;
    
  Node lastNode = new Node(0,0);
    
  NodeMap(int rows, int cols, double xOffset, double yOffset)
  {
    map = new Node[rows][cols];

      
    //scaled adjustments
//    double xAdjustment = perspectivex*Main.scale;
//    double yAdjustment = perspectivey*Main.scale;
    double xAdjustment = Math.sqrt(3)/2;
    double yAdjustment = 5.0;
    double nodey=0;
    double nodex=0;
    int x, y = 0;
    //Create nodes w/ (x, y) positions in mind
    for(x = 0; x < rows; x++)
    {
      yAdjustment -= 5;
      for(y = 0; y < cols; y++)
      {
        nodex = xOffset + (double) x*Main.scale;
        nodey = yOffset + (double) y*Main.scale;
        Node n = new Node( nodex, nodey+yAdjustment );
        lastNode = n;
        map[x][y] = n;
      }
    }
      
    //Connect nodes to adjacent nodes
    for(y = 1; y < cols-1; y++)
    {
      for(x = 1; x < rows-1; x++)
      {
        connectAdjacentNode(map[x][y], map[x  ][y-1], 0);
        connectAdjacentNode(map[x][y], map[x+1][y  ], 1);
        connectAdjacentNode(map[x][y], map[x+1][y+1], 2);
        connectAdjacentNode(map[x][y], map[x  ][y+1], 3);
        connectAdjacentNode(map[x][y], map[x-1][y  ], 4);
        connectAdjacentNode(map[x][y], map[x-1][y-1], 5);
      }
    }
      
    //Initialize cube field
    for(int i = 0; i < rows; i++)
    {  
      for(int j = 2+2*(i%3); j < cols; j+=3)
      {
        map[i][j-2].connection = cubeDeep.clone();
        map[i][j-1].connection = cubeTall.clone();
        map[i][j  ].connection = cubeAll.clone();
      }
    }
  }
    
  public Boolean rotateNode(int x, int y)
  {
    Boolean output = false;
    if(evaluateConnections(map[x][y].connection, cubeDeep))
    {
      map[x][y].connection = cubeTall.clone();
      for(Node an: map[x][y].adjacent)
      {
        int index = Arrays.asList(an.adjacent).indexOf(map[x][y]);
        if(index >=0) an.connection[index] = !an.connection[index];
      }
      output = true;
      map[x][y].angle += Math.PI;
    }
    
    else if(evaluateConnections(map[x][y].connection, cubeTall))
    {
      map[x][y].connection = cubeDeep.clone(); 
      for(Node an: map[x][y].adjacent)
      {
        int index = Arrays.asList(an.adjacent).indexOf(map[x][y]);
        if(index >=0) an.connection[index] = !an.connection[index];
      }
      output = true;
    }
    return output;
  }
  
  public Boolean evaluateConnections(ArrayList<Boolean> l1, ArrayList<Boolean> l2)
  {
    Boolean output = true;
    for(int i = 0; i < l1.size(); i++)
    {
      output &= (l1.get(i) == l2.get(i));
    }
    return output;
  }

  public Boolean evaluateConnections(Boolean[] l1, Boolean[] l2)
  {
    Boolean output = true;
    for(int i = 0; i < l1.length; i++)
    {
      output &= (l1[i] == l2[i]);
    }
    return output;
  }
  
  private void connectAdjacentNode(Node n1, Node n2, int index)
  {
    n1.adjacent[index]       = n2;
    n2.adjacent[(index+3)%6] = n1;
    connectNode(n1, n2, index);
  }
  
  public void connectNode(Node n1, Node n2, int index)
  {
    n1.connection[index] = true;
    n2.connection[(index+3)%6] = true;
  }
  
  public void disconnectNode(Node n1, Node n2)
  {
    n1.connection[Arrays.asList(n1.adjacent).indexOf(n2)] = false;
    n2.connection[Arrays.asList(n2.adjacent).indexOf(n1)] = false;
  }
  
  public Boolean checkConnected(Node n1, Node n2)
  {
    Boolean check1 = false;
    Boolean check2 = false;
    if(Arrays.asList(n1.adjacent).contains(n2) && Arrays.asList(n2.adjacent).contains(n1))
    {
      check1 = n1.connection[Arrays.asList(n1.adjacent).indexOf(n2)];
      check2 = n2.connection[Arrays.asList(n2.adjacent).indexOf(n1)];
    }
    
    return check1 && check2;
  }
    
  public int getDrawingInt(int input)
  {
    int output=0;
    switch(input)
    {
      //Deep cube, Tall Cube, and 6 connections
      case 42: case 21: case 63: 
        output = input; 
        break;
      //X faces
      case 27: case 45: case 54:
        output = 0; 
        break;
      //Y edges zup zdown
      case 43: case 29://
        output = 9; 
        break;
      //Y edges yup ydown
      case 23: case 58:
        output = 18;
        break;
      //Y edges xup xdown
      case 46: case 53:
        output = 36;
        break;
      //5 corners
      case 31: output = 14; break;
      case 47: output = 7;  break;
      case 55: output = 35; break;
      case 59: output = 49; break;
      case 61: output = 56; break;
      case 62: output = 28; break;
      default:
        output = 63;
        break;
        
    }
    return output;
  }
  
}