package org.freehold.utility.webutility;

public class WebUtility {

	public static String appendToUrl(String url, String path)
	{
		path = path.startsWith("/") ? path.substring(1, (path.length())) : path;
		return url.endsWith("/") ? (url + path) : (url + "/" + path);
	}
	
//	public Object DeserializeEntity(ClientRequest request) throws Exception {
//		try {
//			StringWriter writer = new StringWriter();
//			IOUtils.copy(request.getEntity(), writer, "UTF8");
//			return writer.toString();
//		} catch (Exception ex) {
//			throw ex;
//		}
//	}
	
}
