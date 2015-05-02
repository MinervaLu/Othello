import java.util.ArrayList;

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
public static void printDisk(ArrayList<Disk> d){
if (d.isEmpty()){
System.out.println("NO valid move");	
}
else{
	for (int i=0;i<d.size();i++){
		System.out.println(d.get(i));
	}
}
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
			while(number<7&&state[row][number]==-color){
				number++;
			}
			if (number<=7&&state[row][number]==0){
				disk.add(new Disk(row,number,color));
			}
		}
	}
	//in the last column
	else if (col==7){
		if(state[row][6]==-color){
			int number=1;
			while(col-number>0&&state[row][col-number]==-color){
				number++;
			}
			if(col-number>=0&&state[row][col-number]==0)
				disk.add(new Disk(row,col-number,color));
		}
	}

	
	else{
		if(state[row][col-1]==-color){
			int number=1;
			while(col-number>0&&state[row][col-number]==-color){
				number++;
			}
			if(col-number>=0&&state[row][col-number]==0)
				disk.add(new Disk(row,col-number,color));
			
		}
		
		
		if(state[row][col+1]==-color){
			int number=1;
			while(col+number<7&&state[row][col+number]==-color){
				number++;
			}
			if (number<=7&&state[row][col+number]==0){
				disk.add(new Disk(row,col+number,color));
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
			if (number<=state.length-1&&state[number][col]==0){
				disk.add(new Disk(number,col,color));
			}
		}
	}
	//in the last row
	else if (row==7){
		if(state[row-1][col]==-color){
			int number=1;
			while(row-number>0&&state[row-number][col]==-color){
				number++;
			}
			if(row-number>=0&&state[row-number][col]==0)
				disk.add(new Disk(row-number,col,color));
		}
	}
	
	else{
		if(state[row-1][col]==-color){
			int number=row-1;
			while(number>0&&state[number][col]==-color){
				number--;
			}
			if(number>=0&&state[number][col]==0)
				disk.add(new Disk(number,col,color));
			
		}
		
		
		if(state[row+1][col]==-color){
			int number=row+1;
			while(number<state.length&&state[number][col]==-color){
				number++;
			}
			if (number<=state.length-1&&state[number][col]==0){
				disk.add(new Disk(number,col,color));
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
	
	if(row==0&&col==0){
		if(state[1][1]==-color){
			int number=1;
			while(row+number<7&&state[number][number]==-color){
				number++;
			}
			if(state[number][number]==0){
				disk.add(new Disk(number,number,color));
			}
		}
	}
	
	else if(row==7&&col==0){
		if(state[6][1]==-color){
			
			int number=1;
			while(row-number>0&&state[7-number][number]==-color){
				number++;
			}
			
			if(state[7-number][number]==0){
				disk.add(new Disk(7-number,number,color));
			}
		}
	}
	
	else if(row==0&&col==7){
		if(state[1][6]==-color){
			int number=1;
			while(col-number>0&&state[number][7-number]==-color){
				number++;
			}
			if(state[number][7-number]==0){
				disk.add(new Disk(number,7-number,color));
			}
		}
	}
	//
	else if(row==7&&col==7){
		if(state[6][6]==-color){
			int number=1;
			while(row-number>0&&state[7-number][7-number]==-color){
				number++;
				
			}
			if(state[7-number][7-number]==0){
				disk.add(new Disk(7-number,7-number,color));
			}
		}
	}
//the first row other than corner
	else if (row==0){
		if(state[1][col+1]==-color){
			int number=1;
			while(col+number<7&&state[number][col+number]==-color){
				number++;
			}
			if (number<=7&&state[row+number][col+number]==0){
				disk.add(new Disk(row+number,col+number,color));
			}
		}
		if(state[1][col-1]==-color){
			int number=1;
			while(col-number>0&&state[row+number][col-number]==-color){
				number++;
			}
			if(col-number>=0&&state[row+number][col-number]==0){
				disk.add(new Disk(row+number,col-number,color));
			}
			
		}
	}

//the last row
	else if (row==7){
		if(state[row-1][col-1]==-color){
			int number=1;
			while(col-number>0&&state[row-number][col-number]==-color){
				number++;
			}
			if(col-number>=0&&state[row-number][col-number]==0){
				disk.add(new Disk(row-number,col-number,color));
			}
			
		}
		if(state[row-1][col+1]==-color){
			int number=1;
			while(col+number<7&&state[row-number][col+number]==-color){
				number++;
			}
			if(col+number<=7&&state[row-number][col+number]==0){
				disk.add(new Disk(row-number,col+number,color));
			}
		}
	}
	
	
//the first column
	else if(col==0){
		if(state[row-1][1]==-color){
			int number=1;
			while(row-number>0&&state[row-number][number]==-color){
				number++;
			}
			if(row-number>=0&&state[row-number][number]==0){
				disk.add(new Disk(row-number,number,color));
			}
		}
		
		if(state[row+1][1]==-color){
			int number=1;
			while(row+number<7&&state[row+number][number]==-color){
				number++;
			}
			if(row+number<=7&&state[row+number][number]==0){
				disk.add(new Disk(row+number,number,color));
			}
		}
	}
	
//in the last col
	else if(col==7){
		if(state[row-1][6]==-color){
			int number=1;
			while(row-number>0&&state[row-number][7-number]==-color){
				number++;
			}
			if(row-number>=0&&state[row-number][7-number]==0){
				disk.add(new Disk(row-number,7-number,color));
			}
		}
		
		if(state[row+1][6]==-color){
			int number=1;
			while(row+number<7&&state[row+number][7-number]==-color){
				number++;
			}
			if(row+number<=7&&state[row+number][7-number]==0){
				disk.add(new Disk(row+number,7-number,color));
			}
		}
	}
	
	
	
//in the middle	
	else{
		if(state[row-1][col-1]==-color){
			int number=1;
			while(row-number>0&&col-number>0&&state[row-number][col-number]==-color){
				number++;
			}
			if(row-number>=0&&col-number>=0&&state[row-number][col-number]==0){
				disk.add(new Disk(row-number,col-number,color));
			}
		}
		
		if(state[row+1][col+1]==-color){
			int number=1;
			while(row+number<7&&col+number<7&&state[row+number][col+number]==-color){
				number++;
			}
			if(row+number<=7&&col+number<=7&&state[row+number][col+number]==0){
				disk.add(new Disk(row+number,col+number,color));
			}
		}
		
		if(state[row-1][col+1]==-color){
			int number=1;
			while(row-number>0&&col+number<7&&state[row-number][col+number]==-color){
				number++;
			}
			if(row-number>=0&&col-number<=7&&state[row-number][col+number]==0){
				disk.add(new Disk(row-number,col+number,color));
			}
		}
		
		if(state[row+1][col-1]==-color){
			int number=1;
			while(row+number<7&&col-number>0&&state[row+number][col-number]==-color){
				number++;
			}
			if(row+number<=7&&col+number>=0&&state[row+number][col-number]==0){
				disk.add(new Disk(row+number,col-number,color));
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
	Disk upperleft=new Disk(-1,-1,0);
	Disk lowerright=new Disk(-1,-1,0);
	Disk upperright=new Disk(-1,-1,0);
	Disk lowerleft=new Disk(-1,-1,0);
	int maxleft=-1;
	int minright=8;
	int mindown=8;
	int maxup=-1;
	int maxupperleft=-1;
	int minlowerright=8;
	int minupperright=8;
	int maxlowerleft=-1;
	if(color==1){
		for(int i=0;i<white.size();i++){
			//update horizontally
			//to the left of the disk
			if(white.get(i).row==row&&white.get(i).col<col){
				if(white.get(i).col>maxleft){
			    maxleft=white.get(i).col;
				left=white.get(i);
				}
			}
			//to the right of the disk
			else if(white.get(i).row==row&&white.get(i).col>col){
				if(white.get(i).col<minright){
					minright=white.get(i).col;
					right=white.get(i);
				}
			}
			//update vertically
			//to the up of the disk
			else if(white.get(i).col==col&&white.get(i).row<row){
				if(white.get(i).row>maxup){
					maxup=white.get(i).row;
					up=white.get(i);
				}
			}
			//to the down of the disk	
			else if(white.get(i).col==col&&white.get(i).row>row){
				if(white.get(i).row<mindown){
					mindown=white.get(i).row;
					down=white.get(i);
				}
			}
			//update diagonatically

			//to the upper left of the disk
			else if(white.get(i).col-col==white.get(i).row-row&&white.get(i).col-col<0){
				if(white.get(i).col>maxupperleft){
					maxupperleft=white.get(i).col;
					upperleft=white.get(i);
				}
			}
			//to the upper right
			else if(white.get(i).col-col==-(white.get(i).row-row)&&white.get(i).col-col>0){
				if(white.get(i).col<minupperright){
					minupperright=white.get(i).col;
					upperright=white.get(i);
				}
			}
			//to the lower left
			else if(white.get(i).col-col==-(white.get(i).row-row)&&white.get(i).col-col<0){
				if(white.get(i).col>maxlowerleft){
					maxlowerleft=white.get(i).col;
					lowerleft=white.get(i);
				}
			}
			//to the lower right
			else if(white.get(i).col-col==white.get(i).row-row&&white.get(i).col-col>0){
				if(white.get(i).col<minlowerright){
					minlowerright=white.get(i).col;
					lowerright=white.get(i);
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
			
			//to the upper left of the disk
			else if(black.get(i).col-col==black.get(i).row-row&&black.get(i).col-col<0){
				if(black.get(i).col>maxupperleft){
					maxupperleft=black.get(i).col;
					upperleft=black.get(i);
				}
			}
			//to the upper right
			else if(black.get(i).col-col==-(black.get(i).row-row)&&black.get(i).col-col>0){
				if(black.get(i).col<minupperright){
					minupperright=black.get(i).col;
					upperright=black.get(i);
				}
			}
			//to the lower left
			else if(black.get(i).col-col==-(black.get(i).row-row)&&black.get(i).col-col<0){
				if(black.get(i).col>maxlowerleft){
					maxlowerleft=black.get(i).col;
					lowerleft=black.get(i);
				}
			}
			//to the lower right
			else if(black.get(i).col-col==black.get(i).row-row&&black.get(i).col-col>0){
				if(black.get(i).col<minlowerright){
					minlowerright=black.get(i).col;
					lowerright=black.get(i);
				}
			}
		}
	}
	
	/*
	int maxleft=-1;
	int minright=8;
	int mindown=8;
	int maxup=-1;
	int maxupperleft=-1;
	int minlowerright=8;
	int minupperright=8;
	int maxlowerleft=-1;
	 */
	if(minupperright!=8&&checkAllSameDiagonal(row,col,upperright.row,upperright.col)){
		//System.out.println("Upperright");
		updateDiagonal(row,col,upperright.row,upperright.col);
	}
	
	if(maxlowerleft!=-1&&checkAllSameDiagonal(lowerleft.row,lowerleft.col,row,col)){
		//System.out.println("Lowerleft");
		updateDiagonal(lowerleft.row,lowerleft.col,row,col);
	}
	
	
	if(maxupperleft!=-1&&checkAllSameDiagonal(upperleft.row,upperleft.col,row,col)){
		//System.out.printf("(%d,%d),(%d,%d)",upperleft.row,upperleft.col,row,col);
		//System.out.println("Upperleft");
		updateDiagonal(upperleft.row,upperleft.col,row,col);
	}
	
	if(minlowerright!=8&&checkAllSameDiagonal(row,col,lowerright.row,lowerright.col)){
		//System.out.printf("(%d,%d),(%d,%d)",row,col,lowerright.row,lowerright.col);
		//System.out.println("lowerright");
		updateDiagonal(row,col,lowerright.row,lowerright.col);
	}

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

	black.clear();
	white.clear();
	for(int i=0;i<8;i++){
		for (int j=0;j<8;j++){
			if (state[i][j]==1)
				white.add(new Disk(i,j,1));
			
			else if(state[i][j]==-1)
				black.add(new Disk(i,j,-1));
		}
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

public boolean checkAllSameDiagonal(int i,int j,int row,int col){
	if(i-row==j-col){
	int n=state[i+1][j+1];
	for(int k=1;k<row-i;k++){
		if (state[i+k][j+k]!=n)
			return false;
	}
	return true;
	}
	else{
		int n=state[i-1][j+1];
		for(int k=1;k<row-i;k++){
			if (state[i-k][j+k]!=n)
				return false;
		}
		return true;	
	}
}

public void updateHorizon(int i,int j,int row){
	for(int k=i+1;k<j;k++)
		state[row][k]=-state[row][k];
	white.clear();
	black.clear();
	for(int m=0; m<8;m++){
		for (int n=0;n<8;n++){
			if (state[m][n]==1)
				white.add(new Disk(m,n,1));
			else if (state[m][n]==-1)
				black.add(new Disk(m,n,-1));
		}
	}
}

public void updateVertical(int i,int j,int col){
	for(int k=i+1;k<j;k++)
		state[k][col]=-state[k][col];
	white.clear();
	black.clear();
	for(int m=0; m<8;m++){
		for (int n=0;n<8;n++){
			if (state[m][n]==1)
				white.add(new Disk(m,n,1));
			else if (state[m][n]==-1)
				black.add(new Disk(m,n,-1));
		}
	}
}


/////////

public static void printBoard(int[][] s){
	for(int i=0;i<s.length;i++){
		for(int j=0;j<s[i].length;j++){
			 System.out.printf("%5d",s[i][j]);
		}
		System.out.println();
	}
}

/////////
public void updateDiagonal(int i,int j,int row, int col){
	if(i-row==j-col){
	//System.out.println("Check111111111");
	//printBoard(state);
	
	//printDisk(white);
	//System.out.printf("(%d,%d)\n",i,j);
	//System.out.printf("(%d,%d)\n",row,col);
	for (int k=1;k<row-i;k++){
		state[i+k][j+k]=-state[i+k][j+k];
		//System.out.println("updated");
	}
	white.clear();
	black.clear();
	for(int m=0; m<8;m++){
		for (int n=0;n<8;n++){
			if (state[m][n]==1)
				white.add(new Disk(m,n,1));
			else if (state[m][n]==-1)
				black.add(new Disk(m,n,-1));
		}
	}
	}
	else{
		//System.out.println("Check");
		//
		//state[i-1][j+1]=-state[i-1][j+1];
		for (int k=1;k<Math.abs(row-i);k++){
			state[i-k][j+k]=-state[i-k][j+k];
		}
		white.clear();
		black.clear();
		for(int m=0; m<8;m++){
			for (int n=0;n<8;n++){
				if (state[m][n]==1)
					white.add(new Disk(m,n,1));
				else if (state[m][n]==-1)
					black.add(new Disk(m,n,-1));
			}
		}	
	}
}






public static ArrayList<Disk> combine(ArrayList<Disk> d1, ArrayList<Disk> d2){
	if(d2.isEmpty()){
		return d1;
	}
	else{
		for(int i=0;i<d2.size();i++)
			//if(!d1.contains(d2.get(i)))
			if(!have(d1,d2.get(i)))
			d1.add(d2.get(i));
	}
	return d1;
}

public static boolean have(ArrayList<Disk> list,Disk d){
	for (int i=0;i<list.size();i++){
		if(list.get(i).row==d.row&&list.get(i).col==d.col&&list.get(i).color==d.color)
			return true;
	}
	return false;
}



}
