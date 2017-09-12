package com.adamjahangiri.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {

	SpriteBatch batch;
	Texture background;
    ShapeRenderer shapeRenderer;
    Texture botTube;
    Texture topTube;
    Texture gameOver;

    float gap = 200;
    float maxTubeOffset;
    Random randomGenerator;

    float tubeVelocity = 2;

    int numberOfTubes = 4;
    float[] tubeX = new float[numberOfTubes];
    float[] tubeOffset = new float[numberOfTubes];
    float distanceBetweenTubes;

	Texture[] birds;
    int flapState = 0;
    float birdY = 0;
    float velocity = 0;
    Circle birdCircle;
    Rectangle[] botColumnRectangle;
    Rectangle[] topColumnRectangle;
    //Rectangle[] scoreBox;

    float gravity = 1;

    int gameState = 0;
    int score;
    int scoringTube = 0;

    BitmapFont font;
	
	@Override
	public void create () {

		batch = new SpriteBatch();
        background = new Texture("bg.png");
        botTube = new Texture("bottube.png");
        topTube = new Texture("toptube.png");
        maxTubeOffset = Gdx.graphics.getHeight()/2 - gap/2 - 100;
        randomGenerator = new Random();
        gameOver = new Texture("gameover.png");

        distanceBetweenTubes = Gdx.graphics.getWidth()/2 * 3/4;
        shapeRenderer = new ShapeRenderer();
        birdCircle = new Circle();
        botColumnRectangle = new Rectangle[numberOfTubes];
        topColumnRectangle = new Rectangle[numberOfTubes];
        //scoreBox = new Rectangle[numberOfTubes];

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(10);

		birds = new Texture[2];
		birds[0]= new Texture("bird.png");
		birds[1]= new Texture("bird2.png");
        startGame();





	}

    public void startGame() {

        birdY = Gdx.graphics.getHeight()/2-birds[0].getHeight()/2;

        for (int i=0; i < numberOfTubes; i++){

            tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);

            tubeX[i] = Gdx.graphics.getWidth()/2 - botTube.getWidth()/2 + Gdx.graphics.getWidth()/2 + i * distanceBetweenTubes;

            topColumnRectangle[i] = new Rectangle();
            botColumnRectangle[i] = new Rectangle();
            //scoreBox[i] = new Rectangle();


        }


    }


	@Override
	public void render () {

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        //shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        //shapeRenderer.setColor(Color.RED);

        if (gameState == 1) {


            if (tubeX[scoringTube] < Gdx.graphics.getWidth()/2){

                score++;

                Gdx.app.log("score", String.valueOf(score));

                if (scoringTube < numberOfTubes -1 ){

                    scoringTube++;

                } else {

                    scoringTube =0;

                }

            }




            if (Gdx.input.isTouched()){


                velocity = -15;



            }

            for (int i=0; i < numberOfTubes; i++) {

                if (tubeX[i] < - topTube.getWidth()){


                    tubeX[i] += numberOfTubes * distanceBetweenTubes;
                    tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);


                } else {

                    tubeX[i] -= tubeVelocity;




                }

                batch.draw(botTube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - botTube.getHeight() + tubeOffset[i]);
                batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);

               topColumnRectangle[i].set(tubeX[i],Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
               botColumnRectangle[i].set(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - botTube.getHeight() + tubeOffset[i], botTube.getWidth(), botTube.getHeight());
               //scoreBox[i].set(tubeX[i] + botTube.getWidth(),Gdx.graphics.getHeight() / 2 - gap / 2  + tubeOffset[i] , botTube.getWidth()*0.02f,gap );

            }

            if (birdY > 0 ) {

                velocity += gravity;
                birdY -= velocity;


            } else {

                gameState = 2;

            }


        } else if (gameState == 0){

           if (Gdx.input.isTouched()){


                gameState = 1;

            }


        } else if (gameState == 2) {

            batch.draw(gameOver, Gdx.graphics.getWidth()/2 - gameOver.getWidth()/2,Gdx.graphics.getHeight()/2 - gameOver.getHeight()/2 );

            if (Gdx.input.isTouched()){


                gameState = 1;
                startGame();
                score = 0;
                scoringTube = 0;
                velocity = 0;

            }


        }

        if (flapState == 0) {

            flapState = 1;

        } else {

            flapState = 0;

    }


        batch.draw(birds[flapState], Gdx.graphics.getWidth() / 2 - birds[flapState].getWidth() / 2, birdY);

        font.draw(batch, String.valueOf(score), 50, 150);


        birdCircle.set(Gdx.graphics.getWidth() / 2, birdY + birds[flapState].getHeight() / 2, birds[flapState].getWidth() / 2);

    for (int i=0; i < numberOfTubes; i++) {

          //shapeRenderer.rect(topColumnRectangle[i].x,topColumnRectangle[i].y,topColumnRectangle[i].getWidth(),topColumnRectangle[i].getHeight());
         // shapeRenderer.rect(botColumnRectangle[i].x, botColumnRectangle[i].y, botColumnRectangle[i].getWidth(), botColumnRectangle[i].getHeight());
           // shapeRenderer.rect(scoreBox[i].x, scoreBox[i].y, scoreBox[i].getWidth(), scoreBox[i].getHeight());


         if (Intersector.overlaps(birdCircle, topColumnRectangle[i]) || Intersector.overlaps(birdCircle, botColumnRectangle[i])){


             gameState = 2;


              }


        }


       //shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);
       //shapeRenderer.end();
        batch.end();
	}
	

}
