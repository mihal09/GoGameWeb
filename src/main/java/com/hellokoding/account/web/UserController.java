package com.hellokoding.account.web;

import com.hellokoding.account.model.User;
import com.hellokoding.account.service.SecurityService;
import com.hellokoding.account.service.UserService;
import com.hellokoding.account.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserValidator userValidator;

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registration(Model model) {
        model.addAttribute("userForm", new User());

        return "registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registration(@ModelAttribute("userForm") User userForm, BindingResult bindingResult, Model model) {
        userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "registration";
        }

        userService.save(userForm);

        securityService.autologin(userForm.getUsername(), userForm.getPasswordConfirm());

        return "redirect:/search";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("error", "Your username and password is invalid.");

        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");

        return "login";
    }
    @RequestMapping(value = {"/","/search"}, method = RequestMethod.GET)
    public String search(Model model) {
        model.addAttribute("userForm", new User());

        return "search";
    }
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String search(@RequestParam("board")String s){
        return "redirect:/welcome?gameID="+s;
    }

    @RequestMapping(value = "/welcome", method = RequestMethod.GET)
    public String welcome(@RequestParam(name = "gameID") int gameID,  Model model) {
        model.addAttribute("message", gameID);
        return "welcome";
    }

    @RequestMapping(value = "/submit", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    public String Submit(@RequestParam("x") int x, @RequestParam("y") int y, @RequestParam("user") String name, @RequestParam("gameID") int gameID){
        System.out.println(name);
        return "welcome";
    }



}
