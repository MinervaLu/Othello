//class for disk
public class Disk {
int color;
int row;
int col;

public Disk(int row,int col,int color){
	this.row=row;
	this.col=col;
	this.color=color;
}

public String toString(){
	return String.format("(%d, %d) color %s.\n",row,col,color==1?"white":"black");
}
}
