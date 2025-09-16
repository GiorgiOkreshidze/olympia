package dev.local.olympia.hibernateImpl;

import dev.local.olympia.domain.Trainee;
import dev.local.olympia.domain.Training;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HibernateTrainingDAOTests {

    @Mock
    private SessionFactory sessionFactory;
    @Mock
    private Session session;
    @Mock
    private Query<Training> query;

    @InjectMocks
    private HibernateTrainingDAO trainingDAO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(sessionFactory.getCurrentSession()).thenReturn(session);
    }

    @Test
    void save_ShouldPersistTraining() {
        Training training = new Training();
        Training result = trainingDAO.save(training);
        verify(session).persist(training);
        assertEquals(training, result);
    }

    @Test
    void findById_ShouldReturnTraining() {
        Training training = new Training();
        when(session.createQuery(anyString(), eq(Training.class))).thenReturn(query);
        when(query.setParameter(eq("id"), anyString())).thenReturn(query);
        when(query.uniqueResult()).thenReturn(training);

        Optional<Training> result = trainingDAO.findById("123");
        assertTrue(result.isPresent());
    }

    @Test
    void findAll_ShouldReturnList() {
        Training training = new Training();
        when(session.createQuery(anyString(), eq(Training.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(training));

        List<Training> result = trainingDAO.findAll();
        assertEquals(1, result.size());
    }

    @Test
    void findTrainingsByTrainee_ShouldReturnList() {
        Trainee trainee = new Trainee();
        LocalDate now = LocalDate.now();

        when(session.createQuery(anyString(), eq(Training.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(new Training()));

        List<Training> result = trainingDAO.findTrainingsByTrainee(trainee, now, now, null, null);
        assertEquals(1, result.size());
    }
}

