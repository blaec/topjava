package ru.javawebinar.topjava.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.service.UserService;

import javax.servlet.http.HttpServletRequest;

@Controller
public class RootController {
    @Autowired
    private UserService service;

    @GetMapping(path = "/")
    public String root() {
        return "index";
    }

    @GetMapping(path = "/users")
    public String users(Model model) {
        model.addAttribute("users", service.getAll());
        return "users";
    }

    @PostMapping(path = "/users")
    public String setUser(HttpServletRequest request) {
        int userId = Integer.valueOf(request.getParameter("userId"));
        AuthorizedUser.setId(userId);
        return "redirect:meals";
    }
}
