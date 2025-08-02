package dao;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import modelo.*;

public class ObraDao {
	
	private static final String ARQUIVO = "obras.json";

	private static Gson createGson() {
		return new Gson();
	}

	public static List<Obra> carregarTodas() {
		try (FileReader reader = new FileReader(ARQUIVO)) {
			// Usamos TypeToken para List<Object> em vez de List<Obra>
			List<Object> objetos = createGson().fromJson(reader, new TypeToken<List<Object>>() {
			}.getType());

			List<Obra> obras = new ArrayList<>();
			if (objetos != null) {
				for (Object obj : objetos) {
					String json = createGson().toJson(obj);
					if (json.contains("\"tipo\":\"Livro\"") || json.contains("\"class\":\"Livro\"")) {
						obras.add(createGson().fromJson(json, Livro.class));
					} else if (json.contains("\"tipo\":\"Revista\"") || json.contains("\"class\":\"Revista\"")) {
						obras.add(createGson().fromJson(json, Revista.class));
					} else if (json.contains("\"tipo\":\"Artigo\"") || json.contains("\"class\":\"Artigo\"")) {
						obras.add(createGson().fromJson(json, Artigo.class));
					}
				}
			}
			return obras;
		} catch (Exception e) {
			System.err.println("Erro ao carregar obras: " + e.getMessage());
			return new ArrayList<>();
		}
	}

	public static boolean salvarTodas(List<Obra> obras) {
		try (FileWriter writer = new FileWriter(ARQUIVO)) {
			createGson().toJson(obras, writer);
			System.out.println("Obra salva com sucesso. Total: " + obras.size());
			return true;
		} catch (Exception e) {
			System.err.println("Erro ao salvar obras: " + e.getMessage());
			return false;
		}
	}

	public static Obra buscarPorCodigo(int codigo) {
		return carregarTodas().stream().filter(o -> o.getCodigo() == codigo).findFirst().orElse(null);
	}
}