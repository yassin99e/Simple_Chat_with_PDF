package ma.ensa.rag_ollama.entites;


import jakarta.persistence.*;
import lombok.*;

import java.util.Date;


@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;



    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

}
