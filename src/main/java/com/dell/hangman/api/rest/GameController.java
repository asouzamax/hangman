package com.dell.hangman.api.rest;

import com.dell.hangman.api.representation.http.GameDTO;
import com.dell.hangman.domain.exception.InvalidGuessException;
import com.dell.hangman.domain.game.Game;
import com.dell.hangman.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("game")
@RequiredArgsConstructor
public class GameController extends MapperRest<Game, GameDTO> {

    private final GameService gameService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GameDTO create(@Valid @RequestParam String player) {
        final var gameCreated = gameService.create(player);
        return fromModel(gameCreated);
    }

    @RequestMapping(path = "/{gameId}/guesses", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public GameDTO guess(@PathVariable String gameId, @RequestParam String letter) {
        return fromModel(gameService.guess(letter, gameId));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<GameDTO> readAll() {
        return fromModel(gameService.findAll());
    }
}
