package com.dell.hangman.api.representation.http;

import com.dell.hangman.api.representation.BaseDTO;
import com.dell.hangman.domain.game.StatusEnum;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
public class GameDTO extends BaseDTO {

    @Size(min = 3, max = 128)
    private String player;

    private String guessHits;
    private String guessMisses;
    private String word;
    private String hit;
    private String wordSecret;
    private StatusEnum status;
}
