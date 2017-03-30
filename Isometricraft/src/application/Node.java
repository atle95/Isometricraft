package application;

import javafx.scene.paint.Color;

public class Node
{
    
  Node[]    adjacent   = new Node[6];
  Boolean[] connection = new Boolean [6];
  int connections = 0;
  double x, y;
//  RandomLine tree;
  
  boolean rotating = false;
  double numTicks = 30;
  double currentTick = 0;
  double angle = 7*Math.PI/6.0;
  
  public Node(double x, double y)
  {
    this.x = x;
    this.y = y;
    connection = new Boolean [] {false, false, false, false, false, false};
    adjacent   = new Node    [] {this,  this,  this,  this,  this,  this};
      
    for(int i = 0; i<=6; i++)
    {
      connections += 2^i;
    }
  }
  
//  public void addTree(double width, double length)
//  {
//    tree = new RandomLine(x, y, width, -Math.PI/2, 1, Color.SPRINGGREEN, length);
//    tree.grow();
//  }
  double progress = 0;
  public double tick(double offset)
  {
    currentTick++;
    progress = currentTick/numTicks;
    angle += 1.0/numTicks*Math.PI;
    //currentTick/numTicks*2*Math.PI/3+Math.PI/6
    if(currentTick == numTicks)
    {
      rotating = false;
      currentTick = 0;
      angle = 7*Math.PI/6.0;
    }
    
    return angle;
  }
  
  int getBoolConnect(Boolean[] input)
  {
    int boolConnect = 0;
    for(int i = 0; i < input.length ; i++)
    {
      if (input[i]) 
      {
        boolConnect+=Math.pow(2, i);
      }
    }
    return boolConnect;
  }
  
  Boolean[] getIntConnect(int input)
  {
    Boolean[] intConnect = new Boolean [6];
    for (int i = 5; i >= 0; i--) 
    {
      intConnect[i] = (input & (1 << i)) != 0;
    }
    return intConnect;
  }
    
}
