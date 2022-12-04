package docSharing.controller;

import docSharing.entity.Document;
import docSharing.entity.Permission;
import docSharing.entity.User;
import docSharing.response.JoinRes;
import docSharing.response.Response;
import docSharing.service.DocumentService;
import docSharing.utils.EmailUtil;
import docSharing.service.UserService;
import docSharing.utils.Invite;
import docSharing.utils.Share;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.security.auth.login.AccountNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private FacadeController facadeController;

    @RequestMapping(value = "/permission/give", method = RequestMethod.PATCH)
    public ResponseEntity<Response> givePermission(@RequestParam Long documentId, @RequestParam Long uid, @RequestParam Permission permission, @RequestAttribute Long userId) {
        Response response = facadeController.givePermission(documentId, uid, permission);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @RequestMapping(value = "/share", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<Response> givePermissionToAll(@RequestBody List<String> emails, @RequestParam Long documentId, @RequestAttribute Long userId) {
        Response response = facadeController.givePermissionToAll(emails, documentId);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @RequestMapping(value = "sharedDocuments", method = RequestMethod.GET)
    public ResponseEntity<Response> getDocuments(@RequestAttribute Long userId) {
        return new ResponseEntity<>(new Response.Builder()
                .data(userService.documentsOfUser(userId))
                .message("Successfully managed to fetch all shared documents for a user!")
                .statusCode(200)
                .status(HttpStatus.OK)
                .build(), HttpStatus.OK);
    }

    @RequestMapping(value = "getUser", method = RequestMethod.GET)
    public ResponseEntity<Response> getUser(@RequestAttribute Long userId) {
        return new ResponseEntity<>(new Response.Builder()
                .data(userService.getUser(userId))
                .status(HttpStatus.OK)
                .statusCode(200)
                .message("Successfully managed to get the user from the database.")
                .build(), HttpStatus.OK);
    }

    @RequestMapping(value = "document/getUser", method = RequestMethod.GET)
    public ResponseEntity<Response> getUserPermissionForSpecificDocument(@RequestParam Long documentId, @RequestAttribute Long userId) {
        try {
            User user = userService.findById(userId);
            Permission permission = documentService.getUserPermissionInDocument(userId, documentId);
            return new ResponseEntity<>(new Response.Builder()
                    .data(new JoinRes(user.getName(), userId, permission))
                    .message("Successfully managed to fetch a user with his permission")
                    .status(HttpStatus.OK)
                    .statusCode(200)
                    .build(), HttpStatus.OK);

        } catch (AccountNotFoundException e) {
            return new ResponseEntity<>(new Response.Builder()
                    .statusCode(400)
                    .status(HttpStatus.BAD_REQUEST)
                    .message(e.getMessage())
                    .build(), HttpStatus.BAD_REQUEST);
        }
    }
}
