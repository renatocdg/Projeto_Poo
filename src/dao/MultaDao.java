package dao;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import modelo.Multa;

public class MultaDao {
	
	private static final String ARQUIVO = "multas.json";
	
	private static final Gson gson = new Gson();

	public static List<Multa> carregarMultas() {
		try {
			FileReader reader = new FileReader(ARQUIVO);
			return gson.fromJson(reader, new TypeToken<List<Multa>>() {
			}.getType());
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}

	public static void salvar(List<Multa> multas) {
		try {
			FileWriter writer = new FileWriter(ARQUIVO);
			gson.toJson(multas, writer);
			writer.close();
		} catch (Exception e) {
			System.out.println("Erro ao salvar multas");
		}
	}
}