package com.dell.hangman.api.rest;

import com.dell.hangman.api.representation.http.GameDTO;
import com.dell.hangman.domain.game.Game;
import com.dell.hangman.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class GameUIController extends MapperRest<Game, GameDTO> {
    private  GameService gameService;

    @Autowired
    public GameUIController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("gameDTO", new GameDTO());
        return "index";
    }

    @PostMapping("/newGame")
    public String newGameSubmit(@ModelAttribute GameDTO gameDTO, Model model) {
        model.addAttribute("gameDTO", fromModel(gameService.create(gameDTO.getPlayer())));
        return "gameSession";
    }

    @PostMapping("/newGuess/{id}")
    public String newGuessSubmit(@PathVariable("id") String id, @Valid GameDTO gameDTO, Model model) {
        try{
            model.addAttribute("gameDTO", fromModel(gameService.guess(gameDTO.getHit(), id)));
        }catch (Exception e){
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "gameSession";
    }
}
