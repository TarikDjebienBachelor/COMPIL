package ava.genCode;

import org.apache.bcel.generic.*;
import static org.apache.bcel.Constants.*;
import org.apache.bcel.*;
import java.util.Map;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.io.IOException;

// TODO
// - jump, goto
// - if...


// 4 problems
// - getLastInstructionIndex does not work for composed bytecode (eg SPRINT)
// - I guess getNextInstructionIndex is more useful
// - what if a jump instruction is not followed by anything ?
// - SPRINT dos not interpret \" and \n ?


// index of a composed bytecode = index of its first bytecode
// do not use this.addXXX() in addXXX methods for complex bytecode, or
// printed code will be incorrect.

/** A generator of Java bytecode for Ava. Uses <a
 * href="http://jakarta.apache.org/bcel/">BCEL</a>, the Byte Code
 * Engineering Library from the Apache Jakarta Project.
 * From a first version of CG for Ava by Yves Roos.
 * @version 08/08
 * @author Mirabelle Nebut
 */
public class GenByteCode {

    /** BCEL object which represents a class. The class to be generated. */
    ClassGen cg; 
    /** its name. */
    String cgName;
    /** BCEL object : the main function of this class. */
    MethodGen mg;
    /** BCEL object : a list of instructions for mg. */
    InstructionList il;
    /** True if the class is closed (no more instructions can be added). */
    boolean closed;

    // stuff for Ava
    /** friendly constants table for easy printing. */
    Map constants;
    /** friendly variables table for easy printing. */
    Map variables;
    /** converse variables table (easy access to sorted indexes). */
    Map variablesOp;
    /** friendly code for easy printing. */
    Map<Integer,String> code2;

    /** Memorization of (source, target) for branching instructions. */
    Map<Integer,Integer> jumps;


    /** Builds a generator whichs generates a Java class named <tt>name</tt>. 
     *  @param name the name of the class to be generated.
     */
    public GenByteCode(String name) {
	this.cgName = name;
	// builds the class
	this.cg = new ClassGen(cgName, "java.lang.Object",
				"<generated>", ACC_PUBLIC | ACC_SUPER,
				null);
	this.closed = false;
	// adds a main to this class

	// its constant pool (ConstantPoolGen = used to build up a constant pool) 

	ConstantPoolGen cp = cg.getConstantPool(); // cg creates constant pool
	// instructions for the main function
	il = new InstructionList();
	mg = new MethodGen(ACC_STATIC | ACC_PUBLIC, // access flags
                                Type.VOID,               // return type
                                new Type[] {             // argument types
                                  new ArrayType(Type.STRING, 1) },
                                new String[] { "argv" }, // arg names
                                "main", "HelloWorld",    // method, class
                                il, cp);

	// now just add instructions to the main (ie to this.il) !!

	this.constants = new HashMap();
	this.variables = new HashMap();
	this.variablesOp = new HashMap();
	this.code2 = new HashMap<Integer, String>();
	this.jumps = new HashMap<Integer, Integer>();
    }


    /** Closes the class and dumps it to a .class in the current directory. 
     * @throws IllegalStateException the class is already closed.
     * @throws ClassCastException problem while setting targets: some source target does not correspond to a source instruction
     * @throws IOException problem when writing .class
     */
    public void closeClass() throws IllegalStateException, IOException {

	if (this.closed)
	    throw new IllegalStateException();
	// adds a RETURN (mandatory)
	InstructionHandle ih = this.il.append(InstructionConstants.RETURN);
	this.il.setPositions();
	int position = ih.getPosition();
	// easy printing code
	this.code2.put(position, "RET" + "\n");

	// set offsets now
	Iterator it = this.jumps.keySet().iterator();
	while (it.hasNext()) {
	    Integer source = (Integer) it.next();
	    this.setInternalTarget(source.intValue(), this.jumps.get(source));
	}

 	// ??? but also mandatory
 	this.mg.setMaxStack();

	// adds an empty default constructor
	this.cg.addEmptyConstructor(ACC_PUBLIC);

 	// adds main to the class
 	this.cg.addMethod(mg.getMethod());

	// dump to a class file
	this.cg.getJavaClass().dump(this.cgName+".class");

	this.closed = true;
    }
    

    /*********** CONSTANTS *************/

    /** Adds a new constant of type String in the constants table, if
     * not already there, and returns its index.
     * @throws IllegalStateException the class is already closed.
     */
    public int newConstant(String aString) throws IllegalStateException {
	if (this.closed)
	    throw new IllegalStateException();
	int index =  this.cg.getConstantPool().addString(aString);
	this.constants.put(new Integer(index), aString);
	return index;
    }


    /** Adds a new constant of type Integer in the constants table, if
     * not already there, and returns its index.  
     * @throws IllegalStateException the class is already closed.
     */
    public int newConstant(int i) throws IllegalStateException {
	if (this.closed)
	    throw new IllegalStateException();
	int index = this.cg.getConstantPool().addInteger(i);
	this.constants.put(new Integer(index), new Integer(i));
	return index;
    }





    /************* VARIABLES ********************/

    /** Adds a new variable of type Integer and name <tt>name</tt> in
     * the variables table, if not already there, and returns its
     * index.  
     * @param the name of the variable to be added.
     * @return the index of the added variable.
     */
    public int newVarInt(String name) 
	throws IllegalStateException {
	if (this.closed)
	    throw new IllegalStateException();
	LocalVariableGen lg = mg.addLocalVariable("name",
						  Type.INT, null, null);
	int index = lg.getIndex();

	Integer i = new Integer(index);
	this.variables.put(name, i);
	this.variablesOp.put(i, name);
	return index;
    }

