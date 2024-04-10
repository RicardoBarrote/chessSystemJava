package xadrez.pecas;

import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.Cor;
import xadrez.PartidaXadrez;
import xadrez.PecaXadrez;

public class Peao extends PecaXadrez {

	private PartidaXadrez partidaXadrez;

	public Peao(Tabuleiro tabuleiro, Cor cor, PartidaXadrez partidaXadrez) {
		super(tabuleiro, cor);
		this.partidaXadrez = partidaXadrez;
	}

	@Override
	public boolean[][] movimentosPossiveis() {
		boolean[][] mat = new boolean[getTabuleiro().getLinhas()][getTabuleiro().getColunas()];

		Posicao p = new Posicao(0, 0);

		if (getCor() == Cor.WHITE) {
			p.setValor(posicao.getLinha() - 1, posicao.getColuna());
			if (getTabuleiro().posicaoExistente(p) && !getTabuleiro().temUmaPeca(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			p.setValor(posicao.getLinha() - 2, posicao.getColuna());
			Posicao p2 = new Posicao(posicao.getLinha() - 1, posicao.getColuna());
			if (getTabuleiro().posicaoExistente(p) && !getTabuleiro().temUmaPeca(p)
					&& getTabuleiro().posicaoExistente(p2) && !getTabuleiro().temUmaPeca(p2)
					&& getContagemMovimento() == 0) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			p.setValor(posicao.getLinha() - 1, posicao.getColuna() - 1);
			if (getTabuleiro().posicaoExistente(p) && existePecaAdversaria(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			p.setValor(posicao.getLinha() - 1, posicao.getColuna() + 1);
			if (getTabuleiro().posicaoExistente(p) && existePecaAdversaria(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}

			// Movimento especial enPassant (Peças brancas)
			if (posicao.getLinha() == 3) {
				Posicao posicaoEsquerda = new Posicao(posicao.getLinha(), posicao.getColuna() - 1);
				if (getTabuleiro().posicaoExistente(posicaoEsquerda) && existePecaAdversaria(posicaoEsquerda) && getTabuleiro().pecas(posicaoEsquerda) == partidaXadrez.getEnPassantVulneravel()) {
					mat[posicaoEsquerda.getLinha() - 1][posicaoEsquerda.getColuna()] = true;
				}
				Posicao posicaoDireita = new Posicao(posicao.getLinha(), posicao.getColuna() + 1);
				if (getTabuleiro().posicaoExistente(posicaoDireita) && existePecaAdversaria(posicaoDireita) && getTabuleiro().pecas(posicaoDireita) == partidaXadrez.getEnPassantVulneravel()) {
					mat[posicaoDireita.getLinha() - 1][posicaoDireita.getColuna()] = true;
				}
			}
		}

		else {
			p.setValor(posicao.getLinha() + 1, posicao.getColuna());
			if (getTabuleiro().posicaoExistente(p) && !getTabuleiro().temUmaPeca(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			p.setValor(posicao.getLinha() + 2, posicao.getColuna());
			Posicao p2 = new Posicao(posicao.getLinha() + 1, posicao.getColuna());
			if (getTabuleiro().posicaoExistente(p) && !getTabuleiro().temUmaPeca(p)
					&& getTabuleiro().posicaoExistente(p2) && !getTabuleiro().temUmaPeca(p2)
					&& getContagemMovimento() == 0) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			p.setValor(posicao.getLinha() + 1, posicao.getColuna() - 1);
			if (getTabuleiro().posicaoExistente(p) && existePecaAdversaria(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}
			p.setValor(posicao.getLinha() + 1, posicao.getColuna() + 1);
			if (getTabuleiro().posicaoExistente(p) && existePecaAdversaria(p)) {
				mat[p.getLinha()][p.getColuna()] = true;
			}

			// Movimento especial enPassant (Peças pretas)
			if (posicao.getLinha() == 4) {
				Posicao posicaoEsquerda = new Posicao(posicao.getLinha(), posicao.getColuna() - 1);
				if (getTabuleiro().posicaoExistente(posicaoEsquerda) && existePecaAdversaria(posicaoEsquerda)&& getTabuleiro().pecas(posicaoEsquerda) == partidaXadrez.getEnPassantVulneravel()) {
					mat[posicaoEsquerda.getLinha() + 1][posicaoEsquerda.getColuna()] = true;
				}
				Posicao posicaoDireita = new Posicao(posicao.getLinha(), posicao.getColuna() + 1);
				if (getTabuleiro().posicaoExistente(posicaoDireita) && existePecaAdversaria(posicaoDireita) && getTabuleiro().pecas(posicaoDireita) == partidaXadrez.getEnPassantVulneravel()) {
					mat[posicaoDireita.getLinha() + 1][posicaoDireita.getColuna()] = true;
				}
			}
		}
		return mat;
	}

	@Override
	public String toString() {
		return "P";
	}
}
