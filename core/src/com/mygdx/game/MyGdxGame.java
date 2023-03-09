package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.awt.event.ActionEvent;
import java.awt.geom.Path2D;

import javax.xml.datatype.Duration;


public class MyGdxGame extends Game {
	SpriteBatch batch;
	Texture img;
	Image playerImage;
	OrthographicCamera camera;
	AssetManager manager;
	Sprite playerSprite; // Added sprite object for the ball
	Sprite spriteMessi; // Added sprite object for the Animation
	Sprite objetivo;
	float scaleMessi; // Factor de escala,
	Stage stage;
	Viewport viewport;
	float frameDuration = 0.2f; // Duración en segundos de cada frame
	float elapsedTime = 0;
	int currentFrame = 0;
	TextureRegion[] frames;
	boolean isRunning=false;
	Vector3 worldCoords;

	boolean hit=false;
	//Obstaculo
	Obstacle obstacle;

	//impulso()
	float menost;
	float efecto;

	//Score
	BitmapFont smallFont;
	BitmapFont hitFont;
	BitmapFont scoreFont;

	float score;

	//Level Obstacle
	int level;

	@Override
	public void create () {

		level = 1;

		batch = new SpriteBatch();
		img = new Texture("porteria.jpg");
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

		//Carga de fuente de Letra

		// Create bitmap fonts from TrueType font
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("8bitOperatorPlus-Bold.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
		params.size = 22;
		params.borderWidth = 2;
		params.borderColor = Color.BLACK;
		params.color = Color.WHITE;
		smallFont = generator.generateFont(params); // font size 22 pixels

// Create bitmap fonts from TrueType font HIT
		FreeTypeFontGenerator.FreeTypeFontParameter params2 = new FreeTypeFontGenerator.FreeTypeFontParameter();
		params2.size = 52;
		params2.borderWidth = 2;
		params2.borderColor = Color.BLACK;
		params2.color = Color.GREEN;
		hitFont = generator.generateFont(params2); // font size 52 pixels

// Create bitmap fonts from TrueType font HIT
		FreeTypeFontGenerator.FreeTypeFontParameter params3 = new FreeTypeFontGenerator.FreeTypeFontParameter();
		params3.size = 38;
		params3.borderWidth = 2;
		params3.borderColor = Color.BLUE;
		params3.color = Color.BLUE;
		scoreFont = generator.generateFont(params3); // font size 52 pixels

		//Carga de Assets
		manager = new AssetManager();
		manager.load("pilota.png", Texture.class);
		manager.load("porteria.jpg", Texture.class);
		manager.load("messiSprite.png", Texture.class);
		manager.load("bullseye.png", Texture.class);
		manager.load("sun.png", Texture.class);
		manager.load("objetivonaranja.png", Texture.class);
		manager.load("explosion.png", Texture.class);

		manager.finishLoading();

		//Creamos el punto de mira
		Texture objetivoNaranja = manager.get("objetivonaranja.png", Texture.class);
		objetivo = new Sprite(objetivoNaranja);
		objetivo.scale(0.5f);

        //Creamos el obstaculo
		obstacle=new Obstacle(level);
        obstacle.setX(800);
		obstacle.setY(300);

		//Creamos la textura de la pelota;
		Texture birdTexture = manager.get("pilota.png", Texture.class);
		playerSprite = new Sprite(birdTexture);
// Crea un objeto de la clase Image con la textura del playerSprite
		playerImage = new Image(playerSprite);
		playerImage.setPosition(  400, 50);
		playerImage.setScale(0.27f);

// Agrega el objeto Image al escenario
		float screenWidth = Gdx.graphics.getWidth();
		float screenHeight = Gdx.graphics.getHeight();
		viewport = new FitViewport(800, 480);
		viewport.update((int)screenWidth, (int)screenHeight, true);
		stage = new Stage(viewport);
		stage.addActor(playerImage);
        stage.addActor(obstacle);
		obstacle.setManager(manager);
		// Y la Textura de la animacion
		Texture messiTexture = manager.get("messiSprite.png", Texture.class);
		frames = TextureRegion.split(messiTexture, (int)messiTexture.getWidth()/10, (int)messiTexture.getHeight())[0];
		spriteMessi = new Sprite(frames[0]);
		//spriteMessi.setPosition(80, 160);
		scaleMessi = 2;




	}

	@Override
	public void render () {
		super.render(); // important!
		ScreenUtils.clear(1, 0, 0, 1);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		float scale = Math.max(Gdx.graphics.getWidth() / img.getWidth(), Gdx.graphics.getHeight() / img.getHeight());
		batch.draw(img, 0, 0, 800, 480);

		// Game batch: HUD
		smallFont.draw(batch, "Score: " + (int)score, 10, 470);
		scoreFont.draw(batch, "Score: " + (int)score, 380, 470);

		//Sprite Primera posicion


		if (Gdx.input.justTouched()) {
			if (!hit) {
				float touchX = Gdx.input.getX();
				float touchY = Gdx.input.getY();
				worldCoords = camera.unproject(new Vector3(touchX, touchY, 0));
				isRunning = true;
				tempImpulso(worldCoords.x, worldCoords.y);// llamar al método impulso con Temporizadorhj
			}
			else {
				// Game batch: HUD
				hit=false;
				obstacle.setPosition(800,300);
                obstacle.colision=false;
				//stage.addActor(obstacle);
			}
		}
        if (hit) hitFont.draw(batch, "           Hit\n" +
				"Pulsa para nivel "+ obstacle.getLevel(), 100, 300);


		if (obstacle.getX()<-64){
			score -=1;
			obstacle.setPosition(800,300);
		}
		if (isRunning) {
			System.out.println(Gdx.graphics.getWidth()*scale+"  x         Y     "+ Gdx.graphics.getHeight()*scale);
			batch.draw(objetivo,worldCoords.x,worldCoords.y,50,50);
			stage.act();
			stage.draw();
			batch.draw(spriteMessi, 200, -50, spriteMessi.getWidth() * scaleMessi, spriteMessi.getHeight() * scaleMessi);
			elapsedTime += Gdx.graphics.getDeltaTime();
			batch.end();
			if (elapsedTime >= frameDuration) {
				updateFrame();
				elapsedTime -= frameDuration;
			}
		}
		else {
			batch.draw(spriteMessi, 200, -50, spriteMessi.getWidth() * scaleMessi, spriteMessi.getHeight() * scaleMessi);
			stage.act();
			stage.draw();
			batch.end();
		}
	}

	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}

