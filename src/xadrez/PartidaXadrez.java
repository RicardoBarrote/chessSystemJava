package xadrez;

import tabuleiro.ExcecaoTabuleiro;
import tabuleiro.Pecas;
import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.pecas.Rei;
import xadrez.pecas.Torre;

public class PartidaXadrez {

	private Tabuleiro tabuleiro;

	public PartidaXadrez() throws ExcecaoTabuleiro {
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

	public boolean [][] possivelMovimento (PosicaoXadrez origemPosicao) throws ExcecaoTabuleiro {
		Posicao posicao = origemPosicao.toPosicao();
		validarPosicaoOrigem(posicao);
		return tabuleiro.pecas(posicao).movimentosPossiveis();
	}
	
	private void novaPeca(char coluna, int linha, PecaXadrez peca) throws ExcecaoTabuleiro {
		tabuleiro.lugarPeca(peca, new PosicaoXadrez(coluna, linha).toPosicao());
	}
	
	public PecaXadrez executarMovimentoXadrez (PosicaoXadrez posicaoOrigem, PosicaoXadrez posicaoAlvo) throws ExcecaoTabuleiro {
		Posicao origem = posicaoOrigem.toPosicao();
		Posicao alvo = posicaoAlvo.toPosicao();
		validarPosicaoOrigem (origem);
		validarPosicaoAlvo (origem, alvo);
		Pecas capturarPeca = fazerMover (origem, alvo);
		return (PecaXadrez) capturarPeca;
	}
	
	private Pecas fazerMover ( Posicao origem, Posicao alvo) throws ExcecaoTabuleiro {
		Pecas p = tabuleiro.removerPeca(origem);
		Pecas capturarPeca = tabuleiro.removerPeca(alvo);
		tabuleiro.lugarPeca(p, alvo);
		return capturarPeca;
	}
	
	private void validarPosicaoOrigem (Posicao posicao) throws ExcecaoTabuleiro {
		if (!tabuleiro.temUmaPeca(posicao)) {
			throw new ExcecaoXadrez("Não existe uma peça nesta posição. ");
		}
		if (!tabuleiro.pecas(posicao).existeMovimentoPossivel()) {
			throw new ExcecaoXadrez ("Não existe movimentos possíveis para a peça escolhida.");
		}
	}
	
	private void validarPosicaoAlvo (Posicao origem, Posicao alvo) throws ExcecaoTabuleiro {
		if (!tabuleiro.pecas(origem).movimentoPossivel(alvo)) {
			throw new ExcecaoXadrez("Peça escolhida não pode se mover para a função de destino.");
		}
	}

	private void configuracaoInicial() throws ExcecaoTabuleiro {
		novaPeca('a', 8, new Torre(tabuleiro, Cor.BLACK));
		novaPeca('e', 8, new Rei(tabuleiro, Cor.BLACK));
	}
}
