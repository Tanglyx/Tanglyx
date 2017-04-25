package ichat;
import java.net.*;
import java.io.*;
import java.util.*;

public class MyServer
{
	// ���屣������Socket��ArrayList
	public static Map<String,Socket> socketList = new HashMap<>();

    public static void main(String[] args)
		throws IOException
    {
		ServerSocket ss = new ServerSocket(12345);
		while(true)
		{
			// ���д������������һֱ�ȴ����˵�����
			Socket s = ss.accept();
			System.out.println("new socket come in");
			// ÿ���ͻ������Ӻ�����һ��ServerThread�߳�Ϊ�ÿͻ��˷���
			new Thread(new ServerThread(s)).start();
		}
    }
}
