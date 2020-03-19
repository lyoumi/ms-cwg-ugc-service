package com.cwd.tg.ugc.services.impl;

import com.cwd.tg.ugc.clients.CoreWebClient;
import com.cwd.tg.ugc.data.internal.character.GameCharacter;
import com.cwd.tg.ugc.services.GameControllerService;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class GameControllerServiceImpl implements GameControllerService {

    private final CoreWebClient coreWebClient;

    @Override
    public Mono<GameCharacter> saveCharacter(GameCharacter gameCharacter) {
        return coreWebClient.saveUserCharacter(gameCharacter);
    }

    @Override
    public Mono<GameCharacter> getUserCharacter(String userId) {
        return coreWebClient.getUserCharacter(userId);
    }
}
