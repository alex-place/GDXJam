package com.gdxjam.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.IntMap.Entry;
import com.gdxjam.Assets;
import com.gdxjam.components.Components;
import com.gdxjam.components.SquadComponent;
import com.gdxjam.ui.SquadManagmentTable;
import com.gdxjam.utils.Constants;

public class GUISystem extends EntitySystem implements Disposable {

	private Stage stage;
	private Skin skin;
	private Table squadSidebar;
	
	private IntMap<Entity> squads = new IntMap<Entity>();
	
	private SquadManagmentTable squadManagment;
	private Label resourceLabel;

	public GUISystem() {
		this.stage = new Stage();
		this.skin = Assets.skin;

		initGUI();
	}

	public void initGUI() {
		/** Resource Table		 */
		resourceLabel = new Label("Resources: XXX", skin);
		Table resourceTable = new Table();
		resourceTable.add(resourceLabel);
		
		

		squadManagment = new SquadManagmentTable(skin);
		
		Table squadManagmentContainer = new Table();
		squadManagmentContainer.setFillParent(true);
		squadManagmentContainer.add(squadManagment).padTop(30);
		squadManagmentContainer.center().bottom();
		stage.addActor(squadManagmentContainer);
		
		Table topTable = new Table();
		topTable.setFillParent(true);
		topTable.top().right();
		
		topTable.defaults().pad(5);
		topTable.add(resourceTable);

		stage.addActor(topTable);
		
	}

	@Override
	public void addedToEngine (Engine engine) {
		super.addedToEngine(engine);
	}
	
	public void addSquad(Entity squad) {
		for(int i = 0; i < Constants.maxSquads; i++){
			boolean valid = true;
			if(squads.containsKey(i)){
				if(squads.get(i) != null){
					valid = false;
				}
			}
			if(valid){
				squads.put(i, squad);
				squadManagment.addSquad(squad, i);
				break;
			}
		}

	}
	
	public void removeSquad(Entity squad){
		for(Entry<Entity> entry : squads){
			if(entry.value == squad){
				squads.remove(entry.key);
				squadManagment.removeSquad(squad, entry.key);
				return;
			}
		}
		

	}
	
	public void updateSquad(Entity squad){
		for(Entry<Entity> entry : squads){
			if(entry.value == squad){
				squadManagment.updateSquadTable(entry.key);
			}
		}
	}

	public void setSelected(int index, boolean selected) {
		if(squads.containsKey(index)){
			Entity squad = squads.get(index);
			SquadComponent squadComp = Components.SQUAD.get(squad);
			squadComp.selected = selected;
			squadManagment.setSelected(index, squadComp.isSelected());
		}
	}
	
	public void setAllSelected(boolean selected){
		for(Entry<Entity> entry : squads){
			setSelected(entry.key, selected);
		}
	}

	public void resize(int screenWidth, int screenHeight) {
		stage.getViewport().update(screenWidth, screenHeight);
	}
	
	public void updateResource(int amount){
		resourceLabel.setText("Resources: " + amount);
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);

		stage.act();
		stage.draw();
	}

	public Stage getStage() {
		return stage;
	}

//	public HotkeyTable getHotkeyTable() {
//		return hotkeyTable;
//	}
	
	@Override
	public void dispose() {
		stage.dispose();
	}

}
