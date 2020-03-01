import arc.ApplicationCore;
import arc.backend.headless.HeadlessApplication;
import arc.struct.*;
import arc.util.*;
import mindustry.Vars;
import mindustry.core.*;
import mindustry.core.GameState.*;
import mindustry.game.*;
import mindustry.io.SaveIO.*;
import mindustry.maps.Map;
import mindustry.net.Net;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.storage.*;
import org.junit.jupiter.api.*;

import static mindustry.Vars.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class ZoneTests{
    static Map testMap;
    static boolean initialized;

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

    @BeforeEach
    void resetWorld(){
        Time.setDeltaProvider(() -> 1f);
        logic.reset();
        state.set(State.menu);
    }

    @TestFactory
    DynamicTest[] testZoneValidity(){
        Array<DynamicTest> out = new Array<>();
        if(world == null) world = new World();

        for(Zone zone : content.zones()){
            out.add(dynamicTest(zone.name, () -> {
                zone.generator.init(zone.loadout);
                logic.reset();
                try{
                    world.loadGenerator(zone.generator);
                }catch(SaveException e){
                    e.printStackTrace();
                    return;
                }
                zone.rules.get(state.rules);
                ObjectSet<Item> resources = new ObjectSet<>();
                boolean hasSpawnPoint = false;

                for(int x = 0; x < world.width(); x++){
                    for(int y = 0; y < world.height(); y++){
                        Tile tile = world.tile(x, y);
                        if(tile.drop() != null){
                            resources.add(tile.drop());
                        }
                        if(tile.block() instanceof CoreBlock && tile.getTeam() == state.rules.defaultTeam){
                            hasSpawnPoint = true;
                        }
                    }
                }

                Array<SpawnGroup> spawns = state.rules.spawns;
                for(int i = 1; i <= 100; i++){
                    int total = 0;
                    for(SpawnGroup spawn : spawns){
                        total += spawn.getUnitsSpawned(i);
                    }

                    assertNotEquals(0, total, "Zone " + zone + " has no spawned enemies at wave " + i);
                }

                assertTrue(hasSpawnPoint, "Zone \"" + zone.name + "\" has no spawn points.");
                assertTrue(spawner.countSpawns() > 0 || (state.rules.attackMode && state.teams.get(state.rules.waveTeam).hasCore()), "Zone \"" + zone.name + "\" has no enemy spawn points: " + spawner.countSpawns());

                for(Item item : resources){
                    assertTrue(zone.resources.contains(item), "Zone \"" + zone.name + "\" is missing item in resource list: \"" + item.name + "\"");
                }

                for(Item item : zone.resources){
                    assertTrue(resources.contains(item), "Zone \"" + zone.name + "\" has unnecessary item in resource list: \"" + item.name + "\"");
                }
            }));
        }

        return out.toArray(DynamicTest.class);
    }
}
