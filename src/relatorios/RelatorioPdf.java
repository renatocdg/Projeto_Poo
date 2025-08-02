package relatorios;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import dao.*;
import modelo.*;

public class RelatorioPdf {
	private static final String PASTA_RELATORIOS = "relatorios/";

	private static void configurarDocumento(Document doc, String titulo) throws DocumentException {
		doc.add(new Paragraph(titulo, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)));
		doc.add(new Paragraph("Gerado em: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
		doc.add(Chunk.NEWLINE);
	}

	private static void mostrarSucesso(String caminho) {
		String caminhoAbsoluto = new File(caminho).getAbsolutePath();
		JOptionPane.showMessageDialog(null, "Relatório gerado com sucesso!\nSalvo em: " + caminhoAbsoluto, "Relatório",
				JOptionPane.INFORMATION_MESSAGE);
	}

	public static void gerarRelatorioEmprestimosDoMes() {
		String nomeArquivo = "emprestimos_mes_" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"))
				+ ".pdf";
		String caminhoCompleto = PASTA_RELATORIOS + nomeArquivo;

		try {
			new File(PASTA_RELATORIOS).mkdirs();
			Document doc = new Document();
			PdfWriter.getInstance(doc, new FileOutputStream(caminhoCompleto));
			doc.open();

			configurarDocumento(doc, "Relatório de Empréstimos do Mês");

			PdfPTable tabela = new PdfPTable(4);
			tabela.addCell("Usuário");
			tabela.addCell("Obra");
			tabela.addCell("Data Empréstimo");
			tabela.addCell("Status");

			DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			List<Emprestimo> emprestimos = EmprestimoDao.carregar();

			for (Emprestimo emp : emprestimos) {
				if (emp.getDataEmprestimo().getMonth() == LocalDate.now().getMonth()) {
					tabela.addCell(emp.getUsuario().getNome());
					tabela.addCell(emp.getObra().getTitulo());
					tabela.addCell(emp.getDataEmprestimo().format(fmt));
					tabela.addCell(emp.getDataDevolucao() != null ? "Devolvido" : "Pendente");
				}
			}

			doc.add(tabela);
			doc.close();
			mostrarSucesso(caminhoCompleto);

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao gerar relatório de empréstimos: " + e.getMessage(), "Erro",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void gerarRelatorioObrasMaisEmprestadas() {
		String nomeArquivo = "obras_mais_emprestadas_" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
				+ ".pdf";
		String caminhoCompleto = PASTA_RELATORIOS + nomeArquivo;

		try {
			new File(PASTA_RELATORIOS).mkdirs();
			Document doc = new Document();
			PdfWriter.getInstance(doc, new FileOutputStream(caminhoCompleto));
			doc.open();

			configurarDocumento(doc, "Obras Mais Emprestadas");

			PdfPTable tabela = new PdfPTable(2);
			tabela.addCell("Obra");
			tabela.addCell("Quantidade");

			List<Emprestimo> emprestimos = EmprestimoDao.carregar();
			Map<String, Integer> contagem = new HashMap<>();

			for (Emprestimo emp : emprestimos) {
				String chave = emp.getObra().getTitulo();
				contagem.put(chave, contagem.getOrDefault(chave, 0) + 1);
			}

			contagem.entrySet().stream().sorted((a, b) -> b.getValue().compareTo(a.getValue())).forEach(entry -> {
				tabela.addCell(entry.getKey());
				tabela.addCell(entry.getValue().toString());
			});

			doc.add(tabela);
			doc.close();
			mostrarSucesso(caminhoCompleto);

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao gerar relatório de obras: " + e.getMessage(), "Erro",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void gerarRelatorioUsuariosComMaisAtrasos() {
		String nomeArquivo = "usuarios_atrasos_" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
				+ ".pdf";
		String caminhoCompleto = PASTA_RELATORIOS + nomeArquivo;

		try {
			new File(PASTA_RELATORIOS).mkdirs();
			Document doc = new Document();
			PdfWriter.getInstance(doc, new FileOutputStream(caminhoCompleto));
			doc.open();

			configurarDocumento(doc, "Usuários com Mais Atrasos");

			PdfPTable tabela = new PdfPTable(2);
			tabela.addCell("Usuário");
			tabela.addCell("Atrasos");

			List<Multa> multas = MultaDao.carregarMultas();
			Map<String, Integer> atrasos = new HashMap<>();

			for (Multa multa : multas) {
				String usuario = multa.getEmprestimo().getUsuario().getNome();
				atrasos.put(usuario, atrasos.getOrDefault(usuario, 0) + 1);
			}

			atrasos.entrySet().stream().sorted((a, b) -> b.getValue().compareTo(a.getValue())).forEach(entry -> {
				tabela.addCell(entry.getKey());
				tabela.addCell(entry.getValue().toString());
			});

			doc.add(tabela);
			doc.close();
			mostrarSucesso(caminhoCompleto);

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao gerar relatório de atrasos: " + e.getMessage(), "Erro",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}