		package com.ems.controller;

import com.ems.entity.Manager;
import com.ems.service.ManagerService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/") // Base URL for EMS module
public class EmsController {

    private final ManagerService managerService;

    public EmsController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @GetMapping("")
    public String showHome() {
        return "index";
    }

    @GetMapping("loginManager")
    public String showLoginManager() {
        return "login-manager";
    }
    @PostMapping("login-manager")
    public String loginManager(@RequestParam String email, 
                               @RequestParam String password, 
                               HttpServletRequest request) {

        Manager manager = managerService.getManagerByEmail(email);

        if (manager != null && managerService.authenticateManager(email, password)) {
            // Create session and store manager's name & email
            HttpSession session = request.getSession();
            session.setAttribute("managerFirstName", manager.getFirstName());
            session.setAttribute("managerLastName", manager.getLastName());
            session.setAttribute("managerEmail", manager.getEmail());

            return "redirect:/manager/dashboard";
        } else {
            return "redirect:loginManager?error=true";
        }
    }

    // Logout Manager - Invalidate Session
    @GetMapping("/logout")
    public String logoutManager(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false); // Get session if exists
        if (session != null) {
            session.invalidate(); 
        }
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        
        return "redirect:/loginManager";
    }
    
    @GetMapping("/aboutUs")
    public String showAboutUs() {
    	return "aboutUs";
    }
    
    
}
