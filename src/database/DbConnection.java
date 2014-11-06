package database;

import java.net.MalformedURLException;

import android.content.Context;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;

public class DbConnection {
	
	public static MobileServiceClient connectToAzureService(Context context){
		//String url = "https://pandabears.azure-mobile.net/";
		//String key  = "BLHpgClAaVeLTPYTCNkUBJczvEJzVg75";
		
		String url = "https://prevkit.azure-mobile.net/";
		String key = "rXootnVHePtKaJRpTRSbqABoIdvzkE36";
		
		MobileServiceClient mClient = null; 
		
		try {
			mClient = new MobileServiceClient(url,key,context);
			return mClient;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mClient;
	}
	
}
