package dev.local.olympia.hibernateImpl;

import dev.local.olympia.domain.Trainee;
import dev.local.olympia.interfaces.TraineeDAO;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("hibernateTraineeDAO")
public class HibernateTraineeDAO extends AbstractHibernateDAO<Trainee, String> implements TraineeDAO {

    public HibernateTraineeDAO() {
        super(Trainee.class);
    }

    @Override
    public Trainee save(Trainee trainee) {
        Session session = getCurrentSession();
        session.persist(trainee);
        return trainee;
    }

    @Override
    public Optional<Trainee> findByUsername(String username) {
        Session session = getCurrentSession();
        try {
            Trainee trainee = session.createQuery("FROM Trainee t JOIN FETCH t.user u WHERE u.username = :username", Trainee.class)
                    .setParameter("username", username)
                    .uniqueResult();
            return Optional.ofNullable(trainee);
        } catch (Exception e) {
            System.err.println("Error finding trainee by username: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<Trainee> findById(String id) {
        Session session = getCurrentSession();
        try {
            Trainee trainee = session.createQuery("FROM Trainee t JOIN FETCH t.user WHERE t.user.id = :id", Trainee.class)
                    .setParameter("id", id)
                    .uniqueResult();
            return Optional.ofNullable(trainee);
        } catch (Exception e) {
            System.err.println("Error finding trainee by ID: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public List<Trainee> findAll() {
        Session session = getCurrentSession();
        try {
            Query<Trainee> query = session.createQuery("FROM Trainee t JOIN FETCH t.user", Trainee.class);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error finding all trainees: " + e.getMessage());
            return List.of();
        }
    }

    @Override
    public void delete(Trainee trainee) {
        Session session = getCurrentSession();
        try {
                session.remove(trainee);
        } catch (Exception e) {
            System.err.println("Error deleting trainee: " + e.getMessage());
        }
    }


    @Override
    public boolean existsById(String id) {
        return false;
    }
}
