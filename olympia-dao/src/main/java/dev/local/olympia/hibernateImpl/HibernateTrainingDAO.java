package dev.local.olympia.hibernateImpl;

import dev.local.olympia.domain.Trainee;
import dev.local.olympia.domain.Training;
import dev.local.olympia.interfaces.TrainingDAO;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository("hibernateTrainingDAO")
public class HibernateTrainingDAO extends AbstractHibernateDAO<Training, String> implements TrainingDAO {
    public HibernateTrainingDAO() {
        super(Training.class);
    }

    @Override
    public Training save(Training training) {
        Session session = getCurrentSession();
        session.persist(training);
        return training;
    }

    @Override
    public Optional<Training> findById(String id) {
        Session session = getCurrentSession();
        try {
            Training training = session.createQuery("FROM Training WHERE id = :id", Training.class)
                    .setParameter("id", id)
                    .uniqueResult();
            return Optional.ofNullable(training);
        } catch (Exception e) {
            System.err.println("Error finding training by ID: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public List<Training> findAll() {
        Session session = getCurrentSession();
        return session.createQuery("FROM Training", Training.class).getResultList();
    }

    @Override
    public List<Training> findTrainingsByTrainee(Trainee trainee, LocalDate fromDate, LocalDate toDate, String trainerName, String trainingType) {
        Session session = getCurrentSession();
        String hql = "FROM Training t WHERE t.trainee = :trainee " +
                     "AND t.date >= :fromDate AND t.date <= :toDate " +
                     "AND (:trainerName IS NULL OR t.trainer.name = :trainerName) " +
                     "AND (:trainingType IS NULL OR t.training_type_name = :trainingType)";

        return session.createQuery(hql, Training.class)
                      .setParameter("trainee", trainee)
                      .setParameter("fromDate", fromDate)
                      .setParameter("toDate", toDate)
                      .setParameter("trainerName", trainerName)
                      .setParameter("trainingType", trainingType)
                      .getResultList();
    }
}
