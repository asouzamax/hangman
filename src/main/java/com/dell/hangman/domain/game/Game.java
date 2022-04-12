package com.dell.hangman.domain.game;

import com.dell.hangman.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Game extends BaseEntity {

    private String player;
    private String guessHits;
    private String word;
    private StatusEnum status;
    private int maxFailedHits;

    @Transient
    private String wordSecret;

    private static final String PASSWORD_PATTERN = "[a-zA-Z]";

    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    public static boolean isGuessValid(final String guess) {
        Matcher matcher = pattern.matcher(guess);
        return matcher.matches();
    }

    public String getWordSecret() {
        final StringBuilder result = new StringBuilder();
        Set<Character> guessedHitsSet = guessHits.codePoints()
                .mapToObj(i -> (char) i).collect(Collectors.toSet());

        word.codePoints().mapToObj(i -> (char) i).map(character -> {
            if (guessedHitsSet.contains(character)) {
                return character;
            } else {
                return '_';
            }
        }).forEach(result::append);
        this.wordSecret = result.toString();
        return this.wordSecret;
    }

    public void hitGuess(String letter){
        this.guessHits += letter;
        if(getWordSecret().equals(getWord())) {
            this.setStatus(StatusEnum.SOLVED);
        }else if(guessHits.length() > getMaxFailedHits()){
            this.setStatus(StatusEnum.FAILED);
        }
    }
}
