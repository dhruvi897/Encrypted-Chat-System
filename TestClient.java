import java.net.*;  
import java.io.*;
import java.util.Base64;
class TestClient
{  
	public static void main(String args[]) throws Exception
	{  
		
		try
		{
			RSA obj=new RSA();
			obj.Keygenerate();
			Socket s=new Socket("localhost",3333);  
			DataInputStream din=new DataInputStream(s.getInputStream());  
			DataOutputStream dout=new DataOutputStream(s.getOutputStream());  
			BufferedReader br=new BufferedReader(new InputStreamReader(System.in));  
			  
			String pk1="",str="",str2=""; 
			String pubkey=obj.getpubkey();
        	String privkey=obj.getprivkey();
			dout.writeUTF(pubkey);
			pk1=din.readUTF();
			System.out.println("Eve's public key is: "+pk1);
			while(true)
			{  
				int flag=0;
				try
				{
					str=br.readLine(); 
					String encryptedString = Base64.getEncoder().encodeToString(obj.encrypt(str, pk1)); 
					dout.writeUTF(encryptedString);  
					dout.flush();  
					str2=din.readUTF();
					System.out.println(str2);
					System.out.println("Want to Decrypt Message? Y/N");
                	String ans=br.readLine();
                	if(ans.equalsIgnoreCase("Y")) 
                	{ 
						String msg=obj.decrypt(str2,privkey);
						System.out.println("Eve says: "+msg); 
						if(msg.equals("stop"))
						flag=1; 
					}
					
				}
				catch(Exception e){}
				if(flag==1)
					break;
			}  
			dout.flush();  
			din.close();
			dout.close();  
			s.close(); 
		}
		catch(Exception e)
		{
			e.printStackTrace();
		} 
	}
}  
