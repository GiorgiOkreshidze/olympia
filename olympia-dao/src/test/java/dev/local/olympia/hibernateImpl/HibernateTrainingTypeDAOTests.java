package dev.local.olympia.hibernateImpl;

import dev.local.olympia.domain.TrainingType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HibernateTrainingTypeDAOTests {

    @Mock
    private SessionFactory sessionFactory;
    @Mock
    private Session session;
    @Mock
    private Query<TrainingType> query;
    @Mock
    private Query<Integer> intQuery;

    @InjectMocks
    private HibernateTrainingTypeDAO trainingTypeDAO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(sessionFactory.getCurrentSession()).thenReturn(session);
    }

    @Test
    void save_ShouldPersistTrainingType() {
        TrainingType trainingType = new TrainingType();
        TrainingType result = trainingTypeDAO.save(trainingType);
        verify(session).persist(trainingType);
        assertEquals(trainingType, result);
    }

    @Test
    void findByName_ShouldReturnTrainingType() {
        TrainingType trainingType = new TrainingType();
        when(session.createQuery(anyString(), eq(TrainingType.class))).thenReturn(query);
        when(query.setParameter(eq("name"), anyString())).thenReturn(query);
        when(query.uniqueResult()).thenReturn(trainingType);

        TrainingType result = trainingTypeDAO.findByName("Yoga");
        assertEquals(trainingType, result);
    }

    @Test
    void count_ShouldReturnCorrectCount() {
        when(session.createQuery(anyString(), eq(Integer.class))).thenReturn(intQuery);
        when(intQuery.uniqueResult()).thenReturn(5);

        int count = trainingTypeDAO.count();
        assertEquals(5, count);
    }
}

