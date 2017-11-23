package com.niit.collaborate.restcontroller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import com.niit.collaborate.model.Chat;

@Controller
public class SockController {
	private static final Log logger = LogFactory.getLog(SockController.class);

	private final SimpMessagingTemplate messagingTemplate;

	private List<String> users = new ArrayList<String>();


	@Autowired

	public SockController(SimpMessagingTemplate messagingTemplate) {

		this.messagingTemplate = messagingTemplate;

	}

	@SubscribeMapping("/join/{userId}")

	public List<String> join(@DestinationVariable("userId") String userId) {
        

		 System.out.println("userId in sockcontroller" + userId);
		 
		 if(!users.contains(userId)) {
				users.add(userId);
			}


		System.out.println("====JOIN==== " + userId);

		// notify all subscribers of new user

		messagingTemplate.convertAndSend("/topic/join", userId);

		return users;

	}

	@MessageMapping(value = "/chat")

	public void chatReveived(Chat chat) {


		if ("all".equals(chat.getTo())) {

			System.out.println("IN CHAT REVEIVED " + chat.getMessage() + " " + chat.getFrom() + " to " + chat.getTo());

			messagingTemplate.convertAndSend("/queue/chats", chat);

		}

		else {

			System.out.println("CHAT TO " + chat.getTo() + " From " + chat.getFrom() + " Message " + chat.getMessage());

			messagingTemplate.convertAndSend("/queue/chats/" + chat.getTo(), chat);

			messagingTemplate.convertAndSend("/queue/chats/" + chat.getFrom(), chat);

		}

	}

}
