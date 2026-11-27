package repository;

import exception.PersistenciaException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import model.Identificavel;

public class RepositorioArquivo<T extends Identificavel & Serializable> implements Repositorio<T> {
    private final String caminhoArquivo;
    private List<T> itens;

    public RepositorioArquivo(String caminhoArquivo) throws PersistenciaException {
        if (caminhoArquivo == null || caminhoArquivo.isBlank()) {
            throw new IllegalArgumentException("caminhoArquivo é obrigatório.");
        }
        this.caminhoArquivo = caminhoArquivo;
        this.itens = new ArrayList<>();
        carregar();
    }

    @Override
    public void adicionar(T item) throws PersistenciaException {
        Objects.requireNonNull(item, "item não pode ser nulo");
        if (buscarPorId(item.getId()) != null) {
            throw new IllegalArgumentException("Já existe um item com id " + item.getId() + ".");
        }
        alterar(() -> itens.add(item));
    }

    @Override
    public T buscarPorId(int id) {
        for (T item : itens) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    @Override
    public List<T> listarTodos() {
        return List.copyOf(itens);
    }

    @Override
    public void atualizar(T item) throws PersistenciaException {
        Objects.requireNonNull(item, "item não pode ser nulo");
        final int indice = indiceDe(item.getId());
        alterar(() -> itens.set(indice, item));
    }

    @Override
    public void remover(int id) throws PersistenciaException {
        final int indice = indiceDe(id);
        alterar(() -> itens.remove(indice));
    }

    @Override
    public int proximoId() {
        int maior = 0;
        for (T item : itens) {
            maior = Math.max(maior, item.getId());
        }
        return maior + 1;
    }

    private int indiceDe(int id) {
        for (int i = 0; i < itens.size(); i++) {
            if (itens.get(i).getId() == id) {
                return i;
            }
        }
        throw new IllegalArgumentException("Não existe item com id " + id + ".");
    }

    private void alterar(Runnable alteracao) throws PersistenciaException {
        List<T> copia = new ArrayList<>(itens);
        alteracao.run();
        try {
            salvar();
        } catch (PersistenciaException e) {
            itens = copia;
            throw e;
        }
    }

    @SuppressWarnings("unchecked")
    private void carregar() throws PersistenciaException {
        Path caminho = Paths.get(caminhoArquivo);
        if (!Files.exists(caminho)) {
            return;
        }
        try (ObjectInputStream in =
                new ObjectInputStream(new BufferedInputStream(Files.newInputStream(caminho)))) {
            itens = new ArrayList<>((List<T>) in.readObject());
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            throw new PersistenciaException("Não foi possível ler o arquivo '" + caminhoArquivo + "'.", e);
        }
    }

    private void salvar() throws PersistenciaException {
        Path destino = Paths.get(caminhoArquivo);
        Path temporario = Paths.get(caminhoArquivo + ".tmp");
        try {
            Path pasta = destino.toAbsolutePath().getParent();
            if (pasta != null) {
                Files.createDirectories(pasta);
            }
            try (ObjectOutputStream out =
                    new ObjectOutputStream(new BufferedOutputStream(Files.newOutputStream(temporario)))) {
                out.writeObject(new ArrayList<>(itens));
            }
            Files.move(temporario, destino, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new PersistenciaException("Não foi possível gravar o arquivo '" + caminhoArquivo + "'.", e);
        }
    }
}
