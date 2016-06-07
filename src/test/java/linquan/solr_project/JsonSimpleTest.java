package linquan.solr_project;

import java.util.Map;

import com.github.wnameless.json.flattener.JsonFlattener;

public class JsonSimpleTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String json = "{ \"a\" : { \"b\" : 1, \"c\": null, \"d\": [false, true] }, \"e\": \"f\", \"g\":2.3 }";
		Map<String, Object> flattenJson = JsonFlattener.flattenAsMap(json);

		System.out.println(flattenJson);
	}

}