	public void checkForCollision(float xt, float yt) {
		// Crear un circulo para la pelota
		final Circle circle1 = new Circle(xt, yt, 16);
		Timer.schedule(new Timer.Task() {
			@Override
			public void run() {


				// Crear un circulo para el obstaculo
				Circle obstacleCircle = new Circle(obstacle.getX(), obstacle.getY(), 16);
				System.out.println(obstacleCircle.x+" ------------ y "+obstacleCircle.y);
				float dx = circle1.x - obstacleCircle.x;
				float dy = circle1.y - obstacleCircle.y;
				float distance = (float) Math.sqrt(dx * dx + dy * dy);
				float radiusSum = circle1.radius + obstacleCircle.radius;
				// Verificar si los rectángulos se superponen
				if (distance <= radiusSum){
					score+=100;
					level+=1;
					obstacle.removeObstacle();
					hit=true;
					//obstacle=new Obstacle(level);
					//obstacle.setPosition(800,300);

				}
			}

	}, 1.3f); // establecer canImpulse en true después de 1 segundo

}


	void tempImpulso(final float xt, final float yt){
		Timer.schedule(new Timer.Task() {
			@Override
			public void run() {
				impulso(xt, yt);// llamar al método impulso si canImpulse es true
                checkForCollision(xt, yt);
			}
		}, 1); // establecer canImpulse en true después de 1 segundo
	}

	void impulso(float xt, float yt) {

			float duration = 1.2f; // Duración de la animación en segundos
			float duration_vuelta = 0.0001f;

			//  Interpolation para la curva
            if (xt >0){
				menost = 0.7f;
				efecto = -2;
			}
		    else{
				menost = 1.0f;
				efecto = -1;
			}
			final float potencia = 1.1f;

			Interpolation customInterpolation = new Interpolation.Pow(2) {
				@Override
				public float apply(float t) {
					return efecto * (t - menost) * (t - menost) + potencia;
				}
			};

			//Crea la acción de movimiento hacia la posición xt, yt
			MoveToAction moveAction = new MoveToAction();
			moveAction.setPosition(xt, yt);
			moveAction.setDuration(duration);
			moveAction.setInterpolation(customInterpolation); // Agrega la interpolación personalizada

			// Crea la acción de movimiento de vuelta a la posición inicial
			MoveToAction moveActionVuelta = new MoveToAction();
			moveActionVuelta.setPosition(400f, 50f);
			moveActionVuelta.setDuration(duration_vuelta);

			// Crea la acción de escalado
			ScaleToAction scaleAction = new ScaleToAction();
			scaleAction.setScale(0.05f); // cambia la escala a la mitad
			scaleAction.setDuration(duration);

			// Crea la acción de escalado de vuelta
			ScaleToAction scaleAction2 = new ScaleToAction();
			scaleAction2.setScale(0.27f); // cambia la escala al doble
			scaleAction2.setDuration(duration_vuelta);

			// Combina las dos acciones en una acción de paralelo
			ParallelAction parallelAction = new ParallelAction();
			parallelAction.addAction(scaleAction);
			parallelAction.addAction(moveAction);

			// Crea una secuencia que ejecuta las acciones en orden
			SequenceAction sequence = new SequenceAction();
			sequence.addAction(parallelAction);
			sequence.addAction(moveActionVuelta);
			sequence.addAction(scaleAction2);

			// Agrega la secuencia de acciones al objeto Image
			playerImage.addAction(sequence);
		}
		// Función que cambia el frame que se está dibujando
		private void updateFrame () {
			currentFrame++;
			if (currentFrame >= frames.length) {
				currentFrame = 0;
				isRunning = false;
			}
			spriteMessi.setRegion(frames[currentFrame]);
		}
}
