
import java.util.*;


public class play {
	static int interval;
	static Timer timer;
	static boolean timeUp;

public static void main(String[] args) throws InterruptedException{
	Scanner scan=new Scanner(System.in);
	String input=scan.nextLine();
	String[] para=input.split(" ");
	double max=Double.MAX_VALUE;
	double min=Double.MIN_VALUE;
	boolean gameover=false;
	boolean illegal=false;
	boolean novalid=false;
	boolean timeout=false;
	int computer=(para[1].equals("B")?-1:1);
	int depth=Integer.parseInt(para[2]);
	int time1=Integer.parseInt(para[3]);
	int time2=Integer.parseInt(para[4]);
	Board newGame=new Board();
	int count=0;
	
//don't have time limit for the entire game	
if(time2==0){
	//computer plays black	
		if(computer==-1){
			while(!gameover){
				
				boolean novalidblack=false;
				//no valid move for black 
				if(newGame.validMove(-1).isEmpty()){
					System.out.println("Pass");
					novalidblack=true;
				}
				//black has valid move
				else{
					
				    Long starttime=System.currentTimeMillis();
					Value v=AlphaBeta(newGame,0,min,max,computer,depth,time1,time2,starttime);
				     newGame.add(v.best);
				     newGame.update(v.best);
					 System.out.printf("%d %d\n",v.best.row,v.best.col);
					 //no valid move for white
					 if(newGame.validMove(1).isEmpty()){
						 if(novalidblack){
							 novalid=true;
						 }
						 else{
							 System.out.println("Pass");
						 }
					 }
					 //white has valid move
					 else{
						 String playerinput=scan.nextLine();	
						 if (playerinput.equals("quit")){
							 gameover=true;
						 }
						 else{ 
						 String[] player=playerinput.split(" ");
						 int x=Integer.parseInt(player[0]);
						 int y=Integer.parseInt(player[1]);
						 Disk playerMove=new Disk(x,y,1);
						 if (have(newGame.validMove(1),playerMove)){
							 newGame.add(playerMove);
							 newGame.update(playerMove);
						 }
						 else{
							 gameover=true;
							 illegal=true;
						 }
						 } 
					 }
				}
	//after one round
		gameover=illegal||novalid||checkOver(newGame);		
				 
				
			}
		}

		
		
	//computer plays white	
		else if(computer==1){
			while(!gameover){
				Boolean novalidblack=false;
				//no valid move for the black
				if (newGame.validMove(-1).isEmpty()==true){
					novalidblack=true;
				}
				//black has the valid move
				else{
					String[] userinput=scan.nextLine().split(" ");
					int x=Integer.parseInt(userinput[0]);
					int y=Integer.parseInt(userinput[1]);
					Disk usermove=new Disk(x,y,-1);
					//white move is valid
					if (have(newGame.validMove(-1),usermove)){
						newGame.add(usermove);
						newGame.update(usermove);
						//no valid move for the white
						if(newGame.validMove(1).isEmpty()==true){
							if(novalidblack){
								novalid=true;
							}
							else{
							    System.out.println("Pass");
							}
							
						}
						//white has valid move
						else{
							long currenttime=System.currentTimeMillis();
							 Value v=AlphaBeta(newGame,0,min,max,computer,depth,time1,time2,currenttime);
						     newGame.add(v.best);
						     newGame.update(v.best);
							 System.out.printf("%d %d\n",v.best.row,v.best.col);
						}
					}
					else{
					illegal=true;	
					}
					
					
				}
				
			}
		}
			
		
		if(illegal){
			if(computer==1){
				System.out.printf("winner %s %d %s","W",newGame.white.size()-newGame.black.size(),"illegal");
			}
			else{
				System.out.printf("winner %s %d %s","B",newGame.black.size()-newGame.white.size(),"illegal");
			}
			
		}
		else if(novalid||gameover){
			if (newGame.white.size()>newGame.black.size()){
				System.out.printf("winner %s %d %s","W",newGame.white.size()-newGame.black.size(),"legal");
			}
			else if(newGame.white.size()<newGame.black.size()){
				System.out.printf("winner %s %d %s","B",newGame.black.size()-newGame.white.size(),"legal");
			}
			else{
				System.out.printf("winner %s %d %s","T",newGame.black.size()-newGame.white.size(),"legal");
			}
		}
		
		else {
			if (newGame.white.size()>newGame.black.size()){
				System.out.printf("winner %s %d %s","W",newGame.white.size()-newGame.black.size(),"timeout");
			}
			else if(newGame.white.size()<newGame.black.size()){
				System.out.printf("winner %s %d %s","B",newGame.black.size()-newGame.white.size(),"timeout");
			}
			else{
				System.out.printf("winner %s %d %s","T",newGame.black.size()-newGame.white.size(),"timeout");
			}
		}
}





//have time limit for the whole game
else{
	long start=System.currentTimeMillis();
	//computer plays black	
	if(computer==-1){
		while(!gameover&&!timeout){
			
			boolean novalidblack=false;
			//no valid move for black 
			if(newGame.validMove(-1).isEmpty()){
				System.out.println("Pass");
				novalidblack=true;
			}
			//black has valid move
			else{
				
			    Long starttime=System.currentTimeMillis();
				Value v=AlphaBeta(newGame,0,min,max,computer,depth,time1,time2,starttime);
			     newGame.add(v.best);
			     newGame.update(v.best);
				 System.out.printf("%d %d\n",v.best.row,v.best.col);
				 //no valid move for white
				 if(newGame.validMove(1).isEmpty()){
					 if(novalidblack){
						 novalid=true;
					 }
					 else{
						 System.out.println("Pass");
					 }
				 }
				 //white has valid move
				 else{
					 String playerinput=scan.nextLine();	
					 if (playerinput.equals("quit")){
						 gameover=true;
					 }
					 else{ 
					 String[] player=playerinput.split(" ");
					 int x=Integer.parseInt(player[0]);
					 int y=Integer.parseInt(player[1]);
					 Disk playerMove=new Disk(x,y,1);
					 if (have(newGame.validMove(1),playerMove)){
						 newGame.add(playerMove);
						 newGame.update(playerMove);
					 }
					 else{
						 gameover=true;
						 illegal=true;
					 }
					 } 
				 }
			}
//after one round
	gameover=illegal||novalid||checkOver(newGame);		
	long current=System.currentTimeMillis();
	if(current-start>time2*1000){
		timeout=true;
	}
			
		}
	}

	
	
//computer plays white	
	else if(computer==1){
		while(!gameover&&!timeout){
			Boolean novalidblack=false;
			//no valid move for the black
			if (newGame.validMove(-1).isEmpty()==true){
				novalidblack=true;
			}
			//black has the valid move
			else{
				String[] userinput=scan.nextLine().split(" ");
				int x=Integer.parseInt(userinput[0]);
				int y=Integer.parseInt(userinput[1]);
				Disk usermove=new Disk(x,y,-1);
				//white move is valid
				if (have(newGame.validMove(-1),usermove)){
					newGame.add(usermove);
					newGame.update(usermove);
					//no valid move for the white
					if(newGame.validMove(1).isEmpty()==true){
						if(novalidblack){
							novalid=true;
						}
						else{
						    System.out.println("Pass");
						}
						
					}
					//white has valid move
					else{
						long currenttime=System.currentTimeMillis();
						 Value v=AlphaBeta(newGame,0,min,max,computer,depth,time1,time2,currenttime);
					     newGame.add(v.best);
					     newGame.update(v.best);
						 System.out.printf("%d %d\n",v.best.row,v.best.col);
					}
				}
				else{
				illegal=true;	
				}
				
				
			}
			long current=System.currentTimeMillis();
			if(current-start>time2*1000){
				timeout=true;
			}	
		}
	}
		
	if(timeout){
			if (newGame.white.size()>newGame.black.size()){
				System.out.printf("winner %s %d %s","W",newGame.white.size()-newGame.black.size(),"timeout");
			}
			else if(newGame.white.size()<newGame.black.size()){
				System.out.printf("winner %s %d %s","B",newGame.black.size()-newGame.white.size(),"timeout");
			}
			else{
				System.out.printf("winner %s %d %s","T",newGame.black.size()-newGame.white.size(),"timeout");
			}
	}
	else if(illegal){
		if(computer==1){
			System.out.printf("winner %s %d %s","W",newGame.white.size()-newGame.black.size(),"illegal");
		}
		else{
			System.out.printf("winner %s %d %s","B",newGame.black.size()-newGame.white.size(),"illegal");
		}
		
	}
	else {
		if (newGame.white.size()>newGame.black.size()){
			System.out.printf("winner %s %d %s","W",newGame.white.size()-newGame.black.size(),"legal");
		}
		else if(newGame.white.size()<newGame.black.size()){
			System.out.printf("winner %s %d %s","B",newGame.black.size()-newGame.white.size(),"legal");
		}
		else{
			System.out.printf("winner %s %d %s","T",newGame.black.size()-newGame.white.size(),"legal");
		}
	}
	
	
	
	
	
	
}


}



//heuristic functions
public static double heuristic(Board b,int color){

	//int color=d.color;
	double discsquare=0;
	int[][] V={{20, -3, 11, 8, 8, 11, -3, 20},{-3, -7, -4, 1, 1, -4, -7, -3},
			{11, -4, 2, 2, 2, 2, -4, 11},{8, 1, 2, -3, -3, 2, 1, 8},
			{8, 1, 2, -3, -3, 2, 1, 8},{11, -4, 2, 2, 2, 2, -4, 11},
			{-3, -7, -4, 1, 1, -4, -7, -3},{20, -3, 11, 8, 8, 11, -3, 20}};
	
	//piece difference
	int selfpiece=b.white.size();
	int oppopiece=b.white.size();
	double diff=0;
	if(selfpiece!=oppopiece)
		diff=100.0*selfpiece/(selfpiece+oppopiece);
	else
		diff=0;
		
	//cornerCaptured
	int selfcorner=0;
	int oppocorner=0;

	if(b.state[0][0]==color)
		selfcorner++;
	else if(b.state[0][0]==color)
		oppocorner++;

	if(b.state[0][7]==color)
		selfcorner++;
	else if(b.state[0][0]==color)
		oppocorner++;
	
	if(b.state[7][7]==color)
		selfcorner++;
	else if(b.state[0][0]==color)
		oppocorner++;

	if(b.state[7][0]==color)
		selfcorner++;
	else if(b.state[0][0]==color)
		oppocorner++;

	
	double cornercaptured= 25*(selfcorner-oppocorner);
	
//cornercloseness
	int self=0;
	int oppo=0;
	int[][] state=b.state;

	if(state[0][0]==0){
		if(state[0][1]==color)
			self++;
		else if(state[0][1]==-color)
			oppo++;
		if(state[1][1]==color)
			self++;
		else if(state[1][1]==-color)
			oppo++;
		if(state[1][0]==color)
			self++;
		else if(state[1][0]==-color)
			oppo++;
			}
	
	if(state[0][7]==0){
		if(state[0][6]==color)
			self++;
		else if(state[0][6]==-color)
			oppo++;
		if(state[1][6]==color)
			self++;
		else if(state[1][6]==-color)
			oppo++;
		if(state[1][7]==color)
			self++;
		else if(state[1][7]==-color)
			oppo++;
			}
	
	if(state[7][0]==0){
		if(state[7][1]==color)
			self++;
		else if(state[7][1]==-color)
			oppo++;
		if(state[6][1]==color)
			self++;
		else if(state[6][1]==-color)
			oppo++;
		if(state[6][0]==color)
			self++;
		else if(state[6][0]==-color)
			oppo++;
			}
	
	if(state[7][7]==0){
		if(state[6][6]==color)
			self++;
		else if(state[6][6]==-color)
			oppo++;
		if(state[7][6]==color)
			self++;
		else if(state[7][6]==-color)
			oppo++;
		if(state[6][7]==color)
			self++;
		else if(state[6][7]==-color)
			oppo++;
			}
	
	double cornercloseness= -12.5*(self-oppo);

	//mobility
	
	int m=0;
	int m2=b.validMove(color).size();
	m=b.validMove(-color).size();
	double mobility=100.0*m2/(m2+m);
	
	
//frontier disc
	int selffront=0;
	int oppofront=0;
	for(int i=0;i<8;i++){
		for(int j=0;j<8;j++){
			//disc square
			discsquare+=b.state[i][j]*V[i][j];
			if(b.state[i][j]==color&&isFront(b,i,j))
				selffront++;
			else if(b.state[i][j]==-color&&isFront(b,i,j))
				oppofront++;
		}
	}
double frontier=100.0*selffront/(selffront+oppofront);


return 10*diff+801.724*cornercaptured+382.026*cornercloseness+78.922*mobility+74.396*frontier+10+discsquare;
}







//find the number of empty neighbours
public static boolean isFront(Board b,int row,int col){
	int[][] state=b.state;
	int num=0;
	if(row>0&&row<7&&col>0&&col<7){
	 num= (state[row-1][col]+1)%2+(state[row+1][col]+1)%2+(state[row][col+1]+1)%2+(state[row][col-1]+1)%2;	
	}
	return num!=0;
}











//alpha beta pruning
public static Value AlphaBeta(Board board, int depth,double alpha,double beta,int player, int specifiedLimit,int timeLimit1,int timeLimit2, long starttime){
	ArrayList<Disk> valid=new ArrayList<Disk>();
	long currenttime=System.currentTimeMillis();
	Value value;
	Disk best=new Disk(1,1,1);

	
	
//depth!=0;time1=0;time2==0
	if (specifiedLimit!=0&&timeLimit1==0&&timeLimit2==0){
		
		if(depth>=specifiedLimit)
			return(new Value(heuristic(board,player),null));
	 
		if (player==-1){
			valid=board.validMove(player);
			for(Disk action:valid){
				Board bcopy=makeCopy(board);
				bcopy.add(action);
				bcopy.update(action);
				Board child=bcopy;
				value=AlphaBeta(child,depth+1,alpha,beta,-player,specifiedLimit,timeLimit1,timeLimit2,starttime);
			
				if (value.number>alpha){
					alpha=value.number;
					best=action;
			}
				if (beta<=alpha){
					break;
			}
			}
			return new Value(alpha,best);
		}
		//white player
		else {
			valid=board.validMove(player);
			for(Disk action:valid){
				Board bcopy=makeCopy(board);
				bcopy.add(action);
				bcopy.update(action);
				Board child=bcopy;
				
				value=AlphaBeta(child,depth+1,alpha,beta,-player,specifiedLimit,timeLimit1,timeLimit2,starttime);
				if (value.number<beta){
					beta=value.number;
					best=action;
				}
				if (beta<=alpha){
					break;
				}
			}
			return new Value(beta,best);
		}
	}
	
	
//depth!=0;timeLimit1!=0;timeLimit2=0	
	else if(specifiedLimit!=0&&timeLimit1!=0&&timeLimit2==0){
		
		if(currenttime-starttime>1000*timeLimit1||depth>=specifiedLimit)
			return(new Value(heuristic(board,player),null));
	 
		if (player==-1){
			valid=board.validMove(player);
			for(Disk action:valid){
				Board bcopy=makeCopy(board);
				bcopy.add(action);
				bcopy.update(action);
				Board child=bcopy;
				value=AlphaBeta(child,depth+1,alpha,beta,-player,specifiedLimit,timeLimit1,timeLimit2,starttime);
			
				if (value.number>alpha){
					alpha=value.number;
					best=action;
			}
				if (beta<=alpha){
					break;
			}
			}
			return new Value(alpha,best);
		}
		//white player
		else {
			valid=board.validMove(player);
			for(Disk action:valid){
				Board bcopy=makeCopy(board);
				bcopy.add(action);
				bcopy.update(action);
				Board child=bcopy;
				
				value=AlphaBeta(child,depth+1,alpha,beta,-player,specifiedLimit,timeLimit1,timeLimit2,starttime);
				if (value.number<beta){
					beta=value.number;
					best=action;
				}
				if (beta<=alpha){
					break;
				}
			}
			return new Value(beta,best);
		}
	}
//depth==0;timeLimit1!=0;timeLimit2==0	
	else if(specifiedLimit==0&&timeLimit1!=0&&timeLimit2==0){
		if(currenttime-starttime>1000*timeLimit1)
			return(new Value(heuristic(board,player),null));
	 
		if (player==-1){
			valid=board.validMove(player);
			for(Disk action:valid){
				Board bcopy=makeCopy(board);
				bcopy.add(action);
				bcopy.update(action);
				Board child=bcopy;
				value=AlphaBeta(child,depth+1,alpha,beta,-player,specifiedLimit,timeLimit1,timeLimit2,starttime);
			
				if (value.number>alpha){
					alpha=value.number;
					best=action;
			}
				if (beta<=alpha){
					break;
			}
			}
			return new Value(alpha,best);
		}
		//white player
		else {
			valid=board.validMove(player);
			for(Disk action:valid){
				Board bcopy=makeCopy(board);
				bcopy.add(action);
				bcopy.update(action);
				Board child=bcopy;
				
				value=AlphaBeta(child,depth+1,alpha,beta,-player,specifiedLimit,timeLimit1,timeLimit2,starttime);
				if (value.number<beta){
					beta=value.number;
					best=action;
				}
				if (beta<=alpha){
					break;
				}
			}
			return new Value(beta,best);
		}
	}
//depth==0;timeLimit1==0;timeLimit2!=0	
	else if(timeLimit2!=0){
		if(currenttime-starttime>1000*timeLimit2/2)
			return(new Value(heuristic(board,player),null));
	 
		if (player==-1){
			valid=board.validMove(player);
			for(Disk action:valid){
				Board bcopy=makeCopy(board);
				bcopy.add(action);
				bcopy.update(action);
				Board child=bcopy;
				value=AlphaBeta(child,depth+1,alpha,beta,-player,specifiedLimit,timeLimit1,timeLimit2,starttime);
			
				if (value.number>alpha){
					alpha=value.number;
					best=action;
			}
				if (beta<=alpha){
					break;
			}
			}
			return new Value(alpha,best);
		}
		//white player
		else {
			valid=board.validMove(player);
			for(Disk action:valid){
				Board bcopy=makeCopy(board);
				bcopy.add(action);
				bcopy.update(action);
				Board child=bcopy;
				
				value=AlphaBeta(child,depth+1,alpha,beta,-player,specifiedLimit,timeLimit1,timeLimit2,starttime);
				if (value.number<beta){
					beta=value.number;
					best=action;
				}
				if (beta<=alpha){
					break;
				}
			}
			return new Value(beta,best);
		}
	}

//all equal to zero
	else{
		if(depth>=6)
			return(new Value(heuristic(board,player),null));
	 
		if (player==-1){
			valid=board.validMove(player);
			for(Disk action:valid){
				Board bcopy=makeCopy(board);
				bcopy.add(action);
				bcopy.update(action);
				Board child=bcopy;
				value=AlphaBeta(child,depth+1,alpha,beta,-player,specifiedLimit,timeLimit1,timeLimit2,starttime);
			
				if (value.number>alpha){
					alpha=value.number;
					best=action;
			}
				if (beta<=alpha){
					break;
			}
			}
			return new Value(alpha,best);
		}
		//white player
		else {
			valid=board.validMove(player);
			for(Disk action:valid){
				Board bcopy=makeCopy(board);
				bcopy.add(action);
				bcopy.update(action);
				Board child=bcopy;
				
				value=AlphaBeta(child,depth+1,alpha,beta,-player,specifiedLimit,timeLimit1,timeLimit2,starttime);
				if (value.number<beta){
					beta=value.number;
					best=action;
				}
				if (beta<=alpha){
					break;
				}
			}
			return new Value(beta,best);
		}
	}
	

	
}

public static Board makeCopy(Board b){
	   int[][] s=new int[8][8];
	   Board copy=new Board();
	   for(int i=0;i<8;i++){
		   for (int j=0;j<8;j++){
			   s[i][j]=b.state[i][j];
			   if (s[i][j]==1)
				   copy.white.add(new Disk(i,j,1));
			   if(s[i][j]==-1)
				   copy.black.add(new Disk(i,j,-1));
		   }
	   }
	   copy.state=s;
	   return copy;
}

public static boolean have(ArrayList<Disk> list,Disk d){
	for (int i=0;i<list.size();i++){
		if(list.get(i).row==d.row&&list.get(i).col==d.col&&list.get(i).color==d.color)
			return true;
	}
	return false;
}



public static void printBoard(int[][] s){
	for(int i=0;i<s.length;i++){
		for(int j=0;j<s[i].length;j++){
			 System.out.printf("%5d",s[i][j]);
		}
		System.out.println();
	}
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

public static boolean checkOver(Board b){
	return b.white.size()+b.black.size()==64;
}



}
