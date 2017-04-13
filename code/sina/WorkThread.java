package sina;

import java.util.List;

import javax.swing.JLabel;

import sina.JsonBean.CardsEntity;
import sina.JsonBean.CardsEntity.UserEntity;

import com.google.gson.Gson;

public class WorkThread extends Thread{
	public volatile boolean running=true;
	private HttpClient hc=new HttpClient();
	private DBhelper dBhelper=DBhelper.getInstance();
	private int currPage=1;
	private int maxPage=2;
	private boolean getFans;//��ǰ�û���˿�ȹ�ע��
	private Gson gson=new Gson();
	private JLabel label;
	WorkThread(JLabel label){
		this.label=label;
	}
	void tip(String t){
		label.setText(getName()+">>>>>>>>"+t);
	}
	@Override
	public void run() {
		while (running) {
			int count=0;
			int rcount=0;
			//ȡ��һ���û�
			User user = null;
			try {
				user = WorkQueue.take();
			} catch (InterruptedException e) {
				break;
			}
			tip("��ʼ��"+user.getName());
			getFans=user.getFans()<user.getFollowers();
			//��ҳ��ȡ��ǰ��˿���߹�ע
			for(currPage=1,maxPage=2;currPage<=maxPage;currPage++){
				String url=
						"http://m.weibo.cn/container/getSecond?containerid=100505"
						+user.getUid()+"_-_"+(getFans?"FANS":"FOLLOWERS")+(currPage!=1?("&page="+currPage):"");
				String json=null;
				int errorCount=0;
				while (errorCount<2) {
					try {
						json=hc.get(url);
					} catch (Exception e) {
						hc=new HttpClient();
					}
					if (json==null) {//400�˰�
						errorCount++;
						hc=new HttpClient();
						tip("[40x������]"+errorCount);
						try {
							sleep(60000*1);//����ôЪһ����
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}else {
						break;
					}
				}//while
				if(errorCount>1){
					running=false;
					System.out.println("400����2��");
					//���°�����û����ض���
					WorkQueue.queue.add(user);
					break;
					}//����400��Σ��ɴ�ص��߳�
				//String json=hc.get(url);
//				try {
//					Thread.sleep(1000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
				//����json
				JsonBean jb=gson.fromJson(json, JsonBean.class);
				if (jb==null) {
					continue;
				}
				if (currPage==1) {
					maxPage=jb.getMaxPage();
//					System.out.println("����maxPage"+maxPage);
					tip("����maxPage"+maxPage);
				}
				List<CardsEntity> cards;
				try {
					cards = jb.getCards();
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
				if(cards==null){continue;}
				int fansCount=0;
				for(CardsEntity c:cards){
					UserEntity ue=c.getUser();
					count++;
					//System.out.println(ue.getScreen_name());
					//�ȷ�װ��User
					User fans=new User();
					fans.setUid(String.valueOf(ue.getId()));
					fans.setName(ue.getScreen_name());
					fans.setGender(ue.getGender());
					fans.setAvatar(ue.getProfile_image_url());
					fans.setFans(ue.getFollowers_count());
					fans.setFollowers(ue.getFollow_count());
					fans.setLv(user.getLv()+1);
					//�����ж�Ҫ��Ҫ���뵽��������,������û���û�����˿������400
					if (fans.getFans()<400) {
						if(!dBhelper.haveUser(fans)){
							WorkQueue.add(fans);
							tip("������У�"+fans.getName());
						}
						//д���ϵ���ݱ�
						dBhelper.addRelation2DB(user.getUid(), fans.getUid());
						rcount++;
					}
					
					fansCount++;
				}//for each �û�
//				System.out.println(currPage+"========"+maxPage);
				tip("��ǰҳ��:"+currPage+"/"+maxPage);
				try {
					sleep(200*fansCount);
				} catch (Exception e) {
					
				}	
			}//for ��ҳ
//			System.out.println(count+"==>"+rcount);
			tip("����"+count+"==>�������"+rcount);
		}//while
		System.out.println("�����߳̽���:"+getName());
		tip("�߳̽�����");
	}//run
	
}//class
