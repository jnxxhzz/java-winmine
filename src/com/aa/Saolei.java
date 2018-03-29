package com.aa;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class Saolei implements MouseListener{
	JFrame frame = new JFrame("扫雷");
	JButton reset = new JButton("Again");
	Container container = new Container();
	JPanel Header=new JPanel();
	
	
	final int dirx[]= {-1, 0, 1,0, 1,-1,-1,1};
	final int diry[]= { 0,-1, 0,1,-1,-1, 1,1};
	final int dirn = 8;
	
	Config config = new Config();
	final int Mine = -1;
	
	JButton border[][] = new JButton[config.row][config.col]; //按钮
	int score[][] = new int[config.row][config.col];  //数字
	int sign[][] = new int[config.row][config.col];  //标记
	int nowsigns = config.Minenumber;

	JButton scoreborder = new JButton();
	
	
	public Saolei() {
		//设置界面大小
		frame.setSize(600,700);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		
		//加载reset
		addOtherButton();
		
		//加载主界面按钮
		addborder();
		
		//加载雷
		addMine();

		
		frame.setVisible(true);
	}
	
	void addMine() {
		scoreborder.setText("雷数:" + nowsigns);
		Random rand = new Random();
		nowsigns = config.Minenumber;
		for (int i = 0 ; i < config.Minenumber; i++) {
			int x = rand.nextInt(config.row);
			int y = rand.nextInt(config.col);
			if (score[x][y] == Mine) i--;
			else {
				score[x][y] = Mine;
				for (int k=0;k<dirn;k++) {
					int gx = x + dirx[k];
					int gy = y + diry[k];
					if (gx>=0 && gx<config.row && gy>=0 && gy<config.col && score[gx][gy] != Mine ) 
						score[gx][gy]++;
				}
			}
		}
	}
	
	void addborder() {
		frame.add(container,BorderLayout.CENTER);
		container.setLayout(new GridLayout(config.row,config.col));
		for (int i = 0; i < config.row ; i++)
			for (int j = 0; j < config.col ; j++) {
				JButton x = new JButton();
				
				x.setMargin(new Insets(0,0,0,0));
				x.setBackground(config.Nocolor);
				x.setOpaque(false);
				x.addMouseListener(this);
				border[i][j] = x;
				container.add(x);
			}
	}
	
	void addOtherButton() {
		//加载重新开始按钮
		reset.setBackground(config.Nocolor);
		reset.setOpaque(false); //透明度
		reset.addMouseListener(this);
		
		//加载分数面板
		scoreborder.setEnabled(false);
		scoreborder.setOpaque(true); //透明度

		Header.add(reset);
		Header.add(scoreborder);
		

		frame.add(Header,BorderLayout.NORTH);
		
	}	

	void openButton(int x,int y) {
		border[x][y].setText( "" + score[x][y]);
		border[x][y].setBackground(config.Opencolor);
		border[x][y].setEnabled(false);
		border[x][y].setOpaque(true);
		if (score[x][y] !=0 ) return;
		for (int i=0;i<dirn/2;i++) {
			int gx = x + dirx[i];
			int gy = y + diry[i];
			if (gx>=0 && gy>=0 && gx<config.row && gy<config.col && border[gx][gy].isEnabled() == true &&score[gx][gy] != Mine) openButton(gx,gy);
		}
	}
	
	
	void checkWin() {
		for (int i=0;i<config.row;i++)
			for (int j=0;j<config.col;j++)
				if (border[i][j].isEnabled() == true && score[i][j] != Mine)
					return;
		WinGame();
	}
	
	void WinGame() {
		//Showall();
		JOptionPane.showMessageDialog(frame, "You win!!");
	}
	void LoseGame() {
		Showall();
		JOptionPane.showMessageDialog(frame, "You lose!!");
	}
	void Showall() {
		for (int i=0;i<config.row;i++)
			for (int j=0;j<config.col;j++) 
			if (score[i][j] == Mine){
				border[i][j].setText( "X" );
				border[i][j].setBackground(config.Minecolor);
				border[i][j].setEnabled(false);
				border[i][j].setOpaque(true);
			}else {
				border[i][j].setText( "" + score[i][j]);
				border[i][j].setBackground(Color.green);
				border[i][j].setEnabled(false);
				border[i][j].setOpaque(true);
			}
	}
	
	void AgainGame() {
		for (int i=0;i<config.row;i++)
			for (int j=0;j<config.col;j++) {
				score[i][j] = 0;
				border[i][j].setBackground(config.Nocolor);
				border[i][j].setEnabled(true);
				border[i][j].setOpaque(false);
				border[i][j].setText("");
			}
		nowsigns = config.Minenumber;
		addMine();
		
	}
	
	@Override
	public void mouseClicked(MouseEvent xx) {

		//获取点击button
		JButton button = (JButton)xx.getSource();
		int tx = 0,ty = 0;
		for (int i=0;i<config.row;i++)
			for (int j=0;j<config.col;j++)
				if (button.equals(border[i][j])) {
					tx=i;
					ty=j;
					break;
				}
		if (button.isEnabled() == false) return;
		if (button.equals(reset)) { //左键点击reset重新开始按钮
			if (xx.getButton() == MouseEvent.BUTTON1) //左键点击reset重新开始
				AgainGame();
		}else {
			if (xx.getButton() == MouseEvent.BUTTON3) { //右键
				if (border[tx][ty].getBackground() == config.Nocolor ||
					border[tx][ty].getBackground() == config.Signcolor ) {
					if (sign[tx][ty] == config.signed) { // 右键被标记的格子则取消标记
						sign[tx][ty] = config.notsigned;
						nowsigns++;
						border[tx][ty].setBackground(config.Nocolor);
						border[tx][ty].setOpaque(false);
						border[tx][ty].setText("");
						scoreborder.setText("雷数:" + nowsigns); 
					}else {  //右键未标记的格子则标记该格子，剩余雷数-1
						sign[tx][ty] = config.signed;
						nowsigns--;
						border[tx][ty].setBackground(config.Signcolor);
						border[tx][ty].setOpaque(true);
						border[tx][ty].setText("");
						scoreborder.setText("雷数:" + nowsigns);
					}
				}
				
			}else { // 左键
				//点击不被标记的格子才需要作出响应
				if (sign[tx][ty] != config.signed) { 
					if (score[tx][ty] == Mine ) {  //左键点击雷
						LoseGame();
					}else { //左键点击到数字块，则翻开所有相连的0块
						openButton(tx,ty);
						checkWin();
					}
				}else {
					//点击被标记的格子则不作任何响应
				}
			}
		}
	}

	public static void main(String[] args) {
		Saolei lei = new Saolei();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
