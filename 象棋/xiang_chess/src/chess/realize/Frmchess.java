package chess.realize;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import chess.realize.ChessBoard;

public class Frmchess extends JFrame{
	public static ChessBoard panel2=new ChessBoard();   //存放棋盘的对象
	public static JButton button1=new JButton("认输");  //认输按钮
	public static JButton button2=new JButton("开始");  //开始按钮
	public static JLabel label;
	public static JTextField jtextfield1=new JTextField();   //IP地址编辑框
	public static JTextField jtextfield2=new JTextField();   //端口号编辑框
	public static final short REDPLAYER=1;
	public static final short BLACKPLAYER=0;
	
	public Frmchess()
	{
		//JPanel panel1=new JPanel(new BorderLayout());/////
		JPanel panel3=new JPanel(new BorderLayout()); //放IP地址和端口的布局
		//String urlString ="res\\帅.png";
		//label=new JLabel(new ImageIcon(urlString));
		//panel1.add(label,BorderLayout.CENTER);
		panel2.setLayout(new BorderLayout());
		panel3.setLayout(new FlowLayout());       //设置为流动布局
		JLabel jLabel1=new JLabel("输入对方IP");
		JLabel jLabel2=new JLabel("输入对方端口");
		panel3.add(jLabel1);
		panel3.add(jLabel2);
		jtextfield1.setText("127.0.0.1");
		jtextfield2.setText("3004");             //默认端口号3004    
		panel3.add(jLabel1);
		panel3.add(jtextfield1);
		panel3.add(jLabel2);
		panel3.add(jtextfield2);
		panel3.add(button1);
		panel3.add(button2);
		this.getContentPane().setLayout(new BorderLayout());     
		//this.getContentPane().add(panel1,BorderLayout.NORTH);
		this.getContentPane().add(panel2,BorderLayout.CENTER);    //添加到总窗口中
		this.getContentPane().add(panel3,BorderLayout.SOUTH);
		this.setSize(710,830);                                //设置窗口的大小
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("网络中国象棋游戏");
		this.setVisible(true);
		button1.setEnabled(false);
		button2.setEnabled(true);
		setVisible(true);
		this.addWindowListener(new WindowAdapter() {         //添加窗口关闭监听
			public void windowClosing(WindowEvent e) {
				try {
					panel2.send("quit|");
					System.exit(0);
				}catch(Exception ex) {			
					}
			}
		});
		button1.addMouseListener(new MouseAdapter() {        //添加认输按钮监听
			public void mouseClicked(MouseEvent e) {
				try {
					panel2.send("lose|");
				}catch(Exception ex) {		
				}
			}
		});
		button2.addMouseListener(new MouseAdapter() {       //添加开始按钮监听
			public void mouseClicked(MouseEvent e) {
				String ip=jtextfield1.getText();
				int remoteport=Integer.parseInt(jtextfield2.getText());
				int receiveport;
				if(remoteport==3003)
					receiveport=3004;
				else
					receiveport=3003;
				panel2.startJoin(ip, remoteport, receiveport);
				button1.setEnabled(true);
				button2.setEnabled(true);
			}
		});
	}
	
	private boolean IsMyChess(int idx) {                 //判断点击是否是自己的棋子
			boolean functionReturnValue=false;
			if(idx>=0&&idx<16&&ChessBoard.LocalPlayer==BLACKPLAYER) {////////
				functionReturnValue=true;
			}
			if(idx>=16&&idx<32&&ChessBoard.LocalPlayer==REDPLAYER) {///////////
				functionReturnValue=true;
			}
			return functionReturnValue;
		}
	
	public static void SetMyTurn(boolean bolIsMyTurn) {  //设置轮到自己下棋
		ChessBoard.IsMyTurn=bolIsMyTurn;
		if(bolIsMyTurn) {
			ChessBoard.message="请您开始走棋";
		}
		else {
			ChessBoard.message="对方正在思考中......";
		}
	}
		
	
	public static void main(String[] args) {
		Frmchess f=new Frmchess();
	}
}	
	
	
	
