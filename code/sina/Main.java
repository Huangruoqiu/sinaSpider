package sina;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Main extends JFrame {
	
	private JButton startButton;
	private static JPanel jPanel;
	static MainThread mainThread;
	static JLabel tip;
	Main(){
		super("΢��������ȡ");
		jPanel = new JPanel();
		tip = new JLabel("��������ʾ");
		startButton=new JButton("��ʼ");
		JButton endButton=new JButton("ֹͣ");
		jPanel.add(tip);
		jPanel.add(startButton);
		jPanel.add(endButton);
		this.add(jPanel);
		this.setSize(400, 200);
		this.setVisible(true);
		this.setDefaultCloseOperation(3);
		startButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!mainThread.isAlive()) {
					mainThread.start();
					tip.setText("�������߳�");
				}	
			}
		});
		endButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				mainThread.stopWork();
				tip.setText("���ڵȴ����й����̹߳ر�");
			}
		});
	}
	
	public static void main(String[] args) {
		Main m=new Main();
		mainThread=new MainThread(jPanel);
	}

}
