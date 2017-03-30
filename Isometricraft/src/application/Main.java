package application;
	
import java.awt.MouseInfo;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class Main extends Application 
{
  static int width  = 800;
  static int height = 800;
  public static double scale = 10;
  int rows = 40;
  int cols = 40;
  
  double mouseX;
  double mouseY;
  
  BufferedImage bimg;
  WritableImage wimg;
  GraphicsContext writer;
  PixelReader reader;
  ArrayList<Color> colorList;
  int [][] vals;
  String mode = "Lines";
  
  NodeMap nodeMap;
  NodeMap nodeMap2;
  NodeMap nodeMap3;
  
  Random r = new Random();
  
	@Override
	public void start(Stage primaryStage) 
	{
		try 
		{
		  
		  BorderPane root = new BorderPane();
      Canvas canvas = new Canvas(width, height);
      
		  bimg = ImageIO.read(new File("resources/template.jpg"));
		  wimg = SwingFXUtils.toFXImage(bimg, null);
		  writer = canvas.getGraphicsContext2D();
		  reader = wimg.getPixelReader();
		  
		  colorList = new ArrayList<>();
  
		  int numColors = 360;
		  double offset = 0;
		  double range = Math.PI/3;
		  
		  for(int i = 0; i < numColors; i++)
		  {
		    double r = (Math.sin(i*range/numColors+offset)+1)/2;
		    double g = (Math.sin(i*range/numColors+offset+2*Math.PI/3)+1)/2;
		    double b = (Math.sin(i*range/numColors+offset+4*Math.PI/3)+1)/2;
		    Color c = new Color(r, g, b, 1);
		    colorList.add(c);
		  }
		  
		  writer.setLineWidth(2.0);
		  writer.setFill(Color.BLACK);
		  nodeMap  = new NodeMap(rows, cols, width/8,                     height/8);
		  nodeMap2 = new NodeMap(rows, cols, nodeMap.map[rows-1][0].x+10, nodeMap.map[rows-1][0].y);
		  nodeMap3 = new NodeMap(rows, cols, nodeMap.map[0][cols-1].x,    nodeMap.map[0][cols-1].y+10);
		  
		  
//		  RandomLine tree = new RandomLine(nodeMap.map[10][10].x, nodeMap.map[10][10].x, 1, -Math.PI/2, range, Color.WHITE, range);
//		  for(int i = 0; i < 16; i++)
//		  {
//		    String tempString = "";
//		    Boolean[] tempBoolArr = nodeMap.map[10][10].getIntConnect(i);
//		    for(Boolean b : tempBoolArr)
//		    {
//		      tempString += String.format("[%5b]", b);
//		    }
//		    int tempInt = nodeMap.map[10][10].getBoolConnect(tempBoolArr);
//		    System.out.printf("[%2d]->[%s]->[%2d]%n", i, tempString, tempInt);
//		  }
		  
		  
//		  vals = new int[(int) wimg.getWidth()][(int) wimg.getHeight()];
//		  
//		  for(int x = 1; x < wimg.getWidth(); x++)
//		  {
//		    for(int y = 1; y < wimg.getHeight(); y++)
//		    {
//		      vals[x][y] = r.nextInt(255);
//		      //(int) (Math.sin(Math.atan(y/x))+1)*128;
//		    }
//		  }
//		  
//		  ImageView imgv = new ImageView();
//		  imgv.setImage(wimg);
		  
		  root.getChildren().add(canvas);
			Scene scene = new Scene(root, width, height);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			MainGameLoop gameLoop = new MainGameLoop();
	    gameLoop.start();
		} 
		catch(Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	private void drawPoint(double x, double y)
	{
    writer.fillRect(x-2, y-2, 4, 4);
  }
	
	private void drawLine(Node n1, Node n2)
	{
	  writer.strokeLine(n1.x, n1.y, (n1.x+n2.x)/2, (n1.y+n2.y)/2);
	}

	private void drawLine(Node n1, double xpos, double ypos)
	{
	  writer.strokeLine(n1.x, n1.y, xpos, ypos);
	}

	private void fillCell(Node node, int index)
	{
	  
	  double [] xPoints = new double []
	      {
	          node.x,
	          node.adjacent[(index  )%6].x,
            node.adjacent[(index+1)%6].x,
            node.adjacent[(index+2)%6].x
	      };
	  
	  double [] yPoints = new double []
	      {
            node.y,
            node.adjacent[(index  )%6].y,
            node.adjacent[(index+1)%6].y,
            node.adjacent[(index+2)%6].y
        };
	  
//	  writer.setFill( colorList.get(index*89) );
	  writer.fillPolygon(xPoints, yPoints, 4);
	  
	}
	
	private double distance(double x1, double y1, double x2, double y2)
	{
	  double dx = x1-x2;
	  double dy = y1-y2;
	  return Math.sqrt(dx*dx+dy*dy);
	}
	
	private double angle(double x1, double y1, double x2, double y2)
	{
	  return Math.atan((y2-y1)/(x2-x1));
	}
	
	private Boolean[] getDrawingNodeConnections(Node node) 
  {
    int temp = node.getBoolConnect(node.connection);
    int drawingNumber = nodeMap.getDrawingInt(temp);
    Boolean[] output = node.getIntConnect(drawingNumber);// {true,true,true,true,true,true};
    return output;
  }
  
  void drawDebugLine(Node node, Node adjacentNode)
  {
    int boolConnect = node.getBoolConnect(node.connection);
    switch(boolConnect)
    {
      case 27: case 45: case 54:
        writer.setStroke(Color.ORANGE);
        writer.setFill(Color.ORANGE);
        drawPoint(node.x, node.y);
        break;
      case 21: case 42:
        writer.setStroke(Color.RED);
        break;
      case 23: case 29: case 43: case 46: case 53: case 58:
        writer.setStroke(Color.YELLOW);
        break;
      case 31: case 47: case 55: case 59: case 61: case 62:
        writer.setStroke(Color.GREEN);
        break;
      case 63:
        writer.setStroke(Color.BLUE);
        break;
      default:
        writer.setStroke(Color.WHITE);
        break;
    }
    drawLine(node, adjacentNode);
  }
  
	private void drawConnectionCounts(NodeMap nodeMap)
	{
	  for( Node[] drawMap : nodeMap.map)
    {  
      for( Node node: drawMap )
      {
        String output = "";
        for(Boolean b : node.connection)
        {
          output += String.format(" %b", b);
        }
        
        int boolConnect = node.getBoolConnect(node.connection);
        System.out.printf("%d (%3.3f,%3.3f) %s%n", boolConnect, node.x, node.y, output);
        writer.strokeText(""+boolConnect, node.x, node.y);
      }
    }
	}
	
	private void drawGraph(NodeMap nodeMap)
	{
	  for( Node[] drawMap : nodeMap.map)
	  {  
	    for( Node node: drawMap )
	    {
	      switch(mode)
	      {
	        case "Tiles":
	          if(!node.connection[1])
	          {
	            writer.setFill(colorList.get(0));
	            fillCell(node, 0);
	          }
	          if(!node.connection[3])
	          {
	            
	            writer.setFill(colorList.get(120));
	            fillCell(node, 2);
	          }
	          if(!node.connection[5])
	          {
	            writer.setFill(colorList.get(240));
	            fillCell(node, 4);
	          }
	          break;
	        case "Lines":
	          Boolean [] draw = getDrawingNodeConnections(node);
	          int counter = 0;
	          for ( Node adjacentNode : node.adjacent )
	          {
	            if(nodeMap.checkConnected(node, adjacentNode))
	            {
	              if((node.rotating == false) && (adjacentNode.rotating == false) && draw[counter])
	              {
	                writer.setStroke(Color.WHITE);
	                drawLine(node, adjacentNode);
	              }
	            }
	            counter++;
	          }
	          break;
	      }
	    }
	  }
  
//	  for( Node[] m : nodeMap.map)
//	  {  
//	    for( Node n: m)
//	    {
//	      drawPoint(n.x, n.y);
//	    }
//	  }
	  
	}
	
  class MainGameLoop extends AnimationTimer
  {
	  int counter = 0;
	  int numSeconds = 6;
	    
	  
	  ArrayList<Node> animationList = new ArrayList<>();
	  ArrayList<Node> removalList = new ArrayList<>();
	  

	  @Override
	  public void handle(long now)
	  {
//	    Color c = colorList.get(0);
//	    colorList.remove(0);
//	    colorList.add(c);

	    mouseX = MouseInfo.getPointerInfo().getLocation().getX();
	    mouseY = MouseInfo.getPointerInfo().getLocation().getY();
//	    System.out.printf("[%f, %f]%n", mouseX, mouseY);

	    counter++;
	    writer.setFill(Color.BLACK);
	    writer.fillRect(0, 0, width, height);
	    drawGraph(nodeMap);
	    drawGraph(nodeMap2);
	    drawGraph(nodeMap3);
//	    drawTree(nodeMap.map[10][10].tree);

//	    if(counter%(60*numSeconds)==0)
//	    {
//	      switch(mode)
//	      {
//	        case "Lines":
//	          mode = "Tiles";
//	          break;
//	        case "Tiles":
//	          mode = "Lines";
//	          break;
//	      }
//	    }
	    
	    if(counter%60==0)// && counter < numSeconds*60)
      {
        for(int numActions = 0; numActions <= 40; numActions++)
        {
          int x = r.nextInt(rows);
          int y = r.nextInt(cols);
          if(nodeMap.map[x][y].rotating == false)
          {
            nodeMap.map[x][y].rotating = nodeMap.rotateNode(x, y);
            if (nodeMap.map[x][y].rotating) animationList.add(nodeMap.map[x][y]);
            
          }
          if(nodeMap2.map[x][y].rotating == false)
          {
            nodeMap2.map[x][y].rotating = nodeMap2.rotateNode(x, y);
            if (nodeMap2.map[x][y].rotating) animationList.add(nodeMap2.map[x][y]);
          }
          if(nodeMap3.map[x][y].rotating == false)
          {
            nodeMap3.map[x][y].rotating = nodeMap3.rotateNode(x, y);
            if (nodeMap3.map[x][y].rotating) animationList.add(nodeMap3.map[x][y]);
          }
        }
      }
      
      
      double angle = 0;
      double offset = 2*Math.PI/3;
      for(Node n : animationList)
      {
        angle = n.tick(0);
        if(mode == "Lines")
        {
          if( n.rotating )
          {
            writer.setStroke(Color.WHITE);
            drawLine(n, n.x+Math.cos(angle)*scale,          n.y+Math.sin(angle)*scale);
            drawLine(n, n.x+Math.cos(angle+offset)*scale,   n.y+Math.sin(angle+offset)*scale);
            drawLine(n, n.x+Math.cos(angle+2*offset)*scale, n.y+Math.sin(angle+2*offset)*scale);
          }
          else
          {
            removalList.add(n);
          }
        }
      }
      
      for(Node n : removalList)
      {
        animationList.remove(n);
      }
      
      removalList = new ArrayList<Node>();
    }
        
      
//      Color change = colorList.get(0);
//      colorList.remove(0);
//      colorList.add(change);
//      int samplex = 1;// (int) wimg.getWidth()-1;
//      int sampley = 1;//(int) wimg.getHeight()-1;
//      Color temp = reader.getColor(samplex, sampley);
//      for(int x = 0; x < wimg.getWidth(); x++)
//      {
//        for(int y = 0; y < wimg.getHeight(); y++)
//        {
//          boolean red   = reader.getColor(x, y).getRed()   > 0.5;
//          boolean green = reader.getColor(x, y).getGreen() > 0.5;
//          boolean blue  = reader.getColor(x, y).getBlue()  > 0.5;
//          if(red&&green&&blue)
//          {
//            writer.setColor(x, y, colorList.get(vals[x][y]));
//          }
//        }
//      }
      
  }
    
	public static void main(String[] args)
	{
		launch(args);
	}
}
