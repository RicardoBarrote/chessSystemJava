package xadrez;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import tabuleiro.Pecas;
import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.pecas.Bispo;
import xadrez.pecas.Cavalo;
import xadrez.pecas.Peao;
import xadrez.pecas.Rainha;
import xadrez.pecas.Rei;
import xadrez.pecas.Torre;

public class PartidaXadrez {

	private int turno;
	private Cor jogadorAtual;
	private Tabuleiro tabuleiro;
	private boolean check;
	private boolean checkMate;
	private PecaXadrez enPassantVulneravel;
	private PecaXadrez promocao;

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

	public boolean getCheck() {
		return check;
	}

	public boolean getCheckMate() {
		return checkMate;
	}

	public PecaXadrez getEnPassantVulneravel() {
		return enPassantVulneravel;
	}

	public PecaXadrez getPromocao() {
		return promocao;
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
			throw new ExcecaoXadrez("Você não pode se colocar em CHECK.");
		}

		PecaXadrez pecaQueMovel = (PecaXadrez) tabuleiro.pecas(alvo);

		// Movimento especial promoção
		promocao = null;
		if (pecaQueMovel instanceof Peao) {
			if ((pecaQueMovel.getCor() == Cor.WHITE && alvo.getLinha() == 0 || (pecaQueMovel.getCor() == Cor.BLACK && alvo.getLinha() == 7))) {
				promocao = (PecaXadrez) tabuleiro.pecas(alvo);
				promocao = substituirPecaPromovida("Q");
			}
		}

		check = (testandoCheck(oponente(jogadorAtual))) ? true : false;

		if (testandoCheckMate(oponente(jogadorAtual))) {
			checkMate = true;
		}
		else {
			trocarTurno();
		}

		// #Especial movimento enPassant
		if (pecaQueMovel instanceof Peao
				&& (alvo.getLinha() == origem.getLinha() - 2 || alvo.getLinha() == origem.getLinha() + 2)) {
			enPassantVulneravel = pecaQueMovel;
		} 
		else {
			enPassantVulneravel = null;
		}

