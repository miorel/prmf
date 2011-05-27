// basic prototype parser for a language with Lisp-like syntax
// by Miorel-Lucian Palii
// May 26, 2011

import java.util.*;
import java.io.*;

class Parser {
	private final static int BUF_SIZE = 1 << 12;
	private final static byte[] buf = new byte[BUF_SIZE];
	private static int pos = 0;
	private static int len = 0;

	public static void main(String[] arg) throws IOException {
		for(Node n; (n = readNode()) != null;)
			System.out.println(n);
	}

	private static boolean isUppercaseAlpha(int c) {
		return 65 <= c && c <= 90;
	}

	private static boolean isLowercaseAlpha(int c) {
		return 97 <= c && c <= 122;
	}

	private static boolean isAlpha(int c) {
		return isUppercaseAlpha(c) || isLowercaseAlpha(c);
	}

	private static Node readNode() throws IOException {
		Node ret = null;
		Node cur = null;
		do {
			int c = read();
			if(c == ')')
				cur = cur.parent;
			else {
				Node n = new Node();
				n.parent = cur;
				if(c == '(') {
					n.children = new ArrayList<Node>();
					cur = n;
				}
				else if(isAlpha(c)) {
					--pos;
					n.token = readToken();
				}
				else {
					if(c == ' ') {
						int p = peek();
						if(isAlpha(p) || p == '(')
							continue;
					}
					if(c < 0 && ret == null)
						break;
					throw new RuntimeException("Encountered unexpected character '" + ((char) c) + "' (ASCII code " + c + ")");
				}
				if(n.parent != null)
					n.parent.children.add(n);
				if(ret == null)
					ret = n;
			}
		} while(cur != null);
		return ret;
	}

	private static String readToken() throws IOException {
		StringBuilder ret = new StringBuilder();
		for(;;) {
			int c = read();
			if(isAlpha(c))
				ret.append((char) c);
			else {
				--pos;
				return ret.length() == 0 ? null : ret.toString();
			}
		}
	}

	private static int peek() throws IOException {
		int ret = read();
		--pos;
		return ret;
	}

	private static int read() throws IOException {
		while(len >= 0 && pos >= len) {
			len = System.in.read(buf, 0, BUF_SIZE);
			pos = 0;
		}
		if(len < 0)
			return -1;
		int ret = (int) buf[pos++];
		if(!(
			isAlpha(ret) ||
			(40 <= ret && ret <= 41) || // parentheses
			(ret == 32) || // space
			false
		))
			throw new IOException("Byte value (" + ret + ") is outside allowed range.");
		return ret;
	}
}

class Node {
	public String token = null;
	public List<Node> children = null;
	public Node parent = null;

	public String toString() {
		return token != null ? token : children.toString();
	}
}

/*

TOKEN = [A-Za-z]+
SENTENCE = TOKEN | (TOKEN*)





*/
