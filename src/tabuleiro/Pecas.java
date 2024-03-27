package tabuleiro;

public class Pecas {

	protected Posicao posicao;
	private Tabuleiro tabuleiro;

	public Pecas() {

	}

	public Pecas(Tabuleiro tabuleiro) {
		this.tabuleiro = tabuleiro;
	}

	protected Tabuleiro getTabuleiro() {
		return tabuleiro;
	}

	

}