    /** Adds a new variable of type Boolean and name <tt>name</tt> in
     * the variables table, if not already there, and returns its
     * index.  
     * @param the name of the variable to be added.
     * @return the index of the added variable.
     */
    public int newVarBool(String name) 
	throws IllegalStateException {
	if (this.closed)
	    throw new IllegalStateException();
	LocalVariableGen lg = mg.addLocalVariable("name",
						  Type.BOOLEAN, null, null);
	int index = lg.getIndex();

	Integer i = new Integer(index);
	this.variables.put(name, i);
	this.variablesOp.put(i, name);
	return index;
    }


    
    /** Returns the index of the variable <tt>name</tt>.
     * @param the name of the variable whose index is required.
     * @return the index of the variable <tt>name</tt>.
     * @throws IllegalArgumentException <tt>name</tt> does not belong to the
     * variables table.
     */
    public int getIndex(String name) throws IllegalStateException {
	if (this.closed)
	    throw new IllegalStateException();
	Integer index = (Integer) this.variables.get(name);
	if (index == null)
	    throw new IllegalArgumentException(name + " is not present in the variables table.");
	return index;
    }

    /*************** ADDING BYTECODE ***********/

    /****** LOAD and STORE *******/


    /** Adds a LDC instruction, which loads the value of the constant
     * of index <tt>idx</tt> on the stack. Returns the index of the
     * new instruction.
     * @param the index of the constant to be loaded.
     * @return the index of the LDC instruction in code.
     * @throws IllegalStateException the class is closed.
     * @throws InvalidIndexException <tt>idx</tt> is not an index of constant. 
     */
    public int addLDC(int idx) throws IllegalStateException, InvalidIndexException {
	if (this.closed)
	    throw new IllegalStateException();
	if (! this.constants.keySet().contains(new Integer(idx)))
	    throw new InvalidIndexException();
	// Bcel
	InstructionHandle ih = this.il.append(new LDC(idx));
	this.il.setPositions();
	int position = ih.getPosition();
	// easy printing code
	this.code2.put(position, "LDC " + idx + "\n");
	return position;
    }

    /** Adds a ILOAD instruction, which loads the value of the
     * variable (int or bool) of index <tt>idx</tt> on the
     * stack. Returns the index of the new instruction.
     * @param the index of the constant to be loaded.
     * @return the index of the instruction in code.
     * @throws IllegalStateException the class is closed.
     * @throws InvalidIndexException <tt>idx</tt> is not an index of variable. 
     */
    public int addILOAD(int idx) throws IllegalStateException, InvalidIndexException {
	if (this.closed)
	    throw new IllegalStateException();
	if (! this.variables.values().contains(new Integer(idx)))
	    throw new InvalidIndexException();
	// Bcel
	InstructionHandle ih = this.il.append(new ILOAD(idx));
	// easy printing
	this.il.setPositions();
	int position = ih.getPosition();
	// easy printing code
	this.code2.put(position, "ILOAD " + idx + "\n");
	return position;
    }
    

    /** Adds a ICONST_0 instruction (loads 0 on the stack). 
     * @param the index of the instruction in code. Returns the index
     * of the new instruction.
     * @return the index of the instruction in code.
     * @throws IllegalStateException the class is closed.
     */
    public int addICONST_0() throws IllegalStateException {
	if (this.closed)
	    throw new IllegalStateException();
	// Bcel
	InstructionHandle ih = this.il.append(new ICONST(0));
	this.il.setPositions();
	int position = ih.getPosition();
	// easy printing code
	this.code2.put(position, "ICONST_0 " + "\n");
	return position;
    }


    /** Adds a ICONST_1 instruction (loads 1 on the stack).  Returns
     * the index of the new instruction.
     * @return the index of the instruction in code.
     * @throws IllegalStateException the class is closed.
     */
    public int addICONST_1() throws IllegalStateException {
	if (this.closed)
	    throw new IllegalStateException();
	// Bcel
	InstructionHandle ih = this.il.append(new ICONST(1));
	// easy printing
	this.il.setPositions();
	int position = ih.getPosition();
	this.code2.put(position, "ICONST_1 " + "\n");
	return position;
    }

    /** Adds a ISTORE instruction : loads the top of the stack in the
     * variable of index <tt>idx</tt>.  Returns the index
     * of the new instruction.
     * @param the index if the variable to be stored.
     * @return the index of the instruction in code. 
     * @throws IllegalStateException the class is closed.
     * @throws InvalidIndexException <tt>idx</tt> is not an index of variable. 
     */
    public int addISTORE(int idx) throws IllegalStateException, 
                                          InvalidIndexException {
	if (this.closed)
	    throw new IllegalStateException();
	if (! this.variables.values().contains(new Integer(idx)))
	    throw new InvalidIndexException();
	// Bcel
	InstructionHandle ih = this.il.append(new ISTORE(idx));
	// easy 
	this.il.setPositions();
	int position = ih.getPosition();
	this.code2.put(position, "ISTORE " + idx + "\n");
	return position;
    }

    /****** PRINT ********/


