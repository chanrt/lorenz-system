import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import peasy.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class lorenz_system extends PApplet {

float rho = 28;
float sigma = 10;
float beta = 8.0f / 3.0f;

  
PeasyCam camera;

ArrayList<PVector> points = new ArrayList<PVector>();
float hue_start;
int steps_per_frame = 10;

int generateRequest, chunkSize = 100;

float x = 1, y = 2, z = 3;
float dx, dy, dz;
float prec;

boolean paused;

public void setup() {
  
  colorMode(HSB);

  camera = new PeasyCam(this, 500);
  prec = 0.005f;
  hue_start = random(255);

  textSize(20);
  textAlign(CENTER);
}

public void draw() {

  if (!paused) {
    for (int cycle = 0; cycle < steps_per_frame; cycle++) {
      dx = sigma * (y - x) * prec;
      dy = x * (rho - z) * prec;
      dz = (x * y - beta * z) * prec;

      x += dx;
      y += dy;
      z += dz;
      points.add(new PVector(x, y, z));
    }
  }

  while (--generateRequest > 0) {
    dx = sigma * (y - x) * prec;
    dy = x * (rho - z) * prec;
    dz = (x * y - beta * z) * prec;

    x += dx;
    y += dy;
    z += dz;
    points.add(new PVector(x, y, z));
  }

  float hue = hue_start;

  background(0);
  translate(0, 0, -80);
  scale(4);
  noFill();
  beginShape();
  for (PVector point : points) {
    stroke(hue, 255, 255);
    vertex(point.x, point.y, point.z);
    hue += 0.1f;

    if (hue > 255) {
      hue = 0;
    }
  }
  endShape();

  text("Rho: " + Float.toString(rho), -width/4, -height/4, -500);
  text("Sigma: " + Float.toString(sigma), 0, -height/4, -500);
  text("Beta: " + Float.toString(beta), width/4, -height/4, -500);
  text("Generation rate: " + Integer.toString(steps_per_frame), 0, height / 4, -500);
}

public void keyPressed() {
  if (key == 'p' || key == 'P') {
    paused = paused ? false : true;
  }
  if (key == 'c' || key == 'C') {
    camera.reset(1000);
  }
  if (key == 'r' || key == 'R') {
    x = 1;
    y = 2;
    z = 3;
    points.clear();
    hue_start = random(255);
  }

  if (keyCode == RIGHT) {
    generateRequest = chunkSize;
  }

  if (keyCode == UP) {
    steps_per_frame += 5;
  }
  if (keyCode == DOWN && steps_per_frame >= 5) {
    steps_per_frame -= 5;
  }

  if (key == 'q' || key == 'Q') {
    rho -= 1;
  }
  if (key == 'w' || key == 'W') {
    rho += 1;
  }

  if (key == 'a' || key == 'A') {
    sigma -= 1;
  }
  if (key == 's' || key == 'S') {
    sigma += 1;
  }

  if (key == 'z' || key == 'Z') {
    beta -= 1;
  }
  if (key == 'x' || key == 'X') {
    beta += 1;
  }
}

  public void settings() {  fullScreen(P3D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "lorenz_system" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
