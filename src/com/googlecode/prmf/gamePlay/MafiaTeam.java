package com.googlecode.prmf.gamePlay;

class MafiaTeam extends Team {

	public MafiaTeam() {
		setName("MafiaTeam");
	}

	@Override
	public String getName() {
		return "MafiaTeam";
	}
	
	@Override
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
			{
				++mafia;
				System.err.println(p + " is a mafia");
			}
				
			else
			{
				++nonMafia;
				System.err.println(p + " is not a mafia");
			}
		}
		return nonMafia <= mafia;
	}
	
	@Override
	public String toString()
	{
		
		StringBuilder toReturn = new StringBuilder();
		toReturn.append("The current living mafia team members are: ");
		for (Player p : getList())
			if(p.isAlive())
			toReturn.append(p + " ");
		return toReturn.toString();
	}
	
	public boolean agreeOnTarget()
	{
		Player target = null;
		boolean agree = true;
		for(Player p : getList())
		{
			if (!p.isAlive())
				continue;
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
