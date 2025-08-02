package telas;

import javax.swing.*;
import modelo.*;
import dao.ObraDao;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class TelaObra extends JFrame {
	
	private JTextField campoCodigo, campoTitulo, campoAutor, campoAno;
	private JComboBox<String> comboTipo;
	private JComboBox<Obra> comboObras;
	private JButton btnSalvar, btnExcluir;
	private Obra obraSelecionada;

	public TelaObra() {
		setTitle("Gerenciamento de Obras");
		setSize(400, 350);
		setLayout(new GridLayout(7, 2, 10, 10));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		// Componentes
		comboObras = new JComboBox<>();
		campoCodigo = new JTextField();
		campoTitulo = new JTextField();
		campoAutor = new JTextField();
		campoAno = new JTextField();
		comboTipo = new JComboBox<>(new String[] { "Livro", "Revista", "Artigo" });
		btnSalvar = new JButton("Salvar");
		btnExcluir = new JButton("Excluir");
		JButton btnNovo = new JButton("Novo");

		// Layout
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
		add(btnNovo);
		add(btnSalvar);
		add(new JLabel());
		add(btnExcluir);

		// Carrega obras
		carregarObras();

		// Listeners
		comboObras.addActionListener(e -> selecionarObra());
		btnSalvar.addActionListener(this::salvarObra);
		btnExcluir.addActionListener(this::excluirObra);
		btnNovo.addActionListener(e -> limparCampos());

		setVisible(true);
	}

	private void carregarObras() {
		comboObras.removeAllItems();
		List<Obra> obras = ObraDao.carregarTodas();
		obras.forEach(comboObras::addItem);
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
			comboTipo.setSelectedItem(obraSelecionada.getClass().getSimpleName());
		}
	}

	private void salvarObra(ActionEvent e) {
		try {
			int codigo = Integer.parseInt(campoCodigo.getText().trim());
			String titulo = campoTitulo.getText().trim();
			String autor = campoAutor.getText().trim();
			int ano = Integer.parseInt(campoAno.getText().trim());
			String tipo = (String) comboTipo.getSelectedItem();

			if (titulo.isEmpty() || autor.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Preencha todos os campos.");
				return;
			}

			List<Obra> obras = ObraDao.carregarTodas();

			if (obraSelecionada == null) {
				// Nova obra
				if (obras.stream().anyMatch(o -> o.getCodigo() == codigo)) {
					JOptionPane.showMessageDialog(this, "Já existe uma obra com esse código.");
					return;
				}

				Obra nova = switch (tipo) {
				case "Livro" -> new Livro(codigo, titulo, autor, ano, "Disponível");
				case "Revista" -> new Revista(codigo, titulo, autor, ano, "Disponível");
				case "Artigo" -> new Artigo(codigo, titulo, autor, ano, "Disponível");
				default -> null;
				};

				if (nova != null) {
					obras.add(nova);
					obraSelecionada = nova;
				}
			} else {
				// Edição
				obraSelecionada.setCodigo(codigo);
				obraSelecionada.setTitulo(titulo);
				obraSelecionada.setAutor(autor);
				obraSelecionada.setAnoPublicacao(ano);
			}

			ObraDao.salvarTodas(obras);
			atualizarTelasConsulta();
			JOptionPane.showMessageDialog(this, "Obra salva com sucesso!");
			carregarObras();
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "Código e Ano devem ser números válidos.");
		}
	}

	private void excluirObra(ActionEvent e) {
		if (obraSelecionada == null) {
			JOptionPane.showMessageDialog(this, "Selecione uma obra para excluir.");
			return;
		}

		int confirmacao = JOptionPane.showConfirmDialog(this,
				"Tem certeza que deseja excluir a obra " + obraSelecionada.getTitulo() + "?", "Confirmar Exclusão",
				JOptionPane.YES_NO_OPTION);

		if (confirmacao == JOptionPane.YES_OPTION) {
			List<Obra> obras = ObraDao.carregarTodas();
			obras.remove(obraSelecionada);
			ObraDao.salvarTodas(obras);
			atualizarTelasConsulta();
			JOptionPane.showMessageDialog(this, "Obra excluída com sucesso!");
			limparCampos();
			carregarObras();
		}
	}

	private void atualizarTelasConsulta() {
		for (Window window : Window.getWindows()) {
			if (window instanceof TelaConsultaObras) {
				((TelaConsultaObras) window).atualizarDados();
			}
		}
	}

	private void limparCampos() {
		obraSelecionada = null;
		campoCodigo.setText("");
		campoTitulo.setText("");
		campoAutor.setText("");
		campoAno.setText("");
		comboTipo.setSelectedIndex(0);
		comboObras.setSelectedIndex(0);
	}
}