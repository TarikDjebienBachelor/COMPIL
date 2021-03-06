package ava.genCode;

import java.io.IOException;
import java.util.Iterator;

import ava.arbreAbstrait.*;

/** Classe servant à générer du bytecode Java à partir d'un arbre
 * abstrait pour Ava. Implanté par un visiteur, utilisation de la classe
 * CG qui sert de point d'accès aux instructions de byteCode.
 * @author M Nebut 
 * @version 12/07 
 */

public class GenerateurCode implements Visiteur { // A COMPLETER

	// point d'accès aux instructions de bytecode
	private GenByteCode generateur;
	// programme à traiter
	private Programme programme;

	/** Crée un générateur de code pour le programme <tt>p</tt>, étant
	 * donné un objet <tt>generateur</tt>, point d'accès au bytecode généré.
	 * @param generateur l'accès aux instructions de bytecode générées.
	 * @param p le programme pour lequel on souhaite générer du code.
	 */
	public GenerateurCode(GenByteCode generateur, Programme p) {
		this.generateur = generateur;
		this.programme = p;
	}

	/** Génère le code pour le programme. 
	 */
	public void generationCode() throws GenCodeException, VisiteurException {
		this.visiteProgramme(this.programme);
	}

	/**
	 * OK
	 */
	@Override
	public void visiteProgramme(Programme p) throws VisiteurException {
		this.visiteListeDecl(p.getListeDecl());
		this.visiteListeInstr(p.getListeInstr());
		try {
			this.generateur.closeClass();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * OK
	 */
	@Override
	public void visiteAffect(Affectation a) throws VisiteurException {
		Expression e = a.getExpr();
		int indexIdent = this.generateur.getIndex(a.getIdent());
		e.visite(this);
		try {
			this.generateur.addISTORE(indexIdent);
		} catch (IllegalStateException e1) {
			e1.printStackTrace();
		} catch (InvalidIndexException e1) {
			e1.printStackTrace();
		}

	}

	/**
	 * OK
	 */
	@Override
	public void visiteDeclBool(DeclarationBooleen decl)
	throws VisiteurException {
		int index = this.generateur.newVarBool(decl.getIdent());
		this.generateur.addICONST_0();
		try {
			this.generateur.addISTORE(index);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (InvalidIndexException e) {
			e.printStackTrace();
		}
	}
	/**
	 * OK
	 */
	@Override
	public void visiteDeclInt(DeclarationEntier decl) throws VisiteurException {
		int index = this.generateur.newVarInt(decl.getIdent());
		this.generateur.addICONST_0();
		try {
			this.generateur.addISTORE(index);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (InvalidIndexException e) {
			e.printStackTrace();
		}
	}


	/**
	 * OK
	 */
	@Override
	public void visiteEntier(Entier e) throws VisiteurException {
		int index = this.generateur.newConstant(e.getValeur());
		try {
			this.generateur.addLDC(index);
		} catch (IllegalStateException e1) {
			e1.printStackTrace();
		} catch (InvalidIndexException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * OK
	 */
	@Override
	public void visiteExprFalse(ExprFalse i) throws VisiteurException {
		this.generateur.addICONST_0();
	}

	/**
	 * OK
	 */
	@Override
	public void visiteExprTrue(ExprTrue i) throws VisiteurException {
		this.generateur.addICONST_1();
	}

	/**
	 * OK
	 */
	@Override
	public void visiteIdentExpr(IdentExpr i) throws VisiteurException {
		int index = this.generateur.getIndex(i.getNom());
		try {
			this.generateur.addILOAD(index);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (InvalidIndexException e) {
			e.printStackTrace();
		}
	}

	/**
	 * OK
	 */
	@Override
	public void visiteImpressionBooleenAvecSautDeLigne(
			ImpressionBooleenAvecSautDeLigne i) throws VisiteurException {
		Expression e = i.getExpr();
		e.visite(this);
		this.generateur.addBPRINT();
		this.generateur.addPRINTLN();
	}

	/**
	 * OK
	 */
	@Override
	public void visiteImpressionBooleenSansSautDeLigne(
			ImpressionBooleenSansSautDeLigne i) throws VisiteurException {
		Expression e = i.getExpr();
		e.visite(this);
		this.generateur.addBPRINT();
	}

	/**
	 * OK
	 */
	@Override
	public void visiteImpressionChaineAvecSautDeLigne(
			ImpressionChaineAvecSautDeLigne i) throws VisiteurException {
		int index = this.generateur.newConstant(i.getChaine());
		try {
			this.generateur.addLDC(index);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (InvalidIndexException e) {
			e.printStackTrace();
		}
		this.generateur.addSPRINT();
		this.generateur.addPRINTLN();
	}

	/**
	 * OK
	 */
	@Override
	public void visiteImpressionChaineSansSautDeLigne(
			ImpressionChaineSansSautDeLigne i) throws VisiteurException {
		int index = this.generateur.newConstant(i.getChaine());
		try {
			this.generateur.addLDC(index);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (InvalidIndexException e) {
			e.printStackTrace();
		}
		this.generateur.addSPRINT();
	}

	/**
	 * OK
	 */
	@Override
	public void visiteImpressionEntierAvecSautDeLigne(
			ImpressionEntierAvecSautDeLigne i) throws VisiteurException {
		Expression e = i.getExpr();
		e.visite(this);
		this.generateur.addIPRINT();
		this.generateur.addPRINTLN();
	}

	/**
	 * OK
	 */
	@Override
	public void visiteImpressionEntierSansSautDeLigne(
			ImpressionEntierSansSautDeLigne i) throws VisiteurException {
		Expression e = i.getExpr();
		e.visite(this);
		this.generateur.addIPRINT();
	}

	/**
	 * OK
	 */
	@Override
	public void visiteImpressionSautDeLigne(ImpressionSautDeLigne i)
	throws VisiteurException {
		this.generateur.addPRINTLN();
	}

	/**
	 * OK
	 */
	@Override
	public void visiteLecture(Lecture l) throws VisiteurException {
		this.generateur.addIREAD();
		int indexVariable = this.generateur.getIndex(l.getIdent());
		try {
			this.generateur.addISTORE(indexVariable);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (InvalidIndexException e) {
			e.printStackTrace();
		}
	}

	/**
	 * OK
	 */
	@Override
	public void visiteListeDecl(ListeDeclaration ldecl)
	throws VisiteurException {
		Iterator<Declaration> it = ldecl.iterator();
		while (it.hasNext()) {
			Declaration uneDeclaration = (Declaration) it.next();
			uneDeclaration.visite(this);
		}
	}

	/**
	 * OK
	 */
	@Override
	public void visiteListeInstr(ListeInstruction linstr)
	throws VisiteurException {
		Iterator<Instruction> it = linstr.iterator();
		while (it.hasNext()) {
			Instruction uneInstruction = (Instruction) it.next();
			uneInstruction.visite(this);
		}
	}
	/**
	 * OK
	 */
	@Override
	public void visiteModulo(Modulo i) throws VisiteurException {
		Expression exprGauche = i.getExprGauche();
		exprGauche.visite(this);
		Expression exprDroite = i.getExprDroite();
		exprDroite.visite(this);
		this.generateur.addIREM();
	}
	/**
	 * OK
	 */
	@Override
	public void visiteMoinsUnaire(MoinsUnaire i) throws VisiteurException {
		Expression expr = i.getExpr();
		expr.visite(this);
		this.generateur.addINEG();
	}


	/**
	 * OK
	 */
	@Override
	public void visiteNegation(Negation i) throws VisiteurException {
		Expression expr = i.getExpr();
		expr.visite(this);
		this.generateur.addINOT();
	}

	/**
	 * Operation Binaire
	 */

	/**
	 * OK
	 */
	@Override
	public void visiteSoustraction(Soustraction i) throws VisiteurException {
		Expression exprGauche = i.getExprGauche();
		exprGauche.visite(this);
		Expression exprDroite = i.getExprDroite();
		exprDroite.visite(this);	
		this.generateur.addISUB();
	}

	/**
	 * OK
	 */
	@Override
	public void visiteAddition(Addition i) throws VisiteurException {
		Expression exprGauche = i.getExprGauche();
		exprGauche.visite(this);
		Expression exprDroite = i.getExprDroite();
		exprDroite.visite(this);
		this.generateur.addIADD();
	}

	/**
	 * OK
	 */
	@Override
	public void visiteMultiplication(Multiplication i) throws VisiteurException {
		Expression exprGauche = i.getExprGauche();
		exprGauche.visite(this);
		Expression exprDroite = i.getExprDroite();
		exprDroite.visite(this);
		this.generateur.addIMUL();
	}

	/**
	 * OK
	 */
	@Override
	public void visiteDivision(Division i) throws VisiteurException {
		Expression exprGauche = i.getExprGauche();
		exprGauche.visite(this);
		Expression exprDroite = i.getExprDroite();
		exprDroite.visite(this);
		this.generateur.addIDIV();
	}

	/**
	 * OK
	 */
	@Override
	public void visiteDisjonction(Disjonction i) throws VisiteurException {
		Expression exprGauche = i.getExprGauche();
		exprGauche.visite(this);
		Expression exprDroite = i.getExprDroite();
		exprDroite.visite(this);
		this.generateur.addIOR();
	}

	/**
	 * OK
	 */
	@Override
	public void visiteConjonction(Conjonction i) throws VisiteurException {
		Expression exprGauche = i.getExprGauche();
		exprGauche.visite(this);
		Expression exprDroite = i.getExprDroite();
		exprDroite.visite(this);
		this.generateur.addIAND();
	}


	/**
	 * COMPARAISONS
	 */

	@Override
	public void visiteEgal(Egal e) throws VisiteurException {
		Expression exprGauche = e.getExprGauche();
		exprGauche.visite(this);
		Expression exprDroite = e.getExprDroite();
		exprDroite.visite(this);
		int indexIF = this.generateur.getNextInstructionIndex();
		this.generateur.addIF_ICMPNE();
		this.generateur.addICONST_1();
		int indexGOTO = this.generateur.getNextInstructionIndex();
		this.generateur.addGOTO();
		this.generateur.setTarget(indexIF, this.generateur.getNextInstructionIndex());
		this.generateur.addICONST_0();
		this.generateur.setTarget(indexGOTO, this.generateur.getNextInstructionIndex());
	}

	@Override
	public void visiteSuperieurOuEgal(SuperieurOuEgal e)
	throws VisiteurException {
		Expression exprGauche = e.getExprGauche();
		exprGauche.visite(this);
		Expression exprDroite = e.getExprDroite();
		exprDroite.visite(this);
		int indexIF = this.generateur.getNextInstructionIndex();
		this.generateur.addIF_ICMPLT();
		this.generateur.addICONST_1();
		int indexGOTO = this.generateur.getNextInstructionIndex();
		this.generateur.addGOTO();
		this.generateur.setTarget(indexIF, this.generateur.getNextInstructionIndex());
		this.generateur.addICONST_0();
		this.generateur.setTarget(indexGOTO, this.generateur.getNextInstructionIndex());
	}

	@Override
	public void visiteSuperieurStrict(SuperieurStrict e)
	throws VisiteurException {
		Expression exprGauche = e.getExprGauche();
		exprGauche.visite(this);
		Expression exprDroite = e.getExprDroite();
		exprDroite.visite(this);
		int indexIF = this.generateur.getNextInstructionIndex();
		this.generateur.addIF_ICMPLE();
		this.generateur.addICONST_1();
		int indexGOTO = this.generateur.getNextInstructionIndex();
		this.generateur.addGOTO();
		this.generateur.setTarget(indexIF, this.generateur.getNextInstructionIndex());
		this.generateur.addICONST_0();
		this.generateur.setTarget(indexGOTO, this.generateur.getNextInstructionIndex());
	}


	@Override
	public void visiteInferieurOuEgal(InferieurOuEgal e)
	throws VisiteurException {
		System.out.println("pass ici");
		Expression exprGauche = e.getExprGauche();
		exprGauche.visite(this);
		Expression exprDroite = e.getExprDroite();
		exprDroite.visite(this);
		int indexIf = this.generateur.getNextInstructionIndex();
		this.generateur.addIF_ICMPGT();
		this.generateur.addICONST_1();
		int indexGoto = this.generateur.getNextInstructionIndex();
		this.generateur.addGOTO();
		this.generateur.setTarget(indexIf, this.generateur.getNextInstructionIndex());
		this.generateur.addICONST_0();
		this.generateur.setTarget(indexGoto, this.generateur.getNextInstructionIndex());
	}

	@Override
	public void visiteInferieurStrict(InferieurStrict e)
	throws VisiteurException {
		Expression exprGauche = e.getExprGauche();
		exprGauche.visite(this);
		Expression exprDroite = e.getExprDroite();
		exprDroite.visite(this);
		int indexIf = this.generateur.getNextInstructionIndex();
		this.generateur.addIF_ICMPGE();
		this.generateur.addICONST_1();
		int indexGoto = this.generateur.getNextInstructionIndex();
		this.generateur.addGOTO();
		this.generateur.setTarget(indexIf, this.generateur.getNextInstructionIndex());
		this.generateur.addICONST_0();
		this.generateur.setTarget(indexGoto, this.generateur.getNextInstructionIndex());
	}

	@Override
	public void visiteDifferent(Different e) throws VisiteurException {
		Expression exprGauche = e.getExprGauche();
		exprGauche.visite(this);
		Expression exprDroite = e.getExprDroite();
		exprDroite.visite(this);
		int indexIf = this.generateur.getNextInstructionIndex();
		this.generateur.addIF_ICMPEQ();
		this.generateur.addICONST_1();
		int indexGoto = this.generateur.getNextInstructionIndex();
		this.generateur.addGOTO();
		this.generateur.setTarget(indexIf, this.generateur.getNextInstructionIndex());
		this.generateur.addICONST_0();
		this.generateur.setTarget(indexGoto, this.generateur.getNextInstructionIndex());
	}

	@Override
	public void visiteConditionnelleAvecAlternative(ConditionnelleAvecAlternative l) throws VisiteurException {
			
		Expression cond = l.getCondition();
		cond.visite(this);
		int indIf = this.generateur.getNextInstructionIndex();
		this.generateur.addIFEQ();
		ListeInstruction instrAlors = l.getListeInstrAlors();
		Iterator<Instruction> itThen = instrAlors.iterator();
		while (itThen.hasNext()) {
			itThen.next().visite(this);
		}
		int indGoTo = this.generateur.getNextInstructionIndex();
		this.generateur.addGOTO();
		this.generateur.setTarget(indIf, this.generateur.getNextInstructionIndex());
		ListeInstruction instrElse = l.getListeInstrSinon();
		Iterator<Instruction> itElse = instrElse.iterator();
		while (itElse.hasNext()) {
			itElse.next().visite(this);
		}
		this.generateur.setTarget(indGoTo, this.generateur.getNextInstructionIndex());
	}

	@Override
	public void visiteConditionnelleSimple(ConditionnelleSimple l)
	throws VisiteurException {
		Expression condition = l.getCondition();
		condition.visite(this);
		int indexIF = this.generateur.getNextInstructionIndex();
		this.generateur.addIFEQ();
		ListeInstruction instruction = l.getListeInstrAlors();
		Iterator<Instruction> it = instruction.iterator();
		while (it.hasNext()) {
			it.next().visite(this);
		}
		this.generateur.setTarget(indexIF, this.generateur.getNextInstructionIndex());
	}

	@Override
	public void visiteBoucle(Boucle b) throws VisiteurException {
		Expression condition = b.getCondition();
		condition.visite(this);
		int indexEndWhile = this.generateur.getNextInstructionIndex();
		this.generateur.addIFEQ();
	}
}