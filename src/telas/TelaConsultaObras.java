package telas;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import modelo.*;
import dao.*;
import java.awt.*;
import java.util.List;

public class TelaConsultaObras extends JFrame {

	private DefaultTableModel modeloTabela;
	private JTable tabela;
	private JComboBox<String> comboFiltro;

	public TelaConsultaObras() {
		configurarJanela();
		criarComponentes();
		carregarTodas();
		setVisible(true);
	}

	private void configurarJanela() {
		setTitle("Consulta de Obras");
		setSize(900, 600);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	private void criarComponentes() {
// Painel principal
		JPanel painelPrincipal = new JPanel(new BorderLayout(10, 10));
		painelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

// Painel de filtros
		JPanel painelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

		comboFiltro = new JComboBox<>(
				new String[] { "Todas", "Disponíveis", "Emprestadas", "Livros", "Revistas", "Artigos" });
		comboFiltro.addActionListener(e -> filtrarObras());

		JButton btnAtualizar = new JButton("Atualizar");
		btnAtualizar.addActionListener(e -> carregarTodas());

		painelFiltros.add(new JLabel("Filtrar por:"));
		painelFiltros.add(comboFiltro);
		painelFiltros.add(btnAtualizar);

		painelPrincipal.add(painelFiltros, BorderLayout.NORTH);

// Tabela
		modeloTabela = new DefaultTableModel(new Object[] { "Código", "Título", "Autor", "Ano", "Tipo", "Status" }, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tabela = new JTable(modeloTabela);
		tabela.setAutoCreateRowSorter(true);
		tabela.setFillsViewportHeight(true);
		tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// Melhorando a aparência da tabela
		tabela.setRowHeight(25);
		tabela.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));

		JScrollPane scrollPane = new JScrollPane(tabela);
		scrollPane.setPreferredSize(new Dimension(850, 400));
		painelPrincipal.add(scrollPane, BorderLayout.CENTER);

		add(painelPrincipal);
	}

	public void carregarTodas() {
		modeloTabela.setRowCount(0);
		List<Obra> obras = ObraDao.carregarTodas();

		for (Obra obra : obras) {
			modeloTabela.addRow(new Object[] { obra.getCodigo(), obra.getTitulo(), obra.getAutor(),
					obra.getAnoPublicacao(), obra.getClass().getSimpleName(), obra.getStatus() });
		}
	}

	private void filtrarObras() {
		String filtro = (String) comboFiltro.getSelectedItem();
		List<Obra> obras = ObraDao.carregarTodas();

		modeloTabela.setRowCount(0);

		for (Obra obra : obras) {
			boolean adicionar = false;

			switch (filtro) {
			case "Todas":
				adicionar = true;
				break;
			case "Disponíveis":
				adicionar = obra.getStatus().equals("Disponível");
				break;
			case "Emprestadas":
				adicionar = obra.getStatus().equals("Emprestado");
				break;
			case "Livros":
				adicionar = obra instanceof Livro;
				break;
			case "Revistas":
				adicionar = obra instanceof Revista;
				break;
			case "Artigos":
				adicionar = obra instanceof Artigo;
				break;
			}

			if (adicionar) {
				modeloTabela.addRow(new Object[] { obra.getCodigo(), obra.getTitulo(), obra.getAutor(),
						obra.getAnoPublicacao(), obra.getClass().getSimpleName(), obra.getStatus() });
			}
		}
	}

	public void atualizarDados() {
		carregarTodas();
	}
}