package com.cwd.tg.ugc.services;

import com.cwd.tg.ugc.data.internal.character.GameCharacter;

import reactor.core.publisher.Mono;

public interface GameControllerService {
    Mono<GameCharacter> saveCharacter(GameCharacter gameCharacter);

    Mono<GameCharacter> getUserCharacter(String userId);
}