    /** Adds a SPRINT instruction (prints as a string the top of the
     * stack, and pops it).  Returns the index of the new
     * instruction. This byte-code is not a Java one, it was added to
     * simplify your life avoiding methods calls.
     * @return the index of the instruction in code.  
     * @throws IllegalStateException the class is closed.
     */
    public int addSPRINT() throws IllegalStateException {
	if (this.closed)
	    throw new IllegalStateException();    
	// tricky code
	// the rigth byte-code is :
	// 1- add access to System.out
	// 2- add the constant to print
	// 3- add the invokation
	// since the Ava spirit is :
	// a- push something
	// b- add some instruction
	// I added a swap to invert 1 and 2. Eurk !
	
	// Bcel

	// a factory for cg
	InstructionFactory factory = new InstructionFactory(cg);
	ObjectType p_stream = new ObjectType("java.io.PrintStream");
	// access to System.out
	InstructionHandle ih = 
	    this.il.append(factory.createFieldAccess("java.lang.System", 
						     "out", p_stream,
						     Constants.GETSTATIC));
	// SWAP
	this.il.append(new SWAP());
	// invokation
	this.il.append(factory.createInvoke("java.io.PrintStream", "print",
                                 Type.VOID, new Type[] { Type.STRING },
                                 Constants.INVOKEVIRTUAL));
	// easy printing
	this.il.setPositions();
	int position = ih.getPosition();
	this.code2.put(position, "SPRINT" + "\n");
	return position;
    }


    /** Adds a PRINTLN (prints a carriage return on
     * System.out). Returns the index of the new instruction. This
     * byte-code is not a Java one, it was added to simplify your life
     * avoiding methods calls.
     * @return the index of the instruction in code.  
     * @throws IllegalStateException the class is closed.
     */
    public int addPRINTLN() throws IllegalStateException {
	if (this.closed)
	    throw new IllegalStateException();    
	// pushes an empty string then call print : Eurk !

	// a factory for cg
	InstructionFactory factory = new InstructionFactory(cg);
	ObjectType p_stream = new ObjectType("java.io.PrintStream");
	// access to System.out
	InstructionHandle ih = 
	    this.il.append(factory.createFieldAccess("java.lang.System", 
						     "out", p_stream,
						     Constants.GETSTATIC));
	// pushes ""
	int index = this.cg.getConstantPool().addString("");
	this.il.append(new LDC(index));

	// invokation
	this.il.append(factory.createInvoke("java.io.PrintStream", "println",
                                 Type.VOID, new Type[] { Type.STRING },
                                 Constants.INVOKEVIRTUAL));

	// easy printing
	this.il.setPositions();
	int position = ih.getPosition();
	this.code2.put(position, "PRINTLN" + "\n");
	return position;
    }


    /** Adds a IPRINT instruction (prints as an Integer the top of the
     * stack, and pops it). Returns the index of the new
     * instruction. This byte-code is not a Java one, it was added to
     * simplify your life avoiding methods calls.
     * @return the index of the instruction in code.  
     * @throws IllegalStateException the class is closed.
     */
    public int addIPRINT() throws IllegalStateException {
	if (this.closed)
	    throw new IllegalStateException();    
	// same tricky code as for SPRINT
	
	// a factory for cg
	InstructionFactory factory = new InstructionFactory(cg);
	ObjectType p_stream = new ObjectType("java.io.PrintStream");
	// access to System.out
	InstructionHandle ih = 
	    this.il.append(factory.createFieldAccess("java.lang.System", "out", 
						     p_stream,
						     Constants.GETSTATIC));
	// SWAP
	this.il.append(new SWAP());
	// invokation
	this.il.append(factory.createInvoke("java.io.PrintStream", "print",
                                 Type.VOID, new Type[] { Type.INT },
                                 Constants.INVOKEVIRTUAL));
	// easy printing
	this.il.setPositions();
	int position = ih.getPosition();
	this.code2.put(position, "IPRINT" + "\n");
	return position;
    }


    /** Adds a BPRINT instruction (prints as a Boolean the top of the
     * stack, and pops it). Returns the index of the new
     * instruction. This byte-code is not a Java one, it was
     * added to simplify your life avoiding methods calls. 
     * @return the index of the instruction in code.  
     * @throws IllegalStateException the class is closed.
     */
    public int addBPRINT() throws IllegalStateException {
	if (this.closed)
	    throw new IllegalStateException();    
	// same tricky code as for SPRINT
	
	// a factory for cg
	InstructionFactory factory = new InstructionFactory(cg);
	ObjectType p_stream = new ObjectType("java.io.PrintStream");
	// access to System.out
	InstructionHandle ih = 
	    this.il.append(factory.createFieldAccess("java.lang.System", "out", 
						     p_stream,
						     Constants.GETSTATIC));
	// SWAP
	this.il.append(new SWAP());
	// invokation
	this.il.append(factory.createInvoke("java.io.PrintStream", "print",
                                 Type.VOID, new Type[] { Type.BOOLEAN },
                                 Constants.INVOKEVIRTUAL));
	// easy printing
	this.il.setPositions();
	int position = ih.getPosition();
	this.code2.put(position, "BPRINT" + "\n");
	return position;
    }


    /****** READ ************/

