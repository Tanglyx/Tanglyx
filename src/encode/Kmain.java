package encode;
import java.io.BufferedReader;
import java.security.Key;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.*;

public class Kmain {
	public static void main(String[] args) throws Exception {
			// TODO Auto-generated method stub
			HashMap<String, Object> map = RSA.getKeys();
			//����RSA��Կ��˽Կ
			RSAPublicKey publicKey = (RSAPublicKey) map.get("public");
			RSAPrivateKey privateKey = (RSAPrivateKey) map.get("private");
			
			//ģ
			String modulus = publicKey.getModulus().toString();
			//��Կָ��
			String public_exponent = publicKey.getPublicExponent().toString();
			//˽Կָ��
			String private_exponent = privateKey.getPrivateExponent().toString();
			//����AES��Կ
			byte[] key = AES.initSecretKey();
		    System.out.println("AES��key��"+AES.showByteArray(key));
		    Key k = AES.toKey(key); //������Կ
		       
		    //��AES��Կת��Ϊ������   
		    String binKey=AES.toBinary(key);
		    int binLen=binKey.length();
		    System.out.println("���ɵ�aes��������Կ"+binKey);
		    System.out.println("���ɵ�aes��������Կ����"+binLen);
					       
		     String mingorigin=binKey;  //AES�Ķ����Ʊ�ʾ
			//String mingorigin = "12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678";
			//δʹ�õ�temp���������ԱȽ��ܺ�������ļ�
			//String temp     = "12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678";
			//�з�Ҫ�������Կ,��128λ��Կ�з�Ϊ������Կ
			int len=mingorigin.length();
			//��ʾԭʼ���ݵĳ���
			System.out.println("Ҫ�����ַ�������Ϊ��"+len);
			String s1="";
			String s2="";
			if (len>117)
			{
				s1 = mingorigin.substring(0,117);
				s2 = mingorigin.substring(117,len);
				//ʹ��ģ��ָ�����ɹ�Կ��˽Կ
				RSAPublicKey pubKey = RSA.getPublicKey(modulus, public_exponent);
				RSAPrivateKey priKey = RSA.getPrivateKey(modulus, private_exponent);
				System.out.println("RSA��Կ��"+pubKey);
				
				//RSA��Կ��AES��Կkey���ܺ������
				String mi1 = RSA.encryptByPublicKey(s1, pubKey);
				String mi2 = RSA.encryptByPublicKey(s2, pubKey);
				System.err.println("RSA��Կ����AES��Կ��"+mi1+mi2);
				//��RSA˽Կ���ܺ������
				String mingsecond1 = RSA.decryptByPrivateKey(mi1, priKey);
				String mingsecond2 = RSA.decryptByPrivateKey(mi2, priKey);
				String mingsecond=mingsecond1+mingsecond2;
				System.err.println("��RSA˽Կ���ܺ������Ϊ��"+mingsecond);
			}
			//������ԿС��128λ�Ĵ���ʽ�������
			else 
			{
				System.out.println("Ҫ�����ַ�������Ϊ��"+len);
				System.err.println("��ԿС��128λ");
			}
				
			
			
			//AES���ܽ���
//			   byte[] key = AES.initSecretKey();
//		       System.out.println("key��"+AES.showByteArray(key));
//		       Key k = AES.toKey(key); //������Կ
//		       
//		       
//		       String binKey=AES.toBinary(key);
//		       int binLen=binKey.length();
//		       System.out.println("���ɵ�aes��������Կ"+binKey);
//		       System.out.println("���ɵ�aes��������Կ����"+binLen);
		       
		       
		       System.out.println("****************������AES����*****************");
		       String data ="Ҫ���ܵ�����";
		       System.out.println("����ǰ����: string:"+data);
		       System.out.println("����ǰ����: byte[]:"+AES.showByteArray(data.getBytes()));
		       System.out.println();
		       byte[] encryptData = AES.encrypt(data.getBytes(), k);//���ݼ���
		       System.out.println("���ܺ�����: byte[]:"+AES.showByteArray(encryptData));
//		       System.out.println("���ܺ�����: hexStr:"+Hex.encodeHexStr(encryptData));
		       System.out.println();
		       byte[] decryptData = AES.decrypt(encryptData, k);//���ݽ���
		       System.out.println("���ܺ�����: byte[]:"+AES.showByteArray(decryptData));
		       System.out.println("���ܺ�����: string:"+new String(decryptData));
		}
}

