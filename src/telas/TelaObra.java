package telas;

import javax.swing.*;
import modelo.*;
import dao.ObraDao;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class TelaObra extends JFrame {

	private JTextField campoCodigo, campoTitulo, campoAutor, campoAno, campoTempoEmprestimo;
	private JComboBox<String> comboTipo;
	private JComboBox<Obra> comboObras;
	private JButton btnSalvar, btnExcluir, btnLimpar, btnDebug;
	private Obra obraSelecionada;

	public TelaObra() {
		configurarJanela();
		inicializarComponentes();
		configurarLayout();
		carregarObras();
		adicionarListeners();

		setVisible(true);
	}

	private void configurarJanela() {
		setTitle("Gerenciamento de Obras");
		setSize(500, 400);
		setLayout(new GridLayout(10, 2, 10, 5));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	private void inicializarComponentes() {
		comboObras = new JComboBox<>();
		campoCodigo = new JTextField();
		campoTitulo = new JTextField();
		campoAutor = new JTextField();
		campoAno = new JTextField();
		campoTempoEmprestimo = new JTextField();
		comboTipo = new JComboBox<>(new String[] { "Livro", "Revista", "Artigo" });
		btnSalvar = new JButton("Salvar");
		btnExcluir = new JButton("Excluir");
		btnLimpar = new JButton("Limpar");
		btnDebug = new JButton("Debug");
	}

	private void configurarLayout() {
		add(new JLabel("Obra:"));
		add(comboObras);
		add(new JLabel("Código:"));
		add(campoCodigo);
		add(new JLabel("Título:"));
		add(campoTitulo);
		add(new JLabel("Autor:"));
		add(campoAutor);
		add(new JLabel("Ano:"));
		add(campoAno);
		add(new JLabel("Tipo:"));
		add(comboTipo);
		add(new JLabel("Tempo Empréstimo (dias):"));
		add(campoTempoEmprestimo);
		add(btnLimpar);
		add(btnSalvar);
		add(btnDebug);
		add(btnExcluir);
	}

	private void adicionarListeners() {
		comboObras.addActionListener(e -> selecionarObra());
		btnSalvar.addActionListener(this::salvarObra);
		btnExcluir.addActionListener(this::excluirObra);
		btnLimpar.addActionListener(e -> limparCampos());
		btnDebug.addActionListener(e -> debugSistema());

		// Listener para definir valores padrão conforme o tipo
		comboTipo.addActionListener(e -> {
			if (obraSelecionada == null) {
				String tipo = (String) comboTipo.getSelectedItem();
				switch (tipo) {
				case "Livro":
					campoTempoEmprestimo.setText("15");
					break;
				case "Revista":
					campoTempoEmprestimo.setText("7");
					break;
				case "Artigo":
					campoTempoEmprestimo.setText("5");
					break;
				}
			}
		});
	}

	private void carregarObras() {
		comboObras.removeAllItems();
		ObraDao.carregarTodas().forEach(comboObras::addItem);
		comboObras.insertItemAt(null, 0);
		comboObras.setSelectedIndex(0);
	}

	private void selecionarObra() {
		obraSelecionada = (Obra) comboObras.getSelectedItem();
		if (obraSelecionada != null) {
			campoCodigo.setText(String.valueOf(obraSelecionada.getCodigo()));
			campoTitulo.setText(obraSelecionada.getTitulo());
			campoAutor.setText(obraSelecionada.getAutor());
			campoAno.setText(String.valueOf(obraSelecionada.getAnoPublicacao()));
			campoTempoEmprestimo.setText(String.valueOf(obraSelecionada.getTempoEmprestimo()));

			if (obraSelecionada instanceof Livro) {
				comboTipo.setSelectedItem("Livro");
			} else if (obraSelecionada instanceof Revista) {
				comboTipo.setSelectedItem("Revista");
			} else if (obraSelecionada instanceof Artigo) {
				comboTipo.setSelectedItem("Artigo");
			}
		}
	}

	private void salvarObra(ActionEvent e) {
		try {
			int codigo = Integer.parseInt(campoCodigo.getText().trim());
			String titulo = campoTitulo.getText().trim();
			String autor = campoAutor.getText().trim();
			int ano = Integer.parseInt(campoAno.getText().trim());
			String tipo = (String) comboTipo.getSelectedItem();
			int tempoEmprestimo = Integer.parseInt(campoTempoEmprestimo.getText().trim());

			if (titulo.isEmpty() || autor.isEmpty()) {
				mostrarErro("Preencha todos os campos.");
				return;
			}

			if (tempoEmprestimo <= 0) {
				mostrarErro("Tempo de empréstimo deve ser maior que zero.");
				return;
			}

			List<Obra> obras = ObraDao.carregarTodas();
			String status = obraSelecionada != null ? obraSelecionada.getStatus() : "Disponível";

			if (obraSelecionada == null) {
				if (obras.stream().anyMatch(o -> o.getCodigo() == codigo)) {
					mostrarErro("Já existe uma obra com esse código.");
					return;
				}
				obras.add(criarObra(codigo, titulo, autor, ano, tipo, status, tempoEmprestimo));
			} else {
				obras.replaceAll(o -> o.getCodigo() == codigo
						? criarObra(codigo, titulo, autor, ano, tipo, status, tempoEmprestimo)
						: o);
			}

			ObraDao.salvarTodas(obras);
			mostrarSucesso("Obra salva com sucesso!");
			carregarObras();
			limparCampos();

		} catch (NumberFormatException ex) {
			mostrarErro("Código, Ano e Tempo de Empréstimo devem ser números válidos.");
		}
	}

	private Obra criarObra(int codigo, String titulo, String autor, int ano, String tipo, String status,
			int tempoEmprestimo) {
		switch (tipo) {
		case "Livro":
			return new Livro(codigo, titulo, autor, ano, status, tempoEmprestimo);
		case "Revista":
			return new Revista(codigo, titulo, autor, ano, status, tempoEmprestimo);
		case "Artigo":
			return new Artigo(codigo, titulo, autor, ano, status, tempoEmprestimo);
		default:
			return null;
		}
	}

	private void excluirObra(ActionEvent e) {
		if (obraSelecionada == null) {
			mostrarErro("Selecione uma obra para excluir.");
			return;
		}

		int confirmacao = JOptionPane.showConfirmDialog(this,
				"Tem certeza que deseja excluir '" + obraSelecionada.getTitulo() + "'?", "Confirmar Exclusão",
				JOptionPane.YES_NO_OPTION);

		if (confirmacao == JOptionPane.YES_OPTION) {
			List<Obra> obras = ObraDao.carregarTodas();
			obras.removeIf(o -> o.getCodigo() == obraSelecionada.getCodigo());
			ObraDao.salvarTodas(obras);
			mostrarSucesso("Obra excluída com sucesso!");
			limparCampos();
			carregarObras();
		}
	}

	private void limparCampos() {
		obraSelecionada = null;
		campoCodigo.setText("");
		campoTitulo.setText("");
		campoAutor.setText("");
		campoAno.setText("");
		campoTempoEmprestimo.setText("");
		comboTipo.setSelectedIndex(0);
		comboObras.setSelectedIndex(0);
	}

	private void debugSistema() {
		System.out.println("\n=== DEBUG ===");
		System.out.println("Obra selecionada: " + (obraSelecionada != null ? obraSelecionada.getTitulo() : "Nenhuma"));
		System.out.println("Campos:");
		System.out.println("Código: " + campoCodigo.getText());
		System.out.println("Título: " + campoTitulo.getText());
		System.out.println("Autor: " + campoAutor.getText());
		System.out.println("Ano: " + campoAno.getText());
		System.out.println("Tempo Empréstimo: " + campoTempoEmprestimo.getText());
		System.out.println("Tipo: " + comboTipo.getSelectedItem());

		ObraDao.imprimirObras();
	}

	private void mostrarSucesso(String mensagem) {
		JOptionPane.showMessageDialog(this, mensagem, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
	}

	private void mostrarErro(String mensagem) {
		JOptionPane.showMessageDialog(this, mensagem, "Erro", JOptionPane.ERROR_MESSAGE);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new TelaObra());
	}
}