    /** Adds a IREAD instruction (reads an Integer on standard input
     * and pushes it. Returns the index of the new
     * instruction. This byte-code is not a Java one, it was added
     * to simplify your life avoiding methods calls.
     * @return the index of the instruction in code.  
     * @throws IllegalStateException the class is closed.
     */
    public int addIREAD() throws IllegalStateException {
	if (this.closed)
	    throw new IllegalStateException();    
	// a factory for cg
	InstructionFactory factory = new InstructionFactory(cg);
	ObjectType i_stream = new ObjectType("java.io.InputStream");
	
	// BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	// ??? for details
	InstructionHandle ih = 
	    il.append(factory.createNew("java.io.BufferedReader"));
	il.append(InstructionConstants.DUP); // Use predefined constant
 	il.append(factory.createNew("java.io.InputStreamReader"));
	il.append(InstructionConstants.DUP); // Use predefined constant
 	il.append(factory.createFieldAccess("java.lang.System", "in", i_stream,
 					    Constants.GETSTATIC));
 	il.append(factory.createInvoke("java.io.InputStreamReader", "<init>",
 				       Type.VOID, new Type[] { i_stream },
 				       Constants.INVOKESPECIAL));
 	il.append(factory.createInvoke("java.io.BufferedReader", "<init>", Type.VOID,
 				       new Type[] {new ObjectType("java.io.Reader")},
 				       Constants.INVOKESPECIAL));

	LocalVariableGen lvar = mg.addLocalVariable("in",
                          new ObjectType("java.io.BufferedReader"), null, null);
	int in = lvar.getIndex();
	il.append(new ASTORE(in)); // init de in
	// call to in.readLine()
	// load reference
	il.append(new ALOAD(in));
	il.append(factory.createInvoke("java.io.BufferedReader", "readLine",
                                 Type.STRING, Type.NO_ARGS,
                                 Constants.INVOKEVIRTUAL));
	il.append(factory.createInvoke("java.lang.Integer", "parseInt",
				       Type.INT, new Type[] { Type.STRING },
                                 Constants.INVOKESTATIC));
	this.il.setPositions();
	int position = ih.getPosition();
	this.code2.put(position, "IREAD" + "\n");	
	return position;
    }


    /****** ARITHMETIC ******/

    /** Adds an IADD instruction (adds the 2 top stack integers, pops
     * them, pushes the result). Returns the index of the new
     * instruction. 
     * @return the index of the instruction in code.  
     * @throws IllegalStateException the class is closed.
     */
    public int addIADD() throws IllegalStateException {
	if (this.closed)
	    throw new IllegalStateException();    
	// Bcel
	InstructionHandle ih = this.il.append(new IADD());
	// easy printing
	this.il.setPositions();
	int position = ih.getPosition();
	this.code2.put(position, "IADD" + "\n");	
	return position;
    }


    /** Adds an ISUB instruction (substacts the top stack integer from
     * the sub top one, pops them, pushes the result). Returns the
     * index of the new instruction.
     * @return the index of the instruction in code.  
     * @throws IllegalStateException the class is closed.
     */
    public int addISUB() throws IllegalStateException {
	if (this.closed)
	    throw new IllegalStateException();    
	// Bcel
	InstructionHandle ih = this.il.append(new ISUB());
	// easy printing
	this.il.setPositions();
	int position = ih.getPosition();
	this.code2.put(position, "ISUB" + "\n");	
	return position;
    }


    /** Adds an IMUL instruction (multiplies the 2 top stack integers, pops
     * them, pushes the result). Returns the index of the new
     * instruction. 
     * @return the index of the instruction in code.  
     * @throws IllegalStateException the class is closed.
     */
    public int addIMUL() throws IllegalStateException {
	if (this.closed)
	    throw new IllegalStateException();    
	// Bcel
	InstructionHandle ih = this.il.append(new IMUL());
	// easy printing
	this.il.setPositions();
	int position = ih.getPosition();
	this.code2.put(position, "IMUL" + "\n");	
	return position;
    }


    /** Adds an IDIV instruction (divides the sub top stack integer by the
     * top one, pops them, pushes the result). Returns the index of the new
     * instruction. 
     * @return the index of the instruction in code.  
     * @throws IllegalStateException the class is closed.
     */
    public int addIDIV() throws IllegalStateException {
	if (this.closed)
	    throw new IllegalStateException();    
	// Bcel
	InstructionHandle ih = this.il.append(new IDIV());
	// easy printing
	this.il.setPositions();
	int position = ih.getPosition();
	this.code2.put(position, "IDIV" + "\n");	
	return position;
    }


    /** Adds an IREM instruction (remainder between the sub top stack
     * integer and the top one, pops them, pushes the result). Returns
     * the index of the new instruction.
     * @return the index of the instruction in code.  
     * @throws IllegalStateException the class is closed.
     */
    public int addIREM() throws IllegalStateException {
	if (this.closed)
	    throw new IllegalStateException();    
	// Bcel
	InstructionHandle ih = this.il.append(new IREM());
	// easy printing
	this.il.setPositions();
	int position = ih.getPosition();
	this.code2.put(position, "IREM" + "\n");	
	return position;
    }

    /** Adds an INEG instruction (inverses the top of stack). Returns
     * the index of the new instruction.
     * @return the index of the instruction in code.  
     * @throws IllegalStateException the class is closed.
     */
    public int addINEG() throws IllegalStateException {
	if (this.closed)
	    throw new IllegalStateException();    
	// Bcel
	InstructionHandle ih = this.il.append(new INEG());
	// easy printing
	this.il.setPositions();
	int position = ih.getPosition();
	this.code2.put(position, "INEG" + "\n");	
	return position;
    }


