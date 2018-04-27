/**
 * 
 */
package com.multiplemedia.controller;

/**
 * @author Haojie Ma
 * @date Oct 21, 2015
 */


import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.multiplemedia.model.LoginSearch;
import com.multiplemedia.model.SearchCriteria;
import com.multiplemedia.model.User;
import com.multiplemedia.service.UserService;
import com.multiplemedia.util.CommonUtil;
import com.multiplemedia.views.Views;


@Controller
//@RequestMapping("/hello")// the annotation can be mapped to a class
public class BaseController {

	//private final static org.slf4j.Logger logger = LoggerFactory.getLogger(BaseController.class);
	
	private static Logger log=Logger.getLogger(BaseController.class);
	
	@Autowired()
	private DefaultKaptcha captchaProducer;	
	

	
	 @Autowired()
	 private UserService userService;
	 private MappingJackson2JsonView  jsonView = new MappingJackson2JsonView();
	
	

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(ModelMap model) { // it will return the model

		//model.addAttribute("message", "Welcome");
		//model.addAttribute("counter", ++counter);
		//logger.debug("[welcome] counter : {}", counter);

		// Spring uses InternalResourceViewResolver and return back index.jsp
		return "index"; // it is used to return to which jsp

	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(ModelMap model) { // it will return the model

		//model.addAttribute("message", "Welcome");
		//model.addAttribute("counter", ++counter);
		//logger.debug("[welcome] counter : {}", counter);

		// Spring uses InternalResourceViewResolver and return back index.jsp
		return "login"; // it is used to return to which jsp

	}
	
	@RequestMapping(value = "/admin/welcome", method = RequestMethod.GET)
	public String welcome(HttpSession session,ModelMap model) { // it will return the model

		
		
			return "/admin/welcome"; // it is used to return to which jsp
		
			

	}
	
	@JsonView
	@RequestMapping(value = "/admin/validate", method = RequestMethod.POST)
	//public ModelAndView welcome(HttpServletRequest request,@RequestBody LoginSearch search) throws ServletException,IOException{ // it will return the model
	public ModelAndView welcome(HttpSession session,@RequestBody LoginSearch search) throws ServletException,IOException{ // it will return the model

		String kaptchaCode = (String)session.getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);  
		
		String userName=search.getUsername();
		String password=search.getPassword();
		String captcha=search.getKaptcha();
		String code="200";
		String msg="";
		ModelAndView view= new ModelAndView(jsonView);
		
		if(!kaptchaCode.equals(captcha))
			msg="kaotcha is not correct";
		else{
			password=CommonUtil.md5(password);
	        

			
	        log.info("user="+userName+",password="+password);
	        
	        boolean flag=userService.exist(userName, password);
	        
	        if(flag){
	        	List<String> privilegeList=userService.getPrivilegeList(userName);
	        	session.setAttribute("userName", userName);
	        	session.setAttribute("privilege", privilegeList);
	        	//request.getRequestDispatcher("admin/welcome").forward(request, null);
	        	//return "redirect:/admin/welcome";
	        	msg="login succeed!";
	        	
	        }else{
	        	
	        	User user=userService.getUser(userName);
	        	if(user==null){
	        		msg="no such user!";
	        		log.info(msg+userName);
	        	}
	        	else{
	        		msg="UserName and password doesn't match";
	        		log.info(msg);
	        	}
	        		
	        	
	        	// Spring uses InternalResourceViewResolver and return back index.jsp
	        	//request.getRequestDispatcher("login").forward(request, null);
	        	//return "redirect:/login";
	        	
	        }
		}
		
        

        view.addObject("code", code);
        view.addObject("message",msg);

        return view;

		
	}
	
	
	
	@RequestMapping(value = "/admin/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request) {

			HttpSession session=request.getSession();
			session.removeAttribute("userName");
			session.removeAttribute("privilege");
			
			return "redirect:/login"; // it is used to return to which j
			

	}
	
	@RequestMapping(value = "error", method = RequestMethod.GET)
	public String error(HttpSession session,ModelMap model) { // it will return the model

		
		
			return "/error"; // it is used to return to which j
			

	}
	
	
	@RequestMapping("kaptcha")    
    public void initCaptcha(HttpServletRequest request,HttpServletResponse response) throws Exception{  
  
          response.setDateHeader("Expires", 0);     
          response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");    
          response.addHeader("Cache-Control", "post-check=0, pre-check=0");    
          response.setHeader("Pragma", "no-cache");    
          response.setContentType("image/jpeg");    
          String capText = captchaProducer.createText();    
          request.getSession().setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);    
          BufferedImage bi = captchaProducer.createImage(capText);    
  
          ServletOutputStream out = response.getOutputStream();    
          ImageIO.write(bi, "jpg", out);    
            
          try{    
              out.flush();    
          }finally{    
              out.close();    
          }    
    }  
	
	/*
	@RequestMapping(value = "/{name}", method = RequestMethod.GET)
	public String welcomeName(@PathVariable String name, ModelMap model) { // it will return the model

		model.addAttribute("message", "Welcome " + name);
		model.addAttribute("counter", ++counter);
		logger.debug("[welcomeName] counter : {}", counter);
		return VIEW_INDEX; // it is used to return to which jsp

	}

*/
	/*
	@RequestMapping(value = "/addStudent", method = RequestMethod.POST)
	   public String addStudent(@ModelAttribute("SpringWeb")Student student, 
	   ModelMap model) {
	      model.addAttribute("name", student.getName());
	      model.addAttribute("age", student.getAge());
	      model.addAttribute("id", student.getId());
	      
	      return "result";
	   }
	*/
	
}


