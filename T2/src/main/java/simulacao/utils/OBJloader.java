package simulacao.utils;

import simulacao.Modelo;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class OBJloader {

    public Modelo model;

    public OBJloader(String objLocation) {
        InputStream file = OBJloader.class.getResourceAsStream(objLocation);
        Scanner scanner = new Scanner(file);

        parseObjFile(scanner);
    }

    private void parseObjFile(Scanner scanner) {
        String line;

        ArrayList<Float> vertices = new ArrayList<>();
        ArrayList<Integer> indices = new ArrayList<>();

        while (true) {
            try {
                line = scanner.nextLine();
            } catch (NoSuchElementException e) {
                break;
            }

            if (line.startsWith("v ")) {
                String[] coords = line.split("\\s+");

                vertices.add(Float.valueOf(coords[1]));
                vertices.add(Float.valueOf(coords[2]));
                vertices.add(Float.valueOf(coords[3]));
            } else if (line.startsWith("f ")) {
                String[] indicesLine = line.split("\\s+");

                indices.add(Integer.parseInt(indicesLine[1].split("/")[0]));
                indices.add(Integer.parseInt(indicesLine[2].split("/")[0]));
                indices.add(Integer.parseInt(indicesLine[3].split("/")[0]));
            }
        }

        createModel(vertices, indices);
    }

    private void createModel(ArrayList<Float> vertices, ArrayList<Integer> indices) {

        float[] verticesArray = new float[vertices.size()];
        int[] indicesArray = new int[indices.size()];
        int i = 0;

        for (Float v : vertices) {
            verticesArray[i++] = (v != null ? v : Float.NaN);
        }

        i = 0;
        int minIndice = min(indices);
        for (Integer indice : indices) {
            indicesArray[i++] = indice - minIndice;
        }

        float[] colours = new float[vertices.size()];
        for (i = 0; i < colours.length; i++) {
            colours[i] = (i % 3 == 0) ? 0.5f : 0.0f;
        }

        this.model = new Modelo(verticesArray, indicesArray, colours);
    }

    private int min(ArrayList<Integer> list) {
        int min = (int) Float.POSITIVE_INFINITY;
        for (Integer i : list) {
            if (i < min) {
                min = i;
            }
        }
        return min;
    }
}
