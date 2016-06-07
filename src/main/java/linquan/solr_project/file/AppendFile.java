package linquan.solr_project.file;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.util.Random;

/**
 *
 * @author malik
 * @version 2011-3-10 下午10:49:41
 */
public class AppendFile {
	
	public static void method1(String file, String conent) {   
        BufferedWriter out = null;   
        try {   
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));   
            out.write(conent);   
        } catch (Exception e) {   
            e.printStackTrace();   
        } finally {   
            try {   
            	if(out != null){
            		out.close();   
                }
            } catch (IOException e) {   
                e.printStackTrace();   
            }   
        }   
    }   
  
    /**  
     * 追加文件：使用FileWriter  
     *   
     * @param fileName  
     * @param content  
     */  
    public static void method2(String fileName, String content) { 
    	FileWriter writer = null;
        try {   
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件   
            writer = new FileWriter(fileName, true);   
            writer.write(content);     
        } catch (IOException e) {   
            e.printStackTrace();   
        } finally {   
            try {   
            	if(writer != null){
            		writer.close();   
            	}
            } catch (IOException e) {   
                e.printStackTrace();   
            }   
        } 
    }   
  
    /**  
     * 追加文件：使用RandomAccessFile  
     *   
     * @param fileName 文件名  
     * @param content 追加的内容  
     */  
    public static void method3(String fileName, String content) { 
    	RandomAccessFile randomFile = null;
        try {   
            // 打开一个随机访问文件流，按读写方式   
            randomFile = new RandomAccessFile(fileName, "rw");   
            // 文件长度，字节数   
            long fileLength = randomFile.length();   
            // 将写文件指针移到文件尾。   
            randomFile.seek(fileLength);   
            randomFile.writeBytes(content);    
        } catch (IOException e) {   
            e.printStackTrace();   
        } finally{
        	if(randomFile != null){
        		try {
					randomFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        }
    }  

	public static void main(String[] args) {
		try{
			String filepath = "E://test1.json";
			int id = 15015;
			//String [] location = {"CNBJ*","CNSH*","CNTJ*"};
			String location = getL();
			//System.out.println(location);
			String random_location  = "CN"+testRandom1()+"*";
			String text = "";
			File file = new File(filepath);
			/*if(file.createNewFile()){
				System.out.println("Create file successed");
			}*/
			String flag = "";
			for(int i=0;i<1000;i++){
				id++;
				location = getL();
				if(i!=9999) flag = ",";
				else flag = "";
				text = "{\"id\": "+id+",\"cmd\": 0,\"orderId\": 592,\"priority\": 1,\"startTimestamp\": 1461081600,\"endTimestamp\": 1462031999,\"timePreference\": 1,\"speedupControl\": 1,\"method\": {\"dayBudget\": 1000000,\"budget\": 1000000},\"bidInfo\": {\"dynamicBid\": 0,\"bidTarget\": 3,\"optimizeTarget\": 4},\"status\": 1,\"channelId\": [1],\"bannerGroupId\": 5012,\"extraEffectIndicators\": 0,\"adNetworkId\": [\"1\"],\"price\": 1000000,\"ControlledMask\": 8,\"deliveryType\": 3,\"bannerId\": [17083,16422],\"positioningOrder\": {\"positioningOrder_fileName\": \"target/v.5012\",\"positioningOrder_cmd\": 0,\"positioningOrder_content\": \"/1/1/400,/1/1/404,/1/0/0,/1/0/9\"},\"contentTargetFlag\": 1,\"contentTarget\": \"(/1/A01B00C00v2012.1/0,/1/A02B00C00v2012.1/0,/1/A01B01C00v2012.1/0,/1/A01B02C00v2012.1/0,/1/A01B03C00v2012.1/0)\",\"retargetingFlag\": 1,\"retargeting\": \"MXwxMzl8VC0wMDAxMzktMDF8QWxsU2l0ZVZpc2l0b3I=\",\"locationFlag\": 1,\"location\": "+location+",\"whiteList\": [\"*fashion.ifeng.com\",\"*ipush.uudoudou.com\",\"*jiangsu.china.com.cn\",\"*mobile.zol.com.cn\",\"*www.zybang.com\"],\"PMPDealId\": \"qatestMM\",\"device\": {\"device_fileName\": \"target/d.5012\",\"device_cmd\": 0,\"device_content\": \"/1/1/2,/1/1/1,/1/1/3,/1/1/0,/1/2/2\"},\"tagIds\": [\"1/326-24\"]}"+flag+"\n";
				method3(filepath, text);
				
			}
			
			
			
			
			//method1(filepath, "123");
			//method2(filepath, "123");
			//method3(filepath, "123");
		}catch(Exception e){
			System.out.println(e);
		}
		System.out.println("end");
	}
	
	public static String getL() throws Exception{
		
		String  location = "[\"";
		for( int i = 0; i < 2; i ++) {
			if(i!=0) location += "\",\"";
			location += "CN"+testRandom1()+"*";
		}
		location += "\"]";
		return location;
	}
	
	public static String testRandom1() throws Exception {
		String s = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		char[] c = s.toCharArray();
		Random random = new Random();
		String str = "";
		for (int i = 0; i < 2; i++) {
			str += String.valueOf(c[random.nextInt(c.length)]);
			// System.out.println(c[random.nextInt(c.length)]);
		}
		return str;
	}
}
