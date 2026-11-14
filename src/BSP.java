
public class BSP {

	private void splitNode(Cell node, int cutNum) {

		//when the maze has been split enough times this checker will take the program out of the recursive loop
		if (cutNum == iNumMaxCuts) 
		{
			return;
		}

		//initialize variables to decide whether the cell will split horizontally or vertically
		boolean bSplitHorizontal = false;
		int splitPoint = 0;



		//if the rectangle has a bigger width it will cut from the width and vice versa with the height
		if (node.GetWidth() > node.GetHeight()) 
		{
			bSplitHorizontal = false;
			splitPoint = node.GetWidth()/2;
		} 
		else if (node.GetWidth() < node.GetHeight()) 
		{
			bSplitHorizontal = true; 
			splitPoint = node.GetHeight()/2;
		}


		if(bSplitHorizontal == true) 
		{
			//the left child is split by the top or left dependant on whether it is a horizontal or vertical split 
			//and the right child is the right or bottom cell
			node.leftChild = new Cell(node.GetX(), node.GetY(), node.GetWidth(), splitPoint, false);
			node.rightChild = new Cell(node.GetX(), node.GetY() + splitPoint, node.GetWidth(), node.GetHeight() - splitPoint, false);
		} 
		else if(bSplitHorizontal == false)
		{
			node.leftChild = new Cell(node.GetX(), node.GetY(), splitPoint, node.GetHeight() , false);
			node.rightChild = new Cell(node.GetX() + splitPoint, node.GetY(), node.GetWidth() - splitPoint , node.GetHeight() , false);
		}
		else
		{
			System.out.println("Error");
		}

		//added to make the code recursive and will keep splitting until cutNum reaches the same as iNumMaxCuts this 
		//ensure that the code will not keep splitting until each tile is a cell because then rooms will overlap
		//and the code will break
		splitNode(node.leftChild, cutNum + 1);
		splitNode(node.rightChild, cutNum + 1);



	}

	//add leaves is another recursive function that adds each individual leaf to an array list called leaves
	private void addLeaves(Cell node)
	{
		//when the BSP recursive algorithm has finished because there is nothing in them they have a value of null
		//which means when a leftchild or rightchild is null that is one leaf
		if(node.leftChild == null && node.rightChild == null)
		{
			leaves.add(node);
			return;
		}
		
		//recursively go down each path of the tree diagram until every leaf has been added to the leaves
		//array list
		addLeaves(node.leftChild);
		addLeaves(node.rightChild);

	}


	private char[][] createRooms()
	{

		for(int i = 0 ; i < iLeavesNum ; i++)
		{

			int iCreateRoomChance = rand.nextInt(2);


			//for my program i do not want a large percent chance of a lot of rooms, however i have implemented a safety to guarantee 
			//at least some rooms will generate
			if(iCreateRoomChance == 0)
			{


				int iRandWidth = rand.nextInt(13) + 5;
				int iRandHeight = rand.nextInt(9) + 3;

				//get whatever leaf we have out of 32
				Cell Leaf = leaves.get(i);

				//get the top x and y coordinates of the leaf
				int x = Leaf.GetX();
				int y = Leaf.GetY();

				//set it so we know a room has been created in that leaf
				Leaf.hasRoom = true;

				//create the offsets for the rooms so they are properly randomised and not alligned to the grid
				int iOffputx = rand.nextInt(3);
				int iOffputy = rand.nextInt(3);

				//put the middle of every room into an array to store them so they can be used when generating corridors.
				iMidRoomCoords[iCheck][0] = (x + iOffputx) + (iRandWidth/2);
				iMidRoomCoords[iCheck][1] = (y + iOffputy) + (iRandHeight/2);
				
				//if its been decided then a room will be made in that leaf. This method just changes the hashtags in the parameter
				//to spaces
				makeRoom(iRandWidth, iRandHeight , x + iOffputx, y + iOffputy);

				iCheck++;

			}


		}

		//if not enough rooms have been made or too many have been made the maze will reinstantiate itself
		if(iCheck <= 8)
		{
			instantiateMaze();
		}

		return(cMaze);
	}

	//creates room by taking in its height and the offset values it has recieved (x,y)
	private char[][] makeRoom(int Width, int Height, int x , int y)
	{

		for(int i = 0 ; i < Height ; i++)
		{
			for(int j = 0 ; j < Width ; j++)
			{
				cMaze[y + i][x + j] = ' ';
			}
		}



		return(cMaze);
	}
	
	//find corridors gathers the relevant information needed to carve corridors into the dungeon such as width and length of the corridors
	//and the start and end grid positions
	private void findCorridors() 
	{
		int iR1x, iR1y, iR2x, iR2y, x, y;
		int iWidth, iLength;


		for(int i = 0 ; i < iCheck - 1 ; i++)
		{

			//grab room 1's x and y middle of room coordinates
			iR1x = iMidRoomCoords[i][0];
			iR1y = iMidRoomCoords[i][1];

			//grab room 2's mid coords
			iR2x = iMidRoomCoords[i+1][0];
			iR2y = iMidRoomCoords[i+1][1];


			//calculates distance between two rooms in width then height
			if(iR1x <= iR2x)
			{
				x = iR1x;
			}
			else
			{
				x = iR2x;   	
			}

			iWidth = Math.abs(iR2x - iR1x);


			if(iR1y <= iR2y)
			{
				y = iR1y;
			}
			else
			{
				y = iR2y;
			}

			iLength = Math.abs(iR2y - iR1y);


			//carves full L shaped corridor by carving the horizontal distance first and then the vertical distance
			carveHCorridor(iWidth, x, iR1y);

			carveVCorridor(iLength, iR2x, y);


		}
		
		for(int i = 0 ; i < 4 ; i++)
		{
			iR1x = iMidRoomCoords[i][0];
			iR1y = iMidRoomCoords[i][1];

			//grab room 2's mid coords
			iR2x = iMidRoomCoords[i+2][0];
			iR2y = iMidRoomCoords[i+2][1];

			//calculates distance bewteen two rooms in width then height
			if(iR1x <= iR2x)
			{
				x = iR1x;
			}
			else
			{
				x = iR2x;   	
			}

			iWidth = Math.abs(iR2x - iR1x);


			if(iR1y <= iR2y)
			{
				y = iR1y;
			}
			else
			{
				y = iR2y;
			}

			iLength = Math.abs(iR2y - iR1y);


			//carves full L shaped corridor
			carveHCorridor(iWidth, x, iR1y);

			carveVCorridor(iLength, iR2x, y);
		}

	}

	//carves horizontal corridor
	private char[][] carveHCorridor(int width, int x, int y)
	{
		for(int i = 0 ; i <= width ; i++)
		{

			cMaze[y][x + i] = ' ';

		}
		return(cMaze);
	}

	//carves the vertical length corridor
	//the start of the x coordinate is the end of where the horizontal corridor has ended
	private char[][] carveVCorridor(int length, int x, int y)
	{

		for(int i = 0 ; i <= length ; i++)
		{

			cMaze[y + i][x] = ' ';

		}
		return(cMaze);
	}
	
	
	
	
	public static void main(String[] args) {
		

	}

}
