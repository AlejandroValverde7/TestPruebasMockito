package org.AlejandroValverde.testMockito.ejemplos.repositories;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class PreguntaRepositoryImpl implements PreguntaRepository{
    @Override
    public List<String> findPreguntasPorExamenId(Long id) {
        System.out.println("Pregunta repo find preguntas examen id");
        return Datos.PREGUNTAS;
    }

    @Override
    public void guardarVarias(List<String> preguntas) {
        System.out.println("Preguntas repo guardar varias");
    }
}
