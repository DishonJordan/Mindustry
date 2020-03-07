package core;

import arc.ApplicationCore;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.core.World;
import mindustry.world.Tile;
import arc.backend.headless.*;
import arc.util.*;
import mindustry.core.*;
import mindustry.maps.*;
import mindustry.net.Net;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static mindustry.Vars.*;
import static org.junit.jupiter.api.Assertions.*;

public class WorldTests {
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
    public void worldNullTileHeight(){
        World world = new World();
        assertEquals(0, world.height());
    }

    @Test
    public void worldNullTileWidth(){
        World world = new World();
        assertEquals(0, world.width());
    }

    @Test
    public void worldCreateTilesNull(){
        World world = new World();
        world.createTiles(10,10);
        assertEquals(10, world.width());
        assertEquals(10, world.height());
    }

    @Test
    public void worldCreateTilesResize(){
        int width = 10;
        int height = 10;

        World world = new World();
        MockAirGenerator generator = new MockAirGenerator(10,10);
        world.loadGenerator(generator);

        assertEquals(10, world.width(), world.height());

        world.createTiles(5,5);

        assertEquals(5, world.width());
        assertEquals(5, world.height());
    }

    @Test
    public void worldCreateTilesNegativeHeight(){
        World world = new World();
        world.createTiles(5,-1);
        assertNull(world.getTiles());
    }

    @Test
    public void worldGetTiles(){
        int width = 10;
        int height = 10;

        World world = new World();
        MockAirGenerator generator = new MockAirGenerator(10,10);
        world.loadGenerator(generator);

        Tile[][] tiles = world.getTiles();

        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                assertEquals(Blocks.air,tiles[i][j].block());
            }
        }
    }

    @Test
    public void worldNotSolid(){
        int width = 10;
        int height = 10;

        World world = new World();
        MockAirGenerator generator = new MockAirGenerator(10,10);
        world.loadGenerator(generator);

        assertEquals(false, world.solid(2,2));
        assertEquals(false, world.wallSolid(2,2));
        assertEquals(true, world.passable(2,2));
    }

    @Test
    public void worldNullTileGetterFunctions(){
        World world = new World();

        assertEquals(null, world.tile(0));
        assertEquals(null, world.tile(0,0));
        assertEquals(null, world.ltile(0,0));
        assertEquals(null, world.rawTile(0,0));
        assertEquals(null, world.ltileWorld(0,0));
        assertEquals(0, world.toTile((float)0));
    }

    @Test
    public void worldTileGetterFunctions(){
        int width = 10;
        int height = 10;

        World world = new World();
        MockAirGenerator generator = new MockAirGenerator(10,10);
        world.loadGenerator(generator);

        Tile[][] tiles = world.getTiles();

        assertEquals(tiles[0][0], world.tile(0));
        assertEquals(tiles[2][2], world.tile(2,2));
        assertEquals(tiles[3][5], world.ltile(3,5));
        assertEquals(tiles[1][3], world.rawTile(1,3));
        assertEquals(tiles[0][0], world.ltileWorld(1,1));
        assertEquals(1, world.toTile((float)10));
    }

    @Test
    public void worldDarknessTestWithNonSolidBlocks(){
        int width = 5;
        int height = 5;

        World world = new World();
        MockAirGenerator generator = new MockAirGenerator(10,10);
        world.loadGenerator(generator);

        Tile[][] tiles = world.getTiles();

        world.addDarkness(world.getTiles());

        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                assertEquals(false,world.getTiles()[i][j].isDarkened());
            }
        }
    }

}
