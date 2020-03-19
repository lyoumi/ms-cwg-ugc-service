package com.cwd.tg.ugc.clients;

import com.cwd.tg.ugc.data.internal.character.GameCharacter;

import reactor.core.publisher.Mono;

public interface CoreWebClient {

    Mono<GameCharacter> getUserCharacter(String userId);

    Mono<GameCharacter> saveUserCharacter(GameCharacter gameCharacter);
}
