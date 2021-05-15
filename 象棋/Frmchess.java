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
	public static ChessBoard panel2=new ChessBoard();
	public static JButton button1=new JButton("认输");  //
	public static JButton button2=new JButton("开始");
	
	public static JTextField jtextfield1=new JTextField();
	public static JTextField jtextfield2=new JTextField();
	public static final short REDPLAYER=1;
	public static final short BLACKPLAYER=0;
	
	public Frmchess()
	{
		System.out.println("ok11");
		JPanel panel1=new JPanel(new BorderLayout());
		JPanel panel3=new JPanel(new BorderLayout());
		String urlString ="D:\\Java游戏程序\\棋盘和棋子\\image\\帅.png";
		JLabel label=new JLabel(new ImageIcon(urlString));
		panel1.add(label,BorderLayout.CENTER);
		panel2.setLayout(new BorderLayout());
		panel3.setLayout(new FlowLayout());
		JLabel jLabel1=new JLabel("输入IP");
		JLabel jLabel2=new JLabel("输入对方端口");
		panel3.add(jLabel1);
		panel3.add(jLabel2);
		jtextfield1.setText("127.0.0.1");
		jtextfield2.setText("3004");
		panel3.add(jLabel1);
		panel3.add(jtextfield1);
		panel3.add(jLabel2);
		panel3.add(jtextfield2);
		panel3.add(button1);
		panel3.add(button2);
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(panel1,BorderLayout.NORTH);
		this.getContentPane().add(panel2,BorderLayout.CENTER);
		this.getContentPane().add(panel3,BorderLayout.SOUTH);
		this.setSize(698,910);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("网络中国象棋游戏");
		this.setVisible(true);
		button1.setEnabled(false);
		button2.setEnabled(true);
		setVisible(true);
		System.out.println("ok12");//////////////111111111111111111111111
		this.addWindowListener(new WindowAdapter() { 
			public void windowClosing(WindowEvent e) {
				try {
					panel2.send("quit|");
					System.exit(0);
				}catch(Exception ex) {			
					}
			}
		});
		button1.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				try {
					panel2.send("lose|");
				}catch(Exception ex) {		
				}
			}
		});
		button2.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				String ip=jtextfield1.getText();
				int remoteport=Integer.parseInt(jtextfield2.getText());
				int receiveport;
				if(remoteport==3003)
					receiveport=3004;
				else
					receiveport=3003;
				System.out.println("ok2");///////////////2222222222222222222222
				panel2.startJoin(ip, remoteport, receiveport);
				button1.setEnabled(true);
				System.out.println("ok5");///////////////////55555555555555555555555
				button2.setEnabled(true);
			}
		});
	}
	
	private boolean IsMyChess(int idx) {
			boolean functionReturnValue=false;
			if(idx>=0&&idx<16&&ChessBoard.LocalPlayer==BLACKPLAYER) {////////
				functionReturnValue=true;
			}
			if(idx>=16&&idx<32&&ChessBoard.LocalPlayer==REDPLAYER) {///////////
				functionReturnValue=true;
			}
			return functionReturnValue;
		}
	
	public static void SetMyTurn(boolean bolIsMyTurn) {
		ChessBoard.IsMyTurn=bolIsMyTurn;
		if(bolIsMyTurn) {
			ChessBoard.message="请您开始走棋";
		}
		else {
			ChessBoard.message="对方正在思考中......";
		}
	}
		
	/*
	 * button2.addMouseListener(new MouseAdapter() { public void
	 * mouseClicked(MouseEvent e) { String ip=jtextfield1.getText(); int
	 * remoteport=Integer.parseInt(jtextfield2.getText()); int receiveport;
	 * if(remoteport==3003) receiveport=3004; else receiveport=3003;
	 * System.out.println("ok2"); panel2.startJoin(ip, remoteport, receiveport);
	 * button1.setEnabled(true); button2.setEnabled(true); } });
	 */
	
	public static void main(String[] args) {
		Frmchess f=new Frmchess();
	}
}	
	
	
	
