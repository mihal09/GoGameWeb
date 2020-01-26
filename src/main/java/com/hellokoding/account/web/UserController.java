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

import javax.validation.constraints.Null;

@Controller
public class UserController {
    public String[] xxx = new String[10000];
    public int[] player = new int[10000];
    public int[] currPlayer = new int[100000];
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
    public String search(){
        return "search";
    }
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String search(Model model, @RequestParam("board")int s, @RequestParam("size") int size){
        model.addAttribute("boardSize", size);
        model.addAttribute("c",player[s]);
        player[s]++;
        return "redirect:/welcome?gameID="+s;
    }

    @RequestMapping(value = "/welcome", method = RequestMethod.GET)
    public String welcome(@RequestParam(name = "gameID") int gameID, @RequestParam(name="boardSize") int boardSize, @RequestParam(name="c") int player, Model model) {
        model.addAttribute("message", gameID);
        model.addAttribute("boardSize", boardSize);
        model.addAttribute("c",player);
        return "welcome";
    }

    @RequestMapping(value = "/submit", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    public String Submit(Model model, @RequestParam("x") int x, @RequestParam("y") int y, @RequestParam("user") int name, @RequestParam("gameID") int gameID){

        xxx[gameID] = new String();
        for(int i = 0; i<13; i++)
        {
            for(int j = 0; j<13; j++)
            {
                if(i==x && j==y)xxx[gameID]+=(name==0? "B " : "W ");
                else xxx[gameID]+="N ";
            }
        }
        if(name==0)currPlayer[gameID]=1;
        else currPlayer[gameID] = 0;
        model.addAttribute("test",name);

        return "data";
    }
    @RequestMapping(value = "/submit", method = RequestMethod.GET)
    public String Submit(Model model, @RequestParam("ID") int gameID){
        model.addAttribute("test",currPlayer[gameID]+"#"+ xxx[gameID]);
        return "data";
    }


}
