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

// Getters e Setters
	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public TipoUsuario getTipo() {
		return tipo;
	}

	public void setTipo(TipoUsuario tipo) {
		this.tipo = tipo;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		if (telefone != null && !telefone.trim().isEmpty()) {
			this.telefone = telefone;
		}
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		if (email != null && email.contains("@")) {
			this.email = email;
		}
	}
}