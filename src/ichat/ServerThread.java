package ichat;
import java.awt.List;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

// ������ÿ���߳�ͨ�ŵ��߳���
public class ServerThread implements Runnable
{
	String u ;//��½���˺�ID
	ArrayList<Map<String,String>> friendList = new ArrayList<>();//������ϵ���б�    
	// ���嵱ǰ�߳��������Socket
	Socket s = null;
	// ���߳��������Socket����Ӧ��������
	BufferedReader br = null;
	String[] getStr = null;
	OutputStream os = null;
	JSONObject jsonReply = new JSONObject();
	JSONArray jsonA;
	Database masterDB = new Database();
	
	public ServerThread (Socket s) throws IOException
	{
		this.s = s;
		// ��ʼ����Socket��Ӧ��������
		os = s.getOutputStream();
		br = new BufferedReader(new InputStreamReader(s.getInputStream() , "utf-8"));   // ��  
	}
	public void run()
	{			
		String Data = null;
		// ����ѭ�����ϴ�Socket�ж�ȡ�ͻ��˷��͹���������
		while ((Data = readFromClient()) != null)
			{
		        System.out.println(Data);
		        JSONObject jsonObj = JSONObject.fromObject(Data);  
		        String Flag = jsonObj.getString("Flag");
		        //��½������
		        if(Flag.equals("Login")){
					try {
						jsonReply.put("Flag","Login");
						masterDB.connSQL();
						String sql = "select * from user_table where username = '" + jsonObj.getString("ID") 
								+ "' and password = '" + jsonObj.getString("PW") + "';";  
						ResultSet resultSet = masterDB.selectSQL(sql);  
						if(resultSet.next() == true){
							jsonReply.put("FlagSign","Yes");
							//�����ID��socketList�б�
							MyServer.socketList.put(jsonObj.getString("ID"), s);
							u = jsonObj.getString("ID");
							System.out.println(MyServer.socketList);
						}							
						else
							jsonReply.put("FlagSign","No");
						masterDB.closeSQL();
						System.out.println(jsonReply);
						os.write((jsonReply+ "\n").getBytes("utf-8"));
						os.flush();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (SQLException e) {
						e.printStackTrace();
					}

				}
		        //ע��������
		        else if (Flag.equals("Regist")){
					try {
						jsonReply.put("Flag","Regist");
						masterDB.connSQL();
						String sql = "insert into user_table(username,password) values('" 
						+ jsonObj.getString("ID") + "','" + jsonObj.getString("PW") + "');";   
						if(masterDB.insertSQL(sql) == true)
							jsonReply.put("FlagSign","Yes");			
						else
							jsonReply.put("FlagSign","No");
						masterDB.closeSQL();						
						System.out.println(jsonReply);
						os.write((jsonReply+ "\n").getBytes("utf-8"));
						os.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
		        }
		        //��ϵ���б�List����
		        else if (Flag.equals("List")){
					try {
			        	jsonReply.put("Flag","List");
			        	//��ȡsocketList�е�key
			    		Iterator<String> friend = MyServer.socketList.keySet().iterator();
			    		jsonA = new JSONArray();
			    		while(friend.hasNext()){	
			    			Map<String,String> friendData = new HashMap<>();
			    			friendData.put("text", friend.next());
			    			jsonA.add(friendData);
			    		}
//			    		System.out.println("frendList:"+friendList);
			        	jsonReply.put("FriendList", jsonA);
						System.out.println(jsonReply);
						os.write((jsonReply+ "\n").getBytes("utf-8"));
						os.flush();
						jsonA = null;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
		        }
		        //����������
		        else{
					String content = jsonObj.getString("Content");
					System.out.println(u+" send to "+jsonObj.getString("SendTo")+":" + content);
					try{
						//��Ϣת��
						s = MyServer.socketList.get(jsonObj.getString("SendTo"));
						os = s.getOutputStream();
						os.write((jsonObj + "\n").getBytes("utf-8"));
						os.flush();
						//�����¼�����ݿ�
						masterDB.connSQL();
						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�������ڸ�ʽ
						System.out.println(df.format(new Date()));// new Date()Ϊ��ȡ��ǰϵͳʱ��
						String sql = "insert into chat_table(src_user,dest_user,content,time) values('" 
								+ u + "','" + jsonObj.getString("SendTo") + 
								"','" + jsonObj.getString("Content") +
								"','" + df.format(new Date())+"');";   
						if(masterDB.insertSQL(sql) == true)
							System.out.println("chat_table insert succeed");							
						else
							System.out.println("chat_table insert fail");
						masterDB.closeSQL();
						//socket�ص�ԭ��½��ID
						s = MyServer.socketList.get(u);
						os = s.getOutputStream();						
					}catch(Exception e){						
						// ɾ����Socket��
						System.out.println(MyServer.socketList);
						System.out.println("�Է�������");
						e.printStackTrace();
					}
				}
			}
		// ɾ����Socket��
		MyServer.socketList.remove(u); 
		System.out.println("socket�ѶϿ�");
		System.out.println(MyServer.socketList);
	}
	 
	private String readFromClient()
	{
		try
		{
			return br.readLine();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
