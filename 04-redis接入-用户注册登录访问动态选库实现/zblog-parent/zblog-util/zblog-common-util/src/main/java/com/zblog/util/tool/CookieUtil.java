package com.zblog.util.tool;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {

  public static Cookie getCookie(HttpServletRequest request, String name) {
    Cookie cookies[] = request.getCookies();
    if (cookies == null || name == null || name.length() == 0) {
      return null;
    }
    for (int i = 0; i < cookies.length; i++) {
        System.out.println(request.getServerName()+":"+cookies[i].getDomain());
      if (name.equals(cookies[i].getName())){
//          && request.getServerName().equals(cookies[i].getDomain())) {
        return cookies[i];
      }
    }
    return null;
  }
  
  
  public static Cookie getCookieByDomain(HttpServletRequest request, String name,String domain) {
	    Cookie cookies[] = request.getCookies();
	    if (cookies == null || name == null || name.length() == 0) {
	      return null;
	    }
	    for (int i = 0; i < cookies.length; i++) {
	      if (name.equals(cookies[i].getName())
	          && request.getServerName().equals(cookies[i].getDomain())) {
	        return cookies[i];
	      }
	    }
	    return null;
	  }

  public static void deleteCookie(HttpServletRequest request,
      HttpServletResponse response, Cookie cookie) {
    if (cookie != null) {
      cookie.setPath("/");
      //cookie.setDomain("wukonglicai.com");
      cookie.setValue("");
      cookie.setMaxAge(0);
      response.addCookie(cookie);
    }
  }

  public static void setCookie(HttpServletRequest request,
      HttpServletResponse response, String name, String value) {
    setCookie(request, response, name, value, 0x278d00);
  }

  public static void setCookie(HttpServletRequest request,
      HttpServletResponse response, String name, String value, int maxAge) {
	  
    Cookie cookie;
	try {
		cookie = new Cookie(name, value == null ? "" : URLEncoder.encode(value,"utf-8"));
		    cookie.setMaxAge(maxAge);
		    cookie.setPath("/");
		    //cookie.setDomain("wukonglicai.com");
		    response.addCookie(cookie);
		    
	} catch (UnsupportedEncodingException e) {
		e.printStackTrace();
	}
   
  }

  private static String getPath(HttpServletRequest request) {
    String path = request.getContextPath();
    return (path == null || path.length()==0) ? "/" : path;
  }

}
