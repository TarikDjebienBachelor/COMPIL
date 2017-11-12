package ava.typeChecking;

import ava.arbreAbstrait.VisiteurException;

public class TypeCheckingException extends VisiteurException {


    public TypeCheckingException() {
	super();
    }

    public TypeCheckingException(String s) {
	super(s);
    }


} 