    /***** BOOLEAN ARITHMETIC ****/

    /** Adds an IOR instruction (ors the 2 top stack integers, pops
     * them, pushes the result). Returns the index of the new
     * instruction.
     * @return the index of the instruction in code.  
     * @throws IllegalStateException the class is closed.
     */
    public int addIOR() throws IllegalStateException {
	if (this.closed)
	    throw new IllegalStateException();    
	// Bcel
	InstructionHandle ih = this.il.append(new IOR());
	// easy printing
	this.il.setPositions();
	int position = ih.getPosition();
	this.code2.put(position, "IOR" + "\n");	
	return position;	
    }


    /** Adds an IAND instruction (ands the 2 top stack integers, pops
     * them, pushes the result). Returns the index of the new
     * instruction.
     * @return the index of the instruction in code.  
     * @throws IllegalStateException the class is closed.
     */
    public int addIAND() throws IllegalStateException {
	if (this.closed)
	    throw new IllegalStateException();    
	// Bcel
	InstructionHandle ih = this.il.append(new IAND());
	// easy printing
	this.il.setPositions();
	int position = ih.getPosition();
	this.code2.put(position, "IAND" + "\n");	
	return position;
    }


    /** Adds an INOT instruction (negates the top of stack). This
     * byte-code is not a Java one, it was added to simplify your life
     * avoiding an integer operation. Returns the index of the new
     * instruction.
     * @return the index of the instruction in code.  
     * @throws IllegalStateException the class is closed.
     */
    public int addINOT() throws IllegalStateException {
	if (this.closed)
	    throw new IllegalStateException();    
	// Bcel
	// do not use GenByteCode addICONST_1() and addISUB() because
	// their memnonics should not appear when printing code
	InstructionHandle ih = this.il.append(new ICONST(1));
	this.il.append(new ISUB());
	// easy printing
	this.il.setPositions();
	int position = ih.getPosition();
	this.code2.put(position, "INOT" + "\n");	
	return position;
    }
    
    /************* JUMP instructions ********************/

    /** Adds an IFEQ instruction (jumps if top stack is 0) whose
    * target is not fixed, and returns its index in the code.
    * @throws IllegalStateException the class is closed.
    */
    public int addIFEQ() throws IllegalStateException {      
	if (this.closed)
	    throw new IllegalStateException();    
	// Bcel
	InstructionHandle ifeq = this.il.append(new IFEQ(null));
	this.il.setPositions();
	int position = ifeq.getPosition();
	// easy printing code
	this.code2.put(position, "IFEQ ?" + "\n");
	return position;	
    }

    /** Adds an IFNE instruction (jumps if top stack is non 0) whose
    * target is not fixed, and returns its index in the code.
    * @throws IllegalStateException the class is closed.
    */
    public int addIFNE() throws IllegalStateException {      
	if (this.closed)
	    throw new IllegalStateException();    
	// Bcel
	InstructionHandle ifne = this.il.append(new IFNE(null));
	this.il.setPositions();
	int position = ifne.getPosition();
	// easy printing code
	this.code2.put(position, "IFNE ?" + "\n");
	return position;	
    }

    /** Adds a GOTO instruction whose target is not fixed, and returns
    * its index in the code.
    * @throws IllegalStateException the class is closed.
    */
    public int addGOTO() throws IllegalStateException {      
	if (this.closed)
	    throw new IllegalStateException();    
	// Bcel
	BranchHandle ifi = this.il.append(new GOTO(null));
	// Eurk : if GOTO has no target, setPositions throws an exception
	// since it tries to update the branch target
	ifi.setTarget(ifi);
	this.il.setPositions();
	int position = ifi.getPosition();
	// easy printing code
	this.code2.put(position, "GOTO ?" + "\n");
	return position;	
    }


    /** Adds a IF_ICMPEQ instruction (compares the top stack and the
    * sub top stack with equality, jumps if equal) whose target is not
    * fixed, and returns its index in the code.
    * @throws IllegalStateException the class is closed.
    */
    public int addIF_ICMPEQ() throws IllegalStateException {      
	if (this.closed)
	    throw new IllegalStateException();    
	// Bcel
	BranchHandle ifi = this.il.append(new IF_ICMPEQ(null));
	this.il.setPositions();
	int position = ifi.getPosition();
	// easy printing code
	this.code2.put(position, "IF_ICMPEQ ?" + "\n");
	return position;	
    }


    /** Adds a IF_ICMPNE instruction (compares the top stack and the
    * sub top stack with difference, jumps if different) whose target is not
    * fixed, and returns its index in the code.
    * @throws IllegalStateException the class is closed.
    */
    public int addIF_ICMPNE() throws IllegalStateException {      
	if (this.closed)
	    throw new IllegalStateException();    
	// Bcel
	BranchHandle ifi = this.il.append(new IF_ICMPNE(null));
	this.il.setPositions();
	int position = ifi.getPosition();
	// easy printing code
	this.code2.put(position, "IF_ICMPNE ?" + "\n");
	return position;	
    }


