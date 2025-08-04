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

	public TelaConsultaObras() {
		configurarJanela();
		criarComponentes();
		carregarTodas();
		setVisible(true);
	}

	private void configurarJanela() {
		setTitle("Consulta de Obras");
		setSize(800, 500);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	private void criarComponentes() {
		modeloTabela = new DefaultTableModel(new Object[] { "Código", "Título", "Autor", "Ano", "Tipo", "Status" }, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tabela = new JTable(modeloTabela);
		tabela.setAutoCreateRowSorter(true);

		JScrollPane scrollPane = new JScrollPane(tabela);
		add(scrollPane, BorderLayout.CENTER);

		JButton btnAtualizar = new JButton("Atualizar");
		btnAtualizar.addActionListener(e -> carregarTodas());

		JPanel painelBotoes = new JPanel();
		painelBotoes.add(btnAtualizar);
		add(painelBotoes, BorderLayout.SOUTH);
	}

	public void carregarTodas() {
		modeloTabela.setRowCount(0); // Limpa a tabela
		List<Obra> obras = ObraDao.carregarTodas();

		if (obras != null) {
			for (Obra obra : obras) {
				if (obra != null) {
					modeloTabela.addRow(new Object[] { obra.getCodigo(), obra.getTitulo(), obra.getAutor(),
							obra.getAnoPublicacao(), obra.getClass().getSimpleName(), obra.getStatus() });
				}
			}
		}
	}

	public void atualizarDados() {
		carregarTodas();
	}
}