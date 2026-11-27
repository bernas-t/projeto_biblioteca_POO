package service;

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

public class MaterialService {
    private final Repositorio<MaterialBibliografico> repositorio;

    public MaterialService(Repositorio<MaterialBibliografico> repositorio) {
        this.repositorio = Objects.requireNonNull(repositorio);
    }

    public int proximoId() {
        return repositorio.proximoId();
    }

    public void cadastrar(MaterialBibliografico material) throws BibliotecaException {
        if (material == null) {
            throw new DadoInvalidoException("O material é obrigatório.");
        }
        repositorio.adicionar(material);
    }

    public MaterialBibliografico buscarPorId(int id) throws BibliotecaException {
        MaterialBibliografico material = repositorio.buscarPorId(id);
        if (material == null) {
            throw new BibliotecaException("Material " + id + " não encontrado.");
        }
        return material;
    }

    public List<MaterialBibliografico> listarTodos() {
        return repositorio.listarTodos();
    }

    public List<MaterialBibliografico> buscarPorTitulo(String trecho) {
        if (trecho == null || trecho.isBlank()) {
            return repositorio.listarTodos();
        }
        String procurado = normalizar(trecho);
        List<MaterialBibliografico> encontrados = new ArrayList<>();
        for (MaterialBibliografico material : repositorio.listarTodos()) {
            if (normalizar(material.getTitulo()).contains(procurado)) {
                encontrados.add(material);
            }
        }
        return encontrados;
    }

    public void atualizar(MaterialBibliografico material) throws BibliotecaException {
        if (material == null) {
            throw new DadoInvalidoException("O material é obrigatório.");
        }
        buscarPorId(material.getId());
        repositorio.atualizar(material);
    }

    public void remover(int id) throws BibliotecaException {
        MaterialBibliografico material = buscarPorId(id);
        if (!material.isDisponivel()) {
            throw new MaterialIndisponivelException(
                    "O material " + id + " está emprestado e não pode ser removido.");
        }
        repositorio.remover(id);
    }

    private static String normalizar(String texto) {
        String semAcentos = Normalizer.normalize(texto, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        return semAcentos.toLowerCase(Locale.ROOT).trim();
    }
}
