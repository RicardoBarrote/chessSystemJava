package xadrez;

import tabuleiro.ExcecaoTabuleiro;
import tabuleiro.Pecas;
import tabuleiro.Posicao;
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
	
	public PecaXadrez executarMovimentoXadrez (PosicaoXadrez posicaoOrigem, PosicaoXadrez posicaoAlvo) throws ExcecaoXadrez, ExcecaoTabuleiro {
		Posicao origem = posicaoOrigem.toPosicao();
		Posicao alvo = posicaoAlvo.toPosicao();
		validarPosicaoOrigem (origem);
		Pecas capturarPeca = fazerMover (origem, alvo);
		return (PecaXadrez) capturarPeca;
	}
	
	private Pecas fazerMover ( Posicao posicaoOrigem, Posicao posicaoAlvo) throws ExcecaoTabuleiro {
		Pecas p = tabuleiro.removerPeca(posicaoOrigem);
		Pecas capturarPeca = tabuleiro.removerPeca(posicaoAlvo);
		tabuleiro.lugarPeca(p, posicaoAlvo);
		return capturarPeca;
	}
	
	private void validarPosicaoOrigem (Posicao posicao) throws ExcecaoXadrez, ExcecaoTabuleiro {
		if (!tabuleiro.temUmaPeca(posicao)) {
			throw new ExcecaoXadrez("Não existe uma peça nesta posição. ");
		}
	}

	private void configuracaoInicial() throws ExcecaoTabuleiro, ExcecaoXadrez {
		novaPeca('a', 8, new Torre(tabuleiro, Cor.BLACK));
		novaPeca('e', 8, new Rei(tabuleiro, Cor.BLACK));
	}
}
