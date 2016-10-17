package cortez.bancodeservidores;

/**
 * Created by Usuario on 16/10/2016.
 */

public class User {

    //fields
    private String username;
    private String senha;
    private String nome;
    private String email;
    private int id;
    private byte[] foto;


    User() {

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String user) {
        this.username = user;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}
}

