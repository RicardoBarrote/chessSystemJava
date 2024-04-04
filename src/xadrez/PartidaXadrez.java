package xadrez;

import tabuleiro.Pecas;
import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.pecas.Rei;
import xadrez.pecas.Torre;

public class PartidaXadrez {

	private int turno;
	private Cor jogadorAtual;
	private Tabuleiro tabuleiro;

	public PartidaXadrez() {
		tabuleiro = new Tabuleiro(8, 8);
		turno = 1;
		jogadorAtual = Cor.WHITE;
		configuracaoInicial();
	}
	
	public int getTurno () {
		return turno;
	}
	
	public Cor getJogadorAtual () {
		return jogadorAtual;
	}

	public PecaXadrez[][] getPecas() {
		PecaXadrez[][] mat = new PecaXadrez[tabuleiro.getLinhas()][tabuleiro.getColunas()];
		for (int i = 0; i < tabuleiro.getLinhas(); i++)
			for (int j = 0; j < tabuleiro.getColunas(); j++) {
				mat[i][j] = (PecaXadrez) tabuleiro.pecas(i, j);
			}
		return mat;
	}

	public boolean[][] movimentosPossiveis(PosicaoXadrez origemPosicao) {
		Posicao posicao = origemPosicao.toPosicao();
		validarPosicaoOrigem(posicao);
		return tabuleiro.pecas(posicao).movimentosPossiveis();
	}

	private void novaPeca(char coluna, int linha, PecaXadrez peca) {
		tabuleiro.lugarPeca(peca, new PosicaoXadrez(coluna, linha).toPosicao());
	}

	public PecaXadrez executarMovimentoXadrez(PosicaoXadrez posicaoOrigem, PosicaoXadrez posicaoAlvo) {
		Posicao origem = posicaoOrigem.toPosicao();
		Posicao alvo = posicaoAlvo.toPosicao();
		validarPosicaoOrigem(origem);
		validarPosicaoAlvo(origem, alvo);
		Pecas capturarPeca = fazerMover(origem, alvo);
		trocarTurno();
		return (PecaXadrez) capturarPeca;
	}

	private Pecas fazerMover(Posicao origem, Posicao alvo) {
		Pecas p = tabuleiro.removerPeca(origem);
		Pecas capturarPeca = tabuleiro.removerPeca(alvo);
		tabuleiro.lugarPeca(p, alvo);
		return capturarPeca;
	}

	private void validarPosicaoOrigem(Posicao posicao) {
		if (!tabuleiro.temUmaPeca(posicao)) {
			throw new ExcecaoXadrez("Não existe uma peça nesta posição. ");
		} 
		if (jogadorAtual != ((PecaXadrez) tabuleiro.pecas(posicao)).getCor()) {
			throw new ExcecaoXadrez("A peça escolhida não é sua. ");
		}
		if (!tabuleiro.pecas(posicao).existeMovimentoPossivel()) {
			throw new ExcecaoXadrez("Não existe movimentos possíveis para a peça escolhida.");
		}
	}

	private void validarPosicaoAlvo(Posicao origem, Posicao alvo) {
		if (!tabuleiro.pecas(origem).movimentoPossivel(alvo)) {
			throw new ExcecaoXadrez("Peça escolhida não pode se mover para a função de destino.");
		}
	}
	
	private void trocarTurno () {
		turno ++;
		jogadorAtual = (jogadorAtual == Cor.WHITE) ? Cor.BLACK : Cor.WHITE;
	}

	private void configuracaoInicial() {
		novaPeca('a', 8, new Torre(tabuleiro, Cor.BLACK));
		novaPeca('e', 8, new Rei(tabuleiro, Cor.BLACK));

		novaPeca('e', 1, new Rei(tabuleiro, Cor.WHITE));
		novaPeca('a', 1, new Torre(tabuleiro, Cor.WHITE));
	}
}
