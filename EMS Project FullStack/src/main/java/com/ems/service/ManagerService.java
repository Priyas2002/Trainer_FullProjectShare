package com.ems.service;

import com.ems.entity.Manager;
import com.ems.repository.ManagerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class ManagerService {

    private static final Logger logger = LoggerFactory.getLogger(ManagerService.class);

    @Autowired
    private ManagerRepository managerRepository;

    // Fetch Manager by Email 
    public Manager getManagerByEmail(String email) {
        try {
            Optional<Manager> managerOpt = managerRepository.findByEmail(email);
            if (managerOpt.isPresent()) {
                logger.info("Manager found with email: {}", email);
                return managerOpt.get();
            } else {
                logger.warn("No manager found with email: {}", email);
                return null; 
            }
        } catch (Exception e) {
            logger.error("Error fetching manager by email: {}", email, e);
            throw new RuntimeException("Error retrieving manager details", e);
        }
    }

    // Authenticate Manager 
    public boolean authenticateManager(String email, String password) {
        try {
            Optional<Manager> managerOpt = managerRepository.findByEmail(email);

            if (managerOpt.isEmpty()) {
                logger.warn("Authentication failed: No manager found with email: {}", email);
                return false; 
            }

            Manager manager = managerOpt.get();

            if (!manager.getPassword().equals(password)) {
                logger.warn("Authentication failed: Incorrect password for email: {}", email);
                return false; // Incorrect password
            }

            logger.info("Manager authentication successful for email: {}", email);
            return true; // Successful authentication

        } catch (Exception e) {
            logger.error("Error during authentication for email: {}", email, e);
            throw new RuntimeException("Authentication error", e);
        }
    }
}