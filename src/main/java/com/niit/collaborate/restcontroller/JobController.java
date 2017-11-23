package com.niit.collaborate.restcontroller;

import java.util.Date;
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
import com.niit.collaborate.model.Error;

import com.niit.collaborate.DAO.JobDAO;
import com.niit.collaborate.DAO.UserDAO;
import com.niit.collaborate.model.Job;
import com.niit.collaborate.model.User;

@Controller
public class JobController {
	@Autowired
	private JobDAO jobDAO;
	
	@Autowired
	private UserDAO userDAO;
	
	@RequestMapping(value="/addJob",method=RequestMethod.POST)
	public ResponseEntity<?>addJob(@RequestBody Job job,HttpSession session){
		String userId=(String)session.getAttribute("userId");
		System.out.println("name of the user:"+userId);
		if(userId==null){
			Error error=new Error(5,"PLEASE LOGIN");
			return new  ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
		}
		User user=userDAO.getUser(userId);
		if(!user.getRole().equals("ADMIN")){
			Error error=new Error(6,"Access Deined");
			return new  ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
		}
		try{
			job.setPostedOn(new Date());
			
			jobDAO.createJob(job);
			
			return new  ResponseEntity<Job>(job,HttpStatus.OK);
		}catch(Exception e){
			Error error=new Error(7,"Unable to Insert the data");
			return new  ResponseEntity<Error>(error,HttpStatus.NOT_ACCEPTABLE);
		}
	}
	@RequestMapping(value="/getJobs",method=RequestMethod.GET)
	public ResponseEntity<?>getJobs(HttpSession session){
		String userId=(String)session.getAttribute("userId");
		System.out.println("name of the user:"+userId);
		if(userId==null){
			Error error=new Error(5,"PLEASE LOGIN");
			return new  ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
		}
		List<Job>jobs=jobDAO.getJobs();
		return new  ResponseEntity<List<Job>>(jobs,HttpStatus.OK);
	}
	
	@RequestMapping(value="/getJobDetails/{jobId}",method=RequestMethod.GET)
	public ResponseEntity<?>getJob(@PathVariable int jobId,HttpSession session){
		String userId=(String)session.getAttribute("userId");
		System.out.println("name of the user:"+userId);
		if(userId==null){
			Error error=new Error(5,"PLEASE LOGIN");
			return new  ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
		}
	Job job=jobDAO.getJob(jobId);
		return new  ResponseEntity<Job>(job,HttpStatus.OK);
	}
}
