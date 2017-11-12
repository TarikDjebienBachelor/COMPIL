package ava.executeurs;

import ava.analyseurs.*;

import ava.arbreAbstrait.Programme;
import ava.decompilateur.Decompilateur;

import java.io.*; 
import java_cup.runtime.Symbol;

/** Lancement du décompilateur, après construction de l'arbre abstrait
 * par l'analyseur syntaxique. Si un nom de fichier est fourni, ce
 * fichier est analysé, sinon c'est l'entrée standard qui l'est.
 * @author M. Nebut 08/07 revu 11/09
 */
public class LanceurDecompilateur {

    public static void main(String[] args) throws Exception {
	if (args.length > 1)
            System.out.println("Attention: un seul fichier pris en compte");
	new LanceurDecompilateur().run(args);
    }

    public void run(String[] args) throws Exception {
	Reader flotLecture = obtenirFlotDepuisFichierOuEntreeStandard(args);
	ScannerAva scanner = construireAnalyseurLexical(flotLecture);
	ParserAva parser = new ParserAva(scanner);
	Symbol symboleProgramme = parser.parse();
	this.decompile(symboleProgramme);
    }

    private Reader obtenirFlotDepuisFichierOuEntreeStandard (String[] argsLigneCommande) 
	throws FileNotFoundException {

	if (argsLigneCommande.length == 0)
	    return new InputStreamReader(System.in);
	return new FileReader(argsLigneCommande[0]);
    }

    private ScannerAva construireAnalyseurLexical(Reader flot) {
	return new ScannerAva(flot);		
    }    


    private void decompile(Symbol symboleProgramme) throws Exception {
	Programme p = (Programme) symboleProgramme.value;
	Decompilateur decompilateur = new Decompilateur(p);
	String res = decompilateur.decompile();
	System.out.println("\n====== Decompilation ======");
	System.out.println(res);
    }
}