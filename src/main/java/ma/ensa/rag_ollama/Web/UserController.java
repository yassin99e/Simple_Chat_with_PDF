package ma.ensa.rag_ollama.Web;

import ma.ensa.rag_ollama.Service.UserService;
import ma.ensa.rag_ollama.entites.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/registration")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String registerUser(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password) {
        User user = new User(username, email, password);

        try {
            userService.saveUser(user);
            return "login"; // Use Thymeleaf view resolution
        } catch (Exception e) {
            e.printStackTrace();  // Log the exception
            return "index";  // Return to the index.html view on error
        }
    }

    @PostMapping("/login")
    public String loginUser(
            @RequestParam String username,
            @RequestParam String password) {

        if (userService.authenticate(username, password)) {
            return "home"; // Redirect to home.html
        } else {
            return "login"; // Redirect to login.html if authentication fails
        }
    }


    @GetMapping("/already")
    public String already() {
        return "login";
    }
}