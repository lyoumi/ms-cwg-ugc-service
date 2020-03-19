package com.cwd.tg.ugc.handlers.impl;

import static java.util.UUID.randomUUID;

import com.cwd.tg.ugc.data.input.PlayerCharacterInput;
import com.cwd.tg.ugc.data.internal.character.CharacterProgress;
import com.cwd.tg.ugc.data.internal.character.CharacterStats;
import com.cwd.tg.ugc.data.internal.character.GameCharacter;
import com.cwd.tg.ugc.data.internal.character.GameInventory;
import com.cwd.tg.ugc.handlers.GameRequestHandler;
import com.cwd.tg.ugc.services.GameControllerService;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class GameRequestHandlerImpl implements GameRequestHandler {

    private final GameControllerService gameControllerService;

    @Override
    public Mono<GameCharacter> createGameCharacter(PlayerCharacterInput playerCharacterInput) {
        return gameControllerService.saveCharacter(
                new GameCharacter(playerCharacterInput.getName(),
                        false, false, false, "",
                        new CharacterProgress(0, 1, 100),
                        new CharacterStats(10, 10, 10, 200, 100, 30),
                        new GameInventory(), randomUUID().toString()));
    }

    @Override
    public Mono<GameCharacter> getUserGameCharacter(String userId) {
        return gameControllerService.getUserCharacter(userId);
    }
}
