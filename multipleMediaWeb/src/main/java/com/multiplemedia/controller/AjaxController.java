/**
 * 
 */
package com.multiplemedia.controller;

/**
 * @author Haojie Ma
 * @date Oct 21, 2015
 */
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;




import javax.servlet.http.HttpSession;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.fasterxml.jackson.annotation.JsonView;
import com.multiplemedia.model.AjaxResponseBody;
import com.multiplemedia.model.SearchCriteria;
import com.multiplemedia.views.User;
import com.multiplemedia.views.Views;


@RestController
public class AjaxController {
	

	private MappingJackson2JsonView  jsonView = new MappingJackson2JsonView();
	List<User> users;
	private String msg;

	// @ResponseBody, not necessary, since class is annotated with @RestController
	// @RequestBody - Convert the json data into object (SearchCriteria) mapped by field name.
	// @JsonView(Views.Public.class) - Optional, filters json data to display.
	@JsonView(Views.Public.class) 
	@RequestMapping(value = "/admin/search/api/getSearchResult")
	public AjaxResponseBody getSearchResultViaAjax(@RequestBody SearchCriteria search) {
	//public ModelAndView getSearchResultViaAjax(@RequestBody SearchCriteria search) {
		/*
		//another way to access sessions
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpSession session = attr.getRequest().getSession();
		
		//the other way to access session is add session in the method
		*/
		
		ModelAndView view= new ModelAndView(jsonView);
		String code="";
		String msg="";
		
		
		AjaxResponseBody<User> result = new AjaxResponseBody<User>();
		List<User> users=null; 
		if (isValidSearchCriteria(search)) {
			 users = findByUserNameOrEmail(search.getUsername(), search.getEmail());

			if (users.size() > 0) {
				result.setCode("200");
				code="200";
				result.setMsg("successed!");
				result.setResult(users);
				msg="successed!";
			} else {
				result.setCode("204");
				result.setMsg("No user!");
				code="204";
				msg="No user!";
			}

		} else {
			result.setCode("400");
			result.setMsg("Search criteria is empty!");
			code="400";
			msg="Search criteria is empty!";
			
		}

		//AjaxResponseBody will be converted into json format and send back to the request.
		return result;
		//return new ModelAndView( jsonView, msg, users);
		
		/*
		view.addObject("message",msg);
		view.addObject("code",code);
		view.addObject("users",users);
		
		return view;
		*/

	}

	private boolean isValidSearchCriteria(SearchCriteria search) {

		boolean valid = true;

		if (search == null) {
			valid = false;
		}

		if ((StringUtils.isEmpty(search.getUsername())) && (StringUtils.isEmpty(search.getEmail()))) {
			valid = false;
		}

		return valid;
	}

	// Init some users for testing
	@PostConstruct
	private void iniDataForTesting() {
		users = new ArrayList<User>();

		User user1 = new User("mkyong", "pass123", "mkyong@yahoo.com", "012-1234567", "address 123");
		User user2 = new User("yflow", "pass456", "yflow@yahoo.com", "016-7654321", "address 456");
		User user3 = new User("laplap", "pass789", "mkyong@yahoo.com", "012-111111", "address 789");
		users.add(user1);
		users.add(user2);
		users.add(user3);

	}

	// Simulate the search function
	private List<User> findByUserNameOrEmail(String username, String email) {

		List<User> result = new ArrayList<User>();

		for (User user : users) {

			if ((!StringUtils.isEmpty(username)) && (!StringUtils.isEmpty(email))) {

				if (username.equals(user.getUsername()) && email.equals(user.getEmail())) {
					result.add(user);
					continue;
				} else {
					continue;
				}

			}
			if (!StringUtils.isEmpty(username)) {
				if (username.equals(user.getUsername())) {
					result.add(user);
					continue;
				}
			}

			if (!StringUtils.isEmpty(email)) {
				if (email.equals(user.getEmail())) {
					result.add(user);
					continue;
				}
			}

		}

		return result;

	}
}

