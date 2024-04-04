package tabuleiro;

public abstract class Pecas {

	protected Posicao posicao;
	private Tabuleiro tabuleiro;

	public Pecas() {

	}

	public Pecas(Tabuleiro tabuleiro) {
		this.tabuleiro = tabuleiro;
	}

	protected Tabuleiro getTabuleiro() {
		return tabuleiro;
	}

	public abstract boolean[][] movimentosPossiveis();

	public boolean movimentoPossivel(Posicao posicao) {
		return movimentosPossiveis()[posicao.getLinha()][posicao.getColuna()];
	}

	public boolean existeMovimentoPossivel() {
		boolean[][] mat = movimentosPossiveis();
		for (int i = 0; i < mat.length; i++) {
			for (int j = 0; j < mat.length; j++) {
				if (mat[i][j]) {
					return true;
				}
			}
		}
		return false;
	}
}
