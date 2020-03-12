package world;

import arc.ApplicationCore;
import arc.backend.headless.HeadlessApplication;
import arc.graphics.Color;
import arc.util.Log;
import mindustry.Vars;
import mindustry.core.FileTree;
import mindustry.core.Logic;
import mindustry.core.NetServer;
import mindustry.maps.Map;
import mindustry.net.Net;
import mindustry.type.Item;
import mindustry.world.ItemBuffer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.*;

import static mindustry.Vars.*;
import static mindustry.Vars.maps;
import static org.junit.jupiter.api.Assertions.*;

public class ItemBufferTests {
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
    public void itemBufferAcceptsTrue(){
        ItemBuffer itemBuffer = new ItemBuffer(2, 4);
        assertTrue(itemBuffer.accepts());
    }

    @Test
    public void itemBufferAcceptsFalse(){
        ItemBuffer itemBuffer = new ItemBuffer(1, 4);
        itemBuffer.accept(new Item("test_item1", new Color()), (short)0);
        assertFalse(itemBuffer.accepts());
    }

    @Test
    public void itemBufferAcceptSingleItemFalse(){
        ItemBuffer itemBuffer = new ItemBuffer(1, 4);
        itemBuffer.accept(new Item("test_item2", new Color()));
        assertFalse(itemBuffer.accepts());
    }

    @Test
    public void itemBufferNullPollTest(){
        ItemBuffer itemBuffer = new ItemBuffer(1, 2);
        assertNull(itemBuffer.poll());
    }

    @Test
    public void itemBufferPollValidItemOutput(){
        ItemBuffer itemBuffer = new ItemBuffer(1, 0);
        itemBuffer.accept(new Item("test_item3", new Color()), (short)10);
        assertEquals("test_item3", itemBuffer.poll().name);
    }

    @Test
    public void itemBufferPollValidItemOutputAfterMultipleAdds(){
        ItemBuffer itemBuffer = new ItemBuffer(3, 0);
        itemBuffer.accept(new Item("test_item4", new Color()), (short)10);
        itemBuffer.accept(new Item("test_item5", new Color()), (short)2);
        assertEquals("test_item4", itemBuffer.poll().name);
    }

    @Test
    public void itemBufferPollInValidDataOutput(){
        ItemBuffer itemBuffer = new ItemBuffer(1, 0);
        assertEquals(-1, itemBuffer.pollData());
    }

    @Test
    public void itemBufferPollValidDataOutput(){
        ItemBuffer itemBuffer = new ItemBuffer(1, 0);
        itemBuffer.accept(new Item("test_item6", new Color()), (short)10);
        assertEquals((short) 10, itemBuffer.pollData());
    }

    @Test
    public void itemBufferPollValidDataOutputAfterMultipleAdds(){
        ItemBuffer itemBuffer = new ItemBuffer(3, 0);
        itemBuffer.accept(new Item("test_item7", new Color()), (short)10);
        itemBuffer.accept(new Item("test_item8", new Color()), (short)2);
        assertEquals((short)10, itemBuffer.pollData());
    }

    @Test
    public void itemBufferRemoveWhenEmpty(){
        ItemBuffer itemBuffer = new ItemBuffer(3, 0);
        itemBuffer.remove();
    }

    @Test
    public void itemBufferRemoveWithOneItemTest(){
        ItemBuffer itemBuffer = new ItemBuffer(3, 0);
        itemBuffer.accept(new Item("test_item9", new Color()), (short)2);
        itemBuffer.remove();
        assertNull(itemBuffer.poll());
    }

    @Test
    public void itemBufferRemoveWithMultipleItemsTest(){
        ItemBuffer itemBuffer = new ItemBuffer(3, 0);
        itemBuffer.accept(new Item("test_item10", new Color()), (short)10);
        itemBuffer.accept(new Item("test_item11", new Color()), (short)2);
        itemBuffer.remove();
        assertEquals("test_item11", itemBuffer.poll().name);
    }

    @Test
    public void itemBufferWriteCatchIOException(){
        ItemBuffer itemBuffer = new ItemBuffer(3, 0);
        Assertions.assertThrows(IOException.class, () -> {
            itemBuffer.write(new DataOutputStream(new FileOutputStream("")));
        });
    }

    @Test
    public void itemBufferReadCatchIOException(){
        ItemBuffer itemBuffer = new ItemBuffer(3, 0);
        Assertions.assertThrows(IOException.class, () -> {
            itemBuffer.read(new DataInputStream(new FileInputStream("")));
        });
    }
}
