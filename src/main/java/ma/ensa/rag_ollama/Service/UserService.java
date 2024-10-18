package ma.ensa.rag_ollama.Service;


import ma.ensa.rag_ollama.entites.User;
import ma.ensa.rag_ollama.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;



    public User saveUser(User user) {
        return userRepository.save(user);

    }

    public boolean authenticate(String username, String rawPassword) {
        User user = userRepository.findByUsername(username);
        return user != null && rawPassword.equals(user.getPassword());
    }




}
