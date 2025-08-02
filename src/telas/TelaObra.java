package telas;

import javax.swing.*;
import modelo.*;
import dao.ObraDao;
import java.awt.event.*;
import java.util.List;
import java.awt.Window;

public class TelaObra extends JFrame {

	private JTextField campoCodigo, campoTitulo, campoAutor, campoAno;
	private JComboBox<String> comboTipo;

	public TelaObra() {
		setTitle("Cadastro de Obra");
		setSize(350, 300);
		setLayout(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JLabel l1 = new JLabel("Código:");
		l1.setBounds(20, 20, 100, 25);
		add(l1);

		campoCodigo = new JTextField();
		campoCodigo.setBounds(120, 20, 180, 25);
		add(campoCodigo);

		JLabel l2 = new JLabel("Título:");
		l2.setBounds(20, 60, 100, 25);
		add(l2);

		campoTitulo = new JTextField();
		campoTitulo.setBounds(120, 60, 180, 25);
		add(campoTitulo);

		JLabel l3 = new JLabel("Autor:");
		l3.setBounds(20, 100, 100, 25);
		add(l3);

		campoAutor = new JTextField();
		campoAutor.setBounds(120, 100, 180, 25);
		add(campoAutor);

		JLabel l4 = new JLabel("Ano:");
		l4.setBounds(20, 140, 100, 25);
		add(l4);

		campoAno = new JTextField();
		campoAno.setBounds(120, 140, 180, 25);
		add(campoAno);

		JLabel l5 = new JLabel("Tipo:");
		l5.setBounds(20, 180, 100, 25);
		add(l5);

		comboTipo = new JComboBox<>(new String[] { "Livro", "Revista", "Artigo" });
		comboTipo.setBounds(120, 180, 180, 25);
		add(comboTipo);

		JButton botaoCadastrar = new JButton("Cadastrar");
		botaoCadastrar.setBounds(120, 220, 100, 30);
		add(botaoCadastrar);

		botaoCadastrar.addActionListener(e -> cadastrarObra());
		setVisible(true);
	}

	private void cadastrarObra() {
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
				ObraDao.salvarTodas(obras);

//Atualiza todas as telas de consulta abertas
				atualizarTelasConsulta();

				JOptionPane.showMessageDialog(this, tipo + " cadastrado com sucesso!");
				limparCampos();
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Código e Ano devem ser números válidos.");
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
		campoCodigo.setText("");
		campoTitulo.setText("");
		campoAutor.setText("");
		campoAno.setText("");
		comboTipo.setSelectedIndex(0);
	}

	public static void main(String[] args) {
		new TelaObra();
	}
}