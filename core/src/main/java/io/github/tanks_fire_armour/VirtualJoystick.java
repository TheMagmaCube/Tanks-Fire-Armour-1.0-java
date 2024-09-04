package io.github.tanks_fire_armour;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class VirtualJoystick {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    public Texture knob;
    public Texture knob_background;
    public Sprite knob_sprite;
    public Sprite knob_background_sprite;
    public boolean touch;
    public boolean shoot;
    public double angle;

    public void point() {
        if (Gdx.input.isTouched()) {
            touch = true;
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);

            double d = Math.sqrt(Math.pow((knob_background_sprite.getX() + knob_background_sprite.getWidth() / 2)- touchPos.x, 2) + (Math.pow((knob_background_sprite.getY() + knob_background_sprite.getHeight() / 2) - touchPos.y, 2)));

            if (d < 100) {

                knob_sprite.setX(touchPos.x - knob_sprite.getWidth() / 2);
                knob_sprite.setY(touchPos.y - knob_sprite.getHeight() / 2);
                double diferencex = (knob_sprite.getX() + knob_sprite.getWidth() / 2) - (knob_background_sprite.getX() + knob_background_sprite.getWidth() / 2);
                double diferencey = (knob_sprite.getY() + knob_sprite.getHeight() / 2) - (knob_background_sprite.getY() + knob_background_sprite.getHeight() / 2);
                angle = Math.toDegrees(Math.atan2(diferencex, diferencey));
                shoot = false;
            }else if(d <= 150 && d >= 100){
                knob_sprite.setX(touchPos.x - knob_sprite.getWidth() / 2);
                knob_sprite.setY(touchPos.y - knob_sprite.getHeight() / 2);
                double diferencex = (knob_sprite.getX() + knob_sprite.getWidth() / 2) - (knob_background_sprite.getX() + knob_background_sprite.getWidth() / 2);
                double diferencey = (knob_sprite.getY() + knob_sprite.getHeight() / 2) - (knob_background_sprite.getY() + knob_background_sprite.getHeight() / 2);
                angle = Math.toDegrees(Math.atan2(diferencex, diferencey));
                shoot = true;
            }else{
                knob_sprite.setX(knob_background_sprite.getX() + knob_sprite.getWidth() / 2);
                knob_sprite.setY(knob_background_sprite.getY() + knob_sprite.getHeight() / 2);
                touch = false;
                shoot = false;
            }
        }else{
            knob_sprite.setX(knob_background_sprite.getX() + knob_sprite.getWidth() / 2);
            knob_sprite.setY(knob_background_sprite.getY() + knob_sprite.getHeight() / 2);
            touch = false;
            shoot = false;
        }
    }
    public void create(){
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        knob = new Texture("joystick_tip_arrows.png");
        knob_background = new Texture("joystick_base_outline.png");
        knob_sprite = new Sprite(knob);
        knob_background_sprite = new Sprite(knob_background);
        angle = 0;
        touch = false;
        shoot = false;
    }
    public void render() {
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        point();
        knob_background_sprite.draw(batch);
        knob_sprite.draw(batch);
        batch.end();
    }
    public void dispose() {
        batch.dispose();
        knob.dispose();
        knob_background.dispose();
    }
}
