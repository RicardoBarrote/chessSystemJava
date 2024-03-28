package aplicacao;

import java.util.Locale;
import java.util.Scanner;

import tabuleiro.ExcecaoTabuleiro;
import xadrez.ExcecaoXadrez;
import xadrez.PartidaXadrez;

public class Programa {

	public static void main(String[] args) throws ExcecaoTabuleiro, ExcecaoXadrez {

		Locale.setDefault(Locale.US);
		Scanner sc = new Scanner(System.in);

		PartidaXadrez partidaXadrez = new PartidaXadrez();

		UI.imprimirTabuleiro(partidaXadrez.getPecas());
		sc.close();
	}

}
