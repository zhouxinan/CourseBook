package utility;

import java.util.Comparator;

import org.json.JSONObject;

public class MyComparator implements Comparator<JSONObject> {
	@Override
	public int compare(JSONObject o1, JSONObject o2) {
		return o2.get("time").toString().compareTo(o1.get("time").toString());
	}
}