package dao;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import modelo.*;

public class ObraDao {
	private static final String ARQUIVO = "obras.json";

	private static final Gson gson = new GsonBuilder().registerTypeAdapter(Obra.class, new ObraDeserializer()).create();

	private static class ObraDeserializer implements JsonDeserializer<Obra> {
		@Override
		public Obra deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
			JsonObject jsonObject = json.getAsJsonObject();

			// Verifica se é um Livro
			if (jsonObject.has("tempoEmprestimo") && jsonObject.get("tempoEmprestimo").getAsInt() == 7) {
				return context.deserialize(json, Livro.class);
			}
			// Verifica se é uma Revista
			else if (jsonObject.has("tempoEmprestimo") && jsonObject.get("tempoEmprestimo").getAsInt() == 3) {
				return context.deserialize(json, Revista.class);
			}
			// Verifica se é um Artigo
			else if (jsonObject.has("tempoEmprestimo") && jsonObject.get("tempoEmprestimo").getAsInt() == 2) {
				return context.deserialize(json, Artigo.class);
			}
			return null;
		}
	}

	private static void criarArquivoSeNecessario() {
		try {
			File file = new File(ARQUIVO);
			if (!file.exists()) {
				file.createNewFile();
				FileWriter writer = new FileWriter(file);
				writer.write("[]");
				writer.close();
			}
		} catch (IOException e) {
			System.err.println("Erro ao criar arquivo: " + e.getMessage());
		}
	}

	public static List<Obra> carregarTodas() {
		criarArquivoSeNecessario();

		try (FileReader reader = new FileReader(ARQUIVO)) {
			Type tipoLista = new TypeToken<List<Obra>>() {
			}.getType();
			List<Obra> obras = gson.fromJson(reader, tipoLista);

			// Filtra obras nulas e inválidas
			if (obras != null) {
				obras.removeIf(Objects::isNull);
				return obras;
			}
			return new ArrayList<>();
		} catch (Exception e) {
			System.err.println("Erro ao carregar obras: " + e.getMessage());
			return new ArrayList<>();
		}
	}

	public static boolean salvarTodas(List<Obra> obras) {
		criarArquivoSeNecessario();

		try (FileWriter writer = new FileWriter(ARQUIVO)) {
			// Filtra obras inválidas antes de salvar
			List<Obra> obrasValidas = new ArrayList<>();
			for (Obra obra : obras) {
				if (obra != null && obra.getCodigo() > 0) {
					obrasValidas.add(obra);
				}
			}

			gson.toJson(obrasValidas, writer);
			return true;
		} catch (Exception e) {
			System.err.println("Erro ao salvar obras: " + e.getMessage());
			return false;
		}
	}

	// Método buscarPorCodigo implementado
	public static Obra buscarPorCodigo(int codigo) {
		List<Obra> obras = carregarTodas();
		for (Obra obra : obras) {
			if (obra != null && obra.getCodigo() == codigo) {
				return obra;
			}
		}
		return null;
	}
}