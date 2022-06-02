package com.advantal.onebrush.api_calls_cntrl_pkg;

import com.advantal.onebrush.api_calls_cntrl_pkg.api_call_controller.AddCookiesInterceptor;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Sonam on 17-02-2022 11:38.
 */

public class RetrofitApiClient {
    //        private static final String BASE_URL = "http://36.255.3.15:8086/onebrush-api/";
//    private static final String BASE_URL = "http://192.168.0.188:8080/api/";//lkn sonakaithwas96@gmail.com/Sona@1111
    private static final String BASE_URL = "http://36.255.3.15:8086/onebrushdentinostic-api/api/";
//    private static final String BASE_URL = "http://192.168.0.188:8080/api/";
//    private static final String BASE_URL = "https://36.255.3.15:8086/onebrushdentinostic-api/api/";//ssl //The connection to the backend could not be established because of an error with the SSL. A possible reason is that the website's certificate is invalid or obsolete."
//    private static final String BASE_URL = "http://sona.kaithwas.com:8089/onebrushdentinostic-api/api/";//ssl //The dentinostic server could not be found in the Internet
//4 . (back url  change) "customer/user_register";//customer/user_registers //The web address used to reach the dentinostic service is wrong. The host can be reached but not the requested web site."
    //3 skip DB
    //5 . //The dentinostic server was found but the access to the database is not permitted."
//    private static final String BASE_URL = "http://36.255.3.15:8086/onebrushdentinostic-sandbox/api/";
//    private static final String BASE_URL = "http://36.255.3.15:8086/onebrushalpha-api/api/";

    private static Retrofit retrofit = null;

    synchronized public static Retrofit getInstance() {
        OkHttpClient client;
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

//        client = builder.build();


        builder.addInterceptor(new AddCookiesInterceptor()); // VERY VERY IMPORTANT
        builder.addInterceptor(new ReceivedCookiesInterceptor()); // VERY VERY IMPORTANT
        builder.connectTimeout(300, TimeUnit.SECONDS);
        builder.readTimeout(300, TimeUnit.SECONDS);
        builder.writeTimeout(300, TimeUnit.SECONDS);

        /***call Api error**/
        try {
            TLSSocketFactory tlsSocketFactory = new TLSSocketFactory();
            if (tlsSocketFactory.getTrustManager() != null) {
                builder.sslSocketFactory(tlsSocketFactory, tlsSocketFactory.getTrustManager());
            }
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }


        client = builder.build();


        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .baseUrl(BASE_URL)
                    .build();
        }
        return retrofit;
    }

    public static ApiServices getservices() {
        return getInstance().create(ApiServices.class);
    }

}
