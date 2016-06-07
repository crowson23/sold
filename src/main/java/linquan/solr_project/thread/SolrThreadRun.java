package linquan.solr_project.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import linquan.solr_project.CountUtil;


public class SolrThreadRun {
	
	public static final Log LOG = LogFactory.getLog(SolrThreadRun.class);

	final static int num = 16;
	final static int total_sub = 10000;
	public static void main(String[] args) {
		long c = System.currentTimeMillis();
		ExecutorService threadPool = Executors.newFixedThreadPool(num);
		for (int i = 0; i < num; i++) {
			threadPool.execute(new SolrClient(i,total_sub));
		}
		
		threadPool.shutdown();
		try {
			while(!threadPool.awaitTermination(10000, TimeUnit.MILLISECONDS)){
				System.out.println("wait for threadPool shutdown!");
				System.out.println("now-->"+CountUtil.avg.getCount());
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("total-->"+CountUtil.avg.getCount());
		System.out.println(CountUtil.getSnapshot(CountUtil.avg.getSnapshot()));
		System.out.println(CountUtil.getSnapshot(CountUtil.hit.getSnapshot()));
		System.out.println((System.currentTimeMillis()-c));
	}

}
