package com.absurd.circle.core.client;

import static org.apache.http.HttpStatus.SC_OK;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Base64;

import com.absurd.circle.app.AppConstant;
import com.absurd.circle.core.util.JsonUtil;
import com.absurd.circle.util.CommonLog;
import com.absurd.circle.util.LogFactory;


public class CampusClient {
	private static final CommonLog log = LogFactory.createLog(AppConstant.TAG);
	
	private static final String CHARSET_UTF8 = HTTP.UTF_8;
	private static HttpClient customHttpClient;
	
	
	protected static final String HEADER_CONTENT_TYPE = "Content-Type";
	
	protected static final String HEADER_ACCEPT = "Accept";
	
	protected static final String HEADER_AUTHORIZATION = "Authorization";
	
	protected static final String JSON = "application/json";
	
	protected static final String AUTH_TOKEN = "Token";
	
	protected static final String AUTH_BASIC = "Basic";
	
	
	
	private String credential;
	
	
	
	public CampusClient setCredential(final String username,final String password){
		if(username != null && username.length() > 0 && password != null
				&& password.length() > 0)
			credential = "Basic "+ 
				Base64.encodeToString((username+':'+password).getBytes(), Base64.NO_WRAP);
		else
			credential =null;
		return this;
				
	}
	
	
	public CampusClient setCredential(String token){
		if(token != null && token.length() > 0)
			credential = AUTH_TOKEN +  ' ' + token;
		else
			credential = null;
		return this;
	}

	
	public CampusClient(){
		// TODO Auto-generated constructor stub
	}

	private boolean isOk(final int code){
		switch(code){
		case SC_OK:
		case HttpStatus.SC_CREATED:
		case HttpStatus.SC_ACCEPTED:
			return true;
		default:
			return false;
		}
	}

	private boolean isError(final int code){
		switch(code){
		case HttpStatus.SC_BAD_REQUEST:
		case HttpStatus.SC_UNAUTHORIZED:
		case HttpStatus.SC_FORBIDDEN:
		case HttpStatus.SC_NOT_FOUND:
		case HttpStatus.SC_CONFLICT:
		case HttpStatus.SC_GONE:
		case HttpStatus.SC_UNPROCESSABLE_ENTITY:
		case HttpStatus.SC_INTERNAL_SERVER_ERROR:
			return true;
		default:
			return false;
		}
	}

	private boolean isEmpty(final int code){
		return HttpStatus.SC_NO_CONTENT == code;
	}
	
	

	private <V> V parseJson(String jsonStr,Type type){
		return JsonUtil.fromJson(jsonStr, type);
	}

	private RequestError parseError(String error){
		log.i("error : " + error);
		return parseJson(error,RequestError.class);
	}
	

	private <V> V handleResponse(final HttpResponse response,
			Class<V> type)throws IOException{
		int code = response.getStatusLine().getStatusCode();
		if(isOk(code)){
			if(type != null){
				String jsonString = EntityUtils.toString(response.getEntity());
				log.i("response json objet " + jsonString);
				return parseJson(jsonString,type);
			}else
				return null;
		}else if(isEmpty(code)){
			return null;
		}
		throw createException(EntityUtils.toString(response.getEntity(),CHARSET_UTF8),
					code);
	}
	
	
	private CampusResponse handleResponse(final HttpResponse response,CampusRequest request) 
			throws ParseException, IOException{
		int code = response.getStatusLine().getStatusCode();
		if(isOk(code)){
			if(request.getType() != null){
				Object body = parseJson(EntityUtils.toString(response.getEntity(),CHARSET_UTF8),
						request.getType());
				return new CampusResponse(body);
			}
			else
				return new CampusResponse(null);
		}
		if(isEmpty(code))
			return null;
		throw createException(EntityUtils.toString(response.getEntity(),CHARSET_UTF8),
					code);
	}
	

	private IOException createException(String response,int code){
		if(isError(code)){
			final RequestError error;
			error = parseError(response);
			if(null != error)
				return new RequestException(error,code);
		}
		return new IOException("Unknown error occured" + " " + code);

	}


	public <V> V post(final String url,final Class<V> type,
			NameValuePair... nameValuePairs)throws Exception{
		HttpPost httpPost = createPost(url, nameValuePairs);
		HttpClient client = getHttpClient();
		HttpResponse response = client.execute(httpPost);
		
		return handleResponse(response, type);
	}
	
