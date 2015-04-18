import java.util.*;

//testing whether can contribute or not
public class Board {
int[][] state;
ArrayList<Disk> white;
ArrayList<Disk> black;
public Board(){
	//initialize the board
	state=new int[8][8];
	white=new ArrayList<Disk>();
	black=new ArrayList<Disk>();
	for(int i=0;i<state.length;i++){
		for (int j=0;j<state[i].length;j++)
			state[i][j]=0;//empty
	}
	state[3][3]=1;//white
	state[3][4]=-1;//black
	state[4][3]=-1;//white
	state[4][4]=1;//black
	white.add(new Disk(3,3,1));
	white.add(new Disk(4,4,1));
	black.add(new Disk(3,4,-1));
	black.add(new Disk(4,3,-1));
}

public void add(Disk d){
	int row=d.row;
	int col=d.col;
	int color=d.color;
	state[row][col]=color;
	if (color==1){
		white.add(new Disk(row,col,1));	
	}
	else{
		black.add(new Disk(row,col,-1));
	}
}



//
public ArrayList<Disk> validMove(int color){

	ArrayList<Disk> disk=new ArrayList<Disk>();
	if (color==1){
		for(int i=0;i<white.size();i++){
			disk=combine(disk,checkHorizon(white.get(i)));
			disk=combine(disk,checkVertical(white.get(i)));
			disk=combine(disk,checkDiagonal(white.get(i)));
		}
	}
	else{
		for(int i=0;i<black.size();i++){
			disk=combine(disk,checkHorizon(black.get(i)));
			disk=combine(disk,checkVertical(black.get(i)));
			disk=combine(disk,checkDiagonal(black.get(i)));
		}
	}
	return disk;
}



//given a position, find possible position to create a outflank situation
public ArrayList<Disk> checkHorizon(Disk d){
	int row=d.row;
	int col=d.col;
	int color=d.color;
	ArrayList<Disk> disk=new ArrayList<Disk>();
	//in the first column
	if (col==0){
		if(state[row][1]==-color){
			int number=1;
			while(number<state[row].length&&state[row][number]==-color){
				number++;
			}
			if (number<=state[row].length-1){
				disk.add(new Disk(row,number,color));
			}
		}
	}
	//in the last column
	else if (col==state[row].length-1){
		if(state[row][col-1]==-color){
			int number=col-1;
			while(number>0&&state[row][number]==-color){
				number--;
			}
			if(number>=0)
				disk.add(new Disk(row,number,color));
			else 
				;
		}
	}
	
	else{
		if(state[row][col-1]==-color){
			int number=col-1;
			while(number>0&&state[row][number]==-color){
				number--;
			}
			if(number>=0)
				disk.add(new Disk(row,number,color));
			
		}
		
		
		if(state[row][col+1]==-color){
			int number=col+1;
			while(number<state[row].length&&state[row][number]==-color){
				number++;
			}
			if (number<=state[row].length-1){
				disk.add(new Disk(row,number,color));
			}	
		}
	}
	
	return disk;
}
//check the up/down/right/left positions



public ArrayList<Disk> checkVertical(Disk d){
	int row=d.row;
	int col=d.col;
	int color=d.color;
	ArrayList<Disk> disk=new ArrayList<Disk>();
	//in the first row
	if (row==0){
		if(state[1][col]==-color){
			int number=1;
			while(number<state.length&&state[number][col]==-color){
				number++;
			}
			if (number<=state.length-1){
				disk.add(new Disk(number,col,color));
			}
		}
	}
	//in the last row
	else if (row==state.length-1){
		if(state[row-1][col]==-color){
			int number=row-1;
			while(number>0&&state[number][col]==-color){
				number--;
			}
			if(number>=0)
				disk.add(new Disk(number,col,color));
		}
	}
	
	else{
		if(state[row-1][col]==-color){
			int number=row-1;
			while(number>0&&state[number][col]==-color){
				number--;
			}
			if(number>=0)
				disk.add(new Disk(number,col,color));
			
		}
		
		
		if(state[row+1][col]==-color){
			int number=row+1;
			while(number<state.length&&state[number][col]==-color){
				number++;
			}
			if (number<=state.length-1){
				disk.add(new Disk(number,row,color));
			}	
		}
	}
	
	return disk;
}


public ArrayList<Disk> checkDiagonal(Disk d){
	int row=d.row;
	int col=d.col;
	int color=d.color;
	ArrayList<Disk> disk=new ArrayList<Disk>();
	if (row==0&&col==0){
		if(state[1][1]==-color){
			int number=1;
			while(number<state.length&&state[number][number]==-color){
				number++;
			}
			if (number<=state.length-1){
				disk.add(new Disk(number,number,color));
			}
		}
	}
	//in the last row
	else if (row==state.length-1&&col==state.length-1){
		if(state[row-1][col-1]==-color){
			int number=row-1;
			while(number>0&&state[number][number]==-color){
				number--;
			}
			if(number>=0)
				disk.add(new Disk(number,number,color));
		}
	}
	
	else{
		if(state[row-1][col-1]==-color){
			int rownumber=row-1;
			int colnumber=col-1;
			while(rownumber>0&&colnumber>0&&state[rownumber][colnumber]==-color){
				rownumber--;
				colnumber--;
			}
			if(rownumber>=0&&colnumber>0)
				disk.add(new Disk(rownumber,colnumber,color));
			
		}
		
		
		if(state[row+1][col+1]==-color){
			int rownumber=row+1;
			int colnumber=col+1;
			while(rownumber<state.length&&colnumber<state.length&&state[rownumber][colnumber]==-color){
				rownumber++;
				colnumber++;
			}
			if (rownumber<=state.length-1&&colnumber<=state.length-1){
				disk.add(new Disk(rownumber,colnumber,color));
			}	
		}
	}
	
	
	
	return disk;
}






public static ArrayList<Disk> combine(ArrayList<Disk> d1, ArrayList<Disk> d2){
	if(d2.isEmpty()){
		return d1;
	}
	else{
		for(int i=0;i<d2.size();i++)
			if(!d1.contains(d2.get(i)))
			d1.add(d2.get(i));
	}
	return d1;
}




}
