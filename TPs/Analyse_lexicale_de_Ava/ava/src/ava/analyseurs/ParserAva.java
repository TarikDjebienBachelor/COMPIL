
//----------------------------------------------------
// The following code was generated by CUP v0.11a beta 20060608
// Tue Sep 21 21:23:01 CEST 2010
//----------------------------------------------------

package ava.analyseurs;


/** CUP v0.11a beta 20060608 generated parser.
  * @version Tue Sep 21 21:23:01 CEST 2010
  */
public class ParserAva extends java_cup.runtime.lr_parser {

  /** Default constructor. */
  public ParserAva() {super();}

  /** Constructor which sets the default scanner. */
  public ParserAva(java_cup.runtime.Scanner s) {super(s);}

  /** Constructor which sets the default scanner. */
  public ParserAva(java_cup.runtime.Scanner s, java_cup.runtime.SymbolFactory sf) {super(s,sf);}

  /** Production table. */
  protected static final short _production_table[][] = 
    unpackFromStrings(new String[] {
    "\000\002\000\002\002\002\000\002\002\004" });

  /** Access to production table. */
  public short[][] production_table() {return _production_table;}

  /** Parse-action table. */
  protected static final short[][] _action_table = 
    unpackFromStrings(new String[] {
    "\000\003\000\004\002\001\001\002\000\004\002\005\001" +
    "\002\000\004\002\000\001\002" });

  /** Access to parse-action table. */
  public short[][] action_table() {return _action_table;}

  /** <code>reduce_goto</code> table. */
  protected static final short[][] _reduce_table = 
    unpackFromStrings(new String[] {
    "\000\003\000\004\002\003\001\001\000\002\001\001\000" +
    "\002\001\001" });

  /** Access to <code>reduce_goto</code> table. */
  public short[][] reduce_table() {return _reduce_table;}

  /** Instance of action encapsulation class. */
  protected CUP$ParserAva$actions action_obj;

  /** Action encapsulation object initializer. */
  protected void init_actions()
    {
      action_obj = new CUP$ParserAva$actions(this);
    }

  /** Invoke a user supplied parse action. */
  public java_cup.runtime.Symbol do_action(
    int                        act_num,
    java_cup.runtime.lr_parser parser,
    java.util.Stack            stack,
    int                        top)
    throws java.lang.Exception
  {
    /* call code in generated class */
    return action_obj.CUP$ParserAva$do_action(act_num, parser, stack, top);
  }

  /** Indicates start state. */
  public int start_state() {return 0;}
  /** Indicates start production. */
  public int start_production() {return 1;}

  /** <code>EOF</code> Symbol index. */
  public int EOF_sym() {return 0;}

  /** <code>error</code> Symbol index. */
  public int error_sym() {return 1;}

}

/** Cup generated class to encapsulate user supplied action code.*/
class CUP$ParserAva$actions {
  private final ParserAva parser;

  /** Constructor */
  CUP$ParserAva$actions(ParserAva parser) {
    this.parser = parser;
  }

  /** Method with the actual generated action code. */
  public final java_cup.runtime.Symbol CUP$ParserAva$do_action(
    int                        CUP$ParserAva$act_num,
    java_cup.runtime.lr_parser CUP$ParserAva$parser,
    java.util.Stack            CUP$ParserAva$stack,
    int                        CUP$ParserAva$top)
    throws java.lang.Exception
    {
      /* Symbol object for return from actions */
      java_cup.runtime.Symbol CUP$ParserAva$result;

      /* select the action based on the action number */
      switch (CUP$ParserAva$act_num)
        {
          /*. . . . . . . . . . . . . . . . . . . .*/
          case 1: // $START ::= CodeSourceAva EOF 
            {
              Object RESULT =null;
		int start_valleft = ((java_cup.runtime.Symbol)CUP$ParserAva$stack.elementAt(CUP$ParserAva$top-1)).left;
		int start_valright = ((java_cup.runtime.Symbol)CUP$ParserAva$stack.elementAt(CUP$ParserAva$top-1)).right;
		Object start_val = (Object)((java_cup.runtime.Symbol) CUP$ParserAva$stack.elementAt(CUP$ParserAva$top-1)).value;
		RESULT = start_val;
              CUP$ParserAva$result = parser.getSymbolFactory().newSymbol("$START",0, ((java_cup.runtime.Symbol)CUP$ParserAva$stack.elementAt(CUP$ParserAva$top-1)), ((java_cup.runtime.Symbol)CUP$ParserAva$stack.peek()), RESULT);
            }
          /* ACCEPT */
          CUP$ParserAva$parser.done_parsing();
          return CUP$ParserAva$result;

          /*. . . . . . . . . . . . . . . . . . . .*/
          case 0: // CodeSourceAva ::= 
            {
              Object RESULT =null;

              CUP$ParserAva$result = parser.getSymbolFactory().newSymbol("CodeSourceAva",0, ((java_cup.runtime.Symbol)CUP$ParserAva$stack.peek()), ((java_cup.runtime.Symbol)CUP$ParserAva$stack.peek()), RESULT);
            }
          return CUP$ParserAva$result;

          /* . . . . . .*/
          default:
            throw new Exception(
               "Invalid action number found in internal parse table");

        }
    }
}