	public CampusResponse post(CampusRequest request) 
			throws IOException{
		String requestUrl = request.generateUri();
		log.i(requestUrl);
		HttpPost httpPost = new HttpPost(requestUrl);
		httpPost = (HttpPost)configureRequest(httpPost);
		HttpClient client = getHttpClient();
		HttpResponse response = client.execute(httpPost);
		return handleResponse(response,request);
		
	}
	
	
	
	
	/**
	 * HttpClient POST methos or post a json
	 * @param url
	 * @param type
	 * @param jsonObject
	 * @param nameValuePairs
	 * @return
	 * @throws Exception
	 */
	public <V> V post(final String url,final Class<V> type,
			String jsonObject,NameValuePair... nameValuePairs)throws Exception{
		String requestUrl = urlParse(url,nameValuePairs);
		HttpPost httpPost = new HttpPost(requestUrl);
		httpPost = (HttpPost)configureRequest(httpPost);
		if(jsonObject != null)
			httpPost.setEntity(new StringEntity(jsonObject));
		
		HttpClient client = getHttpClient();
		
		HttpResponse response = client.execute(httpPost);
		return handleResponse(response,type);
	}
	
	
	/**
	 * create HttpPost
	 * @param url
	 * @param nameValuePairs
	 * @return
	 * @throws java.io.UnsupportedEncodingException
	 */
	private HttpPost createPost(final String url,NameValuePair...nameValuePairs) throws UnsupportedEncodingException{
		//String requestUrl = urlParse(url, nameValuePairs);
		List<NameValuePair> params = getParams(nameValuePairs);

		UrlEncodedFormEntity urlEncoded = new UrlEncodedFormEntity(params,CHARSET_UTF8);
		HttpPost httpPost = new HttpPost(url);
		httpPost = (HttpPost)configureRequest(httpPost);
		httpPost.setEntity(urlEncoded);
		return httpPost;
	}
	
	/**
	 * get params from nameValuePairs
	 * @param nameValuePairs
	 * @return
	 */
	private List<NameValuePair> getParams(NameValuePair...nameValuePairs){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		if(null != nameValuePairs)
			for(int i = 0;i < nameValuePairs.length;i++)
				params.add(nameValuePairs[i]);
		return params;
	}
	/**
	 * create HttpGet
	 * @param url
	 * @param nameValuePairs
	 * @return
	 */
	private HttpGet createGet(final String url,NameValuePair...nameValuePairs){
		String requestUrl = urlParse(url, nameValuePairs);
		HttpGet httpGet = new HttpGet(requestUrl);
		httpGet = (HttpGet)configureRequest(httpGet);
		return httpGet;
	}
	
	/**
	 * Create a httpPut method
	 * @param url
	 * @param nameValuePairs
	 * @return
	 * @throws java.io.UnsupportedEncodingException
	 */
	private HttpPut createPut(final String url,NameValuePair...nameValuePairs) throws UnsupportedEncodingException{
		String requestUrl = urlParse(url,nameValuePairs);
		List<NameValuePair> params = getParams(nameValuePairs);
		
		UrlEncodedFormEntity urlEncoded = new UrlEncodedFormEntity(params,CHARSET_UTF8);
		HttpPut httpPut = new HttpPut(requestUrl);
		httpPut = (HttpPut)configureRequest(httpPut);
		httpPut.setEntity(urlEncoded);
		
		return httpPut;
	}
	
	public <V> V put(final String url,Class<V> type,
			NameValuePair...nameValuePairs)throws Exception{
		HttpPut httpPut = createPut(url, nameValuePairs);
		HttpClient httpClient = getHttpClient();
		HttpResponse response = httpClient.execute(httpPut);
		
		return handleResponse(response,type);
	}
	
