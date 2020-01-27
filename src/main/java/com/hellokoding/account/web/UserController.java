package com.hellokoding.account.web;

import com.hellokoding.account.GameLogic.MoveValidation;
import com.hellokoding.account.managers.GameManager;
import com.hellokoding.account.managers.GameStateManager;
import com.hellokoding.account.managers.PlayerManager;
import com.hellokoding.account.model.GamesEntity;
import com.hellokoding.account.model.GamesstatesEntity;
import com.hellokoding.account.model.User;
import com.hellokoding.account.model.UsersEntity;
import com.hellokoding.account.service.SecurityService;
import com.hellokoding.account.service.UserService;
import com.hellokoding.account.service.UserServiceImpl;
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
    public String welcome(@RequestParam(name = "gameID") int gameID, @RequestParam(name="boardSize") int boardSize, Model model) {
        model.addAttribute("message", gameID);
        model.addAttribute("boardSize", boardSize);
        return "welcome";
    }

    @RequestMapping(value = "/submit", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    public String Submit(Model model, @RequestParam("x") int x, @RequestParam("y") int y, @RequestParam("user") String name, @RequestParam("gameID") int gameID){
        model.addAttribute("test","121");
        String previousBoard= "";
        String currentBoard = "";
        int boardSize = 0;

        synchronized (this){
            GameStateManager gameStateManager = new GameStateManager();
            GameManager gameManager = new GameManager();
            PlayerManager playerManager = new PlayerManager();
            GamesstatesEntity gameState = gameStateManager.getLastGameState(gameID);
            GamesEntity gameEntity = gameManager.getGame(gameID);
            UsersEntity user = playerManager.getPlayer(name);
            int userId = user.getId();
            previousBoard = gameState.getLastMove();
            currentBoard = gameState.getGrid();
            boardSize = gameEntity.getBoardSize();
            char player;
            if(userId == gameEntity.getIdPlayerOne())
                player = 'B';
            else if(userId == gameEntity.getIdPlayerTwo())
                player = 'W';
            else
                return "data";
            String playerToMove = gameState.getNextMove();
            if(playerToMove.charAt(0) != player) {
                return "data";
            }
            String futureBoard = MoveValidation.MakeMove(currentBoard, previousBoard, boardSize, x, y, player);
            if(!currentBoard.equals(futureBoard)) {
                if(playerToMove.equals("B"))
                    playerToMove = "W";
                else playerToMove = "B";
                gameStateManager.addState(gameID, futureBoard, currentBoard, previousBoard, playerToMove);
                System.out.println("SIEMANO");
            }
        }

        return "data";
    }
    @RequestMapping(value = "/submit", method = RequestMethod.GET)
    public String Submit(Model model, @RequestParam("ID") int gameID){
        GameStateManager gameStateManager = new GameStateManager();
        GamesstatesEntity gameState = gameStateManager.getLastGameState(gameID);
        String currentBoard = gameState.getGrid();
        System.out.println(gameState.getId() + " - "+currentBoard);
        model.addAttribute("test",0+"#"+ currentBoard);
        return "data";
    }


}
