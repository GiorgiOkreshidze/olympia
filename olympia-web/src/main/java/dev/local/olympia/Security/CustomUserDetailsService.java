package dev.local.olympia.Security;

import dev.local.olympia.domain.Trainee;
import dev.local.olympia.domain.Trainer;
import dev.local.olympia.interfaces.TraineeDAO;
import dev.local.olympia.interfaces.TrainerDAO;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final TraineeDAO traineeDAO;
    private final TrainerDAO trainerDAO;

    public CustomUserDetailsService(TraineeDAO traineeDAO, TrainerDAO trainerDAO) {
        this.traineeDAO = traineeDAO;
        this.trainerDAO = trainerDAO;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Trainer> trainer = trainerDAO.findByUsername(username);
        if (trainer.isPresent()) {
            return User.withUsername(trainer.get().getUser().getUsername())
                    .password(trainer.get().getUser().getPassword())
                    .build();
        }

        Optional<Trainee> trainee = traineeDAO.findByUsername(username);
        if (trainee.isPresent()) {
            return User.withUsername(trainee.get().getUser().getUsername())
                    .password(trainee.get().getUser().getPassword())
                    .build();
        }

        throw new UsernameNotFoundException("User not found");
    }
}
