package encode;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AES {

   public static final String KEY_ALGORITHM = "AES";
   public static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";//Ĭ�ϵļ����㷨

   public static byte[] initSecretKey() {

       //��������ָ���㷨��Կ�������� KeyGenerator ����
       KeyGenerator kg = null;
       try {
           kg = KeyGenerator.getInstance(KEY_ALGORITHM);
       } catch (NoSuchAlgorithmException e) {
           e.printStackTrace();
           return new byte[0];
       }
       //��ʼ������Կ��������ʹ�����ȷ������Կ��С
       //AES Ҫ����Կ����Ϊ 128
       kg.init(128);
       //����һ����Կ
       SecretKey  secretKey = kg.generateKey();
       return secretKey.getEncoded();
   }

   public static Key toKey(byte[] key){
       //������Կ
       return new SecretKeySpec(key, KEY_ALGORITHM);
   }

   public static byte[] encrypt(byte[] data,Key key) throws Exception{
       return encrypt(data, key,DEFAULT_CIPHER_ALGORITHM);
   }

   public static byte[] encrypt(byte[] data,byte[] key) throws Exception{
       return encrypt(data, key,DEFAULT_CIPHER_ALGORITHM);
   }

   public static byte[] encrypt(byte[] data,byte[] key,String cipherAlgorithm) throws Exception{
       //��ԭ��Կ
       Key k = toKey(key);
       return encrypt(data, k, cipherAlgorithm);
   }

   public static byte[] encrypt(byte[] data,Key key,String cipherAlgorithm) throws Exception{
       //ʵ����
       Cipher cipher = Cipher.getInstance(cipherAlgorithm);
       //ʹ����Կ��ʼ��������Ϊ����ģʽ
       cipher.init(Cipher.ENCRYPT_MODE, key);
       //ִ�в���
       return cipher.doFinal(data);
   }

   public static byte[] decrypt(byte[] data,byte[] key) throws Exception{
       return decrypt(data, key,DEFAULT_CIPHER_ALGORITHM);
   }

   public static byte[] decrypt(byte[] data,Key key) throws Exception{
       return decrypt(data, key,DEFAULT_CIPHER_ALGORITHM);
   }

   public static byte[] decrypt(byte[] data,byte[] key,String cipherAlgorithm) throws Exception{
       //��ԭ��Կ
       Key k = toKey(key);
       return decrypt(data, k, cipherAlgorithm);
   }

   public static byte[] decrypt( byte[] data,Key key,String cipherAlgorithm) throws Exception{
       //ʵ����
       Cipher cipher = Cipher.getInstance(cipherAlgorithm);
       //ʹ����Կ��ʼ��������Ϊ����ģʽ
       cipher.init(Cipher.DECRYPT_MODE, key);
       //ִ�в���
       return cipher.doFinal(data);
   }

   public static String  showByteArray(byte[] data){
	   
	   if(null == data){
           return null;
       }
       StringBuilder sb = new StringBuilder("{");
       String binStr2="";
       for(byte b:data){
           sb.append(b).append(",");
       }
       String binStr3=binStr2;
       System.out.print(binStr3);
       sb.deleteCharAt(sb.length()-1);
       sb.append("}");
       return sb.toString();
       
   }
   //������ת��Ϊ�������ַ���
public static String  toBinary(byte[] data){
	   
	   if(null == data){
           return null;
       }
      
       String binStr2="";
       for(byte b:data){  
    	   if(b>=0)
    	   {
    		   String binStr = Integer.toBinaryString(b);
    		   if(binStr.length()==8)
    			   binStr2=binStr2+binStr;
    		   else
    		   {
    			   int k=8-binStr.length();
    			   for(;k>0;k--)
    			   {
    				   binStr='0'+binStr;
    			   }
    			   binStr2=binStr2+binStr;
    		   }
    		  System.out.println(binStr);
    	   }
    	   else
    	   {
    		   String binStr = Integer.toBinaryString(b);
    		   //ȡ��8λ
    		   binStr=binStr.substring(24, 32);
    		   binStr2=binStr2+binStr;
    		  System.out.println(binStr);
    	   }
       }
      //System.out.print(binStr2);
       return binStr2;
       
   }

}