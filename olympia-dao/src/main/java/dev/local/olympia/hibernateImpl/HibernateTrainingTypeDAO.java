package dev.local.olympia.hibernateImpl;

import dev.local.olympia.domain.TrainingType;
import dev.local.olympia.interfaces.TrainingTypeDAO;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

@Repository
public class HibernateTrainingTypeDAO extends AbstractHibernateDAO<TrainingType, String> implements TrainingTypeDAO {

    public HibernateTrainingTypeDAO() {
        super(TrainingType.class);
    }

    @Override
    public TrainingType findByName(String name) {
        Session session = getCurrentSession();

        return session.createQuery("FROM TrainingType WHERE trainingTypeName = :name", TrainingType.class)
                .setParameter("name", name)
                .uniqueResult();
    }
}
