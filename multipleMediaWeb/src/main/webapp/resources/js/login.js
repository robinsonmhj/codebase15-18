/* $("#login-button").click(function(event){
		 event.preventDefault();
	 
	 $('form').fadeOut(500);
	 $('.wrapper').addClass('form-success');
});

 
 $("form").submit(function(){
	   var $input = $(this).find("input[name='username']");
	   if (!$input.val()) {
	     // Value is falsey (i.e. null), lets set a new one
	     $input.val("Please input your username");
	   }
	   var $input = $(this).find("input[name='password']");
	   if (!$input.val()) {
	     // Value is falsey (i.e. null), lets set a new one
	     $input.val("Please input your password");
	   }
	});
  */
 /*
 function validateForm() {
	    var x = document.forms["form"]["username"].value;
	    var y = document.forms["form"]["password"].value;
	    if((x == null || x == "")&&(y == null || y == "")){
	    	
	    	 alert("Please input your username and password");
		     return false;
	    }
	    
	    if (x == null || x == "") {
	    	 alert("Please input your username");
		     return false;
	    }
	    
	  
	    if (x == null || x == "") {
	        alert("Please input your password");
	        return false;
	    }
	}
 */

//reference https://formden.com/blog/validate-contact-form-jquery
$('#username').on('input', function() {
	var input=$(this);
	var is_name=input.val();
	if(is_name){input.removeClass("invalid").addClass("valid");}
	else{input.removeClass("valid").addClass("invalid");}
}); 


$('#password').on('input', function() {
	var input=$(this);
	//email regular expression
	//var re = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;
	var re=/[a-zA-Z0-9]{6,}/;
	var is_password=re.test(input.val());
	if(is_password){input.removeClass("invalid").addClass("valid");}
	else{input.removeClass("valid").addClass("invalid");}
}); 

$('#kaptcha').on('input', function() {
	var input=$(this);
	var is_name=input.val();
	if(is_name){input.removeClass("invalid").addClass("valid");}
	else{input.removeClass("valid").addClass("invalid");}
}); 


$("#login-button").click(function(event){
	var elements=["#username","#password"];
	var error_free=true;
	//alert(elements);
	for (var input in elements){
		//alert(elements[input]);
		//console.log(input);
		var element=$(elements[input]);
		var valid=element.hasClass("valid");
		var error_element=$("span", element.parent());
		if (!valid){error_element.removeClass("error").addClass("error_show"); error_free=false;}
		else{error_element.removeClass("error_show").addClass("error");}
	}
	if (!error_free){
		event.preventDefault(); 
	}
	//else{
		//alert('No errors: Form will be submitted');
	//}
});
/*
 $(function() {
	  
	    $(".form").validate({
	    
	        // Specify the validation rules
	        rules: {
	            username: "required",
	            password: "required",
	            password: {
	                required: true,
	                minlength: 6
	            }
	        },
	        
	        // Specify the validation error messages
	        messages: {
	            username: "Please enter your user name",
	            password: {
	                required: "Please provide a password",
	                minlength: "Your password must be at least 6 characters long"
	            }
	        }
	        
	    });

	  });
 */
 jQuery(document).ready(function($) {
	 

	 $('#img_kaptcha').click(  
	          function (event){  
	                $(this).attr('src', 'kaptcha?' + Math.floor(Math.random() * 100));    
	          }  
	 );  
	 
	 
		$(".form").submit(function(event) {

			// Disble the search button
			enableSearchButton(false);

			// Prevent the form from submitting via the browser.
			event.preventDefault();

			searchViaAjax();

		});

	});

	function searchViaAjax() {
		
		var search = {}
		search["username"] = $("#username").val();
		search["password"] = $("#password").val();
		search["kaptcha"] = $("#kaptcha").val();
		/*
		var username = $("#username").val();
		var password = $("#password").val();
		*/
		$.ajax({
			type : "POST",
			contentType : "application/json",
			//url : "http://localhost:8080/multipleMediaWeb/admin/validate",
			url : "admin/validate",
			data : JSON.stringify(search),
			dataType : 'json',
			cache: false,
			timeout : 100000,
			success : function(data) {
				//console.log("SUCCESS: ", data);
				//console.log(search);
				display(data);
			},
			error : function(e) {
				//console.log("ERROR: ", e);
				display(e);
			},
			done : function(e) {
				//console.log("DONE");
				enableSearchButton(true);
			}
		});

	}
	
	function enableSearchButton(flag) {
		$("#login-button").prop("disabled", flag);
	}

	function display(data) {
		//var json = $.parseJSON(data);
		//alert(data.message);
		//var url="http://localhost:8080/multipleMediaWeb/admin/welcome";
		var url="admin/welcome";
		var msg=data.message;
		if(msg=='login succeed!')
			$(location).attr('href',url);
		else
			$('#errors').html(msg);
			
	}