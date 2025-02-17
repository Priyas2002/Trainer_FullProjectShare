package com.ems.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ems.entity.Manager;
import com.ems.repository.ManagerRepository;

@ExtendWith(MockitoExtension.class)
public class ManagerServiceTest {

    @Mock
    private ManagerRepository managerRepository;

    @InjectMocks
    private ManagerService managerService;

    // Test Get Manager by Email
    @Test
    public void testGetManagerByEmail() {
        // Arrange
        Manager manager = new Manager();
        manager.setEmail("test@company.com");
        manager.setFirstName("John");
        manager.setLastName("Doe");

        when(managerRepository.findByEmail("test@company.com")).thenReturn(Optional.of(manager));

        // Act
        Manager foundManager = managerService.getManagerByEmail("test@company.com");

        // Assert
        assertNotNull(foundManager);
        assertEquals("John", foundManager.getFirstName());
        assertEquals("Doe", foundManager.getLastName());
        verify(managerRepository, times(1)).findByEmail("test@company.com");
    }

    //  Test Authenticate Manager (Correct Password)
    @Test
    public void testAuthenticateManager_CorrectPassword() {
        // Arrange
        Manager manager = new Manager();
        manager.setEmail("test@company.com");
        manager.setPassword("password123");

        when(managerRepository.findByEmail("test@company.com")).thenReturn(Optional.of(manager));

        // Act
        boolean isAuthenticated = managerService.authenticateManager("test@company.com", "password123");

        // Assert
        assertTrue(isAuthenticated);
        verify(managerRepository, times(1)).findByEmail("test@company.com");
    }

    
    
}
