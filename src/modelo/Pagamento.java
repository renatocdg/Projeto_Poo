package modelo;

import java.time.LocalDate;

public class Pagamento {
	private String id;
	private String multaId;
	private double valorPago;
	private LocalDate dataPagamento;
	private MetodoPagamento metodoPagamento;
	private String usuarioId;

	public Pagamento(String id, String multaId, double valorPago, LocalDate dataPagamento,
			MetodoPagamento metodoPagamento, String usuarioId) {
		this.id = id;
		this.multaId = multaId;
		this.valorPago = valorPago;
		this.dataPagamento = dataPagamento;
		this.metodoPagamento = metodoPagamento;
		this.usuarioId = usuarioId;
	}

	public String getId() {
		return id;
	}

	public String getMultaId() {
		return multaId;
	}

	public double getValorPago() {
		return valorPago;
	}

	public LocalDate getDataPagamento() {
		return dataPagamento;
	}

	public MetodoPagamento getMetodoPagamento() {
		return metodoPagamento;
	}

	public String getUsuarioId() {
		return usuarioId;
	}

	@Override
	public String toString() {
		return "Pagamento{" + "id='" + id + '\'' + ", multaId='" + multaId + '\'' + ", valorPago=" + valorPago
				+ ", dataPagamento=" + dataPagamento + ", metodoPagamento=" + metodoPagamento + ", usuarioId='"
				+ usuarioId + '\'' + '}';
	}
}