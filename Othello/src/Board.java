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

//assume that the move is valid
public void add(Disk d){
	int row=d.row;
	int col=d.col;
	int color=d.color;
	state[row][col]=color;
	if (color==1){
		white.add(new Disk(row,col,1));	
		//flip the opponent
		
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
			disk=combine(disk,findHorizon(white.get(i)));
			disk=combine(disk,findVertical(white.get(i)));
			disk=combine(disk,findDiagonal(white.get(i)));
		}
	}
	else{
		for(int i=0;i<black.size();i++){
			disk=combine(disk,findHorizon(black.get(i)));
			disk=combine(disk,findVertical(black.get(i)));
			disk=combine(disk,findDiagonal(black.get(i)));
		}
	}
	return disk;
}



//given a position, find possible position to create a outflank situation
public ArrayList<Disk> findHorizon(Disk d){
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



public ArrayList<Disk> findVertical(Disk d){
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


public ArrayList<Disk> findDiagonal(Disk d){
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






public void update(Disk d){
	int row=d.row;
	int col=d.col;
	int color=d.color;
	Disk left=new Disk(-1,-1,0);
	Disk right=new Disk(-1,-1,0);
	Disk up=new Disk(-1,-1,0);
	Disk down=new Disk(-1,-1,0);
	int maxleft=-1;
	int minright=8;
	int mindown=8;
	int maxup=-1;
	if(color==1){
		for(int i=0;i<white.size();i++){
			if(white.get(i).row==row&&white.get(i).col<col){
				if(white.get(i).col>maxleft){
			    maxleft=white.get(i).col;
				left=white.get(i);
				}
			}
			else if(white.get(i).row==row&&white.get(i).col>col){
				if(white.get(i).col<minright){
					minright=white.get(i).col;
					right=white.get(i);
				}
			}
			else if(white.get(i).col==col&&white.get(i).row<row){
				if(white.get(i).row>maxup){
					maxup=white.get(i).row;
					up=white.get(i);
				}
			}
				
			else if(white.get(i).col==col&&white.get(i).row>row){
				if(white.get(i).row<mindown){
					mindown=white.get(i).row;
					down=white.get(i);
				}
			}
		}
	}
	
	else{
		for(int i=0;i<black.size();i++){
			if(black.get(i).row==row&&black.get(i).col<col){
				if(black.get(i).col>maxleft){
					maxleft=black.get(i).col;
					left=black.get(i);
				}
			}
			else if(black.get(i).row==row&&black.get(i).col>col){
				if(black.get(i).col<minright){
					minright=black.get(i).col;
					right=black.get(i);
				}
			}
			else if(black.get(i).col==col&&black.get(i).row<row){
				if(black.get(i).row>maxup){
					maxup=black.get(i).row;
					up=black.get(i);
				}
			}
				
			else if(black.get(i).col==col&&black.get(i).row>row){
				if(black.get(i).row<mindown){
					mindown=black.get(i).row;
					down=black.get(i);
				}
			}
		}
	}
	
	/*
	 int maxleft=-1;
	int minright=8;
	int mindown=8;
	int maxup=-1;
	 */
	if(maxleft!=-1&&checkAllSameHorizon(maxleft,col,row)){
	    updateHorizon(maxleft,col,row);	
	}
	
	if(minright!=8&&checkAllSameHorizon(col,minright,row)){
		updateHorizon(col,minright,row);
	}
	
	if(maxup!=-1&&checkAllSameVertical(maxup,row,col)){
	    updateVertical(maxup,row,col);	
	}
	
	if(mindown!=8&&checkAllSameVertical(row,mindown,col)){
		updateVertical(row,mindown,col);
	}
	
}




public boolean checkAllSameHorizon(int i,int j,int row){
	int n=state[row][i+1];
	for (int k=i+2;k<j;k++){
		if(state[row][k]!=n)
			return false;
	}
	return true;
}


public boolean checkAllSameVertical(int i,int j,int col){
	int n=state[i+1][col];
	for(int k=i+2;k<j;k++){
		if(state[k][col]!=n)
			return false;
	}
	return true;
}

public void updateHorizon(int i,int j,int row){
	for(int k=i+1;k<j;k++)
		state[row][k]=-state[row][k];
}

public void updateVertical(int i,int j,int col){
	for(int k=i+1;k<j;k++)
		state[k][col]=-state[k][col];
}

public void updateDiagonal(int i,int j,int row, int col){
	for (int k=1;k<row-i;k++){
		state[i+k][j+k]=-state[i+k][j+k];
	}
}
//////////
//unfinished
//////////
public boolean checkAllSameDiagonal(int i,int j,int row,int col){
	return false;
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
