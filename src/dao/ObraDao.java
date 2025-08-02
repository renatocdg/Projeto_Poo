package dao;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import modelo.Obra;

public class ObraDao {
	private static final String ARQUIVO = "obras.json";
	private static final Gson gson = new Gson();

	public static List<Obra> carregarTodas() {
		try (FileReader reader = new FileReader(ARQUIVO)) {
			List<Obra> obras = gson.fromJson(reader, new TypeToken<List<Obra>>() {
			}.getType());
			return obras != null ? obras : new ArrayList<>();
		} catch (Exception e) {
			System.err.println("Erro ao carregar obras: " + e.getMessage());
			return new ArrayList<>();
		}
	}

	public static boolean salvarTodas(List<Obra> obras) {
		try (FileWriter writer = new FileWriter(ARQUIVO)) {
			gson.toJson(obras, writer);
			System.out.println("Obra salva com sucesso. Total: " + obras.size());
			return true;
		} catch (Exception e) {
			System.err.println("Erro ao salvar obras: " + e.getMessage());
			return false;
		}
	}
	
	public static Obra buscarPorCodigo(int codigo) {
	    return carregarTodas().stream()
	            .filter(o -> o.getCodigo() == codigo)
	            .findFirst()
	            .orElse(null);
	}
}