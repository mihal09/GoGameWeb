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
    public String search(Model model, @RequestParam("board")int boardId){
        GameManager gameManager = new GameManager();
        GamesEntity game = gameManager.getGame(boardId);
        if(game!=null) {
            int size = game.getBoardSize();
            model.addAttribute("boardSize", size);
            return "redirect:/welcome?gameID=" + boardId;
        }
        return "redirect:/welcome";
    }

    @RequestMapping(value = "/createGame", method = RequestMethod.POST)
    public String createGame(Model model, @RequestParam("size") int size, @RequestParam("player1Name") String player1Name, @RequestParam("player2Name") String player2Name){
        GameManager gameManager = new GameManager();
        PlayerManager playerManager = new PlayerManager();
        int player1Id = playerManager.getPlayer(player1Name).getId();
        int player2Id = playerManager.getPlayer(player2Name).getId();
        gameManager.addGame(size, player1Id, player2Id);
        int gameId = gameManager.getLastGame().getId();
        model.addAttribute("gameID", gameId);
        return "search";
    }

    @RequestMapping(value = "/welcome", method = RequestMethod.GET)
    public String welcome(@RequestParam(name = "gameID") int gameID, @RequestParam(name="boardSize") int boardSize, Model model) {
        model.addAttribute("message", gameID);
        model.addAttribute("boardSize", boardSize);
        return "welcome";
    }

    @RequestMapping(value = "/makeMove", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    public String Submit(Model model, @RequestParam("x") int x, @RequestParam("y") int y, @RequestParam("user") String name, @RequestParam("gameID") int gameID){
        model.addAttribute("test","121");
        String previousBoard= "";
        String currentBoard = "";
        String playerToMove = "B";
        int boardSize = 0;

        synchronized (this){
            GameStateManager gameStateManager = new GameStateManager();
            GameManager gameManager = new GameManager();
            PlayerManager playerManager = new PlayerManager();
            GamesstatesEntity gameState = gameStateManager.getLastGameState(gameID);
            GamesEntity gameEntity = gameManager.getGame(gameID);
            UsersEntity user = playerManager.getPlayer(name);
            int userId = user.getId();
            if(gameEntity == null)
                return "data";
            if(gameState!=null) {
                previousBoard = gameState.getLastMove();
                currentBoard = gameState.getGrid();
                playerToMove = gameState.getNextMove();
            }
            boardSize = gameEntity.getBoardSize();
            char player;
            if(userId == gameEntity.getIdPlayerOne())
                player = 'B';
            else if(userId == gameEntity.getIdPlayerTwo())
                player = 'W';
            else
                return "data";

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


    @RequestMapping(value = "/revert", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    public String Submit(@RequestParam(value = "gameID", required = false) Integer gameID){
        if(gameID != null) {
            System.out.println(gameID);
            GameStateManager gameStateManager = new GameStateManager();
            Boolean success = gameStateManager.deleteLastState(gameID);
            System.out.println("SUCCESS: "+success);
        }
        return "";
    }

    @RequestMapping(value = "/getBoard", method = RequestMethod.GET)
    public String GetBoard(Model model, @RequestParam("ID") int gameID){
        GameStateManager gameStateManager = new GameStateManager();
        GamesstatesEntity gameState = gameStateManager.getLastGameState(gameID);
        String currentBoard;
        if(gameState != null) {
            currentBoard = gameState.getGrid();
        }
        else {
            currentBoard = "";
        }
        model.addAttribute("test",0+"#"+ currentBoard);
        return "data";
    }


}
