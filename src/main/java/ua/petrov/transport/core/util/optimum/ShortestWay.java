package ua.petrov.transport.core.util.optimum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.petrov.transport.core.entity.Arc;
import ua.petrov.transport.core.entity.Station;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Владислав on 10.01.2016.
 */
public class ShortestWay {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShortestWay.class);

    private List<Arc> allArcs;

    private List<Station> allStations;

    private Matrix[][] matrix;

    private int size;

    public ShortestWay(List<Arc> allArcs, List<Station> allStations) {
        this.allArcs = allArcs;
        this.allStations = allStations;
        matrix = new Matrix[allStations.size()][allStations.size()];
        size = allStations.size();
    }

    private void fillMatrix() {
        for (int i = 0; i < size; i++) {
            Station iSt = allStations.get(i);
            for (int j = 0; j < size; j++) {
                Station jSt = allStations.get(j);

                for (Arc arc : allArcs) {
                    if ((arc.getFromStation().equals(iSt) && arc.getToStation().equals(jSt)) ||
                            (arc.getFromStation().equals(jSt) && arc.getToStation().equals(iSt))) {
                        matrix[i][j] = new Matrix(arc, arc.getTravelTimeLong());
                        break;
                    } else {
                        matrix[i][j] = new Matrix(arc, Integer.MAX_VALUE);
                    }
                }
            }
        }
    }

    public void print() {
        StringBuilder str = new StringBuilder("\n");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                str.append(matrix[i][j].arcs.size() + "\t");
            }
            str.append("\n");
        }
        LOGGER.error(str.toString());
    }

    public void printT() {
        StringBuilder str = new StringBuilder("\n");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if(matrix[i][j].travelTime == Integer.MAX_VALUE) {
                    str.append(0 + "\t");
                } else {
                    str.append(matrix[i][j].travelTime + "\t");
                }
            }
            str.append("\n");
        }
        LOGGER.error(str.toString());
    }

    public List<Station> getShortestWay() {
        List<Station> stations = new ArrayList<>();

        fillMatrix();
        printT();
        for (int k = 0; k < size; k++) {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    /*long min = matrix[i][k].travelTime  + matrix[k][j].travelTime;
                    if(matrix[i][j].travelTime > min) {
                        matrix[i][j].travelTime = min;
                        Arc arc = new Arc(matrix[i][k].from, matrix[i][k].to);
                        Arc arc2 = new Arc(matrix[k][j].from, matrix[k][j].to);
//                        LOGGER.debug(arc.toString());
//                        LOGGER.error(arc2.toString());
                        matrix[i][j].addArc(arc);
                        matrix[i][j].addArc(arc2);
                        break;
                    }*/
//                    long min = Math.min(matrix[i][j].travelTime,matrix[i][k].travelTime  + matrix[k][j].travelTime);
                    if(matrix[i][j].travelTime > matrix[i][k].travelTime + matrix[k][j].travelTime) {
                        LOGGER.debug(String.valueOf(matrix[i][k]));
                        LOGGER.debug(String.valueOf(matrix[k][j]));
                        matrix[i][j].travelTime = matrix[i][k].travelTime + matrix[k][j].travelTime;
                        if (matrix[i][j].travelTime == 860) {
                            LOGGER.debug(matrix[i][k].toString());
                            LOGGER.debug(matrix[k][j].toString());
                        }
                    }
                }
            }
        }
        printT();

        print();

        return stations;
    }

    private class Matrix {

        Station from;

        Station to;

        long travelTime;

        private List<Arc> arcs = new ArrayList<>();

        public Matrix(Arc arc, long travelTime) {
            this.from = arc.getFromStation();
            this.to = arc.getToStation();
            this.travelTime = travelTime;
        }

        public void addArc(Arc arc) {
            arcs.add(arc);
        }

        @Override
        public String toString() {
            return "Matrix{" +
                    "to=" + to +
                    ", from=" + from +
                    '}';
        }
    }
}
