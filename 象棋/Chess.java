package chess.realize;

import java.awt.*;
import java.awt.image.ImageObserver;

import javax.swing.JPanel;

public class Chess {
	public static final short REDPLAYER=1;
	public static final short BLACKPLAYER=0;
	public short player;
	public String typeName;
	public Point pos;
	private Image chessImage;
	public Chess(short player,String typename,Point pos){
		this.player=player;
		this.typeName=typename;
		this.pos=pos;
		if(player==REDPLAYER) {
			if(typename.equals("帅"))
				chessImage=Toolkit.getDefaultToolkit().getImage("D:\\Java游戏程序\\棋盘和棋子\\image\\帅.png");
			else if(typename.equals("相"))
				chessImage=Toolkit.getDefaultToolkit().getImage("D:\\Java游戏程序\\棋盘和棋子\\image\\相.png");
			else if(typename.equals("仕"))
				chessImage=Toolkit.getDefaultToolkit().getImage("D:\\Java游戏程序\\棋盘和棋子\\image\\仕.png");
			else if(typename.equals("马"))
				chessImage=Toolkit.getDefaultToolkit().getImage("D:\\Java游戏程序\\棋盘和棋子\\image\\马.png");
			else if(typename.equals("车"))
				chessImage=Toolkit.getDefaultToolkit().getImage("D:\\Java游戏程序\\棋盘和棋子\\image\\车.png");
			else if(typename.equals("炮"))
				chessImage=Toolkit.getDefaultToolkit().getImage("D:\\Java游戏程序\\棋盘和棋子\\image\\炮.png");
			else if(typename.equals("兵"))
				chessImage=Toolkit.getDefaultToolkit().getImage("D:\\Java游戏程序\\棋盘和棋子\\image\\兵.png");
		}
		else {
			if(typename.equals("将"))
				chessImage=Toolkit.getDefaultToolkit().getImage("D:\\Java游戏程序\\棋盘和棋子\\image\\将1.png");
			else if(typename.equals("士"))
				chessImage=Toolkit.getDefaultToolkit().getImage("D:\\Java游戏程序\\棋盘和棋子\\image\\士1.png");
			else if(typename.equals("象"))
				chessImage=Toolkit.getDefaultToolkit().getImage("D:\\Java游戏程序\\棋盘和棋子\\image\\象1.png");
			else if(typename.equals("卒"))
				chessImage=Toolkit.getDefaultToolkit().getImage("D:\\Java游戏程序\\棋盘和棋子\\image\\卒1.png");
			else if(typename.equals("马"))
				chessImage=Toolkit.getDefaultToolkit().getImage("D:\\Java游戏程序\\棋盘和棋子\\image\\马1.png");
			else if(typename.equals("车"))
				chessImage=Toolkit.getDefaultToolkit().getImage("D:\\Java游戏程序\\棋盘和棋子\\image\\车1.png");
			else if(typename.equals("炮"))
				chessImage=Toolkit.getDefaultToolkit().getImage("D:\\Java游戏程序\\棋盘和棋子\\image\\炮1.png");
		}
	}
	public void SetPos(int x,int y)
	{
		pos.x=x;
		pos.y=y;
	}
	
	
	public void ReversePos()
	{
//		System.out.println("转化前"+typeName+"("+pos.x+","+pos.y+")");
		pos.x=10-pos.x;
		pos.y=11-pos.y;
//		System.out.println("转化后"+typeName+"("+pos.x+","+pos.y+")");
	}
	
	protected void paint(Graphics g,JPanel i)
	{
		g.drawImage(chessImage, pos.x*70-33, pos.y*70-20, 70,70,(ImageObserver)i);
//		System.out.println("ok——chess1");
	}
	
	public void DrawSelectedChess(Graphics g)
	{
		g.drawRect(pos.x*70-33,pos.y*70-20,70,70);
		System.out.println("ok______chess2");
	}
}

