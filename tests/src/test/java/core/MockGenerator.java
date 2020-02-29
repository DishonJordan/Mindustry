package core;

import mindustry.content.Blocks;
import mindustry.maps.generators.Generator;
import mindustry.world.Block;
import mindustry.world.Tile;

public class MockGenerator extends Generator {
    protected Block floor;
    protected Block block;
    protected Block ore;

    public MockGenerator(int width, int height){
        super(width,height);
    }

    @Override
    public void generate(Tile[][] tiles) {
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                floor = Blocks.air;
                block = Blocks.air;
                ore = Blocks.air;
                tiles[x][y] = new Tile(x, y, floor.id, ore.id, block.id);
            }
        }
    }
}
