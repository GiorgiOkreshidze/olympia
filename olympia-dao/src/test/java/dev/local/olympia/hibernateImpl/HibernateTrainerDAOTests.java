package dev.local.olympia.hibernateImpl;

import dev.local.olympia.domain.Trainer;
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

class HibernateTrainerDAOTests {

    @Mock
    private SessionFactory sessionFactory;
    @Mock
    private Session session;
    @Mock
    private Query<Trainer> trainerQuery;
    @Mock
    private Query<Training> trainingQuery;
    @Mock
    private Query<Long> longQuery;

    @InjectMocks
    private HibernateTrainerDAO trainerDAO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(sessionFactory.getCurrentSession()).thenReturn(session);
    }

    @Test
    void save_ShouldPersistTrainer() {
        Trainer trainer = new Trainer();
        Trainer result = trainerDAO.save(trainer);
        verify(session).persist(trainer);
        assertEquals(trainer, result);
    }

    @Test
    void findById_ShouldReturnTrainer() {
        Trainer trainer = new Trainer();
        when(session.createQuery(anyString(), eq(Trainer.class))).thenReturn(trainerQuery);
        when(trainerQuery.setParameter(eq("id"), anyString())).thenReturn(trainerQuery);
        when(trainerQuery.uniqueResult()).thenReturn(trainer);

        Optional<Trainer> result = trainerDAO.findById("123");
        assertTrue(result.isPresent());
    }

    @Test
    void findByUsername_ShouldReturnEmpty_WhenNotFound() {
        when(session.createQuery(anyString(), eq(Trainer.class))).thenReturn(trainerQuery);
        when(trainerQuery.setParameter(eq("username"), anyString())).thenReturn(trainerQuery);
        when(trainerQuery.uniqueResult()).thenReturn(null);

        Optional<Trainer> result = trainerDAO.findByUsername("abc");
        assertTrue(result.isEmpty());
    }

    @Test
    void findAll_ShouldReturnList() {
        Trainer trainer = new Trainer();
        when(session.createQuery(anyString(), eq(Trainer.class))).thenReturn(trainerQuery);
        when(trainerQuery.getResultList()).thenReturn(List.of(trainer));

        List<Trainer> result = trainerDAO.findAll();
        assertEquals(1, result.size());
    }

    @Test
    void existsById_ShouldReturnTrue() {
        when(session.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
        when(longQuery.setParameter(eq("id"), anyString())).thenReturn(longQuery);
        when(longQuery.uniqueResult()).thenReturn(1L);

        assertTrue(trainerDAO.existsById("123"));
    }

    @Test
    void findTrainingsByTrainer_ShouldReturnList() {
        Trainer trainer = new Trainer();
        LocalDate now = LocalDate.now();

        when(session.createQuery(anyString(), eq(Training.class))).thenReturn(trainingQuery);
        when(trainingQuery.setParameter(anyString(), any())).thenReturn(trainingQuery);
        when(trainingQuery.getResultList()).thenReturn(List.of(new Training()));

        List<Training> result = trainerDAO.findTrainingsByTrainer(trainer, now, now, null);
        assertEquals(1, result.size());
    }

    @Test
    void findUnassignedTrainers_ShouldReturnList() {
        Trainer trainer = new Trainer();
        when(session.createQuery(anyString(), eq(Trainer.class))).thenReturn(trainerQuery);
        when(trainerQuery.setParameter(eq("traineeUsername"), anyString())).thenReturn(trainerQuery);
        when(trainerQuery.getResultList()).thenReturn(List.of(trainer));

        List<Trainer> result = trainerDAO.findUnassignedTrainers("user1");
        assertEquals(1, result.size());
    }
}

