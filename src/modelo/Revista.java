package modelo;

public class Revista extends Obra{

//construtor
	public Revista(int Codigo, String Titulo, String Autor, int AnoPublicacao, String Status) {
		super(Codigo, Titulo, Autor, AnoPublicacao, Status);
	}

	public int getTempoEmprestimo() {
		return 3;
	}
}
