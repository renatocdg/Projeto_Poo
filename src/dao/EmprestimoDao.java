package dao;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import modelo.Emprestimo;

public class EmprestimoDao {
	private static final String ARQUIVO = "emprestimos.json";
	private static final Gson gson = new Gson();

	public static List<Emprestimo> carregar() {
		try {
			FileReader reader = new FileReader(ARQUIVO);
			return gson.fromJson(reader, new TypeToken<List<Emprestimo>>() {
			}.getType());
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}

	public static void salvar(List<Emprestimo> emprestimos) {
		try {
			FileWriter writer = new FileWriter(ARQUIVO);
			gson.toJson(emprestimos, writer);
			writer.close();
		} catch (Exception e) {
			System.out.println("Erro ao salvar empréstimos");
		}
	}
}