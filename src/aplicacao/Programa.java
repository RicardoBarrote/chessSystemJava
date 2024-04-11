package aplicacao;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import xadrez.ExcecaoXadrez;
import xadrez.PartidaXadrez;
import xadrez.PecaXadrez;
import xadrez.PosicaoXadrez;

public class Programa {

	public static void main(String[] args) {

		Locale.setDefault(Locale.US);
		Scanner sc = new Scanner(System.in);

		PartidaXadrez partidaXadrez = new PartidaXadrez();
		List<PecaXadrez> capturar = new ArrayList<>();

		while (!partidaXadrez.getCheckMate()) {
			try {
				UI.limparTela();
				UI.imprimirPartida(partidaXadrez, capturar);
				System.out.println();
				System.out.print("Origem: ");
				PosicaoXadrez origem = UI.lerPosicaoXadrez(sc);

				boolean[][] movimentosPossiveis = partidaXadrez.movimentosPossiveis(origem);
				UI.limparTela();
				UI.imprimirTabuleiro(partidaXadrez.getPecas(), movimentosPossiveis);

				System.out.println();
				System.out.print("Alvo: ");
				PosicaoXadrez alvo = UI.lerPosicaoXadrez(sc);

				PecaXadrez capturarPeca = partidaXadrez.executarMovimentoXadrez(origem, alvo);

				if (capturarPeca != null) {
					capturar.add(capturarPeca);
				}

				if (partidaXadrez.getPromocao() != null) {
					System.out.print("Informe a peça a ser promovida (B/C/Q/T) ");
					String tipo = sc.nextLine().toUpperCase();
					while (!tipo.equals("B") && !tipo.equals("C") && tipo.equals("T") && tipo.equals("Q")) {
						System.out.println("Valor inválido! Digite novamente. ");
						System.out.print("Informe a peça a ser promovida (B/C/Q/T) ");
						tipo = sc.nextLine().toUpperCase();
					}
					partidaXadrez.substituirPecaPromovida(tipo);
				}
			} 
			catch (ExcecaoXadrez e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
			catch (InputMismatchException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}

		}

		UI.limparTela();
		UI.imprimirPartida(partidaXadrez, capturar);

	}

}
