package core;

import mindustry.content.Blocks;
import mindustry.core.World;

import mindustry.world.Tile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class WorldTests {

    @Test
    public void worldCreateTilesNegativeWidth(){
        World world = new World();
       // world.createTiles(-1,5);
        assertEquals(world.width(),0);
    }

    @Test
    public void worldCreateTilesNegativeHeight(){
        World world = new World();
        //world.createTiles(5,0);
    }

    @Test
    public void worldGetTiles(){
        int width = 10;
        int height = 10;

        World world = new World();
        MockGenerator generator = new MockGenerator(10,10);
        world.loadGenerator(generator);

        Tile[][] tiles = new Tile[width][height];

        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                tiles[x][y] = new Tile(x, y, Blocks.air.id, Blocks.air.id, Blocks.air.id);
            }
        }
        for(int i = 0; i < width; i++){
            assertArrayEquals(tiles[i],world.getTiles()[i]);
        }

    }



}
