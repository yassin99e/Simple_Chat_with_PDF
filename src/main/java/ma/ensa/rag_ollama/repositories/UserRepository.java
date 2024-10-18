package ma.ensa.rag_ollama.repositories;


import ma.ensa.rag_ollama.entites.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    User findByUsername(String username);
}
