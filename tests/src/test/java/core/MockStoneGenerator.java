package core;

import mindustry.content.Blocks;
import mindustry.maps.generators.Generator;
import mindustry.world.Block;
import mindustry.world.Tile;


public class MockStoneGenerator extends Generator {

    public MockStoneGenerator(int width, int height){
        super(width,height);
    }

    @Override
    public void generate(Tile[][] tiles) {

        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                Block floor = Blocks.stone;
                Block block = Blocks.stone;
                Block ore = Blocks.stone;
                tiles[x][y] = new Tile(x, y, floor.id, ore.id, block.id);
            }
        }
    }
}

