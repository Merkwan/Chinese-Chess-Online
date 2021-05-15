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
		if(player==REDPLAYER) {   //加载棋子图片
			if(typename.equals("帅"))
				chessImage=Toolkit.getDefaultToolkit().getImage("res\\帅.png");
			else if(typename.equals("相"))
				chessImage=Toolkit.getDefaultToolkit().getImage("res\\相.png");
			else if(typename.equals("仕"))
				chessImage=Toolkit.getDefaultToolkit().getImage("res\\仕.png");
			else if(typename.equals("马"))
				chessImage=Toolkit.getDefaultToolkit().getImage("res\\马.png");
			else if(typename.equals("车"))
				chessImage=Toolkit.getDefaultToolkit().getImage("res\\车.png");
			else if(typename.equals("炮"))
				chessImage=Toolkit.getDefaultToolkit().getImage("res\\炮.png");
			else if(typename.equals("兵"))
				chessImage=Toolkit.getDefaultToolkit().getImage("res\\兵.png");
		}
		else {
			if(typename.equals("将"))
				chessImage=Toolkit.getDefaultToolkit().getImage("res\\将1.png");
			else if(typename.equals("士"))
				chessImage=Toolkit.getDefaultToolkit().getImage("res\\士1.png");
			else if(typename.equals("象"))
				chessImage=Toolkit.getDefaultToolkit().getImage("res\\象1.png");
			else if(typename.equals("卒"))
				chessImage=Toolkit.getDefaultToolkit().getImage("res\\卒1.png");
			else if(typename.equals("马"))
				chessImage=Toolkit.getDefaultToolkit().getImage("res\\马1.png");
			else if(typename.equals("车"))
				chessImage=Toolkit.getDefaultToolkit().getImage("res\\车1.png");
			else if(typename.equals("炮"))
				chessImage=Toolkit.getDefaultToolkit().getImage("res\\炮1.png");
		}
	}
	public void SetPos(int x,int y)//设置棋子坐标
	{
		pos.x=x;
		pos.y=y;
	}
	
	
	public void ReversePos() //颠倒坐标
	{
		pos.x=10-pos.x;
		pos.y=11-pos.y;
	}
	
	protected void paint(Graphics g,JPanel i) //绘对应位置的棋子
	{
		g.drawImage(chessImage, pos.x*70-33, pos.y*70-20, 70,70,(ImageObserver)i);
	}
	
	public void DrawSelectedChess(Graphics g)
	{
		g.drawRect(pos.x*70-33,pos.y*70-20,70,70);
	}
}

