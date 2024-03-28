package xadrez;

import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.pecas.Rei;
import xadrez.pecas.Torre;

public class PartidaXadrez {

	private Tabuleiro tabuleiro;

	public PartidaXadrez() {
		tabuleiro = new Tabuleiro(8, 8);
		configuracaoInicial();
	}

	public PecaXadrez[][] getPecas() {
		PecaXadrez[][] mat = new PecaXadrez[tabuleiro.getLinhas()][tabuleiro.getColunas()];
		for (int i = 0; i < tabuleiro.getLinhas(); i++)
			for (int j = 0; j < tabuleiro.getColunas(); j++) {
				mat[i][j] = (PecaXadrez) tabuleiro.pecas(i, j);
			}
		return mat;
	}

	private void configuracaoInicial() {
		tabuleiro.lugarPeca(new Torre(tabuleiro, Cor.BLACK), new Posicao(0, 0));
		tabuleiro.lugarPeca(new Torre(tabuleiro, Cor.BLACK), new Posicao(0, 7));
		tabuleiro.lugarPeca(new Rei(tabuleiro, Cor.BLACK), new Posicao(0, 4));
		
		tabuleiro.lugarPeca(new Rei(tabuleiro, Cor.WHITE), new Posicao(7, 4));
		tabuleiro.lugarPeca(new Torre(tabuleiro, Cor.WHITE), new Posicao(7, 0));
		tabuleiro.lugarPeca(new Torre(tabuleiro, Cor.WHITE), new Posicao(7, 7));
	}
}
