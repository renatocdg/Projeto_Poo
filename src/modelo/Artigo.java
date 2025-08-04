package modelo;

public class Artigo extends Obra {

// construtor
	public Artigo(int Codigo, String Titulo, String Autor, int AnoPublicacao, String Status, int TempoEmprestimo) {
		super(Codigo, Titulo, Autor, AnoPublicacao, Status, TempoEmprestimo);
	}

	public int getTempoEmprestimo() {
		return 2;
	}
}
