package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;


public class MyGdxGame extends Game {
	SpriteBatch batch;
	Texture img;
	Image playerImage;
	OrthographicCamera camera;
	AssetManager manager;
	Sprite playerSprite; // Added sprite object for the bird
	Sprite spriteMessi; // Added sprite object for the bird
	float scaleMessi = 2f; // Factor de escala, en este caso x2
	Stage stage;
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("porteria.jpg");
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		//Carga de Assets
		manager = new AssetManager();
		manager.load("pilota.png", Texture.class);
		manager.load("porteria.jpg", Texture.class);
		manager.load("messiSprite.png", Texture.class);
		manager.finishLoading();


		//Creamos la textura de la pelota;

		Texture birdTexture = manager.get("pilota.png", Texture.class);
		playerSprite = new Sprite(birdTexture);
		playerSprite.setPosition( 100, 100);

// Crea un objeto de la clase Image con la textura del playerSprite
		playerImage = new Image(playerSprite);

// Agrega el objeto Image al escenario
		stage = new Stage();
		stage.addActor(playerImage);

		// Y la Textura de la animacion
		Texture messiTexture = manager.get("messiSprite.png", Texture.class);

		TextureRegion[]  frames = TextureRegion.split(messiTexture, (int)messiTexture.getWidth()/10, (int)messiTexture.getHeight())[0];
		TextureRegion firstFrame = frames[0];
		spriteMessi = new Sprite(firstFrame);
		spriteMessi.setPosition( 100, 100);
		scaleMessi = 4;
	}

	@Override
	public void render () {
		super.render(); // important!
		ScreenUtils.clear(1, 0, 0, 1);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		float scale = Math.max(Gdx.graphics.getWidth() / img.getWidth(), Gdx.graphics.getHeight() / img.getHeight());

		batch.draw(img, 0, 0, img.getWidth() * scale, img.getHeight() * scale);

		batch.draw(spriteMessi,100,50,spriteMessi.getWidth()*scaleMessi,spriteMessi.getHeight()*scaleMessi);


		if (Gdx.input.justTouched()) {
			float touchX = Gdx.input.getX();
			float touchY = Gdx.input.getY();
			Vector3 worldCoords = camera.unproject(new Vector3(touchX, touchY, 0));
			impulso(worldCoords.x, worldCoords.y);		}

		stage.act();
		stage.draw();
		batch.end();

	}

	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
	void impulso(float xt, float yt) {

		float duration = 1.2f; // Duración de la animación en segundos
		float duration_vuelta = 0.1f;

		// Crea la acción de movimiento hacia la posición xt, yt
		MoveToAction moveAction = new MoveToAction();
		moveAction.setPosition(xt, yt);
		moveAction.setDuration(duration);

		// Crea la acción de movimiento de vuelta a la posición inicial
		MoveToAction moveActionVuelta = new MoveToAction();
		moveActionVuelta.setPosition(400f, 200f);
		moveActionVuelta.setDuration(duration_vuelta);

		// Combina las dos acciones en una secuencia
		SequenceAction sequence = new SequenceAction();
		sequence.addAction(moveAction);
		sequence.addAction(moveActionVuelta);

		// Agrega la secuencia de acciones al objeto Image
		playerImage.addAction(sequence);
	}


}
