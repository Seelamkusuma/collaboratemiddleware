package com.niit.collaborate.restcontroller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.niit.collaborate.DAO.FriendDAO;
import com.niit.collaborate.model.User;
import com.niit.collaborate.model.Error;
import com.niit.collaborate.model.Friend;
@Controller
public class FriendController {
	@Autowired
	private FriendDAO friendDAO;
	@RequestMapping(value="/getsuggestedusers",method=RequestMethod.GET)
	public ResponseEntity<?> getSuggestedUsers(HttpSession session){
		String userId=(String)session.getAttribute("userId");
		if(userId==null){
			Error error=new Error(5,"Unauthorized access");
			return new ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
		}
		List<User> suggestedUsers=friendDAO.listOfSuggestedUsers(userId);
		return new ResponseEntity<List<User>>(suggestedUsers,HttpStatus.OK);
	}
	@RequestMapping(value="/friendrequest/{toId}",method=RequestMethod.GET)
	public ResponseEntity<?> friendRequest(@PathVariable String toId,HttpSession session){
		String userId=(String)session.getAttribute("userId");
		if(userId==null){
			Error error=new Error(5,"Unauthorized access");
			return new ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
		}
	Friend friend=new Friend();
	friend.setFromId(userId);
	friend.setToId(toId);
	friend.setStatus('P');
	friendDAO.friendRequest(friend);
	System.out.println("working");
	return new ResponseEntity<Friend>(friend,HttpStatus.OK);

	}
	@RequestMapping(value="/pendingRequests",method=RequestMethod.GET)
	public ResponseEntity<?> pendingRequests(HttpSession session){
		String userId=(String)session.getAttribute("userId");
		if(userId==null){
	     Error error=new Error(5,"Unauthorized access");
		return new ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
		}
	List<Friend> pendingRequests=friendDAO.pendingRequests(userId);
	return new ResponseEntity<List<Friend>>(pendingRequests,HttpStatus.OK);
		
	}
	@RequestMapping(value="/updatependingrequest",method=RequestMethod.PUT)
	public ResponseEntity<?> updatePendingrequest(@RequestBody Friend friend,HttpSession session){
		String userId=(String)session.getAttribute("userId");
		if(userId==null){
			Error error=new Error(5,"Unauthorized access");
			return new ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
		}
		System.out.println(friend.getFromId() + "" + friend.getStatus());
		friendDAO.updatePendingRequest(friend);
		return new ResponseEntity<Friend>(friend,HttpStatus.OK);
	}
	@RequestMapping(value="/friendslist",method=RequestMethod.GET)
	public ResponseEntity<?> listOfFriends(HttpSession session){
		String userId=(String)session.getAttribute("userId");
		if(userId==null){
			Error error=new Error(5,"Unauthorized access");
			return new ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
		}
		List<String> friends=friendDAO.listOfFriends(userId);
		return new ResponseEntity<List<String>>(friends,HttpStatus.OK);
		
		
	}
}
