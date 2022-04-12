package com.dell.hangman.common;

import com.dell.hangman.domain.game.Game;
import com.dell.hangman.domain.game.StatusEnum;
import com.dell.hangman.service.GameService;
import com.github.javafaker.Faker;

import java.time.OffsetDateTime;
import java.util.Random;
import java.util.UUID;

public class GameMock {
    public static Game buildGameOpen() {
        final String gameUuid = UUID.randomUUID().toString();
        Faker faker = new Faker();
        Game game = Game.builder()
                .status(StatusEnum.OPEN)
                .guessHits("")
                .word(faker.name().firstName().toUpperCase())
                .maxFailedHits(GameService.MAX_FAILED_HITS)
                .build();

        game.setId(gameUuid);
        game.setRegistrationDate(OffsetDateTime.now());
        return game;
    }
}
