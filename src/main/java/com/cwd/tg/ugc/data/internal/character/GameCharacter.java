package com.cwd.tg.ugc.data.internal.character;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GameCharacter {
    private String id;
    private String name;
    private boolean inAdventure;
    private boolean isFighting;
    private boolean isResting;
    private String currentAction;
    private CharacterProgress progress;
    private CharacterStats stats;
    private GameInventory gameInventory;
    private String userId;

    public GameCharacter(String name, boolean inAdventure, boolean isFighting, boolean isResting,
            String currentAction, CharacterProgress progress, CharacterStats stats,
            GameInventory gameInventory, String userId) {
        this.name = name;
        this.inAdventure = inAdventure;
        this.isFighting = isFighting;
        this.isResting = isResting;
        this.currentAction = currentAction;
        this.progress = progress;
        this.stats = stats;
        this.gameInventory = gameInventory;
        this.userId = userId;
    }
}
