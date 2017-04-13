package sina;

import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class MainThread extends Thread{
	static ArrayList<WorkThread> threads=new ArrayList<WorkThread>();
	public static int threadsCount=1;
	private JPanel jPanel;
	
	public MainThread(JPanel jPanel) {
		this.jPanel=jPanel;
	}
	@Override
	public void run() {
		//�����ݿ⵼�����
		WorkQueue.load();
		//Ϊ�շ����ʼ����
		if (WorkQueue.queue.isEmpty()) {
			User user=new User();
			user.setUid("3268063401");
			user.setName("��С��-Toxicant");
			user.setAvatar("�Լ��Ͳ�Ҫͷ���˰�~");
			user.setGender("m");
			user.setFans(200);
			user.setFollowers(100);
			user.setLv(1);
			WorkQueue.add(user);
		}
		//��ʼ�����߳�
		for(int i=0;i<threadsCount;i++){
			JLabel label=new JLabel("�߳�"+i);
			jPanel.add(label);
			WorkThread t=new WorkThread(label);
			threads.add(t);
			t.start();
		}
		//�����ȴ����й����߳̽���
		boolean run=true;
		while (run) {
			run=false;
			for (WorkThread t : threads) {
				run=run | t.isAlive();
			}
		}
		//�洢����
		WorkQueue.saveAll();
		//����
		System.out.println("=====�����������=====");
	}
	public void stopWork(){
		for (WorkThread workThread : threads) {
			workThread.running=false;
		}
	}
	

}
