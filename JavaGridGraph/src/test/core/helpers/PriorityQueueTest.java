package core.helpers;

import core.Vertex;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class PriorityQueueTest {

    private static PriorityQueue pq;

    @BeforeClass
    public static void setUp() {
        pq = new PriorityQueue(1000);
    }

    @AfterClass
    public static void tearDown() {
        pq = null;
    }

    @Test
    public void testPop() {
        Vertex a;
        Vertex p = null;
        Vertex v;
        Random r = new Random();
        for(int i=0; i < 1000; i++) {
            v = new Vertex(i);
            v.setD(r.nextDouble());
            pq.push(v);
        }
        for(int i=0; i < 1000; i++) {
            a = pq.pop();
            if(p != null) {
                assertTrue("Priority queue does not sort correctly",
                        a.getD() > p.getD());
            }
            p = a;
        }
    }
}