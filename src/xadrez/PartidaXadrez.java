package xadrez;

import tabuleiro.ExcecaoTabuleiro;
import tabuleiro.Tabuleiro;
import xadrez.pecas.Rei;
import xadrez.pecas.Torre;

public class PartidaXadrez {

	private Tabuleiro tabuleiro;

	public PartidaXadrez() throws ExcecaoTabuleiro, ExcecaoXadrez {
		tabuleiro = new Tabuleiro(8, 8);
		configuracaoInicial();
	}

	public PecaXadrez[][] getPecas() throws ExcecaoTabuleiro {
		PecaXadrez[][] mat = new PecaXadrez[tabuleiro.getLinhas()][tabuleiro.getColunas()];
		for (int i = 0; i < tabuleiro.getLinhas(); i++)
			for (int j = 0; j < tabuleiro.getColunas(); j++) {
				mat[i][j] = (PecaXadrez) tabuleiro.pecas(i, j);
			}
		return mat;
	}

	private void novaPeca(char coluna, int linha, PecaXadrez peca) throws ExcecaoTabuleiro, ExcecaoXadrez {
		tabuleiro.lugarPeca(peca, new PosicaoXadrez(coluna, linha).toPosicao());
	}

	private void configuracaoInicial() throws ExcecaoTabuleiro, ExcecaoXadrez {
		novaPeca('a', 8, new Torre(tabuleiro, Cor.BLACK));
		novaPeca('e', 8, new Rei(tabuleiro, Cor.BLACK));
	}
}
