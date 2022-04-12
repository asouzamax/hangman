package com.dell.hangman.service;

import com.dell.hangman.Dictionary;
import com.dell.hangman.common.GameMock;
import com.dell.hangman.domain.exception.BusinessException;
import com.dell.hangman.domain.exception.GameNotFoundException;
import com.dell.hangman.domain.exception.InvalidGuessException;
import com.dell.hangman.domain.game.Game;
import com.dell.hangman.domain.game.GameRepository;
import com.dell.hangman.domain.game.StatusEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private GameRepository mockRepository;
    @Mock
    private Dictionary mockDictionary;

    @InjectMocks
    private GameService gameServiceUnderTest;

    private Game game;

    @BeforeEach
    void setUp() {
        game = GameMock.buildGameOpen();
    }

    @Test
    void testCreate() {
        when(mockDictionary.pickWord()).thenReturn(game.getWord());
        when(mockRepository.save(any(Game.class))).thenReturn(game);

        final Game result = gameServiceUnderTest.create(game.getPlayer());
        assertThat(result).isNotNull();
        verify(mockRepository).save(any(Game.class));
    }

    @Test
    void testGuessesWhenPlayerWon() {
        when(mockRepository.findById(any())).thenReturn(Optional.of(game));
        when(mockRepository.save(any(Game.class))).thenReturn(game);

        Set<String> wordSet = new HashSet<>(Arrays.asList(game.getWord().split("")));
        AtomicReference<Game> result = new AtomicReference<>();
        wordSet.forEach(letter->{
            result.set(gameServiceUnderTest.guess(letter, game.getId()));
        });

        assertThat(result.get().getStatus()).isEqualTo(StatusEnum.SOLVED);
    }

    @Test
    void testGuessesWhenMaxHitsReached() {
        game.setGuessHits("XYZOBQDU");
        when(mockRepository.findById(any())).thenReturn(Optional.of(game));
        when(mockRepository.save(any(Game.class))).thenReturn(game);

        Game result = gameServiceUnderTest.guess("P", game.getId());
        assertThat(result.getStatus()).isEqualTo(StatusEnum.FAILED);
    }

    @Test
    void testGuess_ThrowsInvalidGuessException() {
        String uuid = UUID.randomUUID().toString();
        Throwable exception = assertThrows(InvalidGuessException.class, () -> {
            gameServiceUnderTest.guess("", uuid);
        });
        assertEquals(String.format("Guess '' invalid: must be a single letter", uuid), exception.getMessage());

    }

    @Test
    void testGuess_ThrowsGameNotFoundException() {
        when(mockRepository.findById(any())).thenReturn(Optional.empty());

        String uuid = UUID.randomUUID().toString();
        Throwable exception = assertThrows(GameNotFoundException.class, () -> {
            gameServiceUnderTest.guess("A", uuid);
        });
        assertEquals(String.format("Game with id '%s' not found", uuid), exception.getMessage());

    }

    @Test
    void testGuess_ThrowsBusinessException() {
        when(mockRepository.findById(any())).thenReturn(Optional.of(game));
        Optional<Character> letterOptional = game.getWord().codePoints().mapToObj(i -> (char) i).findFirst();
        game.setGuessHits(letterOptional.get().toString());
        Throwable exception = assertThrows(BusinessException.class, () -> {
            gameServiceUnderTest.guess(letterOptional.get().toString(), game.getId());
        });
        assertEquals("Letter already been sent", exception.getMessage());

    }

    @Test
    void testFindAll() {
        final List<Game> games = List.of(game);
        when(mockRepository.findAll()).thenReturn(games);
        final List<Game> result = gameServiceUnderTest.findAll();
        assertThat(result).isNotEmpty();
    }

    @Test
    void testFindAll_GameRepositoryReturnsNoItems() {
        when(mockRepository.findAll()).thenReturn(Collections.emptyList());
        final List<Game> result = gameServiceUnderTest.findAll();
        assertThat(result).isEqualTo(Collections.emptyList());
    }
}
