package modelo;

public class Usuario {
	private String matricula;
	private String nome;
	private TipoUsuario tipo;
	private String telefone;
	private String email;

	public Usuario(String matricula, String nome, TipoUsuario tipo, String telefone, String email) {
		this.matricula = matricula;
		this.nome = nome;
		this.tipo = tipo;
		this.telefone = telefone;
		this.email = email;
	}

// Getters
	public String getMatricula() {
		return matricula;
	}

	public String getNome() {
		return nome;
	}

	public TipoUsuario getTipo() {
		return tipo;
	}

	public String getTelefone() {
		return telefone;
	}

	public String getEmail() {
		return email;
	}

// Setters
	public void setTelefone(String telefone) {
		if (telefone != null && !telefone.trim().isEmpty()) {
			this.telefone = telefone;
		}
	}

	public void setEmail(String email) {
		if (email != null && email.contains("@")) {
			this.email = email;
		}
	}
}