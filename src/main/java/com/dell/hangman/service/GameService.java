package com.dell.hangman.service;

import com.dell.hangman.Dictionary;
import com.dell.hangman.domain.exception.BusinessException;
import com.dell.hangman.domain.exception.GameNotFoundException;
import com.dell.hangman.domain.exception.InvalidGuessException;
import com.dell.hangman.domain.game.Game;
import com.dell.hangman.domain.game.GameRepository;
import com.dell.hangman.domain.game.StatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequestScope
@RequiredArgsConstructor
public class GameService {

    public static final int MAX_FAILED_HITS = 8;

    private final GameRepository repository;
    private final Dictionary dictionary;

    public Game create(String player) {
        Game game = Game.builder()
                .status(StatusEnum.OPEN)
                .player(player)
                .guessHits("")
                .word(dictionary.pickWord().toUpperCase())
                .maxFailedHits(MAX_FAILED_HITS)
                .build();

        game.setId(null);
        game.setRegistrationDate(OffsetDateTime.now());
        return repository.save(game);

    }

    public Game guess(String letter, String gameId) throws BusinessException {
        if (!Game.isGuessValid(letter)) {
            throw new InvalidGuessException(String.format("Guess '%s' invalid: must be a single letter", letter));
        }

        final var game = repository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException(gameId));

        letter = letter.toUpperCase();
        if (Objects.nonNull(game.getGuessHits()) && game.getGuessHits().contains(letter)) {
            throw new InvalidGuessException("Letter already been sent");
        }

        game.hitGuess(letter);
        game.setWordSecret(game.getWordSecret());
        repository.save(game);

        return game;
    }

    public List<Game> findAll(){
        return repository.findAll();
    }
}
