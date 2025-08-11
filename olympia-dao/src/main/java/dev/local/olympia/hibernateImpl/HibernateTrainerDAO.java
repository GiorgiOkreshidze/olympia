package dev.local.olympia.hibernateImpl;

import dev.local.olympia.domain.Trainer;
import dev.local.olympia.domain.Training;
import dev.local.olympia.interfaces.TrainerDAO;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository("hibernateTrainerDAO")
public class HibernateTrainerDAO extends AbstractHibernateDAO<Trainer, String> implements TrainerDAO {
    public HibernateTrainerDAO() {
        super(Trainer.class);
    }

    @Override
    public Trainer save(Trainer trainer) {
        Session session = getCurrentSession();
        session.persist(trainer);
        return trainer;
    }

    @Override
    public Optional<Trainer> findById(String id) {
        Session session = getCurrentSession();
        try {
            Trainer trainer = session.createQuery("FROM Trainer WHERE id = :id", Trainer.class)
                    .setParameter("id", id)
                    .uniqueResult();
            return Optional.ofNullable(trainer);
        } catch (Exception e) {
            System.err.println("Error finding trainer by ID: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<Trainer> findByUsername(String username) {
        Session session = getCurrentSession();
        try {
            Trainer trainer = session.createQuery("FROM Trainer t JOIN FETCH t.user WHERE username = :username", Trainer.class)
                    .setParameter("username", username)
                    .uniqueResult();
            return Optional.ofNullable(trainer);
        } catch (Exception e) {
            System.err.println("Error finding trainer by username: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public List<Trainer> findAll() {
        Session session = getCurrentSession();
        try {
            return session.createQuery("FROM Trainer", Trainer.class).getResultList();
        } catch (Exception e) {
            System.err.println("Error finding all trainers: " + e.getMessage());
            return List.of();
        }
    }

    @Override
    public boolean existsById(String id) {
        return false;
    }

    @Override
    public List<Trainer> findByFirstNameAndLastName(String firstName, String lastName) {
        return List.of();
    }

    @Override
    public List<Training> findTrainingsByTrainer(Trainer trainer, LocalDate fromDate, LocalDate toDate, String traineeName) {
        Session session = getCurrentSession();
        String hql = "FROM Training t WHERE t.trainer = :trainer " +
                     "AND t.date >= :fromDate AND t.date <= :toDate " +
                     "AND (:traineeName IS NULL OR t.trainee.name = :traineeName)";

        return session.createQuery(hql, Training.class)
                      .setParameter("trainer", trainer)
                      .setParameter("fromDate", fromDate)
                      .setParameter("toDate", toDate)
                      .setParameter("traineeName", traineeName)
                      .getResultList();
    }
}
