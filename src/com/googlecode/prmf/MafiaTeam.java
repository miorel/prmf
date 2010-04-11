package com.googlecode.prmf;

class MafiaTeam extends Team {
	private String name;
		
	public MafiaTeam() {
		name = "MafiaTeam";
		super.createList();
	}

	public String getName() {
		return name;
	}
	
	public boolean hasWon(Player[] players)
	{
		//itt we check for victory
		//if mafia make up at least 50% of the living players return true
		int nonMafia = 0, mafia = 0;
		for (Player p : players)
		{
			if (!p.isAlive())
				continue;
			if (p.getRole().getTeam().equals(this)) // TODO this looks like a bug to me // why is that a bug?
				++mafia;
			else
				++nonMafia;
		}
		return nonMafia <= mafia;
	}
	
	public boolean contains(Player player) {
		return getList().contains(player);
	}
	
	public boolean agreeOnTarget()
	{
		Player target = null;
		boolean agree = true;
		for(Player p : getList())
		{
			if(target == null)
			{
				target = p.getRole().getTarget();
				if (target == null)
				{
					agree = false;
					break;
				}
			}
			try
			{
				agree = (target.equals(p.getRole().getTarget()));
			}
			catch (Exception e)
			{
				System.out.println("still sucks");
			}
			
			if(!agree)
				break;
		}
		if(agree)
			for (Player p : getList())
				p.getRole().setNightAction(true);
		return agree;
	
	}
	
}
