package modelo;

import java.time.LocalDate;

public class Emprestimo {
	private int id;
	private Obra obra;
	private Usuario usuario;
	private LocalDate dataEmprestimo;
	private LocalDate dataDevolucao;
	private boolean devolvido;

	public Emprestimo(Obra obra, Usuario usuario) {
		this.obra = obra;
		this.usuario = usuario;
		this.dataEmprestimo = LocalDate.now();
		this.devolvido = false;
	}

	public void registrarDevolucao() {
		this.dataDevolucao = LocalDate.now();
		this.devolvido = true;
		obra.setStatus("Disponível");
	}

	public Obra getObra() {
		return obra;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public LocalDate getDataEmprestimo() {
		return dataEmprestimo;
	}

	public void setDataEmprestimo(LocalDate dataEmprestimo) {
		this.dataEmprestimo = dataEmprestimo;
	}

	public LocalDate getDataDevolucao() {
		return dataDevolucao;
	}

	public void setDataDevolucao(LocalDate dataDevolucao) {
		this.dataDevolucao = dataDevolucao;
	}

	public int getCodigoObra() {
		return this.obra.getCodigo();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}