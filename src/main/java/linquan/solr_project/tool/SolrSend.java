package linquan.solr_project.tool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.common.SolrInputDocument;

public class SolrSend {
	
	public static final Log LOG = LogFactory.getLog(SolrSend.class);
	public static int num = 1;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SolrSend s = new SolrSend();
		SolrCloudTool tool = new SolrCloudTool();
		
		final String solrUrl = "http://10.0.3.170:8983/solr/mycol";
		final String zkHost = "10.0.3.170:2181,10.0.3.116:2181/solr";
		//final String defaultCollection = "mycol";
		final String defaultCollection = "guzi_solr_collection";
		
		final int zkClientTimeout = 10000;
		final int zkConnectTimeout = 10000;
		CloudSolrClient cloudSolrClient = tool.getCloudSolrServer(zkHost);
		cloudSolrClient.setDefaultCollection(defaultCollection);
		cloudSolrClient.setZkClientTimeout(zkClientTimeout);
		cloudSolrClient.setZkConnectTimeout(zkConnectTimeout);

		long start = System.currentTimeMillis();
		
		int sum = 80000;
		long total = 0l;
		
		try {
			cloudSolrClient.connect();
			System.out.println("connect solr cloud zk sucess");
		} catch (Exception e) {
			LOG.error("connect to collection " + defaultCollection + " error\n");
			System.out.println("error message is:" + e);
			e.printStackTrace();
			System.exit(1);
		}
		SolrInputDocument doc = null;
		Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
		try {
			for (int i = 0; i < sum; i++) {
				doc = s.mokeSolution();
				docs.add(doc);
				//System.out.println("sleep --> " + i);
				//Thread.sleep(1000);
				doc = null;
			}
			tool.addIndex(cloudSolrClient, docs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			cloudSolrClient.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.println("end--->cost--->"+(end-start));
		
		
	}
	
	
	public SolrInputDocument mokeSolution(){
		SolrInputDocument doc = new SolrInputDocument();
		String key = "";
		num = num + 1; 
		key = String.valueOf(num);
		doc.addField("id", key);
		doc.addField("cmd", getNumRandom(4));
		doc.addField("orderId", getNumRandom(1000));
		doc.addField("priority", getNumRandom(10));
		doc.addField("startTimestamp", System.currentTimeMillis()/1000);
		doc.addField("endTimestamp", System.currentTimeMillis()/1000);
		doc.addField("timePreference", getNumRandom(2));
		doc.addField("speedupControl", getNumRandom(2));
		//doc.addField("method", "{\"dayBudget\": 1000000,\"budget\": 1000000}");
		doc.addField("bidInfo", "{\"dynamicBid\": 0,\"bidTarget\": 3,\"optimizeTarget\": 4}");
		doc.addField("status", getNumRandom(2));
		doc.addField("channelId", getNumRandom(10000));
		doc.addField("bannerGroupId", getNumRandom(1000));
		doc.addField("extraEffectIndicators", getNumRandom(100));
		doc.addField("adNetworkId", getNumRandom(10));
		doc.addField("price", getNumRandom(100000));
		doc.addField("ControlledMask", getNumRandom(15));
		doc.addField("deliveryType", getNumRandom(7));
		doc.addField("bannerId", getNumRandom(15000));
		//doc.addField("positioningOrder", "{\"positioningOrder_fileName\": \"target/v.5012\",\"positioningOrder_cmd\": 0,\"positioningOrder_content\": \"/1/1/400,/1/1/404,/1/0/0,/1/0/9\"}");
		doc.addField("contentTargetFlag", getNumRandom(3));
		//doc.addField("contentTarget", "(/1/A01B00C00v2012.1/0,/1/A02B00C00v2012.1/0,/1/A01B01C00v2012.1/0,/1/A01B02C00v2012.1/0,/1/A01B03C00v2012.1/0)");
		doc.addField("retargetingFlag", getNumRandom(3));
		doc.addField("retargeting", "MXwxMzl8VC0wMDAxMzktMDF8QWxsU2l0ZVZpc2l0b3I=");
		doc.addField("locationFlag", getNumRandom(3));
		doc.addField("location", getLocations(3));
		doc.addField("whiteList", getWhiteList());
		doc.addField("PMPDealId", "qatestMM");
		//doc.addField("device", "{\"device_fileName\": \"target/d.5012\",\"device_cmd\": 0,\"device_content\": \"/1/1/2,/1/1/1,/1/1/3,/1/1/0,/1/2/2\"}");
		doc.addField("tagIds", new String[]{"1/326-24"});
		
		return doc;
	}
	
	public int getNumRandom(int ret){
		return  Math.abs(new Random().nextInt() % ret);
	}
	
	public String locationRandom() throws Exception {
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
	
	public String[] getLocations(int total){
		String[] aArray = new String[total];
		for(int i = 0;i<total;i++){
			try {
				aArray[i] = "\"CN"+locationRandom()+"*\"";
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return aArray;
	}
	
	public String[] getWhiteList(){
		String[] aArray = new String[5];
		aArray[0] = "*fashion.ifeng.com";
		aArray[1] = "*ipush.uudoudou.com";
		aArray[2] = "*jiangsu.china.com.cn";
		aArray[3] = "*mobile.zol.com.cn";
		aArray[4] = "*www.zybang.com";
		return aArray;
	}

}
