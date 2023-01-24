package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.role.RoleService;
import ru.kata.spring.boot_security.demo.service.role.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.service.user.UserService;
import ru.kata.spring.boot_security.demo.service.user.UserServiceImpl;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserServiceImpl userService, RoleServiceImpl roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/")
    public String userPage(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.loadUserByEmail(userDetails.getUsername());


        model.addAttribute("user", user);

        return "/user";
    }

    @GetMapping("/show_users")
    public String showUsers(Model model) {

        List<User> users = userService.getAllUsers();

        model.addAttribute("users", users);

        return "users/out_users";
    }

    @GetMapping("/update")
    public String newUser(@ModelAttribute("user") User user) {

        return "users/info_users";
    }

    @PostMapping("/")
    public String addUser(@ModelAttribute("user") User user,
                          @RequestParam(value = "boolRole", required = false) boolean boolRole) {

        if (user.getId() == 0) {
            if (boolRole) {
                Role role = roleService.findByRole("ADMIN");
                role.addUserToRole(user);
            }
            userService.addUser(user);


        } else {
            User userWithRole = userService.getUser(user.getId());
            Role role = roleService.findByRole("ADMIN");

            if (boolRole && !userWithRole.getRoles().contains(role)) {
                role.addUserToRole(user);
            } else {
                userWithRole.getRoles().remove(role);
            }
            user.setRoles(userWithRole.getRoles());

            userService.updateUser(user);
        }
        return "redirect:/admin/show_users";
    }

    @PatchMapping("/update")
    public String updateUser(@RequestParam(value = "id", required = false) Long id,
                             Model model) {
        boolean boolRole = false;
        User user = userService.getUser(id);

        if (user.getRoles().contains(roleService.findByRole("ADMIN"))) {
                boolRole = true;
        }
        model.addAttribute("user", user);
        model.addAttribute("boolRole", boolRole);
        return "users/info_users";
    }

    @DeleteMapping("/remove")
    public String removeUser(@RequestParam(value = "id", required = false) Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/show_users";
    }
}
