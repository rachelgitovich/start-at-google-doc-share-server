package docSharing.controller;

import docSharing.entity.Log;
import docSharing.entity.User;
import docSharing.requests.OnlineUsersReq;
import docSharing.response.AllUsers;
import docSharing.response.UsersInDocRes;
import docSharing.service.DocumentService;
import docSharing.service.UserService;
import docSharing.utils.ConfirmationToken;
import docSharing.utils.Validations;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.CrossOrigin;

import javax.security.auth.login.AccountNotFoundException;

@Controller
@CrossOrigin
public class TextEditController {

    @Autowired
    DocumentService documentService;
    @Autowired
    UserService userService;

    @MessageMapping("/document/{documentId}")
    @SendTo("/document/{documentId}")
    public Log receiveLog(@DestinationVariable Long documentId, @Payload Log log) {
//        if (log.getData() == null || log.getAction() == null || log.getOffset() == null || log.getDocumentId() == null || log.getUserId() == null || log.getCreationDate() == null)
//            // FIXME: What to do if anything fails? Do we do anything with the client?
//            return null;
        log.setUserId(Validations.validateToken("Bearer " + log.getToken()));
        Log copyOfLog = Log.copy(log);
        documentService.updateContent(log);
        System.out.println("Creating a new log for: " + copyOfLog);
        return copyOfLog;
    }



    @MessageMapping("/document/onlineUsers/{documentId}")
    @SendTo("/document/onlineUsers/{documentId}")
    public AllUsers getOnlineUsers(@DestinationVariable Long documentId, @Payload OnlineUsersReq onlineUsersReq) {
        try {
            System.out.println("Looking for online users for document id:" + onlineUsersReq.getDocumentId());
            Long userId = Validations.validateToken("Bearer " + onlineUsersReq.getToken());
            Set<User> onlineUsers = documentService.addUserToDocActiveUsers(userId, onlineUsersReq.getDocumentId(), onlineUsersReq.getMethod());
            List<String> online = onlineUsers.stream().map(u -> u.getEmail()).collect(Collectors.toList());
            List<UsersInDocRes> all = documentService.getAllUsersInDocument(documentId).stream().map(u -> new UsersInDocRes(u.getUser().getEmail(), u.getPermission())).collect(Collectors.toList());
            return new AllUsers(online, all);
        }catch (AccountNotFoundException e) {
            return null;
        }
    }



}
