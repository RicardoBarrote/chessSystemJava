package xadrez;

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
	
	public PosicaoXadrez getPosicaoXadrez () {
		return PosicaoXadrez.daPosicao(posicao);
	}

	protected boolean existePecaAdversaria(Posicao posicao) {
		PecaXadrez pX = (PecaXadrez) getTabuleiro().pecas(posicao);
		return pX != null && pX.getCor() != cor;
	}

}
