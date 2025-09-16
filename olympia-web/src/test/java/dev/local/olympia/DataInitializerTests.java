package dev.local.olympia;

import dev.local.olympia.interfaces.MapStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataInitializerTests {

    /*@Mock
    private MapStorage<Object> mockMapStorage;

    @InjectMocks
    private DataInitializer dataInitializer;

    @TempDir
    Path tempDir;

    private File tempInitialDataFile;

    @BeforeEach
    void setUp() throws IOException {
        tempInitialDataFile = tempDir.resolve("temp-initial-data.txt").toFile();
    }


    @Test
    @DisplayName("Should not initialize if initialDataPath is null or empty")
    void afterPropertiesSet_NoPath_SkipsInitialization() throws Exception {
        dataInitializer.setInitialDataPath(null);
        dataInitializer.afterPropertiesSet();
        verify(mockMapStorage, never()).save(any(), anyString(), any());

        dataInitializer.setInitialDataPath("");
        dataInitializer.afterPropertiesSet();
        verify(mockMapStorage, never()).save(any(), anyString(), any());
    }*/
}
