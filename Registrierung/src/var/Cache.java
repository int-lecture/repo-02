package var;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Cache {
	private static final String ISO8601 = "yyyy-MM-dd'T'HH:mm:ssZ";
	static HashMap<String, String[]> cache = new HashMap<String, String[]>();
	volatile static float cached = 0;
	volatile static float total = 0;
	volatile static float neuAngefragt = 0;
	
	private static String getStats(){
		float percent = (cached / total) * 100;
		return percent + "% cached" + ", total: " + total + ", cached: " + cached + ", aktualisiert: " + neuAngefragt;
	}
	
	public static synchronized void cacheToken(String from, String token, String date) {
		neuAngefragt++;
		String[] usertoken = {token, date};
		cache.put(from, usertoken);
	}

	public static String getCachedToken(String username) {
		if(total % 100 == 98){
			System.out.println(getStats());
		}
		total++;
		if(!isTokenInCache(username)){
			return null;
		}
		String[] usertoken;
		synchronized (cache) {
			usertoken = cache.get(username);	
		}
		String token = usertoken[0];
		cached++;
		return token;
	}

	private static boolean isTokenInCache(String username) {
		if(!cache.containsKey(username)){
			return false;
		}
		String[] usertoken;
		synchronized (cache) {
			usertoken = cache.get(username);	
		}
		String dateString = usertoken[1];
		SimpleDateFormat sdf = new SimpleDateFormat(ISO8601);
		Date expireDate = null;
		try {
			expireDate = sdf.parse(dateString);
		} catch (ParseException e) {
			return false;
		}
		 Calendar cal = Calendar.getInstance();
         if (cal.getTime().after(expireDate)) {
        	 cache.remove(username);
        	 return false;
         } else {
        	 return true;
         }
	}
}
