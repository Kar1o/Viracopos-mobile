package carlos.figueiredo.com.viracopos.Model;

/**
 * Created by k on 6/13/15.
 */
public class Player {
    private String nome;
    private int pontos = 0;

    public Player(String nome) {
        this.nome = nome;
    }

    public Player () { }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getPontos() {
        return pontos;
    }

    public void setPontos() {
        this.pontos += 1;
    }
}