    /** Adds a IF_ICMPGE instruction (compares the top stack and the
    * sub top stack with geq, jumps if sub top stack >= top stack) whose target is not
    * fixed, and returns its index in the code.
    * @throws IllegalStateException the class is closed.
    */
    public int addIF_ICMPGE() throws IllegalStateException {      
	if (this.closed)
	    throw new IllegalStateException();    
	// Bcel
	BranchHandle ifi = this.il.append(new IF_ICMPGE(null));
	this.il.setPositions();
	int position = ifi.getPosition();
	// easy printing code
	this.code2.put(position, "IF_ICMPGE ?" + "\n");
	return position;	
    }

    /** Adds a IF_ICMPLE instruction (compares the top stack and the
    * sub top stack with leq, jumps if sub top stack <= top stack) whose target is not
    * fixed, and returns its index in the code.
    * @throws IllegalStateException the class is closed.
    */
    public int addIF_ICMPLE() throws IllegalStateException {      
	if (this.closed)
	    throw new IllegalStateException();    
	// Bcel
	BranchHandle ifi = this.il.append(new IF_ICMPLE(null));
	this.il.setPositions();
	int position = ifi.getPosition();
	// easy printing code
	this.code2.put(position, "IF_ICMPLE ?" + "\n");
	return position;	
    }

    /** Adds a IF_ICMPGT instruction (compares the top stack and the
    * sub top stack with >, jumps if sub top stack > top stack) whose target is not
    * fixed, and returns its index in the code.
    * @throws IllegalStateException the class is closed.
    */
    public int addIF_ICMPGT() throws IllegalStateException {      
	if (this.closed)
	    throw new IllegalStateException();    
	// Bcel
	BranchHandle ifi = this.il.append(new IF_ICMPGT(null));
	this.il.setPositions();
	int position = ifi.getPosition();
	// easy printing code
	this.code2.put(position, "IF_ICMPGT ?" + "\n");
	return position;	
    }

    /** Adds a IF_ICMPLT instruction (compares the top stack and the
    * sub top stack with <, jumps if sub top stack < top stack) whose target is not
    * fixed, and returns its index in the code.
    * @throws IllegalStateException the class is closed.
    */
    public int addIF_ICMPLT() throws IllegalStateException {      
	if (this.closed)
	    throw new IllegalStateException();    
	// Bcel
	BranchHandle ifi = this.il.append(new IF_ICMPLT(null));
	this.il.setPositions();
	int position = ifi.getPosition();
	// easy printing code
	this.code2.put(position, "IF_ICMPLT ?" + "\n");
	return position;	
    }




//     /* Returns the index of the last instruction added.
//      * @throws IllegalStateException the class is closed.
//      */
//     public int getLastInstructionIndex() throws IllegalStateException {
// 	if (this.closed)
// 	    throw new IllegalStateException();    
// 	this.il.setPositions();
// 	InstructionHandle[] arrayIH = this.il.getInstructionHandles();
// 	InstructionHandle instr = arrayIH[arrayIH.length - 1];
// 	return instr.getPosition();
//     }

    /** Returns the index of the next instruction to be added.
     * @throws IllegalStateException the class is closed.
     */
    public int getNextInstructionIndex() throws IllegalStateException {
	if (this.closed)
	    throw new IllegalStateException();    
	this.il.setPositions();
	InstructionHandle[] arrayIH = this.il.getInstructionHandles();
	InstructionHandle instr = arrayIH[arrayIH.length - 1];
	int beg = instr.getPosition();
	int size = instr.getInstruction().getLength();
	return beg + size;
    }



    /** Sets the target of a branching instruction, effective only when
     * closing class.
     * @param source the index of the branching instruction
     * @param target the index of the target of the branching instruction
     * @throws IllegalStateException the class is closed.
     * @throws ClassCastException the source target does not correspond to a branching instruction.
     */
    public void setTarget(int source, int target) throws IllegalStateException {
	if (this.closed)
	    throw new IllegalStateException();
	// not effective now ! just memorization
	this.jumps.put(source, target);
	// easy printing code
	String s = this.code2.get(source);
	this.code2.put(source, s.substring(0,s.length() - 2) + " " + target + "\n");
    }


    /** Sets the target of a branching instruction, effective now.
     * @param source the index of the branching instruction
     * @param target the index of the target of the branching instruction
     * @throws IllegalStateException the class is closed.
     * @throws ClassCastException the source target does not correspond to a branching instruction.
     */
    private void setInternalTarget(int source, int target) throws IllegalStateException {
	if (this.closed)
	    throw new IllegalStateException();
	this.il.setPositions();
	try {
	    BranchHandle  sourceIH = (BranchHandle) this.il.findHandle(source);
	    InstructionHandle targetIH = this.il.findHandle(target);
	    sourceIH.setTarget(targetIH);
	} catch (ClassCastException e) {
	    throw new ClassCastException("setTarget() : the source target does not correspond to a branching instruction");
	}
    }
    

    


    /************* stuff FOR PRINTING  ******************/    


    /** Friendly representation of the constant table. */
    public String dumpConstantsTable() {
	String res = "";
	Set s = this.constants.keySet();
	ArrayList l = new ArrayList(s);
	Collections.sort (l);
	Iterator it = l.iterator();
	while (it.hasNext()) {
	    Integer i = (Integer) it.next();
	    res += i + "\t";
	    res += this.constants.get(i)+"\n";
	}	    
	return res;
    }


    /** Friendly representation of the variables table. */
    public String dumpVariablesTable() {
	String res = "";
	Set ens = this.variablesOp.keySet();
	ArrayList l = new ArrayList(ens);
	Collections.sort(l);
	Iterator it = l.iterator();
	while (it.hasNext()) {
	    Integer s = (Integer) it.next();
	    res += s + "\t" + this.variablesOp.get(s)+ "\n";
	}	    
	return res;
    }


