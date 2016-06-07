package linquan.solr_project.thread;

import java.io.IOException;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.impl.CloudSolrClient;

import linquan.solr_project.CountUtil;

public class SolrClient extends Thread {
	public static final Log LOG = LogFactory.getLog(SolrClient.class);
	private int thread_num;
	private SolrCloudTool solrCloudTool = new SolrCloudTool();
	public int total = 100;

	public SolrClient(int thread_num,int total_num) {
		this.thread_num = thread_num;
		this.total = total_num;
	}

	public void run() {
		final String solrUrl = "http://10.0.3.170:8983/solr/guzi_solr_collection";
		final String zkHost = "10.0.3.170:2181,10.0.3.116:2181/solr";
		// final String defaultCollection = "mycol";
		final String defaultCollection = "guzi_solr_collection";
		final int zkClientTimeout = 10000;
		final int zkConnectTimeout = 10000;
		CloudSolrClient cloudSolrClient = solrCloudTool.getCloudSolrServer(zkHost);
		cloudSolrClient.setDefaultCollection(defaultCollection);
		cloudSolrClient.setZkClientTimeout(zkClientTimeout);
		cloudSolrClient.setZkConnectTimeout(zkConnectTimeout);

		long start = System.currentTimeMillis();

		try {
			cloudSolrClient.connect();
			System.out.println("connect solr cloud zk sucess");
		} catch (Exception e) {
			LOG.error("connect to collection " + defaultCollection + " error\n");
			System.out.println("error message is:" + e);
			e.printStackTrace();
			System.exit(1);
		}

		SolrQuery query = null;
		int type = 0 ;
		int avg_total = 0;
		for (int i = 0; i < total; i++) {
			query = new SolrQuery();
			start = System.currentTimeMillis();
			
			getSolrQuery( type , query );
			
			/*query.setRows(20);
			query.setFilterQueries("timePreference:" + getNumRandom(2) + " AND speedupControl:" + getNumRandom(2));
					//+ " AND priority:" + getNumRandom(8)+" AND deliveryType:"+getNumRandom(5));
					//+ " AND locationFlag:"+getNumRandom(3));
			//query.setQuery("id:"+getNumRandom(80000));
			//query.setQuery("cmd:" + getNumRandom(3));
			query.setQuery("priority:"+getNumRandom(10));
			query.setFields("id","price");
			query.setSort("price", ORDER.desc);*/
			
			solrCloudTool.search123(cloudSolrClient, query);
			// end = System.currentTimeMillis();
			// /System.out.println("cost-->" + (System.currentTimeMillis() -
			// start) + "ms");
			if (i != 0) {
				avg_total += System.currentTimeMillis() - start;
				CountUtil.updateHistogram(System.currentTimeMillis() - start);
			}

		}
		/*System.out.println("avg_cost-->" + (avg_total / (total - 1)) + "ms");
		System.out.println(CountUtil.getSnapshot(CountUtil.avg.getSnapshot()));
		System.out.println(CountUtil.getSnapshot(CountUtil.hit.getSnapshot()));*/
		// solrCloudTest.deleteSolrData(solrUrl);
		try {
			cloudSolrClient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("end" + Thread.currentThread()+"-->"+thread_num);
	}
	
	public static int getNumRandom(int ret){
		return  Math.abs(new Random().nextInt() % ret);
	}
	
	public SolrQuery getSolrQuery(int type ,SolrQuery query){
		//String timeQueryString = "id:"+getNumRandom(70000);
		String queryString = "priority:"+getNumRandom(10);
		String filterQueries = "timePreference:" + getNumRandom(2) + " AND speedupControl:" + getNumRandom(2);
		//queryString += "";
		String fields = "id,price";
		int rows = 40 ;
		if(type==1){
			query.set("q", queryString);
			query.set("fq", filterQueries);
			query.set("start", 0);
	        query.set("rows", rows);
	        query.set("sort", "price desc");
	        query.setHighlight(false);
	        query.set("qt", "/export");
	        query.setFields(fields);
		}else{
			query.set("q", queryString);
			query.set("fq", filterQueries);
			query.set("start", 0);
			query.set("rows", rows);
			query.setFields(fields);
		}
		return query;
	}

}