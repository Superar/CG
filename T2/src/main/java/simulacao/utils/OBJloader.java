package simulacao.utils;

import com.mokiat.data.front.parser.IMTLParser;
import com.mokiat.data.front.parser.MTLLibrary;
import com.mokiat.data.front.parser.MTLMaterial;
import com.mokiat.data.front.parser.MTLParser;
import simulacao.Modelo;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class OBJloader {

    public static HashMap<String, Modelo> parseObjFile(String path, String filename) {

        try {
            InputStream file = OBJloader.class.getResourceAsStream(path + filename + ".obj");
            InputStream fileMtl = OBJloader.class.getResourceAsStream(path + filename + ".mtl");

            final IMTLParser mtlParser = new MTLParser();
            final MTLLibrary library = mtlParser.parse(fileMtl);

            for (MTLMaterial material : library.getMaterials()) {
                Modelo.materiais.put(material.getName(), material);
            }

            Scanner scanner = new Scanner(file);

            String line;

            HashMap<String, Modelo> modelos = new HashMap<>();

            ArrayList<Float> verticesModelo = new ArrayList<>();
            ArrayList<Integer> indicesModelo = new ArrayList<>();
            ArrayList<Float> coresModelo = new ArrayList<>();

            String current_material = "";
            String current_model = "";

            while (true) {

                try {
                    line = scanner.nextLine();

                    String[] tokens = line.split("\\s+");

                    if (tokens[0].equals("o")) {
                        if (!current_model.equals("")) {
                            modelos.put(current_model, createModel(verticesModelo, indicesModelo, coresModelo));
                        }

                        current_model = tokens[1];
                        verticesModelo = new ArrayList<>();
                        indicesModelo = new ArrayList<>();
                        coresModelo = new ArrayList<>();
                    } else if (tokens[0].equals("v")) {
                        verticesModelo.add(Float.valueOf(tokens[1]));
                        verticesModelo.add(Float.valueOf(tokens[2]));
                        verticesModelo.add(Float.valueOf(tokens[3]));

                    } else if (tokens[0].equals("usemtl")) {
                        current_material = tokens[1];
                    } else if (tokens[0].equals("f")) {

                        indicesModelo.add(Integer.parseInt(tokens[1].split("/")[0]));
                        indicesModelo.add(Integer.parseInt(tokens[2].split("/")[0]));
                        indicesModelo.add(Integer.parseInt(tokens[3].split("/")[0]));

                        MTLMaterial material = Modelo.materiais.get(current_material);

                        coresModelo.add(material.getDiffuseColor().r);
                        coresModelo.add(material.getDiffuseColor().g);
                        coresModelo.add(material.getDiffuseColor().b);
                    }

                } catch (NoSuchElementException e) {
                    break;
                }
            }
            modelos.put(current_model, createModel(verticesModelo, indicesModelo, coresModelo));
            return modelos;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Modelo createModel(ArrayList<Float> vertices,
                                      ArrayList<Integer> indices,
                                      ArrayList<Float> cores) {

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

        float[] colours = new float[cores.size()];

        i = 0;

        for (Float f : cores) {

            colours[i++] = f;
        }

        return new Modelo(verticesArray, indicesArray, colours);
    }

    private static int min(ArrayList<Integer> list) {
        int min = (int) Float.POSITIVE_INFINITY;
        for (Integer i : list) {
            if (i < min) {
                min = i;
            }
        }
        return min;
    }
}
