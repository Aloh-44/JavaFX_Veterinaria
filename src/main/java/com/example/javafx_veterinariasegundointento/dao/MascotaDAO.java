package com.example.javafx_veterinariasegundointento.dao;

import com.example.javafx_veterinariasegundointento.model.Gato;
import com.example.javafx_veterinariasegundointento.model.Mascota;
import com.example.javafx_veterinariasegundointento.model.Perro;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MascotaDAO {
    private static final String ARCHIVO_PERROS = "perros.json";
    private static final String ARCHIVO_GATOS = "gatos.json";
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public void guardarTodos(List<Mascota> mascotas)throws IOException{
        List<Perro> perros = new ArrayList<>();
        List<Gato> gatos = new ArrayList<>();

        for (Mascota m : mascotas){
            if (m instanceof Perro p){
                perros.add(p);
            } else if (m instanceof Gato g) {
                gatos.add(g);
            }
        }
        try (FileWriter writerPerros = new FileWriter(ARCHIVO_PERROS)){
            gson.toJson(perros, writerPerros);
        }
        try (FileWriter writerGatos = new FileWriter(ARCHIVO_GATOS)){
            gson.toJson(gatos, writerGatos);
        }
    }
    public List<Mascota> cargarTodos()throws IOException{
        List<Mascota> mascotas = new ArrayList<>();
        File fileperros = new File(ARCHIVO_PERROS);
        if (fileperros.exists()){
            try (FileReader reader = new FileReader(fileperros)){
                Type tipoListaPerros = new TypeToken<List<Perro>>() {}.getType();
                List<Perro>perros = gson.fromJson(reader, tipoListaPerros);
                if (perros != null){
                    mascotas.addAll(perros);
                }
            }
        }
        File filegatos = new File(ARCHIVO_GATOS);
        if (filegatos.exists()){
            try (FileReader reader = new FileReader(filegatos)){
                Type tipoListaGatos = new TypeToken<List<Perro>>() {}.getType();
                List<Gato>gatos = gson.fromJson(reader, tipoListaGatos);
                if (gatos != null){
                    mascotas.addAll(gatos);
                }
            }
        }
        return mascotas;
    }
}

