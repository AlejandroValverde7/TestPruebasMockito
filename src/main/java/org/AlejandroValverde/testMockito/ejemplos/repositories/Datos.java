package org.AlejandroValverde.testMockito.ejemplos.repositories;

import org.AlejandroValverde.testMockito.ejemplos.models.Examen;

import java.util.Arrays;
import java.util.List;

public class Datos {
    //Datos para las pruebnas del test
    public final static List<Examen> EXAMENES = Arrays.asList(new Examen(5L,"Matematicas"),
            new Examen(6L,"Lengua"),
            new Examen(7L,"Historia"),
            new Examen(8L,"Fisica"));

    public final static List<Examen> EXAMENES_ID_NULL = Arrays.asList(new Examen(null,"Matematicas"),
            new Examen(null,"Lengua"),
            new Examen(null,"Historia"),
            new Examen(null,"Fisica"));

    public final static List<Examen> EXAMENES_ID_NEGATIVOS = Arrays.asList(new Examen(-5L,"Matematicas"),
            new Examen(-6L,"Lengua"),
            new Examen(-7L,"Historia"),
            new Examen(null,"Fisica"));

    public final static List<String> PREGUNTAS = Arrays.asList("aritmetica","integrales","derivadas","trigonometria","geometria");

    public final static Examen EXAMEN = new Examen(null,"Fisica");
}
