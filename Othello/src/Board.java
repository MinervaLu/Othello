//testing whether can contribute or not
public class Board {
int[][] state;
public Board(){
	//initialize the board
	state=new int[8][8];
	for(int i=0;i<state.length;i++){
		for (int j=0;j<state[i].length;j++)
			state[i][j]=-1;//empty
	}
	state[3][3]=1;//white
	state[3][4]=0;//black
	state[4][3]=1;//white
	state[4][4]=0;
}
}
