package simulacao.utils;

import com.mokiat.data.front.parser.IMTLParser;
import com.mokiat.data.front.parser.MTLLibrary;
import com.mokiat.data.front.parser.MTLMaterial;
import com.mokiat.data.front.parser.MTLParser;
import simulacao.Modelo;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class OBJloader {

    public Modelo model;

//    public OBJloader(String objLocation) {
//        InputStream file = OBJloader.class.getResourceAsStream(objLocation);
//        Scanner scanner = new Scanner(file);
//
//        parseObjFile(scanner);
//    }

    public static ArrayList<Modelo> parseObjFile(String path, String filename) {

        try {

            InputStream file = OBJloader.class.getResourceAsStream(path+filename+".obj");
            InputStream fileMtl = OBJloader.class.getResourceAsStream(path+filename+".mtl");

            final IMTLParser mtlParser = new MTLParser();

            final MTLLibrary library = mtlParser.parse(fileMtl);

            for(MTLMaterial material : library.getMaterials()){
                Modelo.materiais.put(material.getName(), material);
            }

            Scanner scanner = new Scanner(file);

            String line;

            ArrayList<Float> vertices = new ArrayList<>();
            ArrayList<Integer> indices = new ArrayList<>();

            ArrayList<Float> cores = new ArrayList<>();

            String current_material = "";

            while (true) {

                try {

                    line = scanner.nextLine();

                    String[] tokens = line.split("\\s+");

                    if (tokens[0].equals("v")) {
                        vertices.add(Float.valueOf(tokens[1]));
                        vertices.add(Float.valueOf(tokens[2]));
                        vertices.add(Float.valueOf(tokens[3]));

                    } else if (tokens[0].equals("usemtl")) {
                        current_material = tokens[1];

                    } else if (tokens[0].equals("f")) {

                        indices.add(Integer.parseInt(tokens[1].split("/")[0]));
                        indices.add(Integer.parseInt(tokens[2].split("/")[0]));
                        indices.add(Integer.parseInt(tokens[3].split("/")[0]));

                        MTLMaterial material = Modelo.materiais.get(current_material);

                        cores.add(material.getDiffuseColor().r);
                        cores.add(material.getDiffuseColor().g);
                        cores.add(material.getDiffuseColor().b);
                    }

                } catch (NoSuchElementException e) {
                    break;
                }
            }

            return createModel(vertices, indices, cores);

        } catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    private static ArrayList<Modelo> createModel(ArrayList<Float> vertices, ArrayList<Integer> indices, ArrayList<Float> cores) {

        ArrayList<Modelo> modelos = new ArrayList<>();

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

        modelos.add(new Modelo(verticesArray, indicesArray, colours));

        return modelos;
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
