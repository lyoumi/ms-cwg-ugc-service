package com.cwd.tg.ugc.handlers;

import com.cwd.tg.ugc.data.input.PlayerCharacterInput;
import com.cwd.tg.ugc.data.internal.character.GameCharacter;

import reactor.core.publisher.Mono;

public interface GameRequestHandler {

    Mono<GameCharacter> createGameCharacter(PlayerCharacterInput playerCharacterInput);

    Mono<GameCharacter> getUserGameCharacter(String userId);
}
