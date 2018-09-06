package io.anuke.mindustry.ui.fragments;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import static io.anuke.mindustry.Vars.*;
import io.anuke.mindustry.core.GameState.State;
import io.anuke.mindustry.input.InputHandler;
import io.anuke.ucore.core.Core;
import io.anuke.ucore.scene.ui.layout.Table;

public class ToolFragment implements Fragment{
	private Table tools;
	
	public void build(){
		InputHandler input = control.input();
		
		float isize = 14*3;
		
		tools = new Table();
		
		tools.addImageButton("icon-cancel", isize, () -> {
			input.placeMode.onActionCancel();
		});
		
		tools.addImageButton("icon-rotate", isize, () -> {
			input.rotation++;
			input.rotation %= 4;
		});
		
		tools.addImageButton("icon-check", isize, () -> {
			if(input.placeMode.isConfirming()){
				input.placeMode.onActionConfirm();
				input.placeMode.setConfirming(false);
			}
		});
		
		Core.scene.add(tools);
		
		tools.setVisible(() ->
					!state.is(State.menu)
							&& mobile
							&& input.placeMode.isConfirming()
		);
		
		tools.update(() -> {
			Vector2 v = input.placeMode.getToolsPosition();
			tools.setPosition(v.x, v.y, Align.top);
		});
	}
	
}
