package game;

import mindustry.game.Gamemode;
import mindustry.game.Rules;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RulesTests {
    @Test
    public void rulesDefaultCopy(){
        Rules rules = new Rules();
        Rules rules1 = rules.copy();

        assertEquals(rules.infiniteResources, rules1.infiniteResources);
        assertEquals(rules.waveTimer, rules1.waveTimer);
        assertEquals(rules.waves, rules1.waves);
        assertEquals(rules.enemyCheat, rules1.enemyCheat);
        assertEquals(rules.pvp, rules1.pvp);
        assertEquals(rules.unitDrops, rules1.unitDrops);
        assertEquals(rules.reactorExplosions, rules1.reactorExplosions);
        assertEquals(rules.unitBuildSpeedMultiplier, rules1.unitBuildSpeedMultiplier);
        assertEquals(rules.unitHealthMultiplier, rules1.unitHealthMultiplier);
        assertEquals(rules.playerHealthMultiplier, rules1.playerHealthMultiplier);
        assertEquals(rules.blockHealthMultiplier, rules1.blockHealthMultiplier);
        assertEquals(rules.playerDamageMultiplier, rules1.playerDamageMultiplier);
        assertEquals(rules.unitDamageMultiplier, rules1.unitDamageMultiplier);
        assertEquals(rules.buildCostMultiplier, rules1.buildCostMultiplier);
        assertEquals(rules.enemyCoreBuildRadius, rules1.enemyCoreBuildRadius);
        assertEquals(rules.dropZoneRadius, rules1.dropZoneRadius);
        assertEquals(rules.respawnTime, rules1.respawnTime);
        assertEquals(rules.waveSpacing, rules1.waveSpacing);
        assertEquals(rules.bossWaveMultiplier, rules1.bossWaveMultiplier);
        assertEquals(rules.launchWaveMultiplier, rules1.launchWaveMultiplier);
        assertEquals(rules.zone, rules1.zone);
        assertEquals(rules.spawns, rules1.spawns);
        assertEquals(rules.limitedRespawns, rules1.limitedRespawns);
        assertEquals(rules.respawns, rules1.respawns);
        assertEquals(rules.waitForWaveToEnd, rules1.waitForWaveToEnd);
        assertEquals(rules.attackMode, rules1.attackMode);
        assertEquals(rules.editor, rules1.editor);
        assertEquals(rules.tutorial, rules1.tutorial);
        assertEquals(rules.canGameOver, rules1.canGameOver);

        assertEquals(rules.loadout.get(0).item, rules1.loadout.get(0).item);
        assertEquals(rules.loadout.get(0).amount, rules1.loadout.get(0).amount);

        assertEquals(rules.bannedBlocks, rules1.bannedBlocks);
        assertEquals(rules.lighting, rules1.lighting);
        assertEquals(rules.ambientLight, rules1.ambientLight);
        assertEquals(rules.defaultTeam, rules1.defaultTeam);
        assertEquals(rules.waveTeam, rules1.waveTeam);
        assertEquals(rules.tags, rules1.tags);
    }

    @Test
    public void rulesModePvP(){
        Rules rules = new Rules();
        rules.pvp = true;
        Gamemode gamemode = rules.mode();
        assertEquals(Gamemode.pvp, gamemode);
    }

    @Test
    public void rulesModeEditor(){
        Rules rules = new Rules();
        rules.editor = true;
        Gamemode gamemode = rules.mode();
        assertEquals(Gamemode.editor, gamemode);
    }

    @Test
    public void rulesModeAttackMode(){
        Rules rules = new Rules();
        rules.attackMode = true;
        Gamemode gamemode = rules.mode();
        assertEquals(Gamemode.attack, gamemode);
    }

    @Test
    public void rulesModeInfiniteResources(){
        Rules rules = new Rules();
        rules.infiniteResources = true;
        Gamemode gamemode = rules.mode();
        assertEquals(Gamemode.sandbox, gamemode);
    }

    @Test
    public void rulesModeSurvival(){
        Rules rules = new Rules();
        Gamemode gamemode = rules.mode();
        assertEquals(Gamemode.survival, gamemode);
    }
}
