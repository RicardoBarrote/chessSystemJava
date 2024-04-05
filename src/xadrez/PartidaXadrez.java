package xadrez;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import tabuleiro.Pecas;
import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.pecas.Rei;
import xadrez.pecas.Torre;

public class PartidaXadrez {

	private int turno;
	private Cor jogadorAtual;
	private Tabuleiro tabuleiro;
	private boolean check;

	private List<Pecas> pecasNoTabuleiro = new ArrayList<>();
	private List<Pecas> pecaCapturada = new ArrayList<>();

	public PartidaXadrez() {
		tabuleiro = new Tabuleiro(8, 8);
		turno = 1;
		jogadorAtual = Cor.WHITE;
		configuracaoInicial();
	}

	public int getTurno() {
		return turno;
	}

	public Cor getJogadorAtual() {
		return jogadorAtual;
	}

	public boolean getCheck () {
		return check;
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

	public PecaXadrez executarMovimentoXadrez(PosicaoXadrez posicaoOrigem, PosicaoXadrez posicaoAlvo) {
		Posicao origem = posicaoOrigem.toPosicao();
		Posicao alvo = posicaoAlvo.toPosicao();
		validarPosicaoOrigem(origem);
		validarPosicaoAlvo(origem, alvo);
		Pecas capturarPeca = fazerMover(origem, alvo);
		
		if (testandoCheck(jogadorAtual)) {
			desfazerMovimentos(origem, alvo, capturarPeca);
			throw new ExcecaoXadrez ("Você não pode se colocar em CHECK.");
		}
		
		check = (testandoCheck (oponente(jogadorAtual))) ? true : false;
		
		trocarTurno();
		return (PecaXadrez) capturarPeca;
	}

	private Pecas fazerMover(Posicao origem, Posicao alvo) {
		Pecas p = tabuleiro.removerPeca(origem);
		Pecas capturarPeca = tabuleiro.removerPeca(alvo);
		tabuleiro.lugarPeca(p, alvo);

		if (capturarPeca != null) {
			pecasNoTabuleiro.remove(capturarPeca);
			pecaCapturada.add(capturarPeca);
		}

		return capturarPeca;
	}

	private void desfazerMovimentos(Posicao origem, Posicao alvo, Pecas pecasCapturada) {
		Pecas p = tabuleiro.removerPeca(alvo);
		tabuleiro.lugarPeca(p, origem);

		if (pecasCapturada != null) {
			tabuleiro.lugarPeca(pecasCapturada, alvo);
			pecaCapturada.remove(pecasCapturada);
			pecasNoTabuleiro.add(pecasCapturada);
		}
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

	private void trocarTurno() {
		turno++;
		jogadorAtual = (jogadorAtual == Cor.WHITE) ? Cor.BLACK : Cor.WHITE;
	}

	private Cor oponente(Cor cor) {
		return (cor == Cor.WHITE) ? Cor.BLACK : Cor.WHITE;
	}

	private PecaXadrez rei(Cor cor) {
		List<Pecas> list = pecasNoTabuleiro.stream().filter(x -> ((PecaXadrez) x).getCor() == cor).collect(Collectors.toList());

		for (Pecas p : list) {
			if (p instanceof Rei) {
				return (PecaXadrez) p;
			}
		}
		throw new IllegalStateException("Erro crítico: Não existe o Rei da cor " + cor + " no tabuleiro.");
	}

	private boolean testandoCheck(Cor cor) {
		Posicao posicaoRei = rei(cor).getPosicaoXadrez().toPosicao();
		List<Pecas> pecaOponente = pecasNoTabuleiro.stream().filter(x -> ((PecaXadrez) x).getCor() == oponente(cor)).collect(Collectors.toList());
		
		for (Pecas p : pecaOponente) {
			boolean[][] mat = p.movimentosPossiveis();
			if (mat[posicaoRei.getLinha()][posicaoRei.getColuna()]) {
				return true;
			}
		}
		return false;
	}

	private void novaPeca(char coluna, int linha, PecaXadrez peca) {
		tabuleiro.lugarPeca(peca, new PosicaoXadrez(coluna, linha).toPosicao());
		pecasNoTabuleiro.add(peca);
	}

	private void configuracaoInicial() {
		novaPeca('a', 8, new Torre(tabuleiro, Cor.BLACK));
		novaPeca('e', 8, new Rei(tabuleiro, Cor.BLACK));

		novaPeca('e', 1, new Rei(tabuleiro, Cor.WHITE));
		novaPeca('a', 1, new Torre(tabuleiro, Cor.WHITE));
	}
}
