/**
 * 
 */
package com.multiplemedia.interceptor;

/**
 * @author Haojie Ma
 * @date Oct 27, 2015
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

 

@Component
public class SessionInterceptor implements HandlerInterceptor  {
		
		private Logger log=Logger.getLogger(SessionInterceptor.class);
		@Override
	    public boolean preHandle(HttpServletRequest request,
	            HttpServletResponse response, Object handler) throws Exception {
			String root=request.getContextPath();
			

			
	
			HttpSession session=request.getSession();
			
			String url=request.getRequestURI();
			
			System.out.println(url);
			
	         
			if(session==null){
				response.sendRedirect(root);
				return false;
			}
				
			String userName=(String)session.getAttribute("userName");
			@SuppressWarnings("unchecked")
			List<String> privilegeList=(List<String>) session.getAttribute("privilege");
			if(userName==null){
				response.sendRedirect(root);
				return false;
			}
			
			if(privilegeList==null){
				
				log.info(userName+"doesn't have any permission!");
				throw new Exception("Permission denied!");
				
			}
	        
			if(!privilegeList.contains(url)){
				log.info(userName+" doesn't have permission to access "+url);
				throw new Exception("Permission denied!");	
			}
				
			
	        
	        return true;
	    }
	     
	    @Override
	    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,ModelAndView modelAndView) throws Exception {
	    }
	     
	    @Override
	    public void afterCompletion(HttpServletRequest request,
	            HttpServletResponse response, Object handler, Exception ex)
	            throws Exception {
	    }
    
}





