package com.tasktop.codejam.visualcodereview.gerrit;

public class DotGenerator {

	public String generate() {
		return "digraph G { a -> b -> c; a-> c; b -> d; c-> d; }";
	}
}
