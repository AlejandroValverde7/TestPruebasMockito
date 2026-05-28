package org.AlejandroValverde.testMockito.ejemplos.services;

import org.AlejandroValverde.testMockito.ejemplos.models.Examen;
import org.AlejandroValverde.testMockito.ejemplos.repositories.ExamenRepository;

import java.util.Optional;

public class ExamenServiceImpl implements ExamenService{

    private ExamenRepository examenRepository;

    public ExamenServiceImpl(ExamenRepository examenRepository) {
        this.examenRepository = examenRepository;
    }

    //Metodo para buscar el examen por nombre segun el primero enconrtado
    @Override
    public Examen findExamenPorNombre(String nombre) {
        Optional<Examen> examenOptional = examenRepository.findAll().stream().filter(n -> n.getNombre().contains(nombre)).findFirst();
        Examen examen = null;
            if(examenOptional.isPresent()){
                examen = examenOptional.orElseThrow();
            }
        return examen;
    }

}
