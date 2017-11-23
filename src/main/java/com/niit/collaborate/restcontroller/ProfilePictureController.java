package com.niit.collaborate.restcontroller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.niit.collaborate.DAO.ProfilePictureDAO;
import com.niit.collaborate.model.ProfilePicture;

import com.niit.collaborate.model.Error;

@Controller
public class ProfilePictureController {
	@Autowired
	private ProfilePictureDAO profilePictureDAO;
		@RequestMapping(value="/uploadprofilepic",method=RequestMethod.POST)
	public ResponseEntity<?> uploadProfilePicture(@RequestParam CommonsMultipartFile image,HttpSession session){
			String userId=(String)session.getAttribute("userId");
			System.out.println("name of the user:"+userId);
			if(userId==null){
				Error error=new Error(5,"PLEASE LOGIN");
				return new  ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
			} 
		ProfilePicture profilePicture=new ProfilePicture();
		profilePicture.setUserId(userId);
		profilePicture.setImage(image.getBytes());
		profilePictureDAO.save(profilePicture);
		return new ResponseEntity<ProfilePicture>(profilePicture,HttpStatus.OK);
	}
		@RequestMapping(value="/getprofilepic/{userId}", method=RequestMethod.GET)
		public @ResponseBody byte[] getProfilePic(@PathVariable String userId,HttpSession session){
			String loginuserId=(String)session.getAttribute("userId");
			if(loginuserId==null)
				return null;
			else
			{
				ProfilePicture profilePic=profilePictureDAO.getProfilePic(userId);
				if(profilePic==null)
					return null;
				else
					return profilePic.getImage();
			}
			
	}
	}
		

