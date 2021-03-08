package com.eris.gitlabanalyzer.controller;

import com.eris.gitlabanalyzer.service.AuthService;
import com.eris.gitlabanalyzer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import static org.springframework.web.bind.annotation.RequestMethod.GET;


@RestController
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @Value("${FRONTEND_URL}")
    private String FRONTEND_URL;

    @Autowired
    public AuthController(AuthService authService, UserService userService){
        this.authService = authService;
        this.userService = userService;
    }

    // TODO: Homepage redirects to frontend so that we know the backend is running when Brian is marking
    // We can remove this once we deploy to the VM
    @GetMapping("/")
    public ModelAndView redirect() {
        return new ModelAndView("redirect:" + FRONTEND_URL);
    }

    @RequestMapping(value = "/api/v1/auth", method = GET)
    @ResponseBody
    public void validateSSO(@RequestParam("ticket") String ticket) {
        String userId = authService.getXMLResponse(ticket);
        Long id;
        if (userId.equals("validation failed")) {
            // This is where error occurred within SFU CAS, and the verification failed for some reason.
            // TODO: We might need to have an error page to explain user that something unexpected happened.
        } else {
            // This is where the validation was successful.
            boolean userExist = authService.checkUserExist(userId);
            if (userExist) {
                // Get the Long data type id of the user
                id = userService.getUserLongId(userId);
                System.out.println("Exist" + id);
            } else {
                // Create new user.
                userService.addUser(userId);
                id = userService.getUserLongId(userId);
                System.out.println("New" + id);
            }
        }
    }
}
