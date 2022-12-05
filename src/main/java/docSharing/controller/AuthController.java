package docSharing.controller;

import docSharing.entity.User;
import docSharing.response.Response;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping(value = "/user/auth")
@AllArgsConstructor
@CrossOrigin
public class AuthController {

    private static Logger logger = LogManager.getLogger(AuthController.class.getName());

    @Autowired
    FacadeAuthController facadeAuthController;

    /**
     * Register function is responsible for creating new users and adding them to the database.
     * Users will use their personal information to create a new account: email, password, name.
     *
     * @param user - User with email, name and password
     * @return ResponseEntity with our Response with data and status 201 if good or 400 if something went wrong.
     */
    @RequestMapping(value = "register", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<Response> register(@RequestBody User user) {
        logger.info("in AuthController -> register");
        Response response = facadeAuthController.register(user);
        return new ResponseEntity<>(response, response.getStatus());
    }

    /**
     * Login function is responsible for logging user into the system.
     * This function accepts only 2 parameters: email, password.
     * If the credentials match to the database's information, it will allow the user to use its functionalities.
     * A token will be returned in a successful request.
     *
     * @param user - user's details with email and password to check if correct
     * @return ResponseEntity with our Response with user's token and status 200 if good or 400 if something went wrong.
     */
    @RequestMapping(value = "login", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<Response> login(@RequestBody User user) {
        logger.info("in AuthController -> login");
        Response response = facadeAuthController.login(user);
        return new ResponseEntity<>(response, response.getStatus());
    }

    /**
     * Activate function is responsible for activating email links.
     * If the link is not expired, make the user activated in the database.
     * If the link is expired, resend a new link to the user with a new token.
     *
     * @param token - A link with activation token
     * @return ResponseEntity<Response>  with data and status 200 if good or 400 if something went wrong.
     */
    @RequestMapping(value = "activate", method = RequestMethod.POST)
    public ResponseEntity<Response> activate(@RequestParam String token) {
        logger.info("in AuthController -> activate");
        Response response = facadeAuthController.activate(token);
        return new ResponseEntity<>(response, response.getStatus());
    }

}
