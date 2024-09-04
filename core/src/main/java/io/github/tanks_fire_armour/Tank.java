package io.github.tanks_fire_armour;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Tank {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Texture hull;
    private Texture turret;
    private Texture bullet_texture;
    public Sprite hull_sprite;
    public Sprite turret_sprite;
    public float hull_angle;
    public float turret_angle;
    private double speed;
    private double bullet_speed;
    private double reload;
    public List <Map<String,Object>> bullets_list;

    private VirtualJoystick virtualJoystick;
    private VirtualJoystick virtualJoystick_2;

    public List <Map<String,Object>> getter(){
        return bullets_list;
    }

    public void movement(){
        double direction_x = Math.cos(Math.toRadians(hull_angle + 90));
        double direction_y = Math.sin(Math.toRadians(hull_angle + 90));

        float x_float = hull_sprite.getX();
        //x_float -= hull_sprite.getWidth();
        x_float += direction_x * speed;
        hull_sprite.setX(x_float);

        float y_float = hull_sprite.getY();
        //y_float -= hull_sprite.getHeight();
        y_float += direction_y * speed;
        hull_sprite.setY(y_float);

        float x_float_2 = turret_sprite.getX();
        //x_float_2 -= turret_sprite.getWidth();
        x_float_2 += direction_x * speed;
        turret_sprite.setX(x_float_2);

        float y_float_2 = turret_sprite.getY();
        //y_float_2 -= turret_sprite.getHeight();
        y_float_2 += direction_y * speed;
        turret_sprite.setY(y_float_2);
    }
    public void turret_rotate(){
        double angle_difference = virtualJoystick_2.angle - turret_angle;
        turret_angle += angle_difference;
        turret_angle = -(turret_angle);
        if(-(float)virtualJoystick_2.angle > turret_angle && virtualJoystick_2.touch){
            turret_angle++;
        }else if(-(float)virtualJoystick_2.angle < turret_angle && virtualJoystick_2.touch){
            turret_angle--;
        }
        turret_sprite.setRotation(turret_angle);
    }
    public void hull_rotate(){
        double angle_hull_difference = virtualJoystick.angle - hull_angle;
        hull_angle += angle_hull_difference;
        hull_angle = -(hull_angle);
        if(-(float)virtualJoystick.angle > hull_angle && virtualJoystick.touch){
            hull_angle++;
        }else if(-(float)virtualJoystick.angle < hull_angle && virtualJoystick.touch){
            hull_angle--;
        }
        hull_sprite.setRotation(hull_angle);
    }
    public void shoot(){
        if(reload <= 0 && virtualJoystick_2.shoot == true){
            reload = 100;
            float bullet_angle = turret_angle + 90;
            Sprite bullet_sprite = new Sprite(bullet_texture);
            bullet_sprite.setPosition(turret_sprite.getX() + turret_sprite.getWidth() / 2 - bullet_sprite.getWidth() / 2, turret_sprite.getY() + turret_sprite.getHeight() / 2 - bullet_sprite.getHeight()/ 2);
            Map<String, Object> bullet_map = new HashMap<>();
            bullet_map.put("bullet_angle", bullet_angle);bullet_map.put("bullet_x", bullet_sprite.getX());bullet_map.put("bullet_y",bullet_sprite.getY());
            bullets_list.add(bullet_map);
        }
    }
    public void after_shoot(){
        if(!bullets_list.isEmpty()){
            for(int i = 0 ; i < bullets_list.size(); i++){
                Map<String, Object> bullet = bullets_list.get(i);
                double direction_x_bullet = Math.cos(Math.toRadians((float) bullet.get("bullet_angle")));
                double direction_y_bullet = Math.sin(Math.toRadians((float) bullet.get("bullet_angle")));
                float new_position_x_bullet = (float) bullet.get("bullet_x");
                new_position_x_bullet += direction_x_bullet * bullet_speed;
                float new_position_y_bullet = (float) bullet.get("bullet_y");
                new_position_y_bullet += direction_y_bullet * bullet_speed;
                if(new_position_x_bullet <= 0){
                    bullets_list.remove(bullet);
                }
                if(new_position_x_bullet >= 800){
                    bullets_list.remove(bullet);
                }
                if(new_position_y_bullet >= 480){
                    bullets_list.remove(bullet);
                }
                if(new_position_y_bullet <= 0){
                    bullets_list.remove(bullet);
                }
                bullet.put("bullet_x", new_position_x_bullet);
                bullet.put("bullet_y", new_position_y_bullet);
            }
        }
    }
    public void render_bullets(){
        if(!bullets_list.isEmpty()){
            for(int i = 0 ; i < bullets_list.size(); i++){
                Map<String, Object> bullet = bullets_list.get(i);
                Sprite bullet_sprite = new Sprite(bullet_texture);
                bullet_sprite.setRotation((float)bullet.get("bullet_angle"));
                bullet_sprite.setPosition((float)bullet.get("bullet_x"), (float)bullet.get("bullet_y"));
                bullet_sprite.draw(batch);
            }
        }
    }
    public void create(){
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        hull = new Texture("hull.png");
        turret = new Texture("turret.png");
        bullet_texture = new Texture("bullet.png");
        bullets_list = new ArrayList<>();
        speed = 4;
        bullet_speed = 2;
        reload = 200;
        hull_sprite = new Sprite(hull);
        hull_sprite.setPosition(400 - hull_sprite.getWidth(), 400 - hull_sprite.getHeight());
        hull_sprite.setScale(2);
        turret_sprite = new Sprite(turret);
        turret_sprite.setPosition(400 - turret_sprite.getWidth(), 400 - turret_sprite.getHeight());
        turret_sprite.setScale(2);
        virtualJoystick = new VirtualJoystick();
        virtualJoystick_2 = new VirtualJoystick();
        virtualJoystick_2.create();
        virtualJoystick.create();
        virtualJoystick.knob_background_sprite.setPosition(120 - (virtualJoystick.knob_background_sprite.getWidth() / 2), 120 - (virtualJoystick.knob_background_sprite.getHeight() / 2));
        virtualJoystick.knob_sprite.setPosition(120 - (virtualJoystick.knob_sprite.getWidth() / 2), 120 - (virtualJoystick.knob_sprite.getHeight() / 2));
        virtualJoystick_2.knob_background_sprite.setPosition(680 - (virtualJoystick_2.knob_background_sprite.getWidth() / 2), 120 - (virtualJoystick_2.knob_background_sprite.getHeight() / 2));
        virtualJoystick_2.knob_sprite.setPosition(680 - (virtualJoystick_2.knob_sprite.getWidth() / 2), 120 - (virtualJoystick_2.knob_sprite.getHeight() / 2));
    }
    public void render(){
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        reload -= 1;
        virtualJoystick.render();
        virtualJoystick_2.render();
        if(virtualJoystick.touch){
            movement();
        }
        turret_rotate();
        hull_rotate();
        shoot();
        after_shoot();
        render_bullets();
        hull_sprite.draw(batch);
        turret_sprite.draw(batch);
        batch.end();
    }
    public void dispose(){
        hull.dispose();
        turret.dispose();
        virtualJoystick.dispose();
        virtualJoystick_2.dispose();
    }
}
