package tabuleiro;

public class Tabuleiro {

	private int linhas;
	private int colunas;
	private Pecas[][] pecas;

	public Tabuleiro() {

	}

	public Tabuleiro(int linhas, int colunas) throws ExcecaoTabuleiro {
		if (linhas < 1 || colunas < 1) {
			throw new ExcecaoTabuleiro("Erro: necessário que haja pelo menos uma linha é uma coluna.");
		}
		this.linhas = linhas;
		this.colunas = colunas;
		pecas = new Pecas[linhas][colunas];
	}

	public int getLinhas() {
		return linhas;
	}

	public int getColunas() {
		return colunas;
	}

	public Pecas pecas(int linha, int coluna) throws ExcecaoTabuleiro {
		if (!posicaoExistente(linha, coluna)) {
			throw new ExcecaoTabuleiro("Posição não existe no tabuleiro.");
		}
		return pecas[linha][coluna];
	}

	public Pecas pecas(Posicao posicao) throws ExcecaoTabuleiro {
		if (!posicaoExistente(posicao)) {
			throw new ExcecaoTabuleiro("Posição não existe no tabuleiro.");
		}
		return pecas[posicao.getLinha()][posicao.getColuna()];
	}

	public void lugarPeca(Pecas peca, Posicao posicao) throws ExcecaoTabuleiro {
		if (temUmaPeca(posicao)) {
			throw new ExcecaoTabuleiro("Já existe uma peça na posicção. ");
		}
		pecas[posicao.getLinha()][posicao.getColuna()] = peca;
		peca.posicao = posicao;
	}

	private boolean posicaoExistente(int linha, int coluna) {
		return linha >= 0 && linha < linhas && coluna >= 0 && coluna < colunas;
	}

	public boolean posicaoExistente(Posicao posicao) {
		return posicaoExistente(posicao.getLinha(), posicao.getColuna());
	}

	public boolean temUmaPeca(Posicao posicao) throws ExcecaoTabuleiro {
		if (!posicaoExistente(posicao)) {
			throw new ExcecaoTabuleiro("Posição não existe no tabuleiro.");
		}
		return pecas(posicao) != null;
	}
}
