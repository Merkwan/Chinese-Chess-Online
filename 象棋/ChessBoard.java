package chess.realize;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import chess.realize.Frmchess;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import chess.realize.Frmchess;

public class ChessBoard extends JPanel implements Runnable {
	public static final short REDPLAYER=1;
	public static final short BLACKPLAYER=0;
	public Chess[] chess =new Chess[32];
	public int[][] Map=new int[10][11];
	public Image bufferimage;
	private Chess firstChess2=null;
	private Chess secondChess2=null;
	private boolean first=true;
	private int x1,x2,y1,y2;
	private int tempx,tempy;
	private int r;
	public static boolean IsMyTurn=true;             ////
	public static short LocalPlayer=REDPLAYER; ///
	public static  String message="";
	private boolean flag=false;
	private int otherport;
	private int receiveport;
	
	private void cls_map() 
	{
		int i,j;
		for(i=1;i<10;i++) 
			for(j=1;j<11;j++)
				Map[i][j]=-1;
	}
	
	public ChessBoard()
	{
		r=40;
		cls_map();
		System.out.println("ok____Board");
//		int m;
//		m=(int)-0.5;
//		System.out.println(m);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				if(IsMyTurn==false) {
					message="该对方走棋";
					repaint();
					return;
				}
				int x=e.getX();
				int y=e.getY();
				selectChess(e);
				System.out.println("ok____Board1");
				System.out.println("("+x+","+y+")");
				repaint();
			}
			
			private void selectChess(MouseEvent e) 
			{
				int idx,idx2;
				if(first) {
					firstChess2=analyse(e.getX(),e.getY());
					x1=tempx;
					y1=tempy;
					if(firstChess2!=null) {
						repaint();
						System.out.println("~~~~~~~~~ok1~~~~~~~~~~~~~~~");
						if(firstChess2.player!=LocalPlayer) {
							message="单击成对方棋子了";
							return;
						}
						first=false;
					}
				}
				else {
					secondChess2=analyse(e.getX(),e.getY());
					x2=tempx;
					y2=tempy;
					if(secondChess2!=null) {
						repaint();
						System.out.println("~~~~~~~~~ok2~~~~~~~~~~~~~~~");
						if(secondChess2.player==LocalPlayer) {
							firstChess2=secondChess2;
							x1=tempx;
							y1=tempy;
							System.out.println("ok__95");
							secondChess2=null;
							return;
						}
						if(IsAbleToPut(firstChess2,x2,y2))
						{
							repaint();
							first=true;
							idx=Map[x1][y1];
							idx2=Map[x2][y2];
							Map[x1][y1]=-1;
							Map[x2][y2]=idx;
							chess[idx].SetPos(x2, y2);
							chess[idx2]=null;
							repaint();
							if(idx2==0) {
								message="红方赢了";
								JOptionPane.showConfirmDialog(null,"红方赢了","提示",JOptionPane.DEFAULT_OPTION);
								send("move"+"|"+idx+"|"+(10-x2)+"|"+String.valueOf(11-y2)+"|");
								send("succ"+"|"+"红方赢了"+"|");
								return;
							}
							if(idx2==16) {
								message="黑方赢了";
								JOptionPane.showConfirmDialog(null, "黑方赢了","提示",JOptionPane.DEFAULT_OPTION);
								send("move"+"|"+idx+"|"+(10-x2)+"|"+String.valueOf(11-y2)+"|");
								send("succ"+"|"+"黑方赢了"+"|");
								return;
							}
							send("move"+"|"+idx+"|"+(10-x2)+"|"+String.valueOf(11-y2)+"|");
							Frmchess.SetMyTurn(false);
						}
						else
						{
							message="不能吃子";
						}
					}
					if(secondChess2==null){
						if(IsAbleToPut(firstChess2,x2,y2)) {
							idx=Map[x1][y1];
							Map[x1][y1]=-1;
							Map[x2][y2]=idx;
							System.out.println("~~~~~~ok3~~~~~~~~~~~~~~~");
							chess[idx].SetPos(x2,y2);
							send("move"+"|"+idx+"|"+(10-x2)+"|"+String.valueOf(11-y2)+"|");
							first=true;
							System.out.println("ok__108");
							repaint();
							Frmchess.SetMyTurn(false);
						}
						else {
							message="不符合走棋规则";
						}
						return;
					}
//					if(secondChess2!=null&&IsAbleToPut(firstChess2,x2,y2))
//					{
//						repaint();
//						first=true;
//						idx=Map[x1][y1];
//						idx2=Map[x2][y2];
//						Map[x1][y1]=-1;
//						Map[x2][y2]=idx;
//						chess[idx].SetPos(x2, y2);
//						chess[idx2]=null;
//						repaint();
//						if(idx2==0) {
//							message="红方赢了";
//							JOptionPane.showConfirmDialog(null,"红方赢了","提示",JOptionPane.DEFAULT_OPTION);
//							send("move"+"|"+idx+"|"+(10-x2)+"|"+String.valueOf(11-y2)+"|");
//							send("succ"+"|"+"红方赢了"+"|");
//							return;
//						}
//						if(idx2==16) {
//							message="黑方赢了";
//							JOptionPane.showConfirmDialog(null, "黑方赢了","提示",JOptionPane.DEFAULT_OPTION);
//							send("move"+"|"+idx+"|"+(10-x2)+"|"+String.valueOf(11-y2)+"|");
//							send("succ"+"|"+"黑方赢了"+"|");
//							return;
//						}
//						send("move"+"|"+idx+"|"+(10-x2)+"|"+String.valueOf(11-y2)+"|");
//						Frmchess.SetMyTurn(false);
//					}
//					else
//					{
//						message="不能吃子";
//					}
				}
			}
		});
	}
		
		public void startJoin(String ip,int otherport,int receiveport)
		{
			flag=true;
			this.otherport=otherport;
			this.receiveport=receiveport;
			send("join|"); //发送参与消息寻求对方
			System.out.println("ok3");//////////////////////333333333333
			Thread th=new Thread(this);////////////446
			th.start();
			message="程序处于等待联机状态";
		}
		
		public final void NewGame(short player)
		{
			cls_map();
			InitChess();
			if(player==BLACKPLAYER)
			{
				ReverseBoard();
			}
			repaint();
		}
		
		public void InitChess() {
			chess[0]=new Chess(BLACKPLAYER,"将",new Point(5,1));
			Map[5][1]=0;
			chess[1]=new Chess(BLACKPLAYER,"士",new Point(4,1));
			Map[4][1]=1;
			chess[2]=new Chess(BLACKPLAYER,"士",new Point(6,1));
			Map[6][1]=2;
			chess[3]=new Chess(BLACKPLAYER,"象",new Point(3,1));
			Map[3][1]=3;
			chess[4]=new Chess(BLACKPLAYER,"象",new Point(7,1));
			Map[7][1]=4;
			chess[5]=new Chess(BLACKPLAYER,"马",new Point(2,1));
			Map[2][1]=5;
			chess[6]=new Chess(BLACKPLAYER,"马",new Point(8,1));
			Map[8][1]=6;
			chess[7]=new Chess(BLACKPLAYER,"车",new Point(1,1));
			Map[1][1]=7;
			chess[8]=new Chess(BLACKPLAYER,"车",new Point(9,1));
			Map[9][1]=8;
			chess[9]=new Chess(BLACKPLAYER,"炮",new Point(2,3));
			Map[2][3]=9;
			chess[10]=new Chess(BLACKPLAYER,"炮",new Point(8,3));
			Map[8][3]=10;
			
			for(int i=0;i<=4;i++) {
				chess[11+i]=new Chess(BLACKPLAYER,"卒",new Point(1+i*2,4));
				Map[1+i*2][4]=11+i;
			}
			
			chess[16]=new Chess(REDPLAYER,"帅",new Point(5,10));
			Map[5][10]=16;
			chess[17]=new Chess(REDPLAYER,"仕",new Point(4,10));
			Map[4][10]=17;
			chess[18]=new Chess(REDPLAYER,"仕",new Point(6,10));
			Map[6][10]=18;
			chess[19]=new Chess(REDPLAYER,"相",new Point(3,10));
			Map[3][10]=19;
			chess[20]=new Chess(REDPLAYER,"相",new Point(7,10));
			Map[7][10]=20;
			chess[21]=new Chess(REDPLAYER,"马",new Point(2,10));
			Map[2][10]=21;
			chess[22]=new Chess(REDPLAYER,"马",new Point(8,10));
			Map[8][10]=22;
			chess[23]=new Chess(REDPLAYER,"车",new Point(1,10));
			Map[1][10]=23;
			chess[24]=new Chess(REDPLAYER,"车",new Point(9,10));
			Map[9][10]=24;
			chess[25]=new Chess(REDPLAYER,"炮",new Point(2,8));
			Map[2][8]=25;
			chess[26]=new Chess(REDPLAYER,"炮",new Point(8,8));
			Map[8][8]=26;
			
			for(int i=0;i<=4;i++)
			{
				chess[27+i]=new Chess(REDPLAYER,"兵",new Point(1+i*2,7));
				Map[1+i*2][7]=27+i;
			}
		}
		
		private void ReverseBoard() {
		     int x,y,c;
			for(int i=0;i<32;i++)
				if(chess[i]!=null)
				{
					chess[i].ReversePos();
					System.out.println("转化"+i+"次");
				}
			for(x=1;x<=9;x++)
				for(y=1;y<5;y++)
					if(Map[x][y]!=-1)
					{
						c=Map[10-x][11-y];
						Map[10-x][11-y]=Map[x][y];
						Map[x][y]=c;
					}			
		}
		
		public void paint(Graphics g)
		{
			g.clearRect(0, 0, this.getWidth(), this.getHeight());
			Image bgImage=Toolkit.getDefaultToolkit().getImage("D:\\Java游戏程序\\棋盘和棋子\\image\\background1.png");
			g.drawImage(bgImage, 1, 20,this);
//			System.out.print(receiveport+":");
			for(int i=0;i<32;i++)
				if(chess[i]!=null) {
					chess[i].paint(g,this);
					//System.out.println(chess[i].typeName+"("+chess[i].pos.x+","+chess[i].pos.y+")");
				}
					
		
			if(firstChess2!=null)
				firstChess2.DrawSelectedChess(g);
			
			if(secondChess2!=null)
				secondChess2.DrawSelectedChess(g);
				
			g.drawString(message, 0, 450);
		}	
	
		public final boolean IsAbleToPut(Chess firstchess,int x,int y)
		{
			int i,j,c;
			int oldx,oldy;
			oldx=firstchess.pos.x;
			oldy=firstchess.pos.y;
			String qi_name=firstchess.typeName;
			if(qi_name.equals("将")||qi_name.equals("帅"))
			{
				if((x-oldx)*(y-oldy)!=0) 
					return false;
				if(Math.abs(x-oldx)>1||Math.abs(y-oldy)>1)
					return false;
				if(x<4||x>6||(y>3&&y<8))
					return false;
				return true;
			}
			if(qi_name.equals("士")||qi_name.equals("仕"))
			{
				if((x-oldx)*(y-oldy)==0) 
					return false;
				if(Math.abs(x-oldx)>1||Math.abs(y-oldy)>1)
					return false;
				if(x<4||x>6||(y>3&&y<8))
					return false;
				return true;
			}
			if(qi_name.equals("象")||qi_name.equals("相"))
			{
				if((x-oldx)*(y-oldy)==0) 
					return false;
				if(Math.abs(x-oldx)!=2||Math.abs(y-oldy)!=2)
					return false;
				if(y<6)
					return false;
				i=0;
				j=0;
				if(x-oldx==2)
					i=x-1;
				if(x-oldx==-2)
					i=x+1;
				if(y-oldy==2)
					j=y-1;
				if(y-oldy==-2)
					j=y+1;
				if(Map[i][j]!=-1)
					return false;
				return true;
			}
			if(qi_name.equals("马")||qi_name.equals("马"))
			{
				if(Math.abs(x-oldx)*Math.abs(y-oldy)!=2)
					return false;
				if(x-oldx==2)
					if(Map[x-1][oldy]!=-1)
						return false;
				if(x-oldx==-2)
					if(Map[x+1][oldy]!=-1)
						return false;
				if(y-oldy==2)
					if(Map[oldx][y-1]!=-1)
						return false;
				if(y-oldy==-2)
					if(Map[oldx][y+1]!=-1)
						return false;
				return true;
			}
			if(qi_name.equals("车")||qi_name.equals("车"))
			{
				if((x-oldx)*(y-oldy)!=0) 
					return false;
				if(x!=oldx) {
					if(oldx>x) {
						int t=x;
						x=oldx;
						oldx=t;
					}
					for(i=oldx;i<=x;i++)
					if(i!=x&&i!=oldx)
						if(Map[i][y]!=-1)
							return false;
				}
				if(y!=oldy) {
					if(oldy>y) {
						int t=y;
						y=oldy;
						oldy=y;
					}
					for(i=oldy;i<=y;i++)
						if(i!=oldy&&i!=y)
							if(Map[x][i]!=-1)
								return false;
				}
				if(x<4||x>6||(y>3&&y<8))
					return false;
				return true;
			}
			if(qi_name.equals("炮")||qi_name.equals("炮"))
			{
				boolean swapflagx=false;
				boolean swapflagy=false;
				if((x-oldx)*(y-oldy)!=0) 
					return false;
				c=0;
				if(x!=oldx) {
					if(oldx>x) {
						int t=oldx;
						oldx=x;
						x=t;
						swapflagx=true;
					}
					for(i=oldx;i<=x;i++)
						if(i!=x&&i!=oldx)
							if(Map[i][y]!=-1)
								c=c+1;
				}
				if(y!=oldy) {
					if(oldy>y) {
						int t=oldy;
						oldy=y;
						y=t;
						swapflagy=true;
					}
					for(j=oldy;j<=y;j++)
						if(j!=y&&j!=oldy)
							if(Map[x][j]!=-1)
								c=c+1;
				}
				if(c>1)
					return false;
				if(c==0)
				{
					if(swapflagx==true)
					{
						int t=x;
						x=oldx;
						oldx=t;
					}
					if(swapflagy==true)
					{
						int t=x;
						y=oldy;
						oldy=t;
					}
					if(Map[x][y]!=-1)
						return false;
				}
				if(c==1)
				{
					if(swapflagx==true)
					{
						int t=x;
						x=oldx;
						oldx=t;
					}
					if(swapflagy==true)
					{
						int t=x;
						y=oldy;
						oldy=t;
					}
					if(Map[x][y]==-1)
						return false;
				}
				return true;
			}
			if(qi_name.equals("卒")||qi_name.equals("兵"))
			{
				if((x-oldx)*(y-oldy)!=0)
					return false;
				if(Math.abs(x-oldx)>1||Math.abs(y-oldy)>1)
					return false;
				if(y>=6&&(x-oldx)!=0)
					return false;
				if(y-oldy>0)
					return false;
				return true;
			}
			return false;
		}
	
		public void run() {
			try {
				DatagramSocket s=new DatagramSocket(receiveport);
				byte[] data=new byte[100];
				System.out.println("ok4");////////////////////444444444444444444444
				DatagramPacket dgp=new DatagramPacket(data,data.length);
				System.out.println("ok6");///////////////////6666666666666666666
				while(flag==true) {
					System.out.println("ok7"); //////////////////////////////7777777777777
					s.receive(dgp);
					String strData=new String(data);
					System.out.println("ok8");//////////////////////////////888888888888888888
					String[] a=new String[6];
					a=strData.split("\\|");
					if(a[0].equals("join"))
					{
						LocalPlayer=BLACKPLAYER;
						System.out.println("ok__run9");//////////////
						NewGame(LocalPlayer);
						System.out.println(receiveport+"是BLACKPLAYER");
						if(LocalPlayer==REDPLAYER)
							Frmchess.SetMyTurn(true);
						else
							Frmchess.SetMyTurn(false);
						send("conn|");
					}
					else if(a[0].equals("conn"))
					{
						LocalPlayer=REDPLAYER;
						NewGame(LocalPlayer);
						System.out.println(receiveport+"是REDPLAYER");
						if(LocalPlayer==REDPLAYER)
							Frmchess.SetMyTurn(true);
						else
							Frmchess.SetMyTurn(false);
					}
					else if(a[0].equals("succ")) {
						if(a[1].equals("黑方赢了"))
							JOptionPane.showConfirmDialog(null, "黑方赢了你可以重新开始！","你输了",JOptionPane.DEFAULT_OPTION);
						if(a[1].equals("红方赢了"))
							JOptionPane.showConfirmDialog(null, "红方赢了你可以重新开始！","你输了",JOptionPane.DEFAULT_OPTION);
						message="你可以重新开局！";
					}
					else if(a[0].equals("move")) {
						int idx=Short.parseShort(a[1]);
						x2=Short.parseShort(a[2]);
						y2=Short.parseShort(a[3]);
						String z=a[4];
						message=x2+":"+y2;
						Chess c=chess[idx];
						x1=c.pos.x;
						y1=c.pos.y;
						
						idx=Map[x1][y1];
						int idx2=Map[x2][y2];
						Map[x1][y1]=-1;
						Map[x2][y2]=idx;
						chess[idx].SetPos(x2, y2);
						if(idx2!=-1) {
							chess[idx2]=null;
						}
						repaint();
						IsMyTurn=true;
					}
					else if(a[0].equals("quit")) {
						JOptionPane.showConfirmDialog(null, "对方退出了，游戏结束！","提示",JOptionPane.DEFAULT_OPTION);
						message="对方退出了，游戏结束！";
						flag=false;
					}
					else if(a[0].equals("lose")) {
						JOptionPane.showConfirmDialog(null, "恭喜你，对方认输了！","你赢了",JOptionPane.DEFAULT_OPTION);
						Frmchess.SetMyTurn(false);
					}
					System.out.println(new String(data));
				}
			}catch(SocketException e) {
				e.printStackTrace();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
		public void send(String str)
		{
			DatagramSocket s=null;
			try {
				s=new DatagramSocket();
				byte[] buffer;
				buffer=new String(str).getBytes();
				InetAddress ia=InetAddress.getLocalHost();
				DatagramPacket dgp=new DatagramPacket(buffer,buffer.length,ia,otherport);
				s.send(dgp);
				System.out.println("端口"+this.receiveport+"发送消息："+str);
			}catch(IOException e) {
				System.out.println(e.toString());
			}finally {
				if(s!=null)
					s.close();
			}
		}
		
		private Chess analyse(int x,int y)
		{
			tempx=(int)Math.floor((double)(x-37)/70)+1;
			tempy=(int)Math.floor((double)(y-50)/70)+1;
			if(tempx>9||tempy>10||tempx<1||tempy<1)
				return null;
			else
			{
				int idx=Map[tempx][tempy];
				if(idx==-1)
					return null;
				return chess[idx];
			}
		}
}
