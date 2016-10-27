package cortez.bancodeservidores;

/**
 * Created by Usuario on 16/10/2016.
 */

public class User {

    //fields
    private String username;
    private String senha;
    private String email;
    private int id;
    private byte[] foto;
    private String first_name;
    private String last_name;


    User() {

    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
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

