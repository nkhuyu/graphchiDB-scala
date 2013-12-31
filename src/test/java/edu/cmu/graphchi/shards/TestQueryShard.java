package edu.cmu.graphchi.shards;

import edu.cmu.graphchi.VertexInterval;
import edu.cmu.graphchi.preprocessing.FastSharder;
import edu.cmu.graphchi.preprocessing.VertexIdTranslate;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Aapo Kyrola
 */
public class TestQueryShard {

    @Test
    public void testEdgeIterator() throws Exception  {
        String baseFilename = "/tmp/testshard";

        long[] srcs = new long[]{100, 99, 98, 97, 10, 0};
        long[] dsts = new long[]{1, 2, 3, 4, 5, 6};
        for(int j=0; j<dsts.length; j++) {
            dsts[j] = VertexIdTranslate.encodeVertexPacket((byte)0, dsts[j], 0);
        }

        FastSharder.writeAdjacencyShard(baseFilename, 0, 1, 1, srcs, dsts, new byte[srcs.length], 0, 101, false, null);

        QueryShard shards = new QueryShard(baseFilename, 0, 1, new VertexInterval(0, 101, 0));

        EdgeIterator iter = shards.edgeIterator();

        assertTrue(iter.hasNext());
        iter.next();
        assertEquals(0, iter.getSrc());
        assertEquals(6, iter.getDst());
        assertTrue(iter.hasNext());
        iter.next();
        assertEquals(10, iter.getSrc());
        assertEquals(5, iter.getDst());
        assertTrue(iter.hasNext());
        iter.next();
        assertEquals(97, iter.getSrc());
        assertEquals(4, iter.getDst());
        assertTrue(iter.hasNext());
        iter.next();
        assertEquals(98, iter.getSrc());
        assertEquals(3, iter.getDst());
        assertTrue(iter.hasNext());
        iter.next();
        assertEquals(99, iter.getSrc());
        assertEquals(2, iter.getDst());
        assertTrue(iter.hasNext());
        iter.next();
        assertEquals(100, iter.getSrc());
        assertEquals(1, iter.getDst());
        assertFalse(iter.hasNext());
    }
}
