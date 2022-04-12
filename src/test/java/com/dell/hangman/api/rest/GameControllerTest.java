package com.dell.hangman.api.rest;

import com.dell.hangman.Dictionary;
import com.dell.hangman.common.GameMock;
import com.dell.hangman.domain.exception.InvalidGuessException;
import com.dell.hangman.domain.game.Game;
import com.dell.hangman.service.GameService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
@WebAppConfiguration
@WebMvcTest(value = GameController.class)
class GameControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @SpyBean
    private ModelMapper modelMapper;

    @MockBean
    private GameService mockGameService;

    @Mock
    private Dictionary dictionary;

    private Game game;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        game = GameMock.buildGameOpen();
    }

    @Test
    void testCreate() throws Exception {
        when(mockGameService.create(any())).thenReturn(game);
        when(dictionary.pickWord()).thenReturn(game.getWord());

        final MockHttpServletResponse response = mockMvc.perform(post("/game/")
                .param("player", "player")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    void testGuess() throws Exception {
        when(mockGameService.guess(any(), any())).thenReturn(game);
        final MockHttpServletResponse response = perfomGuess("A");

        assertThat(response.getStatus()).isEqualTo(HttpStatus.ACCEPTED.value());
    }

    private MockHttpServletResponse perfomGuess(String letter) throws Exception {
        MockHttpServletResponse response = mockMvc.perform(post("/game/{gameId}/guesses", game.getId())
                .param("letter", letter)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        return response;
    }

    @Test
    void testReadAll() throws Exception {
        final List<Game> games = List.of(game);
        when(mockGameService.findAll()).thenReturn(games);

        final MockHttpServletResponse response = mockMvc.perform(get("/game")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void testReadAll_GameServiceReturnsNoItems() throws Exception {
        when(mockGameService.findAll()).thenReturn(Collections.emptyList());
        final MockHttpServletResponse response = mockMvc.perform(get("/game")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();


        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("[]");
    }
}
