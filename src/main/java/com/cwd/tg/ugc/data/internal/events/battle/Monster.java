package com.cwd.tg.ugc.data.internal.events.battle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Monster {
    private String id;
    private long hitPoints;
    private long attack;
    private MonsterType monsterType;
}