	/**
	 * Create a httpPut method to put a JSON object
	 * @param url
	 * @param strJson
	 * @return
	 * @throws java.io.UnsupportedEncodingException
	 */
	private HttpPut createPut(final String url,String strJson) throws UnsupportedEncodingException{
		HttpPut httpPut = new HttpPut(url);
		httpPut = (HttpPut)configureRequest(httpPut);
		StringEntity en = new StringEntity(strJson,"utf_8");
		httpPut.setEntity(en);
		return httpPut;
	}
	/**
	 * Put a Json object
	 * @param url
	 * @param type
	 * @param strJson
	 * @return
	 * @throws org.apache.http.client.ClientProtocolException
	 * @throws java.io.IOException
	 */
	public <V> V put(final String url,Class<V> type,
					String strJson) throws ClientProtocolException, IOException{
		HttpPut httpPut;
		try {
			httpPut = createPut(url, strJson);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		HttpClient httpClient = getHttpClient();
		HttpResponse response = httpClient.execute(httpPut);
		return handleResponse(response,type);
	}
	
	
	
	
	/**
	 * create HttpDelete
	 * @param url
	 * @param nameValuePairs
	 * @return
	 */
	private HttpDelete createDelete(final String url,
			NameValuePair...nameValuePairs){
		String requestUrl = urlParse(url, nameValuePairs);
		HttpDelete httpDelete = new HttpDelete(requestUrl);
		httpDelete = (HttpDelete)configureRequest(httpDelete);
		return httpDelete;
	}


    /**
     *
     * @param url
     * @param type
     * @param nameValuePairs
     * @param <V>
     * @return
     * @throws Exception
     */
	public <V> V get(final String url,Class<V> type,
			NameValuePair...nameValuePairs) throws Exception{
		HttpGet httpGet = createGet(url, nameValuePairs);
		
		HttpClient httpClient = getHttpClient();
		HttpResponse response = httpClient.execute(httpGet);
		
		return handleResponse(response,type);
	}
	
	
	public CampusResponse get(CampusRequest request) 
			throws IOException{
		HttpGet httpGet= new HttpGet(request.generateUri());
		httpGet = (HttpGet)configureRequest(httpGet);
		HttpClient httpClient = getHttpClient();
		HttpResponse response = httpClient.execute(httpGet);
		return handleResponse(response, request);
		
	}
	
	
	

    /**
     * delete this method will throw an Exception
     *
     * when the response status is not a 204
     * @param url
     * @param nameValuePairs
     * @throws Exception
     */
    public void delete(final String url,
			NameValuePair...nameValuePairs)throws Exception{
		HttpDelete httpDelete = createDelete(url, nameValuePairs);
		httpDelete = (HttpDelete)configureRequest(httpDelete);
		HttpClient httpClient = getHttpClient();
		HttpResponse response = httpClient.execute(httpDelete);
		int code = response.getStatusLine().getStatusCode();
		if(!isEmpty(code))
			throw createException(EntityUtils.toString(response.getEntity(),CHARSET_UTF8),
					code);
	}

	public void delete(CampusRequest request) throws ClientProtocolException, IOException{
		String requestUri = request.generateUri();
		log.i(requestUri);
		HttpDelete httpDelete = new HttpDelete(requestUri);
		httpDelete = (HttpDelete)configureRequest(httpDelete);
		HttpClient client = getHttpClient();
		HttpResponse response = client.execute(httpDelete);
		int code = response.getStatusLine().getStatusCode();
		if(!isEmpty(code))
			throw createException(EntityUtils.toString(response.getEntity(),CHARSET_UTF8), 
					code);
	}
	
	

	protected HttpRequestBase configureRequest(HttpRequestBase request){
		if(credential != null)
			request.setHeader(HEADER_AUTHORIZATION,credential);
		request.setHeader(HEADER_CONTENT_TYPE,"application/json; charset=utf_8");
		request.setHeader(HEADER_ACCEPT,JSON);
		return request;
	}
	
	

	private String urlParse(String url,NameValuePair...nameValuePairs){
		StringBuilder sb = new StringBuilder();
		sb.append(url);
		if(null != nameValuePairs){
			sb.append("?");
			for(int i = 0;i < nameValuePairs.length;i++){
				if(i > 0){
					sb.append("&");
				}
				sb.append(String.format("%s=%s",
						nameValuePairs[i].getName(),
						nameValuePairs[i].getValue()));
				
			}
		}
		return sb.toString();
	}

	private  HttpClient getHttpClient(){
		if(null == customHttpClient){
			HttpParams params = new BasicHttpParams();
			//���û����
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, CHARSET_UTF8);
			HttpProtocolParams.setUseExpectContinue(params, true);
			HttpProtocolParams.setUserAgent(params, 
					"Mozilla/5.0(Linux;U;Android 2.2.1;en-us;Nexus One Build.FRG83) "
							+ "AppleWebKit/553.1(KHTML,like Gecko) Version/4.0 Mobile Safari/533.1");
			ConnManagerParams.setTimeout(params, 10000);
			int ConnectionTimeOut = 20000;
			HttpConnectionParams.setConnectionTimeout(params, ConnectionTimeOut);
			HttpConnectionParams.setSoTimeout(params, 20000);
			
			SchemeRegistry schReg = new SchemeRegistry();
			schReg.register(new Scheme("http",PlainSocketFactory.getSocketFactory(),80));
			schReg.register(new Scheme("https",PlainSocketFactory.getSocketFactory(),443));
			
			ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params,schReg);
			
			customHttpClient = new DefaultHttpClient(conMgr,params);
		}
		return customHttpClient;
					
	}
}
	