		return (PecaXadrez) capturarPeca;
	}

	public PecaXadrez substituirPecaPromovida(String tipo) {
		if (promocao == null) {
			throw new IllegalStateException("Não há peça para ser promovida");
		}
		if (!tipo.equals("B") && !tipo.equals("C") && tipo.equals("T") && tipo.equals("Q")) {
			throw new InvalidParameterException("Tipo de promoção inválida");
		}

		Posicao pos = promocao.getPosicaoXadrez().toPosicao();
		Pecas p = tabuleiro.removerPeca(pos);
		pecasNoTabuleiro.remove(p);
		PecaXadrez newPeca = newPeca(tipo, promocao.getCor());
		tabuleiro.lugarPeca(newPeca, pos);
		pecasNoTabuleiro.add(newPeca);

		return newPeca;
	}

	private PecaXadrez newPeca(String tipo, Cor cor) {
		if (tipo.equals("B")) return new Bispo(tabuleiro, cor);
		if (tipo.equals("C")) return new Cavalo(tabuleiro, cor);
		if (tipo.equals("Q")) return new Rainha(tabuleiro, cor);
		return new Torre(tabuleiro, cor);
	}

	private Pecas fazerMover(Posicao origem, Posicao alvo) {
		PecaXadrez p = (PecaXadrez) tabuleiro.removerPeca(origem);
		p.inserirContagemMovimento();
		Pecas capturarPeca = tabuleiro.removerPeca(alvo);
		tabuleiro.lugarPeca(p, alvo);

		if (capturarPeca != null) {
			pecasNoTabuleiro.remove(capturarPeca);
			pecaCapturada.add(capturarPeca);
		}

		// Movimento especial roque ao lado do rei
		if (p instanceof Rei && alvo.getColuna() == origem.getColuna() + 2) {
			Posicao origemTorre = new Posicao(origem.getLinha(), origem.getColuna() + 3);
			Posicao alvoTorre = new Posicao(origem.getLinha(), origem.getColuna() + 1);
			PecaXadrez torre = (PecaXadrez) tabuleiro.removerPeca(origemTorre);
			tabuleiro.lugarPeca(torre, alvoTorre);
			torre.inserirContagemMovimento();
		}

		// Movimento especial roque ao lado da rainha
		if (p instanceof Rei && alvo.getColuna() == origem.getColuna() - 2) {
			Posicao origemTorre = new Posicao(origem.getLinha(), origem.getColuna() - 4);
			Posicao alvoTorre = new Posicao(origem.getLinha(), origem.getColuna() - 1);
			PecaXadrez torre = (PecaXadrez) tabuleiro.removerPeca(origemTorre);
			tabuleiro.lugarPeca(torre, alvoTorre);
			torre.inserirContagemMovimento();
		}

		// #Especial movimento enPassant
		if (p instanceof Peao) {
			if (origem.getColuna() != alvo.getColuna() && capturarPeca == null) {
				Posicao peaoPosicao;
				if (p.getCor() == Cor.WHITE) {
					peaoPosicao = new Posicao(alvo.getLinha() + 1, alvo.getColuna());
				} else {
					peaoPosicao = new Posicao(alvo.getLinha() - 1, alvo.getColuna());
				}
				capturarPeca = tabuleiro.removerPeca(peaoPosicao);
				pecaCapturada.add(capturarPeca);
				pecasNoTabuleiro.remove(capturarPeca);
			}
		}

		return capturarPeca;
	}

	private void desfazerMovimentos(Posicao origem, Posicao alvo, Pecas pecasCapturada) {
		PecaXadrez p = (PecaXadrez) tabuleiro.removerPeca(alvo);
		p.diminuirContagemMovimento();
		tabuleiro.lugarPeca(p, origem);

		if (pecasCapturada != null) {
			tabuleiro.lugarPeca(pecasCapturada, alvo);
			pecaCapturada.remove(pecasCapturada);
			pecasNoTabuleiro.add(pecasCapturada);
		}

		// Movimento especial roque ao lado do rei
		if (p instanceof Rei && alvo.getColuna() == origem.getColuna() + 2) {
			Posicao origemTorre = new Posicao(origem.getLinha(), origem.getColuna() + 3);
			Posicao alvoTorre = new Posicao(origem.getLinha(), origem.getColuna() + 1);
			PecaXadrez torre = (PecaXadrez) tabuleiro.removerPeca(alvoTorre);
			tabuleiro.lugarPeca(torre, origemTorre);
			torre.diminuirContagemMovimento();
		}

		// Movimento especial roque ao lado da rainha
		if (p instanceof Rei && alvo.getColuna() == origem.getColuna() - 2) {
			Posicao origemTorre = new Posicao(origem.getLinha(), origem.getColuna() - 4);
			Posicao alvoTorre = new Posicao(origem.getLinha(), origem.getColuna() - 1);
			PecaXadrez torre = (PecaXadrez) tabuleiro.removerPeca(alvoTorre);
			tabuleiro.lugarPeca(torre, origemTorre);
			torre.diminuirContagemMovimento();
		}

		// #Especial movimento enPassant
		if (p instanceof Peao) {
			if (origem.getColuna() != alvo.getColuna() && pecasCapturada == enPassantVulneravel) {
				PecaXadrez peao = (PecaXadrez) tabuleiro.removerPeca(alvo);
				Posicao peaoPosicao;
				if (p.getCor() == Cor.WHITE) {
					peaoPosicao = new Posicao(3, alvo.getColuna());
				} else {
					peaoPosicao = new Posicao(4, alvo.getColuna());
				}
				tabuleiro.lugarPeca(peao, peaoPosicao);
			}
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
		List<Pecas> list = pecasNoTabuleiro.stream().filter(x -> ((PecaXadrez) x).getCor() == cor)
				.collect(Collectors.toList());

		for (Pecas p : list) {
			if (p instanceof Rei) {
				return (PecaXadrez) p;
			}
		}
		throw new IllegalStateException("Erro crítico: Não existe o Rei da cor " + cor + " no tabuleiro.");
	}

	private boolean testandoCheck(Cor cor) {
		Posicao posicaoRei = rei(cor).getPosicaoXadrez().toPosicao();
		List<Pecas> pecaOponente = pecasNoTabuleiro.stream().filter(x -> ((PecaXadrez) x).getCor() == oponente(cor))
				.collect(Collectors.toList());

		for (Pecas p : pecaOponente) {
			boolean[][] mat = p.movimentosPossiveis();
			if (mat[posicaoRei.getLinha()][posicaoRei.getColuna()]) {
				return true;
			}
		}
		return false;
	}

	private boolean testandoCheckMate(Cor cor) {
		if (!testandoCheck(cor)) {
			return false;
		}
		List<Pecas> list = pecasNoTabuleiro.stream().filter(x -> ((PecaXadrez) x).getCor() == cor)
				.collect(Collectors.toList());

		for (Pecas p : list) {
			boolean[][] mat = p.movimentosPossiveis();
			for (int i = 0; i < tabuleiro.getLinhas(); i++) {
				for (int j = 0; j < tabuleiro.getColunas(); j++) {
					if (mat[i][j]) {
						Posicao origem = ((PecaXadrez) p).getPosicaoXadrez().toPosicao();
						Posicao alvo = new Posicao(i, j);
						Pecas capturarPeca = fazerMover(origem, alvo);
						boolean testandoCheck = testandoCheck(cor);
						desfazerMovimentos(origem, alvo, capturarPeca);
						if (!testandoCheck) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	private void novaPeca(char coluna, int linha, PecaXadrez peca) {
		tabuleiro.lugarPeca(peca, new PosicaoXadrez(coluna, linha).toPosicao());
		pecasNoTabuleiro.add(peca);
	}

	private void configuracaoInicial() {
		novaPeca('a', 1, new Torre(tabuleiro, Cor.WHITE));
		novaPeca('h', 1, new Torre(tabuleiro, Cor.WHITE));
		novaPeca('c', 1, new Bispo(tabuleiro, Cor.WHITE));
		novaPeca('f', 1, new Bispo(tabuleiro, Cor.WHITE));
		novaPeca('b', 1, new Cavalo(tabuleiro, Cor.WHITE));
		novaPeca('g', 1, new Cavalo(tabuleiro, Cor.WHITE));
		novaPeca('d', 1, new Rainha(tabuleiro, Cor.WHITE));
		novaPeca('e', 1, new Rei(tabuleiro, Cor.WHITE, this));
		novaPeca('a', 2, new Peao(tabuleiro, Cor.WHITE, this));
		novaPeca('b', 2, new Peao(tabuleiro, Cor.WHITE, this));
		novaPeca('c', 2, new Peao(tabuleiro, Cor.WHITE, this));
		novaPeca('d', 2, new Peao(tabuleiro, Cor.WHITE, this));
		novaPeca('e', 2, new Peao(tabuleiro, Cor.WHITE, this));
		novaPeca('f', 2, new Peao(tabuleiro, Cor.WHITE, this));
		novaPeca('g', 2, new Peao(tabuleiro, Cor.WHITE, this));
		novaPeca('h', 2, new Peao(tabuleiro, Cor.WHITE, this));

		novaPeca('a', 8, new Torre(tabuleiro, Cor.BLACK));
		novaPeca('h', 8, new Torre(tabuleiro, Cor.BLACK));
		novaPeca('c', 8, new Bispo(tabuleiro, Cor.BLACK));
		novaPeca('f', 8, new Bispo(tabuleiro, Cor.BLACK));
		novaPeca('b', 8, new Cavalo(tabuleiro, Cor.BLACK));
		novaPeca('g', 8, new Cavalo(tabuleiro, Cor.BLACK));
		novaPeca('d', 8, new Rainha(tabuleiro, Cor.BLACK));
		novaPeca('e', 8, new Rei(tabuleiro, Cor.BLACK, this));
		novaPeca('a', 7, new Peao(tabuleiro, Cor.BLACK, this));
		novaPeca('b', 7, new Peao(tabuleiro, Cor.BLACK, this));
		novaPeca('c', 7, new Peao(tabuleiro, Cor.BLACK, this));
		novaPeca('d', 7, new Peao(tabuleiro, Cor.BLACK, this));
		novaPeca('e', 7, new Peao(tabuleiro, Cor.BLACK, this));
		novaPeca('f', 7, new Peao(tabuleiro, Cor.BLACK, this));
		novaPeca('g', 7, new Peao(tabuleiro, Cor.BLACK, this));
		novaPeca('h', 7, new Peao(tabuleiro, Cor.BLACK, this));
	}
}
