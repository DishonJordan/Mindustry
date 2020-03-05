package core;

import arc.ApplicationCore;
import arc.backend.headless.HeadlessApplication;
import arc.util.Log;
import mindustry.Vars;
import mindustry.core.FileTree;
import mindustry.core.GameState;
import mindustry.core.Logic;
import mindustry.core.NetServer;
import mindustry.maps.Map;
import mindustry.net.Net;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static mindustry.Vars.*;
import static mindustry.Vars.maps;
import static org.junit.jupiter.api.Assertions.*;

public class GameStateTests {
    static boolean initialized;
    static Map testMap;

    @BeforeAll
    static void launchApplication(){
        //only gets called once
        if(initialized) return;
        initialized = true;

        try{
            boolean[] begins = {false};
            Throwable[] exceptionThrown = {null};
            Log.setUseColors(false);

            ApplicationCore core = new ApplicationCore(){
                @Override
                public void setup(){
                    headless = true;
                    net = new Net(null);
                    tree = new FileTree();
                    Vars.init();
                    content.createBaseContent();

                    add(logic = new Logic());
                    add(netServer = new NetServer());

                    content.init();
                }

                @Override
                public void init(){
                    super.init();
                    begins[0] = true;
                    testMap = maps.loadInternalMap("groundZero");
                    Thread.currentThread().interrupt();
                }
            };

            new HeadlessApplication(core, null, throwable -> exceptionThrown[0] = throwable);

            while(!begins[0]){
                if(exceptionThrown[0] != null){
                    fail(exceptionThrown[0]);
                }
                Thread.sleep(10);
            }
        }catch(Throwable r){
            fail(r);
        }
    }

    @Test
    public void gameStateIsTest(){
        GameState gameState = new GameState();
        assertTrue(gameState.is(GameState.State.menu));
    }

    @Test
    public void gameStateInitTest(){
        GameState gameState = new GameState();
        assertEquals(gameState.getState(), GameState.State.menu);
    }

    @Test
    public void gameStateChangeState(){
        GameState gameState = new GameState();
        gameState.set(GameState.State.paused);
        assertEquals(gameState.getState(), GameState.State.paused);
        gameState.set(GameState.State.playing);
        assertEquals(gameState.getState(), GameState.State.playing);
    }

    @Test
    public void gameStateNotPaused(){
        GameState gameState = new GameState();
        gameState.set(GameState.State.playing);
        assertFalse(gameState.isPaused());
    }

    @Test
    public void gameStatePaused(){
        GameState gameState = new GameState();
        gameState.set(GameState.State.paused);
        assertTrue(gameState.isPaused());
    }

    @Test
    public void gameStateEditorDefault(){
        GameState gameState = new GameState();
        assertFalse(gameState.isEditor());
    }

    @Test
    public void gameStateEditorFalse(){
        GameState gameState = new GameState();
        gameState.rules.editor = true;
        assertTrue(gameState.isEditor());
    }
}
