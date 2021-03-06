package api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AlgoTest
{
    private static Algo algo;

    @BeforeEach
    void init() throws IOException
    {
        String file = "data/A0";
        String json = new String(Files.readAllBytes(Paths.get(file)));
        algo = new Algo(json);
    }

    @Test
    void shortestPathDist()
    {
        double test1 = algo.shortestPathDist(0,5);
        algo.getGraph().connect(0,5,0.0004);
        double test2 = algo.shortestPathDist(0,5);
        assert (test1 > test2);
    }

    @Test
    void shortestPath()
    {
        List<Node> test1 = algo.shortestPath(0,5);
        algo.getGraph().connect(0,5,0.0004);
        List<Node> test2 = algo.shortestPath(0,5);
        assert (test1.size() > test2.size());
        assert (test1.contains(algo.getGraph().getNode(0)) && test1.contains(algo.getGraph().getNode(5)));
        assert (test2.contains(algo.getGraph().getNode(0)) && test2.contains(algo.getGraph().getNode(5)));
    }

}