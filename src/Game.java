import api.Algo;
import api.DirectedWeightedGraph;
import api.DirectedWeightedGraphAlgorithms;
import api.Location;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

public class Game
{
    private DirectedWeightedGraphAlgorithms algo;
    private HashMap<Integer, Agent> agents;
    private ArrayList<Pokemon> pokemons;

    public Game(String agentsStr, String pokemonStr, String graphStr)
    {
        this.algo = new Algo(graphStr); //init the algo and the graph
        this.agents = new HashMap<>();
        this.pokemons = new ArrayList<>();

        addAgent(agentsStr); //init the list (the hashmap) of agents
        addPokemon(pokemonStr); //init the list (arrayList) of pokemons.
    }

    public void addPokemon(String jsonStr)
    {

    }

    public void addAgent(String jsonStr)
    {
        JSONObject j = new JSONObject(jsonStr);
        JSONArray agentsArray = j.getJSONArray("Agents");

        for(int i = 0; i < agentsArray.length(); i++)
        {
            JSONObject currentAgent = agentsArray.getJSONObject(i).getJSONObject("Agent");
            int id = currentAgent.getInt("id");
            double value = currentAgent.getDouble("value");
            int src = currentAgent.getInt("src");
            int dest = currentAgent.getInt("dest");
            double speed = currentAgent.getDouble("speed");
            String[] pos = currentAgent.getString("pos").split(",");
            double x = Double.parseDouble(pos[0]);
            double y = Double.parseDouble(pos[1]);
            double z = Double.parseDouble(pos[2]);
            this.agents.put(id, new Agent(id, src, dest, speed, new Location(x,y,z)));
        }
    }
}
