package linquan.solr_project.thread;
import org.apache.solr.client.solrj.beans.Field;

public class SpaceFriend {
    @Field("Id")
    int id;
    
    @Field("WebNickname")
    String webNickname;
    
    @Field("portrait")
    String Portrait;
    
    @Field("Gender")
    int gender;
    
    @Field("Age")
    int age;
    
    @Field("IsAgePublic")
    boolean isAgePublic;
    
    @Field("Horoscope")
    int horoscope;
    
    @Field("marriage")
    int marriage;
    
    @Field("FriendType")
    String[] friendType;
    
    @Field("CityNow")
    String cityNow;
    
    @Field("isCityPublic")
    boolean isCityPublic;
}