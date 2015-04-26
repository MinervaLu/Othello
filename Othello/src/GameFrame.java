import java.awt.Color;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author macbookpro
 */

public class GameFrame extends javax.swing.JFrame {
	static Board b=new Board();
	JButton[][] jb=new JButton[8][8];
	
	int color=-1;
	boolean isvalid;
	boolean bselected=false;
	boolean wselected=false;
    /**
     * Creates new form GameFrame2
     */
	
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

/*
 * 
 * heuristic function
 * 
 * reference:https://github.com/kartikkukreja/blog-codes/blob/master/src/Heuristic%20Function%20for%20Reversi%20(Othello).cpp
 */


public static double heuristic(Board board,Disk d){
	printBoard(board.state);
	System.out.println();
Board b=new Board();
b.state=board.state;
b.white=board.white;
b.black=board.black;
	int color=d.color;
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
	b.add(d);
	b.update(d);
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
	b.add(d);
	b.update(d);
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
	m=b.validMove(-color).size();
	double mobility=m;
	
	
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





	
    public GameFrame() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {
    	jButton1 = new javax.swing.JButton();
        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        b74 = new javax.swing.JButton();
        b04 = new javax.swing.JButton();
        b14 = new javax.swing.JButton();
        b24 = new javax.swing.JButton();
        b34 = new javax.swing.JButton();
        b45 = new javax.swing.JButton();
        b55 = new javax.swing.JButton();
        b65 = new javax.swing.JButton();
        b75 = new javax.swing.JButton();
        b00 = new javax.swing.JButton();
        b05 = new javax.swing.JButton();
        b10 = new javax.swing.JButton();
        b20 = new javax.swing.JButton();
        b30 = new javax.swing.JButton();
        b40 = new javax.swing.JButton();
        b15 = new javax.swing.JButton();
        b25 = new javax.swing.JButton();
        b35 = new javax.swing.JButton();
        b46 = new javax.swing.JButton();
        b50 = new javax.swing.JButton();
        b56 = new javax.swing.JButton();
        b60 = new javax.swing.JButton();
        b66 = new javax.swing.JButton();
        b70 = new javax.swing.JButton();
        b76 = new javax.swing.JButton();
        b41 = new javax.swing.JButton();
        b06 = new javax.swing.JButton();
        b51 = new javax.swing.JButton();
        b16 = new javax.swing.JButton();
        b61 = new javax.swing.JButton();
        b26 = new javax.swing.JButton();
        b71 = new javax.swing.JButton();
        b01 = new javax.swing.JButton();
        b11 = new javax.swing.JButton();
        b21 = new javax.swing.JButton();
        b36 = new javax.swing.JButton();
        b47 = new javax.swing.JButton();
        b57 = new javax.swing.JButton();
        b67 = new javax.swing.JButton();
        b31 = new javax.swing.JButton();
        b77 = new javax.swing.JButton();
        b42 = new javax.swing.JButton();
        b07 = new javax.swing.JButton();
        b52 = new javax.swing.JButton();
        b17 = new javax.swing.JButton();
        b62 = new javax.swing.JButton();
        b27 = new javax.swing.JButton();
        b72 = new javax.swing.JButton();
        b37 = new javax.swing.JButton();
        b02 = new javax.swing.JButton();
        b12 = new javax.swing.JButton();
        b22 = new javax.swing.JButton();
        b32 = new javax.swing.JButton();
        b43 = new javax.swing.JButton();
        b53 = new javax.swing.JButton();
        b63 = new javax.swing.JButton();
        b73 = new javax.swing.JButton();
        b03 = new javax.swing.JButton();
        b13 = new javax.swing.JButton();
        b23 = new javax.swing.JButton();
        b33 = new javax.swing.JButton();
        b44 = new javax.swing.JButton();
        b54 = new javax.swing.JButton();
        b64 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        restartButton = new javax.swing.JButton();
        blackRB = new javax.swing.JRadioButton();
        whiteRB = new javax.swing.JRadioButton();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        blackScore = new javax.swing.JLabel();
        whiteScore = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        timeRemain = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        
        jb[0][0]=b00;
        jb[0][1]=b01;
        jb[0][2]=b02;
        jb[0][3]=b03;
        jb[0][4]=b04;
        jb[0][5]=b05;
        jb[0][6]=b06;
        jb[0][7]=b07;

        jb[1][0]=b10;
        jb[1][1]=b11;
        jb[1][2]=b12;
        jb[1][3]=b13;
        jb[1][4]=b14;
        jb[1][5]=b15;
        jb[1][6]=b16;
        jb[1][7]=b17;

        jb[2][0]=b20;
        jb[2][1]=b21;
        jb[2][2]=b22;
        jb[2][3]=b23;
        jb[2][4]=b24;
        jb[2][5]=b25;
        jb[2][6]=b26;
        jb[2][7]=b27;

        jb[3][0]=b30;
        jb[3][1]=b31;
        jb[3][2]=b32;
        jb[3][3]=b33;
        jb[3][4]=b34;
        jb[3][5]=b35;
        jb[3][6]=b36;
        jb[3][7]=b37;

        jb[4][0]=b40;
        jb[4][1]=b41;
        jb[4][2]=b42;
        jb[4][3]=b43;
        jb[4][4]=b44;
        jb[4][5]=b45;
        jb[4][6]=b46;
        jb[4][7]=b47;

        jb[5][0]=b50;
        jb[5][1]=b51;
        jb[5][2]=b52;
        jb[5][3]=b53;
        jb[5][4]=b54;
        jb[5][5]=b55;
        jb[5][6]=b56;
        jb[5][7]=b57;

        jb[6][0]=b60;
        jb[6][1]=b61;
        jb[6][2]=b62;
        jb[6][3]=b63;
        jb[6][4]=b64;
        jb[6][5]=b65;
        jb[6][6]=b66;
        jb[6][7]=b67;

        jb[7][0]=b70;
        jb[7][1]=b71;
        jb[7][2]=b72;
        jb[7][3]=b73;
        jb[7][4]=b74;
        jb[7][5]=b75;
        jb[7][6]=b76;
        jb[7][7]=b77;

        jButton1.setForeground(new java.awt.Color(255, 0, 0));
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        
        
        
              

                setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

             
                for(int i=0;i<8;i++){
                	for(int j=0;j<8;j++){
                		final int row=i;
                		final int col=j;
                		jb[i][j].setBackground(Color.gray);
                		jb[i][j].setLocation(new java.awt.Point(i,j));
                		jb[i][j].addMouseListener(new java.awt.event.MouseAdapter() {
                            public void mouseClicked(java.awt.event.MouseEvent evt) {
                                bMouseClicked(evt,row,col);
                            }
                        });
                	}
                }
           

                b33.setBackground(Color.white);
                b44.setBackground(Color.white);
                b34.setBackground(Color.black);
                b43.setBackground(Color.black);
                
                
                
                
                        
       

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        

        jLabel1.setText("0");

        jLabel2.setText("1");

        jLabel3.setText("2");

        jLabel4.setText("3");

        jLabel5.setText("4");

        jLabel6.setText("6");

        jLabel7.setText("5");

        jLabel8.setText("7");

        jLabel9.setText("0");

        jLabel10.setText("1");

        jLabel11.setText("2");

        jLabel12.setText("4");

        jLabel13.setText("5");

        jLabel14.setText("6");

        jLabel15.setText("  3");

        jLabel16.setText("7");

        restartButton.setText("Restart");
        restartButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                restartButtonMouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .add(jLabel14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                    .add(jPanel1Layout.createSequentialGroup()
                                        .add(11, 11, 11)
                                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                            .add(jLabel9, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                            .add(jLabel10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                            .add(jLabel11, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                            .add(jLabel12, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                            .add(jLabel13, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                                    .add(jLabel15, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED))
                            .add(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .add(jLabel16, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(b70, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(b40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(b60, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(b50, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(b30, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(b00, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(b20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(b10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(b71, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(b41, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(b61, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(b51, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(b31, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(b01, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(b21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(b11, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(b72, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(b42, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(b62, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(b52, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(b32, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(b02, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(b22, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(b12, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(b73, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(b43, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(b63, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(b53, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(b33, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(b03, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(b23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(b13, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(b74, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(b44, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(b64, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(b54, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(b34, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(b04, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(b24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(b14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(b75, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(b45, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(b65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(b55, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(b35, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(b05, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(b25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(b15, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(b76, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(b46, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(b66, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(b56, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(b36, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(b06, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(b26, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(b16, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(b77, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(b47, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(b67, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(b57, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(b37, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(b07, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(b27, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(b17, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(63, 63, 63)
                        .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(51, 51, 51)
                        .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(45, 45, 45)
                        .add(jLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(52, 52, 52)
                        .add(jLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(49, 49, 49)
                        .add(jLabel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(53, 53, 53)
                        .add(jLabel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(46, 46, 46)
                        .add(jLabel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(46, 46, 46)
                        .add(jLabel8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .add(47, 47, 47))
            .add(jPanel1Layout.createSequentialGroup()
                .add(270, 270, 270)
                .add(restartButton)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(restartButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(jLabel1)
                        .add(jLabel2)
                        .add(jLabel3)
                        .add(jLabel4)
                        .add(jLabel5))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(jLabel6)
                        .add(jLabel8))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel7))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(b07, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(b17, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(b27, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(b37, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(b47, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(b57, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(b67, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(b77, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                        .add(jPanel1Layout.createSequentialGroup()
                            .add(b06, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(b16, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(b26, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(b36, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(b46, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(b56, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(b66, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                            .add(b76, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(b05, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(b15, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(b25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(b35, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(b45, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(b55, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(b65, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(b75, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                .add(jPanel1Layout.createSequentialGroup()
                                    .add(b04, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                    .add(b14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                    .add(b24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                    .add(b34, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                    .add(b44, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                    .add(b54, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                    .add(b64, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                    .add(b74, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(jPanel1Layout.createSequentialGroup()
                                        .add(b03, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(b13, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(b23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(b33, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(b43, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(b53, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(b63, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(b73, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                    .add(jPanel1Layout.createSequentialGroup()
                                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                                            .add(b02, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                            .add(b01, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(b12, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(b22, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(b32, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(b42, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(b52, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(b62, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(b72, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                    .add(jPanel1Layout.createSequentialGroup()
                                        .add(65, 65, 65)
                                        .add(b11, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(b21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(b31, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(b41, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(b51, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(b61, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                        .add(b71, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))))
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(b00, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(20, 20, 20)
                                .add(jLabel9)))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(b10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                                .add(jLabel10)
                                .add(24, 24, 24)))
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(b20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(22, 22, 22)
                                .add(jLabel11)))
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(b30, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(b40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(28, 28, 28)
                                .add(jLabel15)
                                .add(47, 47, 47)
                                .add(jLabel12)))
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(b50, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(30, 30, 30)
                                .add(jLabel13)))
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(b60, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(28, 28, 28)
                                .add(jLabel14)))
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(b70, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(jPanel1Layout.createSequentialGroup()
                                .add(31, 31, 31)
                                .add(jLabel16)))))
                .addContainerGap(36, Short.MAX_VALUE))
        );

        blackRB.setText("Black");
        blackRB.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                blackRBMouseClicked(evt);
            }
        });
        blackRB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                blackRBActionPerformed(evt);
            }
        });

        whiteRB.setText("White");
        whiteRB.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                whiteRBMouseClicked(evt);
            }
        });
        whiteRB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                whiteRBActionPerformed(evt);
            }
        });

        jLabel17.setText("Black Score:");

        jLabel18.setText("White Score: ");

        blackScore.setText("2");

        whiteScore.setText("2");

        jLabel19.setText("Time Remaining:");

        timeRemain.setText("0:00");

        jLabel20.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 0, 0));
        jLabel20.setText("OTHELLO");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .add(38, 38, 38)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(layout.createSequentialGroup()
                        .add(jLabel17)
                        .add(18, 18, 18)
                        .add(blackScore)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(jLabel20))
                    .add(layout.createSequentialGroup()
                        .add(jLabel18)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(whiteScore)
                        .add(82, 82, 82)
                        .add(jLabel19)
                        .add(18, 18, 18)
                        .add(timeRemain)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(blackRB)
                    .add(whiteRB))
                .add(62, 62, 62))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(blackRB)
                    .add(jLabel17)
                    .add(blackScore)
                    .add(jLabel20))
                .add(4, 4, 4)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(whiteRB)
                    .add(jLabel18)
                    .add(whiteScore)
                    .add(jLabel19)
                    .add(timeRemain))
                .add(19, 19, 19)
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>                        

   

    private void blackRBActionPerformed(java.awt.event.ActionEvent evt) {                                        
        // TODO add your handling code here:
    }                                       

    private void whiteRBActionPerformed(java.awt.event.ActionEvent evt) {                                        
        // TODO add your handling code here:
    }                                       

    private void whiteRBMouseClicked(java.awt.event.MouseEvent evt) {                                     
        // TODO add your handling code here:
    	wselected=true;
        whiteRB.setEnabled(false);
        ArrayList<Disk> valid=new ArrayList<Disk>();
    	
  	    Value v=AlphaBeta(b,0,Double.MIN_VALUE,Double.MAX_VALUE,-1);

        b.add(v.best);
        b.update(v.best);

        jb[v.best.row][v.best.col].setBackground(Color.black);
        
    	valid=b.validMove(1); 
    	 printBoard(b.state);
         printDisk(valid);
    	for(int i=0;i<8;i++){
    		for(int j=0;j<8;j++){
    			if (b.state[i][j]==-1)
    				jb[i][j].setBackground(Color.black);
    		}
    	}
         for(int i=0;i<valid.size();i++){
    		//System.out.println("valid");
    		Disk disk=valid.get(i);
        	int r=disk.row;
        	int c=disk.col;
        	if(b.state[r][c]==0)
        	jb[r][c].setForeground(Color.white);	
        	jb[r][c].setText("Valid");
    	}
         
        
    }                                    

    private void blackRBMouseClicked(java.awt.event.MouseEvent evt) {                                     
        // TODO add your handling code here:
    	bselected=true;
    	blackRB.setEnabled(false);

    }                                    

    private void restartButtonMouseClicked(java.awt.event.MouseEvent evt) {                                           
        // TODO add your handling code here:
    	bselected=false;
    	wselected=false;
    	whiteRB.setSelected(false);
        blackRB.setSelected(false);
        whiteRB.setEnabled(true);
        blackRB.setEnabled(true);
        b=new Board();
        for(int i=0;i<8;i++){
        	for(int j=0;j<8;j++){
        		jb[i][j].setBackground(Color.gray);
        		jb[i][j].setText("");
        	}
        }
        jb[3][4].setBackground(Color.black);
        jb[4][3].setBackground(Color.black);

        jb[3][3].setBackground(Color.white);
        jb[4][4].setBackground(Color.white);
        blackScore.setText("2");
        whiteScore.setText("2");

    }     
    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {                                      
        // TODO add your handling code here:
    }                                     

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        // TODO add your handling code here:
    }         

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    private void bMouseClicked(java.awt.event.MouseEvent evt, int row, int col) {                                 
    if(wselected!=true&&bselected!=true){
    	JOptionPane pane = new JOptionPane("");
    	pane.showMessageDialog(null,"Please first select your side to start");
    }
    	
    else{
    	
    	if(bselected){
    	
    	for(int i=0;i<8;i++){
   
    		for(int j=0;j<8;j++){
    			if(b.state[i][j]==0)
    				jb[i][j].setText("");
    		}
    	}
    	b.add(new Disk(row,col,-1)); 	
    	b.update(new Disk(row,col,-1));
    	Value v=AlphaBeta(b,0,Double.MIN_VALUE,Double.MAX_VALUE,1);

         b.add(v.best);
         b.update(v.best);
for(int i=0;i<8;i++){
	for(int j=0;j<8;j++){
		jb[i][j].setText("");
		if (b.state[i][j]==1)
			jb[i][j].setBackground(Color.white);
		else if (b.state[i][j]==-1)
			jb[i][j].setBackground(Color.black);
	}
}
    	ArrayList<Disk> valid=new ArrayList<Disk>();
    	valid=b.validMove(-1); 
    	for(int i=0;i<valid.size();i++){
    		jb[valid.get(i).row][valid.get(i).col].setForeground(Color.black);
    		jb[valid.get(i).row][valid.get(i).col].setText("Valid");
    		
    	}
    	blackScore.setText(String.format("%d",b.black.size()));
        whiteScore.setText(String.format("%d",b.white.size()));
    
    	}  
    	
    	else if(wselected){
    		for(int i=0;i<8;i++){
    			   
        		for(int j=0;j<8;j++){
        			if(b.state[i][j]==0)
        				jb[i][j].setText("");
        		}
        	}
        	b.add(new Disk(row,col,1)); 	
        	b.update(new Disk(row,col,1));
        	Value v=AlphaBeta(b,0,Double.MIN_VALUE,Double.MAX_VALUE,-1);

             b.add(v.best);
             b.update(v.best);
    for(int i=0;i<8;i++){
    	for(int j=0;j<8;j++){
    		jb[i][j].setText("");
    		if (b.state[i][j]==1)
    			jb[i][j].setBackground(Color.white);
    		else if (b.state[i][j]==-1)
    			jb[i][j].setBackground(Color.black);
    	}
    }
        	ArrayList<Disk> valid=new ArrayList<Disk>();
        	valid=b.validMove(1); 
        	for(int i=0;i<valid.size();i++){
        		jb[valid.get(i).row][valid.get(i).col].setForeground(Color.white);
        		jb[valid.get(i).row][valid.get(i).col].setText("Valid");
        		
        	}
        	blackScore.setText(String.format("%d",b.black.size()));
            whiteScore.setText(String.format("%d",b.white.size()));
    	}
      	
    }   	
    	
    } 
        
 /*
  * 
  * 
  * 
  * 
  * 
  */
    public static Value AlphaBeta(Board board, int depth,double alpha,double beta,int player){
    	ArrayList<Disk> valid=new ArrayList<Disk>();
    	
    	Value value;
   
    	Disk best=new Disk(1,1,1);
    	//black player
    	//cutoff
    	if(depth>=2)
    		return(new Value(heuristic(board,player),null));
     	System.out.println();
    	System.out.println(depth);
    	System.out.println(player==-1?"Before adding black":"Before adding white");
    	printBoard(board.state);
    	if (player==-1){
    		valid=board.validMove(player);
    		for(Disk action:valid){
    			Board bcopy=makeCopy(board);
    			bcopy.add(action);
    			bcopy.update(action);
    			Board child=bcopy;
    			System.out.println("After adding black");
    			printBoard(child.state);
    			value=AlphaBeta(child,depth+1,alpha,beta,-player);
    		
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
    			//System.out.println("board before copuy:");
    			//printBoard(board.state);
    			bcopy.add(action);
    			bcopy.update(action);
    			Board child=bcopy;
    			//System.out.println("bcopy:");
    			//printBoard(bcopy.state);
    			System.out.println("After adding white");
    			printBoard(child.state);
    			value=AlphaBeta(child,depth+1,alpha,beta,-player);
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
    
    
    /*
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     */
    
     
    public static Value AlphaBeta(Node node, int depth,double alpha,double beta,int player){
    	ArrayList<Disk> valid=new ArrayList<Disk>();
    	Board b=makeCopy(node.board);
    	Value value;
    	System.out.println(depth);
    	printBoard(b.state);
    	Disk best=new Disk(1,1,1);
    	//black player
    	//cutoff
    	if(depth>=2)
    		return(new Value(heuristic(node.board,player),null));
    	
    	if (player==-1){
    		valid=b.validMove(player);
    		for(Disk action:valid){
    			Board bcopy=makeCopy(b);
    			Node child=new Node(node,heuristic(bcopy,action));
    			bcopy.add(action);
    			bcopy.update(action);
    			child.board=bcopy;
    			node.children.add(child);
    			value=AlphaBeta(child,depth+1,alpha,beta,-player);
    		
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
    	else{
    		valid=b.validMove(player);
    		for(Disk action:valid){
    			Board bcopy=makeCopy(b);
    			Node child=new Node(node,heuristic(bcopy,action));
    			bcopy.add(action);
    			bcopy.update(action);
    			child.board=bcopy;
    			node.children.add(child);
    			value=AlphaBeta(child,depth+1,alpha,beta,-player);
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
    
    
   /*
    * 
    * 
    * 
    * 
    */
    
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
    	m=b.validMove(-color).size();
    	double mobility=m;
    	
    	
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


    
    
    
    
    

    public static void main(String args[]) {
        Board b1=new Board();
    	Scanner scan=new Scanner(System.in);  
    	// String input=scan.nextLine();
    	String input="game B 4 0 0";
    	 String[] para=input.split(" ");
    	//System.out.println(para.length);
    	//System.out.println(para[1]);
    	int computer=(para[1].equals("B")?-1:1);
    	int depth=Integer.parseInt(para[2]);
    	int time1=Integer.parseInt(para[3]);
    	int time2=Integer.parseInt(para[4]);
    	
//tournament GUI BLACKPLAYER WHITEPLAYER DEPTHLIMIT TIMELIMIT1 TIMELIMIT2    	
    	if (para[0].equals("game")){
    		
    		if(para[1].equals("B")){
    			System.out.println("Computer plays black");
       
    			try {
    				
            
    				for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
    						javax.swing.UIManager.setLookAndFeel(info.getClassName());
    						break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GameFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GameFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GameFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GameFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
  
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GameFrame().setVisible(true);
            }
        });
        
     
        Board top=new Board();
        Value v=AlphaBeta(top,0,Double.MIN_VALUE,Double.MAX_VALUE,computer);
        System.out.println(v.best);
        top.add(v.best);
        top.update(v.best);
       
        top.add(new Disk(2,4,1));
        System.out.println("second move:");
        printBoard(top.state);
        v=AlphaBeta(top,0,Double.MIN_VALUE,Double.MAX_VALUE,computer);
        System.out.println(v.best);
        
        
    		}
    		else if (para[1].equals("text")){
    			
    		}
    		else if(para[1].equals("none")){
    			
    		}
    	
    	}
 //   	game COLOR DEPTHLIMIT TIMELIMIT1 TIMELIMIT2
    	
    }

    // Variables declaration - do not modify                     
    private javax.swing.JButton b00;
    private javax.swing.JButton b01;
    private javax.swing.JButton b02;
    private javax.swing.JButton b03;
    private javax.swing.JButton b04;
    private javax.swing.JButton b05;
    private javax.swing.JButton b06;
    private javax.swing.JButton b07;
    private javax.swing.JButton b10;
    private javax.swing.JButton b11;
    private javax.swing.JButton b12;
    private javax.swing.JButton b13;
    private javax.swing.JButton b14;
    private javax.swing.JButton b15;
    private javax.swing.JButton b16;
    private javax.swing.JButton b17;
    private javax.swing.JButton b20;
    private javax.swing.JButton b21;
    private javax.swing.JButton b22;
    private javax.swing.JButton b23;
    private javax.swing.JButton b24;
    private javax.swing.JButton b25;
    private javax.swing.JButton b26;
    private javax.swing.JButton b27;
    private javax.swing.JButton b30;
    private javax.swing.JButton b31;
    private javax.swing.JButton b32;
    private javax.swing.JButton b33;
    private javax.swing.JButton b34;
    private javax.swing.JButton b35;
    private javax.swing.JButton b36;
    private javax.swing.JButton b37;
    private javax.swing.JButton b40;
    private javax.swing.JButton b41;
    private javax.swing.JButton b42;
    private javax.swing.JButton b43;
    private javax.swing.JButton b44;
    private javax.swing.JButton b45;
    private javax.swing.JButton b46;
    private javax.swing.JButton b47;
    private javax.swing.JButton b50;
    private javax.swing.JButton b51;
    private javax.swing.JButton b52;
    private javax.swing.JButton b53;
    private javax.swing.JButton b54;
    private javax.swing.JButton b55;
    private javax.swing.JButton b56;
    private javax.swing.JButton b57;
    private javax.swing.JButton b60;
    private javax.swing.JButton b61;
    private javax.swing.JButton b62;
    private javax.swing.JButton b63;
    private javax.swing.JButton b64;
    private javax.swing.JButton b65;
    private javax.swing.JButton b66;
    private javax.swing.JButton b67;
    private javax.swing.JButton b70;
    private javax.swing.JButton b71;
    private javax.swing.JButton b72;
    private javax.swing.JButton b73;
    private javax.swing.JButton b74;
    private javax.swing.JButton b75;
    private javax.swing.JButton b76;
    private javax.swing.JButton b77;
    private javax.swing.JRadioButton blackRB;
    private javax.swing.JLabel blackScore;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton restartButton;
    private javax.swing.JLabel timeRemain;
    private javax.swing.JRadioButton whiteRB;
    private javax.swing.JLabel whiteScore;
    // End of variables declaration                   
}
