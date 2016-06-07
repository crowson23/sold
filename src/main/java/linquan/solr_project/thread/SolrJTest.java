package linquan.solr_project.thread;
import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.apache.solr.client.solrj.*;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.impl.BinaryRequestWriter;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;

public class SolrJTest {
    //多线程共享
    static HttpSolrClient server;

    @BeforeClass
    public static void beforeClassClass() throws Exception {
        server = new HttpSolrClient(
                "http://localhost:8088/solr");
        server.setSoTimeout(1000);
        server.setConnectionTimeout(100);
        server.setDefaultMaxConnectionsPerHost(100);
        server.setMaxTotalConnections(100);
        server.setFollowRedirects(false);
        server.setAllowCompression(true);
        server.setMaxRetries(1);
        
        //提升性能采用流输出方式
        server.setRequestWriter(new BinaryRequestWriter());
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    	
    }
    
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {

    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void QueryTest() throws SolrServerException {
        SolrQuery query = new SolrQuery();
        
        //主查询
        query.setQuery("*:*");
        
        //采用过滤器查询可以提高性能
        
        query.addFilterQuery("Searchable:1");
        query.addFilterQuery("SpaceAccessPrivate:1");
        
        //区间查询
        query.addFilterQuery("Age:[* TO 50]");
        query.addFilterQuery("FriendType:1");
        
        //添加排序
        query.addSort("Score", ORDER.desc);
        
        //分页返回结果
        query.setStart(0);
        query.setRows(10);
        QueryResponse rsp;
		try {
			rsp = server.query(query);
			List<SpaceFriend> beans = rsp.getBeans(SpaceFriend.class);
	        
	        //输出符合条件的结果数
	        System.out.println("NumFound: " + rsp.getResults().getNumFound());
	        
	      //输出结果
	        for (int i = 0; i < beans.size(); i++) {
	            SpaceFriend friend = beans.get(i);
	            
	            System.out.println("SpaceFriend " + i);
	            System.out.println("Id " + friend.id);
	            System.out.println("WebNickname:" + friend.webNickname);
	        }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        
    }
}