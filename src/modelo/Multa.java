package modelo;

import java.time.LocalDate;

public class Multa {
	private LocalDate dataQuitacao;
	private final String id;
	private final Emprestimo emprestimo;
	private final double valor;
	private final int diasAtraso;
	private boolean quitada;
	private final LocalDate dataGeracao;

	public Multa(String id, Emprestimo emprestimo, double valor, int diasAtraso) {
		this.id = id;
		this.emprestimo = emprestimo;
		this.valor = valor;
		this.diasAtraso = diasAtraso;
		this.quitada = false;
		this.dataGeracao = LocalDate.now();
	}

// Método para quitar multa
	public void quitar() {
		this.quitada = true;
	}

// Getters (apenas os essenciais)
	public String getId() {
		return id;
	}

	public double getValor() {
		return valor;
	}

	public boolean isQuitada() {
		return quitada;
	}

	public Emprestimo getEmprestimo() {
		return emprestimo;
	}

	public int getDiasAtraso() {
		return diasAtraso;
	}

	public LocalDate getDataGeracao() {
		return dataGeracao;
	}
	
	public int getCodigoObra() {
	    return this.emprestimo.getObra().getCodigo();
	}

	public LocalDate getDataQuitacao() {
		return dataQuitacao;
	}

	public void setDataQuitacao(LocalDate dataQuitacao) {
		this.dataQuitacao = dataQuitacao;
	}
}