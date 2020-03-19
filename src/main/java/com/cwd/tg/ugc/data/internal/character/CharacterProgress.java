package com.cwd.tg.ugc.data.internal.character;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CharacterProgress {
    private long currentExp;
    private int level;
    private long targetExp;
}
