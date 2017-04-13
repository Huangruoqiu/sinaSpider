package sina;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import com.google.gson.Gson;

public class WorkQueue {
	public static LinkedBlockingQueue<User> queue=new LinkedBlockingQueue<User>();
	/**
	 * ����û�����������
	 * @param u
	 */
	public static void add(User u) {
		try {
			//System.out.println("���");
			queue.put(u);
			//TODO ����Ҫ�����û���
			DBhelper.getInstance().addUser2DB(u);
			//System.out.println("��ӵ����ݿ�");
		} catch (InterruptedException e) {
			
		}
		
	}
	/**
	 * ����һ���û�
	 * @return
	 * @throws InterruptedException 
	 */
	public static User take() throws InterruptedException{
		return queue.take();
	}
	/**
	 * �����������ݵ����ݿ�
	 */
	public static void saveAll(){
		DBhelper db=DBhelper.getInstance();
		Gson gson=new Gson();
		User u=null;
		Main.tip.setText("��ʼ�洢״̬!");
		while((u=queue.poll())!=null){
			String jsonString=gson.toJson(u);
			db.addQueue(jsonString);
		}
		System.out.println("�洢״̬�ɹ�!");
		Main.tip.setText("�洢״̬�ɹ�!");
	}
	/**
	 * �����ݿ�ָ�����
	 */
	public static void load() {
		DBhelper db=DBhelper.getInstance();
//		Gson gson=new Gson();
		List<String> result=db.loadQueue();
//		for (String string : result) {
//			User u=gson.fromJson(string, User.class);
//			try {
//				queue.put(u);
//				//System.out.println("add:"+u.getName());
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
		System.out.println("�ָ�״̬�ɹ�!");
		Main.tip.setText("�ָ�״̬�ɹ�!");
	}
}
