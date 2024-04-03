package xadrez;

import tabuleiro.ExcecaoTabuleiro;
import tabuleiro.Pecas;
import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;

public abstract class PecaXadrez extends Pecas {

	private Cor cor;

	public PecaXadrez(Tabuleiro tabuleiro, Cor cor) {
		super(tabuleiro);
		this.cor = cor;
	}

	public Cor getCor() {
		return cor;
	}

	protected boolean existePecaAdversaria(Posicao posicao) throws ExcecaoTabuleiro {
		PecaXadrez pX = (PecaXadrez) getTabuleiro().pecas(posicao);
		return pX != null && pX.getCor() != cor;
	}

}
