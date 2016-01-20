package ua.petrov.transport.core.util.optimum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.petrov.transport.core.entity.Arc;
import ua.petrov.transport.core.entity.Station;

import java.util.Arrays;
import java.util.List;

public class Solution {

    private static final int INF = 1000 * 1000 * 1000;

    private static final Logger LOGGER = LoggerFactory.getLogger(ShortestWay.class);

    private List<Arc> allArcs;

    private List<Station> allStations;

    private int size;

    public Solution(List<Arc> allArcs, List<Station> allStations) {
        this.allArcs = allArcs;
        this.allStations = allStations;
        size = allStations.size();
    }

    public void solve() {

        int vertexCount = size;
        int edgeCount = allArcs.size();
        int[] distance = new int[vertexCount];

        Arrays.fill(distance, INF);
        distance[0] = 0;
        for (int i = 0; i < vertexCount - 1; i++) {
            for (int j = 0; j < edgeCount; j++) {
                int from = allArcs.get(j).getFromStation().getId();
                int to = allArcs.get(j).getToStation().getId();
                int weight = (int) allArcs.get(j).getTravelTimeLong();

                // Ясно, что если вершина from на
                // текущем шаге работы алгоритма
                // находится бесконечно далеко от
                // 0-ой, то она не изменит ответ
//                if (distance[j] == INF) {
//                    continue;
//                }

                // В противном случае обновим
                // расстояние до вершины to,
                // это называют релаксацией
                distance[to] = Math.min(distance[to], distance[from] + weight);
            }
        }
        // Выводим расстояние от 0-ой вершины
        // до каждой отличной от нее
        for (int i = 1; i < vertexCount; i++) {
            if (distance[i] == INF) {
                LOGGER.debug("No");
            } else {
                LOGGER.debug(String.valueOf(distance[i]));
            }
        }


    }

}