import java.util.*;


public class Test {
public static void main (String[] args){
	Board b=new Board();
	//printBoard(b.state);
	//printDisk(b.validMove(-1));
/*
	//b.add(new Disk(0,0,1));
	b.add(new Disk(0,0,1));
	b.add(new Disk(1,1,-1));
	b.add(new Disk(2,2,-1));
	b.add(new Disk(3,3,1));
	//b.add(new Disk(2,3,-1));
	//b.add(new Disk(2,2,-1));
	//b.add(new Disk(5,5,1));
	//b.add(new Disk(6,6,1));
	//b.add(new Disk(7,7,1));
	//b.add(new Disk(2,3,-1));
	printBoard(b.state);
    b.update(new Disk(0,0,1));
	//b.updateDiagonal(0,0,4,4);
	System.out.println();
	System.out.println();
	System.out.println();
	printBoard(b.state);
	//printDisk(b.validMove(1));
	
*/
	
	Scanner scan=new Scanner(System.in);
	printBoard(b.state);
	int c=-1;
	while(true){
		System.out.println("Next valid moves to choose from: ");
		//printBoard(b.state);
		printDisk(b.validMove(c));
		String s=scan.next();
		String[] para=s.split(",");
		Disk d=new Disk(Integer.parseInt(para[0]),Integer.parseInt(para[1]),Integer.parseInt(para[2]));
		if (d.color==c&&have(b.validMove(c),d)){
			b.add(d);
			b.update(d);
			printBoard(b.state);
		}
		else{
			System.out.println("Invalid Move");
		}
		c=-c;
	}
	
	
}

public static void printBoard(int[][] s){
	for(int i=0;i<s.length;i++){
		for(int j=0;j<s[i].length;j++){
			 System.out.printf("%5d",s[i][j]);
		}
		System.out.println();
	}
}





//


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

public static boolean have(ArrayList<Disk> list,Disk d){
	for (int i=0;i<list.size();i++){
		if(list.get(i).row==d.row&&list.get(i).col==d.col&&list.get(i).color==d.color)
			return true;
	}
	return false;
}
}
