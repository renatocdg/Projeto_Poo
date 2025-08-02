package dao;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import modelo.*;

public class FuncionarioDao {
	
	private static final String ARQUIVO = "funcionarios.json";
	private static final Gson gson = new Gson();
	private static Funcionario funcionarioLogado = null; // Armazena o funcionário logado

	public static List<Funcionario> carregar() {
		try {
			FileReader reader = new FileReader(ARQUIVO);
			return gson.fromJson(reader, new TypeToken<List<Funcionario>>() {
			}.getType());
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}

//Método para autenticação (já existente)
	public static Funcionario autenticar(String login, String senha, TFuncionario tipo) {
		List<Funcionario> funcionarios = carregar();
		for (Funcionario f : funcionarios) {
			if (f.getLogin().equals(login) && f.getSenha().equals(senha) && f.getTipo() == tipo) {
				funcionarioLogado = f; // Armazena o funcionário logado
				return f;
			}
		}
		return null;
	}

//Método para obter o funcionário logado
	public static Funcionario funcionarioLogado() {
		return funcionarioLogado;
	}

	public static void logout() {
		funcionarioLogado = null;
	}

//Método para verificar permissões
	public static boolean temPermissao(TFuncionario tipoRequerido) {
		if (funcionarioLogado == null)
			return false;
		return funcionarioLogado.getTipo() == tipoRequerido;
	}

//Método para salvar (já existente)
	public static void salvar(List<Funcionario> funcionarios) {
		try {
			FileWriter writer = new FileWriter(ARQUIVO);
			gson.toJson(funcionarios, writer);
			writer.close();
		} catch (Exception e) {
			System.out.println("Erro ao salvar funcionários");
		}
	}

//Método para adicionar funcionário
	public static void adicionar(Funcionario novoFuncionario) {
		List<Funcionario> funcionarios = carregar();
		funcionarios.add(novoFuncionario);
		salvar(funcionarios);
	}

//Método para verificar se login já existe
	public static boolean loginExiste(String login) {
		return carregar().stream().anyMatch(f -> f.getLogin().equalsIgnoreCase(login));
	}
}