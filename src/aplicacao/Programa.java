package aplicacao;

import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;

import tabuleiro.ExcecaoTabuleiro;
import xadrez.ExcecaoXadrez;
import xadrez.PartidaXadrez;
import xadrez.PecaXadrez;
import xadrez.PosicaoXadrez;

public class Programa {

	public static void main(String[] args) throws ExcecaoTabuleiro {

		Locale.setDefault(Locale.US);
		Scanner sc = new Scanner(System.in);

		PartidaXadrez partidaXadrez = new PartidaXadrez();

		while (true) {
			try {
				UI.limparTela();
				UI.imprimirTabuleiro(partidaXadrez.getPecas());
				System.out.println();
				System.out.print("Origem: ");
				PosicaoXadrez origem = UI.lerPosicaoXadrez(sc);

				boolean[][] possivelMovimento = partidaXadrez.possivelMovimento(origem);
				UI.limparTela();
				UI.imprimirTabuleiro(partidaXadrez.getPecas(), possivelMovimento);

				System.out.println();
				System.out.print("Alvo: ");
				PosicaoXadrez alvo = UI.lerPosicaoXadrez(sc);

				PecaXadrez capturarPeca = partidaXadrez.executarMovimentoXadrez(origem, alvo);
			} 
			catch (ExcecaoXadrez e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
			catch (ExcecaoTabuleiro e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
			catch (InputMismatchException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}

		}

	}

}
