package main.bindroid.com.bindroid.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by amanbindlish on 15/08/15.
 */
public class BindroidUtils {

	public static void getFbKeyHash(Context context) {

		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				Log.d("YourKeyHash :",
						Base64.encodeToString(md.digest(), Base64.DEFAULT));
			}
		} catch (PackageManager.NameNotFoundException e) {
			Log.e("BindroidUtils", "Exception " + e.getMessage());
		} catch (NoSuchAlgorithmException e) {
			Log.e("BindroidUtils", "Exception " + e.getMessage());
		}

	}
}
