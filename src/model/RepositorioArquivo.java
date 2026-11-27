package model;
import exception.PersistenciaException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.ToIntFunction;

public class RepositorioArquivo<T extends Serializable> implements Repositorio<T> {
    private String caminhoArquivo;
    private List<T> itens;
    private ToIntFunction<T> extratorId;

    public RepositorioArquivo(String caminhoArquvio, ToIntFunction<T> extratorId) throws PersistenciaException {
        this.caminhoArquivo = caminhoArquivo;
        this.itens = new ArrayList<>();
        this.extratorId = extratorId;
    }

    @Override
    public void adicionar(T item) throws PersistenciaException{
        itens.add(item);
    }

    @Override 
    public T buscarPorId(int id){
        for (T item : itens){
            if (extratorId.applyAsInt(item) == id){
                return item;
            }
        }
        return null;
    }
    @Override
    public List<T> listarTodos(){
        return Collections.unmodifiableList(itens);
    }

    @Override 
    public void atualizar(T item) throws PersistenciaException {
        int idAlvo = extratorId.applyAsInt(item);
        for (int i = 0; i < itens.size(); i++) {
            if (extratorId.applyAsInt(itens.get(i)) == idAlvo) {
                itens.set(i, item);
                return;
            }
        }
    }
    public void remover(int id) throws PersistenciaException {
        boolean removido = itens.removeIf(item -> extratorId.applyAsInt(item) == id);
        if (!removido){
            throw new PersistenciaException("id não encontrado");
        }
    }



    private void carregar() throws PersistenciaException {
        File arquivo = new File(caminhoArquivo);
        if (!arquivo.exists()) {
            this.itens = new ArrayList<>();
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
            this.itens = (List<T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new PersistenciaException("Falha ao carregar dados de " + caminhoArquivo + ": " + e.getMessage());
        }
    }

    private void salvar() throws PersistenciaException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(caminhoArquivo))) {
            oos.writeObject(itens);
        } catch (IOException e) {
            throw new PersistenciaException("Falha ao salvar dados em " + caminhoArquivo + ": " + e.getMessage());
        }
    }

}
