package linquan.solr_project.tool;

import java.util.Random;

import org.apache.solr.client.solrj.SolrQuery;

public class SolrSeacher {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		for(int i=0;i<100;i++){
			System.out.println(getNumRandom(3));
		}
	}
	
	public static int getNumRandom(int ret){
		return  Math.abs(new Random().nextInt() % ret);
	}
	
	public SolrQuery randomSeacher() {
		SolrQuery query = new SolrQuery();
		query.setRows(20);
		query.setFilterQueries("deliveryType:"+getNumRandom(5)+" AND locationFlag:"+getNumRandom(3)+"");
		//query.setQuery("id:"+Str);
		query.setQuery("adNetworkId:"+getNumRandom(50)+"");
		return query;
	}
	

}
