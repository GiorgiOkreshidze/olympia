package dev.local.olympia.hibernateImpl;

import dev.local.olympia.domain.Trainee;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HibernateTraineeDAOTests {

    @Mock
    private SessionFactory sessionFactory;
    @Mock
    private Session session;
    @Mock
    private Query<Trainee> query;

    @InjectMocks
    private HibernateTraineeDAO traineeDAO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(sessionFactory.getCurrentSession()).thenReturn(session);
    }

    @Test
    void save_ShouldPersistTrainee() {
        Trainee trainee = new Trainee();
        Trainee result = traineeDAO.save(trainee);
        verify(session).persist(trainee);
        assertEquals(trainee, result);
    }

    @Test
    void findByUsername_ShouldReturnTrainee_WhenFound() {
        Trainee trainee = new Trainee();
        when(session.createQuery(anyString(), eq(Trainee.class))).thenReturn(query);
        when(query.setParameter(eq("username"), anyString())).thenReturn(query);
        when(query.uniqueResult()).thenReturn(trainee);

        Optional<Trainee> result = traineeDAO.findByUsername("john.doe");

        assertTrue(result.isPresent());
        assertEquals(trainee, result.get());
    }

    @Test
    void findById_ShouldReturnEmpty_WhenNotFound() {
        when(session.createQuery(anyString(), eq(Trainee.class))).thenReturn(query);
        when(query.setParameter(eq("id"), anyString())).thenReturn(query);
        when(query.uniqueResult()).thenReturn(null);

        Optional<Trainee> result = traineeDAO.findById("123");

        assertTrue(result.isEmpty());
    }

    @Test
    void findAll_ShouldReturnListOfTrainees() {
        Trainee trainee = new Trainee();
        when(session.createQuery(anyString(), eq(Trainee.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(trainee));

        List<Trainee> result = traineeDAO.findAll();

        assertEquals(1, result.size());
        assertEquals(trainee, result.get(0));
    }

    @Test
    void existsById_ShouldReturnTrue_WhenExists() {
        Query<Long> longQuery = mock(Query.class);
        when(session.createQuery(anyString(), eq(Long.class))).thenReturn(longQuery);
        when(longQuery.setParameter(eq("id"), anyString())).thenReturn(longQuery);
        when(longQuery.uniqueResult()).thenReturn(1L);

        boolean exists = traineeDAO.existsById("123");
        assertTrue(exists);
    }

    @Test
    void delete_ShouldCallRemove() {
        Trainee trainee = new Trainee();
        traineeDAO.delete(trainee);
        verify(session).remove(trainee);
    }
}

