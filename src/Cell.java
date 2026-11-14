/**
 * @ date 11/08/26
 * @author George Goodwin
 * @ aim the contructor class for the nodes and leaves that will be created
 */
public class Cell {

	private int x, y, iWidth, iHeight;
	public static int iMazeWidth;
	public static int iMazeHeight;
	public boolean hasRoom;
	Cell leftChild;
	Cell rightChild;
	
	
	
	//creating constructor for each cell
	public Cell(int x, int y, int width, int height, boolean hasRoom) 
	{
        this.x = x;
        this.y = y;
        this.iWidth = width;
        this.iHeight = height;
        this.hasRoom = hasRoom;
    }
	
	//creating all the getters for the splitnode class in maze generator and also hasroom to identify whether a leaf has a room in it or not
	public int GetX()
	{
		return(x);
	}
	
	public int GetY()
	{
		return(y);
	}
	
	public int GetWidth()
	{
		return(iWidth);
	}

	public int GetHeight()
	{
		return(iHeight);
	}
	
	public boolean hasRoom()
	{
		return(hasRoom);
	}
	
	
	
	public static void main(String[] args) {
		
		
		
		

	}

}
