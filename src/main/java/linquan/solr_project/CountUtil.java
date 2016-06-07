package linquan.solr_project;

import com.codahale.metrics.Histogram;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Snapshot;

public class CountUtil {

	/**
     * 实例化一个registry，最核心的一个模块，相当于一个应用程序的metrics系统的容器，维护一个Map
     */
    public static final MetricRegistry metrics = new MetricRegistry();
    
    public static final Meter requestsTotal = metrics.meter("total");
    
    public static final Histogram avg = metrics.histogram("avg");
    
    public static final Histogram response = metrics.histogram("response");
    
    public static final Histogram hit = metrics.histogram("hit");

    public static void handleRequest() {
    	requestsTotal.mark();
    }
    
    public static void updateHistogram(long l) {
    	avg.update(l);
    }
    
    public static String TotalMsg(){
    	String result = "";
    	result += "requestsTotal-->"+requestsTotal.getCount()+"||";
    	return result;
    }
    
    public static String getSnapshot(Snapshot s){
    	String result =  "";
    	result += "10%-->" + s.getValue(0.10) + "\n";
    	result += "30%-->" + s.getValue(0.30) + "\n";
    	result += "50%-->" + s.getValue(0.50) + "\n";
    	result += "95%-->" + s.get95thPercentile() + "\n";
    	result += "97%-->" + s.getValue(0.97) + "\n";
    	result += "98%-->" + s.getValue(0.98) + "\n";
    	result += "98.9%-->" + s.getValue(0.989) + "\n";
    	result += "99%-->" + s.get99thPercentile() + "\n";
    	result += "99.9%-->" + s.get999thPercentile() + "\n";
    	result += "Median-->" + s.getMedian() + "\n";
    	result += "Mean-->" + s.getMean() + "\n";
    	return result;
    }
}