    /** Friendly representation of code. */
    public String dumpCode() {
	//	return this.code;
	String res = "";
	Set<Integer> indexSet = this.code2.keySet();
	ArrayList<Integer> indexList = new ArrayList<Integer>(indexSet);
	Collections.sort(indexList);
	Iterator<Integer> it = indexList.iterator();
	while (it.hasNext()) {
	    Integer i = it.next();
	    res += i + "\t";
	    res += this.code2.get(i);
	}
	return res;
    }

    /** Bcel representation of the constant pool. */
    public String dumpConstantPool() {
	return this.cg.getConstantPool().toString();
    }

    /** Bcel representation of code. */
    public String dumpRealCode() {
	return this.il.toString();
    }


    public static void main(String[] argv) throws IllegalStateException,
                                                  IOException,
                                                  InvalidIndexException {
	GenByteCode gen = new GenByteCode("toto");

	// test for IPRINT
	int idConst = gen.newConstant("je veux ecrire 2 ");
	gen.addLDC(idConst);
	gen.addSPRINT();
	
	idConst = gen.newConstant(2);
	gen.addLDC(idConst);
	gen.addIPRINT();

	gen.addPRINTLN();

	// test for BPRINT & false
	idConst = gen.newConstant("je veux ecrire false ");
	gen.addLDC(idConst);
	gen.addSPRINT();
	
	idConst = gen.newConstant(0);
	gen.addLDC(idConst);
	gen.addBPRINT();

	gen.addPRINTLN();


	// test for BPRINT & true
	idConst = gen.newConstant("je veux ecrire true ");
	gen.addLDC(idConst);
	gen.addSPRINT();
	
	idConst = gen.newConstant(25);
	gen.addLDC(idConst);
	gen.addBPRINT();

	gen.addPRINTLN();


	// int x
	int idVar = gen.newVarInt("x");
	// if LDC now : Accessing value from uninitialized register 
	// initialization with 0
	gen.addICONST_0();
	gen.addISTORE(idVar);

	// int y
	int idVar2 = gen.newVarInt("y");
	gen.addICONST_0();
	gen.addISTORE(idVar2);

	idConst = gen.newConstant("val pour x init avec 0 : ");
	gen.addLDC(idConst);
	gen.addSPRINT();
	
	gen.addILOAD(idVar);
	gen.addIPRINT();
	gen.addPRINTLN();

	// x := 5
	idConst = gen.newConstant("val pour x := 5 : ");
	gen.addLDC(idConst);
	gen.addSPRINT();

	idConst = gen.newConstant(5);
	gen.addLDC(idConst);
	gen.addISTORE(idVar);

	gen.addILOAD(idVar);
	gen.addIPRINT();
	gen.addPRINTLN();

	// y := 12
	idConst = gen.newConstant("val pour y := 12 : ");
	gen.addLDC(idConst);
	gen.addSPRINT();

	idConst = gen.newConstant(12);
	gen.addLDC(idConst);
	gen.addISTORE(gen.getIndex("y"));

	gen.addILOAD(gen.getIndex("y"));
	gen.addIPRINT();
	gen.addPRINTLN();
	
	// x + y
	idConst = gen.newConstant("val pour x + y = 5 + 12 = 17 : ");
	gen.addLDC(idConst);
	gen.addSPRINT();
	
	gen.addILOAD(gen.getIndex("x"));
	gen.addILOAD(gen.getIndex("y"));
	gen.addIADD();
	gen.addIPRINT();
	gen.addPRINTLN();

	// x - y
	idConst = gen.newConstant("val pour x - y = 5 - 12 = -7 : ");
	gen.addLDC(idConst);
	gen.addSPRINT();
	
	gen.addILOAD(gen.getIndex("x"));
	gen.addILOAD(gen.getIndex("y"));
	gen.addISUB();
	gen.addIPRINT();
	gen.addPRINTLN();

	// x * y
	idConst = gen.newConstant("val pour x * y = 5 * 12 = 60 : ");
	gen.addLDC(idConst);
	gen.addSPRINT();
	
	gen.addILOAD(gen.getIndex("x"));
	gen.addILOAD(gen.getIndex("y"));
	gen.addIMUL();
	gen.addIPRINT();
	gen.addPRINTLN();

	// y / x
	idConst = gen.newConstant("val pour y / x = 12 / 5 = 2 : ");
	gen.addLDC(idConst);
	gen.addSPRINT();
	
	gen.addILOAD(gen.getIndex("y"));
	gen.addILOAD(gen.getIndex("x"));
	gen.addIDIV();
	gen.addIPRINT();
	gen.addPRINTLN();


	// x / y 
	idConst = gen.newConstant("val pour x / y = 5 / 12 = 0 : ");
	gen.addLDC(idConst);
	gen.addSPRINT();
	
	gen.addILOAD(gen.getIndex("x"));
	gen.addILOAD(gen.getIndex("y"));
	gen.addIDIV();
	gen.addIPRINT();
	gen.addPRINTLN();

	// x % y 
	idConst = gen.newConstant("val pour x % y = 5 % 12 = 5 : ");
	gen.addLDC(idConst);
	gen.addSPRINT();
	
	gen.addILOAD(gen.getIndex("x"));
	gen.addILOAD(gen.getIndex("y"));
	gen.addIREM();
	gen.addIPRINT();
	gen.addPRINTLN();

	// test negation
	idConst = gen.newConstant("val pour - x = -5 : ");
	gen.addLDC(idConst);
	gen.addSPRINT();

	gen.addILOAD(gen.getIndex("x"));
	gen.addINEG();
	gen.addIPRINT();
	gen.addPRINTLN();
	
	// boolean b
	int idb = gen.newVarBool("b");
	idConst = gen.newConstant("b init a false : ");
	gen.addLDC(idConst);
	gen.addSPRINT();
	
        gen.addICONST_0();
 	gen.addISTORE(idb);
 	gen.addILOAD(idb);
 	gen.addBPRINT();
	gen.addPRINTLN();

	// test not
	idConst = gen.newConstant("not false : ");
	gen.addLDC(idConst);
	gen.addSPRINT();

	gen.addICONST_0();
	gen.addINOT();
 	gen.addBPRINT();
	gen.addPRINTLN();


	// not false
	idConst = gen.newConstant("not true : ");
	gen.addLDC(idConst);
	gen.addSPRINT();

	gen.addICONST_1();
	gen.addINOT();
 	gen.addBPRINT();
	gen.addPRINTLN();
	
	// x = read();
	idConst = gen.newConstant("valeur lue : ");
	gen.addLDC(idConst);
	gen.addSPRINT();
	gen.addIREAD();
	gen.addIPRINT();
	gen.addPRINTLN();

// 	// IFEQ : if b then write("b vrai") fi; write ("fin if");
// 	// b vaut vrai
// 	gen.addICONST_1();
// 	gen.addISTORE(gen.getIndex("b"));
//  	gen.addILOAD(gen.getIndex("b"));
// 	int indexIfb = gen.addIFEQ();
//  	idConst = gen.newConstant("OK : b vrai");
//  	gen.addLDC(idConst);
//  	gen.addSPRINT();
//  	gen.addPRINTLN();
//  	idConst = gen.newConstant("fin if");
//  	gen.addLDC(idConst);
// 	// il faut sauter sur le write
// 	int indexSaut = gen.getLastInstructionIndex();
// 	gen.setTarget(indexIfb, indexSaut);
// 	gen.addSPRINT();
// 	gen.addPRINTLN();

// 	// IFEQ : if b then write("b vrai"); fi write ("fin if");
// 	// b vaut faux
// 	gen.addICONST_0();
// 	gen.addISTORE(gen.getIndex("b"));
//  	gen.addILOAD(gen.getIndex("b"));
// 	indexIfb = gen.addIFEQ();
//  	idConst = gen.newConstant("KO : b vrai");
//  	gen.addLDC(idConst);
//  	gen.addSPRINT();
//  	gen.addPRINTLN();
//  	idConst = gen.newConstant("fin if");
//  	gen.addLDC(idConst);
// 	// il faut sauter sur le write
// 	indexSaut = gen.getLastInstructionIndex();
// 	gen.setTarget(indexIfb, indexSaut);
// 	gen.addSPRINT();
// 	gen.addPRINTLN();


// 	// IFEQ : if b then write("b vrai 1"); write("b vrai 2"); write("b vrai 3"); fi write ("fin if");
// 	// b vaut vrai
// 	gen.addICONST_1();
// 	gen.addISTORE(gen.getIndex("b"));
//  	gen.addILOAD(gen.getIndex("b"));
// 	indexIfb = gen.addIFEQ();
//  	idConst = gen.newConstant("OK : b vrai 1");
//  	gen.addLDC(idConst);
//  	gen.addSPRINT();
//  	gen.addPRINTLN();
//  	idConst = gen.newConstant("OK : b vrai 2");
//  	gen.addLDC(idConst);
//  	gen.addSPRINT();
//  	gen.addPRINTLN();
//  	idConst = gen.newConstant("OK : b vrai 3");
//  	gen.addLDC(idConst);
//  	gen.addSPRINT();
//  	gen.addPRINTLN();
//  	idConst = gen.newConstant("fin if");
//  	gen.addLDC(idConst);
// 	// il faut sauter sur le write
// 	indexSaut = gen.getLastInstructionIndex();
// 	gen.setTarget(indexIfb, indexSaut);
// // 	System.out.println("Index SPRINT ");
// // 	System.out.println(gen.getNextInstructionIndex());
// 	gen.addSPRINT();
// // 	System.out.println("\nIndex PRINTLN ");
// // 	System.out.println(gen.getNextInstructionIndex());
// 	gen.addPRINTLN();
// // 	System.out.println("\nIndex ds vide ");
// // 	System.out.println(gen.getNextInstructionIndex());


	gen.closeClass();

	/** Table constantes. */
	System.out.println("Table des constantes *********\n");	System.out.println(gen.dumpConstantsTable());

	/** Table variables. */
	System.out.println("Table des variables *********\n");
	System.out.println(gen.dumpVariablesTable());
	
	/** Code. */
	System.out.println("Code *********\n");
	System.out.println(gen.dumpCode());

	/** Constant pool. */
// 	System.out.println("Constant pool *********\n");
// 	System.out.println(gen.dumpConstantPool());
	
	/** Real code. */
// 	System.out.println("Bcel Code *********\n");
// 	System.out.println(gen.dumpRealCode());
	

    }

}