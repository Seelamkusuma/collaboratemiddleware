package com.niit.collaborate.restcontroller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.niit.collaborate.DAO.UserDAO;
import com.niit.collaborate.model.User;
import com.niit.collaborate.model.Error;

@Controller
	public class UserController{
		@Autowired
		UserDAO userDAO;
		
		@RequestMapping(value="/register",method=RequestMethod.POST)
		public ResponseEntity<?> registerUser(@RequestBody User user){
			
		if(!userDAO.isUserIdValid(user.getUserId())){
			
				Error error=new Error(2,"UserId already exists.. please enter different userId");
				return new ResponseEntity<Error>(error,HttpStatus.NOT_ACCEPTABLE);
			}
		if(!userDAO.ValidEmailId(user.getEmailId())){
			Error error=new Error(3,"EmailId already exists.. please enter different emailId");
			return new ResponseEntity<Error>(error,HttpStatus.NOT_ACCEPTABLE);
		}
			boolean result=userDAO.createUser(user);
			if(result){
				return new ResponseEntity<User>(user,HttpStatus.OK);
			}
			else{
				Error error=new Error(1,"Unable to register user details");
				return new ResponseEntity<Error>(error,HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
		@RequestMapping(value="/Login",method=RequestMethod.POST)
		public ResponseEntity<?> Login(@RequestBody User user,HttpSession session){
			User validUser=userDAO.Login(user);
			if(validUser==null){//invalid userId/password
				Error error=new Error(4,"Invalid Userid/Password....");
				return new ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
			}
			validUser.setOnline(true);
			try{
				userDAO.update(validUser);
			}catch(Exception e){
				Error error=new Error(6,"Unable to update online status");
				return new ResponseEntity<Error>(error,HttpStatus.INTERNAL_SERVER_ERROR);
			}
			System.out.println("ONLINE STATUS AFTER UPDATE" + validUser.isOnline());
			session.setAttribute("userId",validUser.getUserId());
			return new ResponseEntity<User>(validUser,HttpStatus.OK);
			

		}
		@RequestMapping(value="/logout",method=RequestMethod.PUT)
		public ResponseEntity<?> logout(HttpSession session){
			String userId=(String)session.getAttribute("userId");
			System.out.println("Id of the user is" + userId);
			if(userId==null){
				Error error=new Error(5,"Unauthorized access.. please Login..");
				return new ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
			}
			User user=userDAO.getUser(userId);
			user.setOnline(false);
			userDAO.update(user);
			session.removeAttribute("userId");
			session.invalidate();
			return new ResponseEntity<User>(user,HttpStatus.OK);
			
		}
		@RequestMapping(value="/getuser",method=RequestMethod.GET)
		public ResponseEntity<?> getUser(HttpSession session){
			String userId=(String)session.getAttribute("userid");
			if(userId==null){
				Error error=new Error(5,"Unauthorized access.. please login..");
				return new ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
				
			}
			User user=userDAO.getUser(userId);
			return new ResponseEntity<User>(user,HttpStatus.OK);
		}
		@RequestMapping(value="/updateuser",method=RequestMethod.PUT)
		public ResponseEntity<?> updateUser(@RequestBody User user,HttpSession session){
			String userId=(String)session.getAttribute("userid");
			if(userId==null){
				Error error=new Error(5,"Unauthorizes access.. please login..");
				return new ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
			}
			if(!userDAO.isUpdatedEmailIdValid(user.getEmailId(),user.getUserId())){
				Error error=new Error(3,"EmailId address already exists...Please enter different email");
				 return new ResponseEntity<Error>(error,HttpStatus.NOT_ACCEPTABLE);
			}
			try{
				userDAO.update(user);
				return new ResponseEntity<User>(user,HttpStatus.OK);
			}catch(Exception e){
				Error error=new Error(4,"Unable to update user details");
				return new ResponseEntity<Error>(error,HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
				
		
			
			
		}


