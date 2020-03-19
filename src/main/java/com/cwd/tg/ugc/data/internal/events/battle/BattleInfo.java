package com.cwd.tg.ugc.data.internal.events.battle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BattleInfo {
    private String id;
    private String characterId;
    private String monsterId;
    private String awardsId;
}
