package linquan.solr_project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.LBHttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

public class SolrCloudTest {
	public static final Log LOG = LogFactory.getLog(SolrCloudTest.class);
	private static CloudSolrClient cloudSolrClient;
	private static LBHttpSolrClient solrClient;

	private static synchronized CloudSolrClient getCloudSolrServer(final String zkHost) {
		LOG.info("connection to : " + zkHost + "\n");
		if (cloudSolrClient == null) {
			cloudSolrClient = new CloudSolrClient(zkHost);
		}
		return cloudSolrClient;
	}

	private void addIndex(SolrClient solrClient) {
		try {
			Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
			for (int i = 0; i <= 2; i++) {
				SolrInputDocument doc = new SolrInputDocument();
				String key = "";
				key = String.valueOf(i);
				doc.addField("id", key);
				doc.addField("test_s", key + "value");
				docs.add(doc);
			}
			LOG.info("docs info:" + docs + "\n");
			solrClient.add(docs);
			solrClient.commit();
		} catch (SolrServerException e) {
			System.out.println("Add docs Exception !!!");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Unknowned Exception!!!!!");
			e.printStackTrace();
		}
	}

	private void addIndex(SolrClient solrClient, SolrInputDocument doc) {
		try {
			Collection<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
			docs.add(doc);
			LOG.info("docs info:" + docs + "\n");
			solrClient.add(docs);
			solrClient.commit();
		} catch (SolrServerException e) {
			System.out.println("Add docs Exception !!!");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Unknowned Exception!!!!!");
			e.printStackTrace();
		}
	}

	public void search(CloudSolrClient cloudSolrClient, String Str) {
		SolrQuery query = new SolrQuery();
		query.setRows(100);
		query.setQuery(Str);

		try {
			// LOG.info("query string: "+ Str);
			// System.out.println("query string: "+ Str);
			QueryResponse response = cloudSolrClient.query(query);
			SolrDocumentList docs = response.getResults();
			// System.out.println(docs);
			// System.out.println(docs.size());
			System.out.println("doc num:" + docs.getNumFound());
			System.out.println("elapse time:" + response.getQTime());
			/*
			 * for (SolrDocument doc : docs) { String area = (String)
			 * doc.getFieldValue("test_s"); String id = (String)
			 * doc.getFieldValue("id"); System.out.println("id: " + id);
			 * System.out.println("tt_s: " + area); System.out.println(); }
			 */
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void search123(CloudSolrClient cloudSolrClient, String Str) {
		SolrQuery query = new SolrQuery();
		query.setRows(20);
		query.setFilterQueries("timePreference:"+getNumRandom(2)
						+" AND speedupControl:"+getNumRandom(2)
						+" AND priority:"+getNumRandom(8) );
		//query.setQuery("id:"+getNumRandom(30000));
		query.setQuery("cmd:"+getNumRandom(3));
		//query.setQuery("priority:"+getNumRandom(10));
		query.setFields("id,price");
		//query.setSort("price", "");
		
		try {
			// LOG.info("query string: "+ Str);
			// System.out.println("query string: "+ Str);
			QueryResponse response = cloudSolrClient.query(query);
			SolrDocumentList docs = response.getResults();
			// System.out.println(docs);
			// System.out.println(docs.size());
			//System.out.println("doc num:" + docs.size());
			//System.out.println("doc num:" + docs.getNumFound());
			//System.out.println("elapse time:" + response.getQTime());
			//CountUtil.response.update(response.getQTime());
			
			/*for (SolrDocument doc : docs) { 
			    String area = (String)
			    doc.getFieldValue("test_s"); 
			    String id = (String) doc.getFieldValue("id"); 
			 
			}*/
						 
		}  catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static int getNumRandom(int ret){
		return  Math.abs(new Random().nextInt() % ret);
	}
	

	public void deleteSolrData(String solrUrl) {
		try {
			solrClient = new LBHttpSolrClient(solrUrl);
			UpdateResponse res = solrClient.deleteByQuery("*:*");
			System.out.println(res.getStatus());
			solrClient.commit();
			LOG.info("Delete Success!");
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {
		//final String solrUrl = "http://10.0.3.170:8983/solr/mycol";
		final String zkHost = "10.0.3.170:2181,10.0.3.116:2181/solr";
		//final String defaultCollection = "mycol";
		final String defaultCollection = "guzi_solr_collection";
		final int zkClientTimeout = 10000;
		final int zkConnectTimeout = 10000;
		CloudSolrClient cloudSolrClient = getCloudSolrServer(zkHost);
		cloudSolrClient.setDefaultCollection(defaultCollection);
		cloudSolrClient.setZkClientTimeout(zkClientTimeout);
		cloudSolrClient.setZkConnectTimeout(zkConnectTimeout);

		long start = System.currentTimeMillis();
		long end = System.currentTimeMillis();
		int sum = 20000;
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
		SolrCloudTest solrCloudTest = new SolrCloudTest();
		SolrInputDocument doc = null;
		try {
			/*
			 * for(int i=0;i<sum;i++){ doc = new SolrInputDocument(); String key
			 * = ""; key = String.valueOf(i); doc.addField("id", key);
			 * doc.addField("test_s", key + "value");
			 * solrCloudTest.addIndex(cloudSolrClient,doc); System.out.println(
			 * "sleep --> "+i); Thread.sleep(1000); doc = null; }
			 */

		} catch (Exception e) {
			e.printStackTrace();
		}

		for (int i = 0; i < sum; i++) {
			start = System.currentTimeMillis();
			// solrCloudTest.search(cloudSolrClient, "id:*");
			solrCloudTest.search123(cloudSolrClient, String.valueOf(i));
			//end = System.currentTimeMillis();
			//System.out.println("cost-->"+(System.currentTimeMillis()-start)+"ms");
			CountUtil.updateHistogram(System.currentTimeMillis()-start);
			//if (i != 0) total += System.currentTimeMillis() - start;
		}
		//System.out.println("avg_cost-->" + (total / (sum - 1)) + "ms");
		System.out.println(CountUtil.getSnapshot(CountUtil.avg.getSnapshot()));
		//System.out.println(CountUtil.getSnapshot(CountUtil.response.getSnapshot()));
		// solrCloudTest.deleteSolrData(solrUrl);
		cloudSolrClient.close();
		System.out.println("end");
	}
}