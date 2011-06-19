class F {
	public static void main(String[] arg) {
		int bestScore = 0;
		for(int a = 0; a < 81; ++a) {
			String as = Integer.toString(a, 3); while(as.length() < 4) as = "0" + as;
			for(int b = 0; b < 81; ++b) {
				String bs = Integer.toString(b, 3); while(bs.length() < 4) bs = "0" + bs;
				for(int c = 0; c < 81; ++c) {
					String cs = Integer.toString(c, 3); while(cs.length() < 4) cs = "0" + cs;
					int score = 0;
					for(int o = 0; o < 8; ++o) {
						String os = Integer.toString(o, 2); while(os.length() < 3) os = "0" + os;
						int av = Integer.parseInt(String.format("%c%c", os.charAt(1), os.charAt(2)), 2);
						int bv = Integer.parseInt(String.format("%c%c", os.charAt(0), os.charAt(2)), 2);
						int cv = Integer.parseInt(String.format("%c%c", os.charAt(0), os.charAt(1)), 2);
						int won = 0;
						int lost = 0;
						if(as.charAt(av) == os.charAt(0)) ++won; else if(as.charAt(av) != '2') ++lost;
						if(bs.charAt(bv) == os.charAt(1)) ++won; else if(bs.charAt(bv) != '2') ++lost;
						if(cs.charAt(cv) == os.charAt(2)) ++won; else if(cs.charAt(cv) != '2') ++lost;
						//if(won + lost != 3) System.out.printf("%s %s %s %s %s %s %s %s %s%n", as, bs, cs, os, av, bv, cv, won, lost);
						if(won > 0 && lost == 0) ++score;
					}
					if(score > bestScore) {
						bestScore = score;
						System.out.println(as + " " + bs + " " + cs);
					}
				}
			}
		}
	}
}
