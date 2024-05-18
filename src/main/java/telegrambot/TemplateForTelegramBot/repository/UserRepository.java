package telegrambot.TemplateForTelegramBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import telegrambot.TemplateForTelegramBot.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "select userName from User where userName=:userName")
    String getUserByUserName(String userName);
}
