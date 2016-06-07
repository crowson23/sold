package linquan.solr_project;

import java.io.IOException;
import java.util.Random;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;


public class SearchTest {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		SearchJobs search = new SearchJobs();
		SolrClient client = search.createSolrServer();
		long total = 0l;
		SolrQuery query = null;
		try {
			int id = getNumRandom(50000);
			String key = "CN";
			int sum = 10000;
			for(int i=0;i<sum;i++){
				long s = System.currentTimeMillis();
				//search.querytop20Test(String.valueOf(id));
				
				query = new SolrQuery();
				query.setRows(20);
				query.setFilterQueries("timePreference:"+getNumRandom(2)
								+" AND speedupControl:"+getNumRandom(2)
								+" AND priority:"+getNumRandom(8) );
				//query.setQuery("id:"+getNumRandom(30000));
				query.setQuery("cmd:"+getNumRandom(3));
				//query.setQuery("priority:"+getNumRandom(10));
				query.setFields("id,price");
				
				search.querytop20BySolrQuery(query);
				
				//search.querytop20Test(key+testRandom1()+"*");
				id++;
				//System.out.println("cost = "+ (System.currentTimeMillis()-s) + "ms");
				if(i!=0) {
					total += (System.currentTimeMillis()-s);
					CountUtil.updateHistogram(System.currentTimeMillis()-s);
				}
				
			}
			client.close();
			System.out.println("avg="+total/(sum-1));
			System.out.println(CountUtil.getSnapshot(CountUtil.avg.getSnapshot()));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
	
	public static int getNumRandom(int ret){
		return  Math.abs(new Random().nextInt() % ret);
	}

}
