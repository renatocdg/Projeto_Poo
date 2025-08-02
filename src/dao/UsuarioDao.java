package dao;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import modelo.Usuario;

public class UsuarioDao {
	private static final String ARQUIVO = "usuarios.json";
	private static final Gson gson = new Gson();

	public static List<Usuario> carregar() {
		try {
			FileReader reader = new FileReader(ARQUIVO);
			List<Usuario> usuarios = gson.fromJson(reader, new TypeToken<List<Usuario>>() {
			}.getType());
			return usuarios != null ? usuarios : new ArrayList<>();
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}

//MÉTODO PARA ADICIONAR USUÁRIO
	public static void adicionarUsuario(Usuario novoUsuario) {
		List<Usuario> usuarios = carregar();
		usuarios.add(novoUsuario);
		salvar(usuarios);
	}

	public static void salvar(List<Usuario> usuarios) {
		
		try {
			FileWriter writer = new FileWriter(ARQUIVO);
			gson.toJson(usuarios, writer);
			writer.close();
		} catch (Exception e) {
			System.out.println("Erro ao salvar usuários");
		}
	}

	public static Usuario buscarPorMatricula(String matricula) {
		List<Usuario> usuarios = carregar();

		for (Usuario usuario : usuarios) {
			if (usuario.getMatricula().equalsIgnoreCase(matricula)) {
				return usuario;
			}
		}
		return null;
	}
}