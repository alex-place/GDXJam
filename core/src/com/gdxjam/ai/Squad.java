package com.gdxjam.ai;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ai.fma.Formation;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.gdxjam.ai.formation.SquadFormationPattern;
import com.gdxjam.ai.formation.SquadFormationPattern.PatternType;
import com.gdxjam.components.Components;
import com.gdxjam.utils.Location2;

public class Squad{
	
	public Array<Entity> members;
	public Formation<Vector2> formation;
	public Location2 targetLocation;

	public SquadSteerable steerable;
	public Arrive<Vector2> ariveSB;
	
	public int index = 0;
	public boolean selected = false;

	public Squad(Vector2 position, int index) {
		this.index = index;
		formation = new Formation<Vector2>(steerable, SquadFormationPattern.patterns.get(PatternType.Ring));
		members = new Array<Entity>();
		
	 	ariveSB = new Arrive<Vector2>(steerable)
	 		.setTarget(targetLocation)
	 		.setTimeToTarget(0.01f)
	 		.setDecelerationRadius(1f)
	 		.setArrivalTolerance(0.1f);
	}
	
	public void addMember(Entity entity){
		members.add(entity);
		formation.addMember(Components.SQUAD_MEMBER.get(entity));
	}

	public void setMemberState(State<Entity> state){
		for(Entity entity : members){
			Components.STATE_MACHINE.get(entity).stateMachine.changeState(state);
		}
	}
	
	public void setFormationPattern(PatternType pattern){
		formation.setPattern(SquadFormationPattern.patterns.get(pattern));
		formation.updateSlotAssignments();
	}
	

	public void setTarget(Vector2 target) {
		targetLocation.getPosition().set(target);
	}
	
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
