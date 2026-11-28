package controller;

import exception.BibliotecaException;
import exception.DadoInvalidoException;
import exception.MaterialIndisponivelException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import model.MaterialBibliografico;
import model.Repositorio;
import java.util.stream.Collectors;

public class MaterialService {
    private Repositorio<MaterialBibliografico> repositorio;

    public MaterialService(Repositorio<MaterialBibliografico> repositorio) {
        this.repositorio = repositorio;
    }

    public void cadastrar(MaterialBibliografico material) throws BibliotecaException {
        if (repositorio.buscarPorId(material.getID()) != null) {
            throw new BibliotecaException("Já existe material com o ID " + material.getId());
        }
        repositorio.adicionar(material);
    }

    public List<MaterialBibliografico> buscarPorTitulo(String titulo) {
        return repositorio.listarTodos().stream()
                .filter(m -> m.getTitulo().toLowerCase().contains(titulo.toLowerCase()))
                .collect(Collectors.toList());
    }

    public MaterialBibliografico buscarPorId(int id) {
        return repositorio.buscarPorId(id);
    }

    public List<MaterialBibliografico> listarTodos() {
        return repositorio.listarTodos();
    }

    public void atualizar(MaterialBibliografico material) throws BibliotecaException {
        repositorio.atualizar(material);
    }

    public void remover(int id) throws BibliotecaException {
        repositorio.remover(id);
    }
}